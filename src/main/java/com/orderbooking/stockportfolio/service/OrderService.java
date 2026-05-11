package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.OrderDto;
import com.orderbooking.stockportfolio.dto.PlaceOrderDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OrderService {

    OrderDto placeOrder(PlaceOrderDto orderDto);
    OrderDto getOrderDetails(Long orderId, Long traderId);
    List<OrderDto> getOrderDetailsOfTrader( Long traderId);
    OrderDto fillOrder(Long orderId,  Long trader);
    OrderDto cancelOrder(Long orderId ,  Long trader);

}
