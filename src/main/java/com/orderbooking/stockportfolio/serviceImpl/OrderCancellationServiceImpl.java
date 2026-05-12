package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.OrderDto;
import com.orderbooking.stockportfolio.entity.Order;
import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.IllegalOrderStateException;
import com.orderbooking.stockportfolio.repository.OrderRepository;
import com.orderbooking.stockportfolio.service.OrderCancellationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderCancellationServiceImpl implements OrderCancellationService {

    private final OrderRepository orderRepository;
    private final CacheDataMap cacheDataMap;

    public OrderCancellationServiceImpl(OrderRepository orderRepository, CacheDataMap cacheDataMap) {
        this.orderRepository = orderRepository;
        this.cacheDataMap = cacheDataMap;
    }
    private final Logger logger = LoggerFactory.getLogger(OrderCancellationServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderDto cancelOrderInternal(Long orderId) {
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