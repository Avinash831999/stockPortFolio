package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.*;
import com.orderbooking.stockportfolio.entity.Order;
import com.orderbooking.stockportfolio.entity.Sector;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.entity.Trader;
import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.IllegalOrderStateException;
import com.orderbooking.stockportfolio.exceptions.MaxPendingOrdersCountException;
import com.orderbooking.stockportfolio.exceptions.NotEnoughSharesException;
import com.orderbooking.stockportfolio.repository.OrderRepository;
import com.orderbooking.stockportfolio.repository.SectorRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.repository.TraderRepository;
import com.orderbooking.stockportfolio.service.HoldingsService;
import com.orderbooking.stockportfolio.service.OrderCancellationService;
import com.orderbooking.stockportfolio.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final SectorRepository sectorRepository;
    private final TraderRepository traderRepository;
    private final CacheDataMap cacheDataMap;
    private final HoldingsService holdingsService;
    private final OrderCancellationService orderCancellationService;

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, StockRepository stockRepository,
                            SectorRepository sectorRepository, TraderRepository traderRepository,
                            CacheDataMap cacheDataMap, HoldingsService holdingsService, OrderCancellationService orderCancellationService) {
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.sectorRepository = sectorRepository;
        this.traderRepository = traderRepository;
        this.cacheDataMap = cacheDataMap;
        this.holdingsService = holdingsService;
        this.orderCancellationService = orderCancellationService;
    }

    @Override
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
//        List<Order> tradersOrder = this.orderRepository.findByTrader_Id(placeOrderDto.getTraderId());
//        long pendingOrders = tradersOrder.stream().filter(o -> o.getStatus().equals(OrderStatus.PENDING)).count();

        List<Order> tradersOrder = this.orderRepository.findByTrader_IdAndStatus(placeOrderDto.getTraderId(), OrderStatus.PENDING);
        if (tradersOrder.size() < 3) {
            Trader trader = traderRepository.findById(placeOrderDto.getTraderId()).orElseThrow(() ->
                    new DataNotFoundException("Trader with id " + placeOrderDto.getTraderId() + " not found"));
            Stock stock = stockRepository.findById(placeOrderDto.getStockId()).orElseThrow(() ->
                    new DataNotFoundException("Stock with id " + placeOrderDto.getStockId() + " not found"));
            Sector sector = sectorRepository.findById(placeOrderDto.getSectorId()).orElseThrow(() ->
                    new DataNotFoundException("Sector with id " + placeOrderDto.getSectorId() + " not found"));

            Order order = new Order();
            order.setTrader(trader);
            order.setStock(stock);
            order.setSector(sector);
            order.setQuantity(placeOrderDto.getQuantity());
            order.setSide(TradeSide.fromName(placeOrderDto.getSide()));
            order.setStatus(OrderStatus.PENDING);
            Float rate = stock.getPrice();
            order.setRate(rate);
            order.setTotal(rate * placeOrderDto.getQuantity());
            order.setCreatedAt(new Date());
            order.setUpdatedAt(new Date());

            order = orderRepository.save(order);
            logger.info("Order with id {} placed successfully", order.getId());
             return convertToDto(order);
        }
        logger.error("Trader with id {} has reached the maximum limit of 3 pending orders and cannot fill more orders", placeOrderDto.getTraderId());
        throw new MaxPendingOrdersCountException("Trader with id " + placeOrderDto.getTraderId() + " has reached the maximum limit of 3 pending orders");
    }

    @Override
    public List<OrderDto> getOrderDetailsOfTrader(Long traderId) {
        List<Order> orders = orderRepository.findByTrader_Id(traderId);
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto getOrderDetails(Long orderId) {

        return convertToDto(orderRepository.findById(orderId).orElseThrow(() -> { logger.error("Order with id {} not found", orderId);
            return new DataNotFoundException("Order not found for id: " + orderId);}));

    }

    @Override
    @Transactional
    public OrderDto fillOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order o = order.get();
            if (!o.getStatus().equals(OrderStatus.PENDING)) {
                logger.error("Order with id {} is not in PENDING state and cannot be filled", orderId);
                throw new IllegalOrderStateException("Order with id " + orderId + " is not in PENDING state and cannot be filled");
            }


                o.setUpdatedAt(new Date());
                o.setStatus(OrderStatus.FILLED);
                OrderDto dto = convertToDto(o);
                try {
                    updateTraderHoldings(dto.getTraderId(), dto, OrderStatus.FILLED);
                    orderRepository.save(o);
                    logger.info("Order with id {} filled successfully", orderId);
                    return dto;
                } catch (NotEnoughSharesException e) {
                    this.orderCancellationService.cancelOrderInternal(orderId);
                    throw e;
                }
        }
        logger.error("Order with id {} not found or not in PENDING state for filling", orderId);
        throw new DataNotFoundException("Order not found for id: " + orderId + " with PENDING state");
    }

    private void updateTraderHoldings(Long traderId, OrderDto dto, OrderStatus status) {
        TradersHoldingsDto holdings = this.holdingsService.getHoldingsByTraderIdAndStockId(traderId, dto.getStockId());
        HoldingDto holdingUpdate = new HoldingDto();
        List<HoldingStock> holdingStockList = holdings.getHoldings();
        holdingUpdate.setUpdatedAt(new Date());
        holdingUpdate.setStockId(dto.getStockId());
        holdingUpdate.setTraderId(traderId);
        holdingUpdate.setSectorId(dto.getSectorId());

        if (holdingStockList.isEmpty() && status.equals(OrderStatus.FILLED)) {
            if (dto.getSide().equals(TradeSide.SELL)) {
                logger.error("Cannot fill sell order for trader id {} stock id {} with no existing holding", traderId, dto.getStockId());
                throw new NotEnoughSharesException("Not enough shares to sell. Available: 0, Attempted to sell: " + dto.getQuantity());
            }
            holdingUpdate.setQuantity(dto.getQuantity());
            holdingsService.addToTraderHoldings(holdingUpdate);
        } else {
            HoldingStock holdingStock = holdingStockList.get(0);
            holdingUpdate.setId(holdingStock.getId());
            if (dto.getSide().equals(TradeSide.BUY)) {
                holdingUpdate.setQuantity(holdingStock.getQuantity() + dto.getQuantity());
            } else if (dto.getSide().equals(TradeSide.SELL)) {
                if (holdingStock.getQuantity() >= dto.getQuantity()) {
                    holdingUpdate.setQuantity(holdingStock.getQuantity() - dto.getQuantity());
                } else {
                    logger.error("Not enough shares to sell for trader id {} and stock id {}. Available: {}, Attempted to sell: {} Cancelling the order", traderId, dto.getStockId(), holdingStock.getQuantity(),
                            dto.getQuantity());

                    throw new NotEnoughSharesException("Not enough shares to sell. Available: " + holdingStock.getQuantity() + ", Attempted to sell: " + dto.getQuantity());
                }
            } else {
                logger.info("Invalid trade side {} for order id {}", dto.getSide(), dto.getId());
                throw new IllegalArgumentException("Invalid trade side: " + dto.getSide());
            }
            if(holdingUpdate.getQuantity() ==0){
                this.holdingsService.deleteHolding(holdingUpdate);
            }
            else{
                this.holdingsService.updateHoldings(holdingUpdate);
            }
        }
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order o = order.get();

            if (!o.getStatus().equals(OrderStatus.PENDING)) {
                logger.error("Order with id {} is not in PENDING state and cannot be cancelled", orderId);
                throw new IllegalOrderStateException("Order with id " + orderId + " is not in PENDING state and cannot be cancelled");
            }

                o.setStatus(OrderStatus.CANCELLED);
                o.setUpdatedAt(new Date());
                orderRepository.save(o);
                OrderDto dto = convertToDto(o);
                logger.info("Order with id {} cancelled successfully", orderId);
                return dto;
            }
        logger.error("Order with id {} not found or not in PENDING state for cancelling", orderId);
        throw new DataNotFoundException("Order not found for id: " + orderId + " with PENDING state");
    }


    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTraderId(order.getTrader().getId());
        orderDto.setTraderName(cacheDataMap.getTraderIdNameMap().get(order.getTrader().getId()));
        orderDto.setStockId(order.getStock().getId());
        orderDto.setStockName(cacheDataMap.getStockIdNameMap().get(order.getStock().getId()));
        orderDto.setSectorId(order.getSector().getId());
        orderDto.setSectorName(order.getSector().getName());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setSide(order.getSide());
        orderDto.setRate(order.getRate());
        orderDto.setTotal(order.getTotal());
        orderDto.setOrderStatus(order.getStatus());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setUpdatedAt(order.getUpdatedAt());

        return orderDto;
    }
}
