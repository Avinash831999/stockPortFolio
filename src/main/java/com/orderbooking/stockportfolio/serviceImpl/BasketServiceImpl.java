package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.BasketDto;
import com.orderbooking.stockportfolio.entity.Basket;
import com.orderbooking.stockportfolio.enums.BasketStatus;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.DuplicateDataException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.service.BasketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BasketServiceImpl implements BasketService {

    private final Logger logger = LoggerFactory.getLogger(BasketServiceImpl.class);

    private final BasketRepository basketRepository;
    private final CacheDataMap cacheDataMap;

    public BasketServiceImpl(BasketRepository basketRepository, CacheDataMap cacheDataMap) {
        this.basketRepository = basketRepository;
        this.cacheDataMap = cacheDataMap;
    }

    @Override
    public BasketDto createBasket(BasketDto basketDto) {
        if(cacheDataMap.getBasketIdNameMap().containsValue(basketDto.getName())) {
            logger.error("Basket with name {} already exists.", basketDto.getName());
            throw new DuplicateDataException("Basket with name " + basketDto.getName() + " already exists.");
        }
        else{
            Basket basket = getBasketFromBasketDto(basketDto);
            basket.setUpdatedAt(new Date());
            basket.setCreatedAt(new Date());

            basket =   basketRepository.save(basket);
            cacheDataMap.getBasketIdNameMap().put(basket.getId(), basket.getName());
            logger.info("Basket with name {} created successfully with id {}.", basket.getName(), basket.getId());
            return convertToDto(basket);
        }
    }

    @Override
    public void delete(Long basketId) {
        if(cacheDataMap.getBasketIdNameMap().containsKey(basketId)) {
            basketRepository.deleteById(basketId);
            cacheDataMap.getBasketIdNameMap().remove(basketId);
            logger.info("Basket with id {} deleted successfully.", basketId);
        }
        else{
            logger.error("Basket with id {} not found for deletion.", basketId);
            throw new DataNotFoundException("Basket with id " + basketId + " not found.");
        }
    }

    @Override
    public BasketDto updateBasket(Long basketId, BasketDto basketDto){
        if(cacheDataMap.getBasketIdNameMap().containsKey(basketId)
                && cacheDataMap.getBasketIdNameMap().get(basketId).equals(basketDto.getName())) {

            Basket basket = this.basketRepository.findById(basketId).get();

            if(basketDto.getName()!=null){
                basket.setName(basketDto.getName());
            }
            if(basketDto.getBasketStatus()!=null) {
                basket.setBasketStatus(BasketStatus.fromName(basketDto.getBasketStatus()));
            }

            basket.setUpdatedAt(new Date());
            basket =  basketRepository.save(basket);
            logger.info("Basket with name {} updated successfully with id {}.", basket.getName(), basket.getId());
            return convertToDto(basket);
        }
        else{
            logger.error("Basket with name {} not found for update.", basketDto.getName());
            throw new DataNotFoundException("Basket with name " + basketDto.getName() + " not found.");
        }
    }

    @Override
    public List<BasketDto> getBasketList() {
        return this.basketRepository.findAll().stream().map(this::convertToDto).toList();
    }


    private Basket getBasketFromBasketDto(BasketDto basketDto){
        Basket basket = new Basket();
        basket.setId(cacheDataMap.getBasketIdNameMap().entrySet().stream()
                .filter(entry -> entry.getValue().equals(basketDto.getName()))
                .map(entry -> entry.getKey())
                .findFirst()
                .orElse(null));
        basket.setName(basketDto.getName());
        basket.setBasketStatus(
                BasketStatus.fromName(basketDto.getBasketStatus())
        );

        return basket;
    }

    private BasketDto convertToDto(Basket basket) {
        BasketDto basketDto = new BasketDto();
        basketDto.setId(basket.getId());
        basketDto.setName(basket.getName());
        basketDto.setBasketStatus(basket.getBasketStatus().name());
        basketDto.setCreatedAt(basket.getCreatedAt());
        basketDto.setUpdatedAt(basket.getUpdatedAt());
        return basketDto;
    }
}
