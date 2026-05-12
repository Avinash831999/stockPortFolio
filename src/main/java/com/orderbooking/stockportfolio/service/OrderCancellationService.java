package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.OrderDto;

public interface OrderCancellationService {
    OrderDto cancelOrderInternal(Long orderId);
}
