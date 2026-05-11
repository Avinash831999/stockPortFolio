package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.*;
import com.orderbooking.stockportfolio.entity.Order;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.entity.Holding;
import com.orderbooking.stockportfolio.enums.BasketStatus;
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
import com.orderbooking.stockportfolio.repository.HoldingRepository;
import com.orderbooking.stockportfolio.service.OrderService;
import com.orderbooking.stockportfolio.service.HoldingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final CacheDataMap cacheDataMap;
    private final HoldingsService holdingsService;
    private final HoldingRepository holdingRepository;


    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    public OrderServiceImpl(OrderRepository orderRepository, StockRepository stockRepository, CacheDataMap cacheDataMap, HoldingsService holdingsService, HoldingRepository holdingRepository){
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.cacheDataMap = cacheDataMap;
        this.holdingsService = holdingsService;
        this.holdingRepository = holdingRepository;
    }


    @Override
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
        List<Order> tradersOrder = this.orderRepository.findByTraderId(placeOrderDto.getTraderId());
        int pendingOrders =  tradersOrder.stream().map(o1 -> o1.getStatus().equals(OrderStatus.PENDING)).toList().size();
        if(pendingOrders > 2){
            Order order = new Order();
            order.setTraderId(placeOrderDto.getTraderId());
            order.setStockId(placeOrderDto.getStockId());
            order.setSectorId(placeOrderDto.getSectorId());
            order.setQuantity(placeOrderDto.getQuantity());
            order.setSide(TradeSide.fromName((placeOrderDto.getSide())));
            order.setStatus(OrderStatus.PENDING);
            Stock stock =  stockRepository.findById(placeOrderDto.getStockId()).orElseThrow();
            Float rate = stock.getPrice();
            order.setRate(rate);
            order.setTotal(rate * placeOrderDto.getQuantity());
            order.setCreatedAt(new Date());
            order.setUpdatedAt(new Date());

            order = orderRepository.save(order);
            logger.info("Order with id {} placed successfully", order.getId());
            return  convertToDto(order);
        }
        else{
            logger.error("Trader with id {} has reached the maximum limit of 3 pending orders and cannot fill more orders", placeOrderDto.getTraderId());
            throw new MaxPendingOrdersCountException("Trader with id " + placeOrderDto.getTraderId() + " has reached the maximum limit of 3 pending orders");
        }


    }

    @Override
    public List<OrderDto> getOrderDetailsOfTrader(Long traderId){
        List<Order> orders = orderRepository.findByTraderId(traderId);
        return orders.stream().map(o -> convertToDto(o)).toList();
    }

    @Override
    public OrderDto getOrderDetails(Long orderId, Long traderId) {

        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent() && order.get().getTraderId().equals(traderId)){
            Order o = order.get();
            return convertToDto(o);
        }
        throw new DataNotFoundException("Order not found for id: " + orderId);
    }

    @Override
    @Transactional
    public OrderDto fillOrder(Long orderId, Long traderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()){
            Order o = order.get();
            if(!o.getStatus().equals(OrderStatus.PENDING)){
                logger.error("Order with id {} is not in PENDING state and cannot be filled", orderId);
                throw new IllegalOrderStateException("Order with id " + orderId + " is not in PENDING state and cannot be filled");
            }

            if(o.getTraderId().equals(traderId)) {
                o.setUpdatedAt(new Date());
                o.setStatus(OrderStatus.FILLED);
                orderRepository.save(o);
//                    updateHoldingsForFilledOrder(o);
                OrderDto dto = convertToDto(o);
                logger.info("Order with id {} filled successfully", orderId);
                updateTraderHoldings(traderId, dto, OrderStatus.FILLED);
                return dto;
            }
            else{
                logger.error("Order with id {} does not belong to trader id {} and cannot be filled", orderId, traderId);
                throw new DataNotFoundException("Order with id {} does not belong to trader id {} and cannot be filled".formatted(orderId, traderId));
            }

        }
        logger.error("Order with id {} not found or not in PENDING state for filling", orderId);
        throw new DataNotFoundException("Order not found for id: " + orderId +" with PENDING state");
    }

//    private void updateHoldingsForFilledOrder(Order order) {
//        Optional<Holding> existingHolding = holdingRepository.findByTraderIdAndStockId(order.getTraderId(), order.getStockId());
//        Holding holding;
//        if (existingHolding.isPresent()) {
//            holding = existingHolding.get();
//            int adjustment = order.getQuantity();
//            holding.setQuantity(holding.getQuantity() + adjustment);
//        } else {
//            holding = new Holding();
//            holding.setTraderId(order.getTraderId());
//            holding.setStockId(order.getStockId());
//            holding.setSectorId(order.getSectorId());
//            holding.setQuantity(order.getSide() == TradeSide.BUY ? order.getQuantity() : -order.getQuantity());
//            holding.setBasketId(null); // Assuming no basket for now
//            holding.setUpdatedAt(new Date());
//        }
//        holdingsService.updateHoldings(convertHoldingToDto(holding));
//    }

    private void updateTraderHoldings(Long traderId, OrderDto dto, OrderStatus status){
        System.out.println("** traderId %s getStockId %s".formatted(traderId, dto.getStockId()));
        TradersHoldingsDto holdings =  this.holdingsService.getHoldingsByTraderIdAndStockId(traderId, dto.getStockId());
        HoldingDto holdingUpdate = new HoldingDto();
        List<HoldingStock> holdingStockList = holdings.getHoldings();
        holdingUpdate.setUpdatedAt(new Date());
        holdingUpdate.setStockId(dto.getStockId());
        holdingUpdate.setTraderId(traderId);
        holdingUpdate.setSectorId(dto.getSectorId());

        if(holdingStockList.isEmpty() && status.equals(OrderStatus.FILLED)){

            if(dto.getSide().equals(TradeSide.BUY)){
                holdingUpdate.setQuantity(dto.getQuantity());
            }
            holdingUpdate.setQuantity(dto.getQuantity());
            holdingsService.addToTraderHoldings(holdingUpdate);
        }
        else{
            HoldingStock holdingStock = holdingStockList.get(0);
            holdingUpdate.setId(holdingStock.getId());
            if(dto.getSide().equals(TradeSide.BUY)){
                holdingUpdate.setQuantity(holdingStock.getQuantity() + dto.getQuantity());
            }
            else if(dto.getSide().equals(TradeSide.SELL)){
                if(holdingStock.getQuantity()>= dto.getQuantity()){
                    holdingUpdate.setQuantity(holdingStock.getQuantity() - dto.getQuantity());
                }
                else{
                    logger.error("Not enough shares to sell for trader id {} and stock id {}. Available: {}, Attempted to sell: {}", traderId, dto.getStockId(), holdingStock.getQuantity(), dto.getQuantity());
                    throw new NotEnoughSharesException("Not enough shares to sell. Available: " + holdingStock.getQuantity() + ", Attempted to sell: " + dto.getQuantity());
                }
            }
            else{
                logger.info("Invalid trade side {} for order id {}", dto.getSide(), dto.getId());
                throw new IllegalArgumentException("Invalid trade side: " + dto.getSide());
            }

            this.holdingsService.updateHoldings(holdingUpdate);
        }

    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long orderId, Long traderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()){
            Order o = order.get();
            if(!o.getStatus().equals(OrderStatus.PENDING)){
                logger.error("Order with id {} is not in PENDING state and cannot be cancelled", orderId);
                throw new IllegalOrderStateException("Order with id " + orderId + " is not in PENDING state and cannot be cancelled");
            }
            if(o.getTraderId().equals(traderId)) {
                o.setStatus(OrderStatus.CANCELLED);
                o.setUpdatedAt(new Date());
                orderRepository.save(o);
                OrderDto dto =  convertToDto(o);
                logger.info("Order with id {} cancelled successfully", orderId);
                return dto;
            }
            else{
                logger.error("Order with id {} does not belong to trader id {} and cannot be cancelled", orderId, traderId);
                throw new DataNotFoundException("Order with id {} does not belong to trader id {} and cannot be cancelled".formatted(orderId, traderId));
            }
        }
        logger.error("Order with id {} not found or not in PENDING state for cancelling", orderId);
        throw new DataNotFoundException("Order not found for id: " + orderId +" with PENDING state");
    }

    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTraderId(order.getTraderId());
        orderDto.setTraderName(cacheDataMap.getTraderIdNameMap().get(order.getTraderId()));
        orderDto.setStockId(order.getStockId());
        orderDto.setStockName(cacheDataMap.getStockIdNameMap().get(order.getStockId()));
        orderDto.setSectorId(order.getSectorId());
        orderDto.setSectorName(cacheDataMap.getSectorIdNameMap().get(order.getSectorId()));
        orderDto.setQuantity(order.getQuantity());
        orderDto.setSide(order.getSide());
        orderDto.setRate(order.getRate());
        orderDto.setTotal(order.getTotal());
        orderDto.setOrderStatus(order.getStatus());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setUpdatedAt(order.getUpdatedAt());


        return orderDto;
    }

//    private HoldingDto convertHoldingToDto(Holding holding) {
//        HoldingDto dto = new HoldingDto();
//        dto.setId(holding.getId());
//        dto.setTraderId(holding.getTraderId());
//        dto.setStockId(holding.getStockId());
//        dto.setSectorId(holding.getSectorId());
//        dto.setQuantity(holding.getQuantity());
//        dto.setUpdatedAt(holding.getUpdatedAt());
//        return dto;
//    }
}
