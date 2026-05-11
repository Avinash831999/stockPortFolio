package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.BasketDto;

import java.util.List;

public interface BasketService {

    BasketDto createBasket(BasketDto basketDto);
    void delete(Long basketId);
    BasketDto updateBasket(Long basketId, BasketDto basketDto);
    List<BasketDto> getBasketList();

}
