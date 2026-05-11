package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.HoldingDto;
import com.orderbooking.stockportfolio.dto.HoldingStock;
import com.orderbooking.stockportfolio.dto.TradersHoldingsDto;
import com.orderbooking.stockportfolio.entity.Holding;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.DuplicateDataException;
import com.orderbooking.stockportfolio.repository.HoldingRepository;
import com.orderbooking.stockportfolio.repository.TraderRepository;
import com.orderbooking.stockportfolio.service.HoldingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HoldingsServiceImpl implements HoldingsService {

    private final HoldingRepository holdingRepository;
    private final CacheDataMap cacheDataMap;
    private final Logger logger = LoggerFactory.getLogger(HoldingsServiceImpl.class);
    public HoldingsServiceImpl(HoldingRepository holdingRepository, CacheDataMap cacheDataMap ) {
        this.holdingRepository = holdingRepository;
        this.cacheDataMap = cacheDataMap;
    }


//    @Override
//    public TradersHoldingsDto addToTraderHoldings(HoldingDto holdingDto) {
//
//        if(cacheDataMap.getTraderIdNameMap().get(holdingDto.getTraderId()) == null) {
//            logger.error("Trader with id {} not found", holdingDto.getTraderId());
//            throw new DataNotFoundException("Trader with id " + holdingDto.getTraderId() + " not found");
//        }
//        if(cacheDataMap.getTraderIdNameMap().get(holdingDto.getStockId()) == null){
//            logger.error("Stock with id {} not found", holdingDto.getStockId());
//            throw new DataNotFoundException("Stock with id " + holdingDto.getStockId() + " not found");
//        }
//
//        Holding holding = getHoldingFromHoldingDto(holdingDto);
//        holding = holdingRepository.save(holding);
//        logger.info("Holdings with id {} added successfully", holding.getId());
//        return getHoldingDtoFromHolding(List.of(holding), holding.getTraderId());
//    }

    @Override
    @Transactional
    public HoldingDto updateHoldings(HoldingDto holdingDto) {
        Holding holding = getHoldingFromHoldingDto(holdingDto);
        if(holdingRepository.existsById(holding.getId())) {

            holding =  holdingRepository.save(holding);
            logger.info("Holdings with id {} updated successfully", holding.getId());
            return convertToDto(holding);
        }
        else {
            logger.error("Holdings with id {} not found", holding.getId());
            throw new DataNotFoundException("Holdings with id " + holding.getId() + " not found");
        }
    }

    @Override
    @Transactional
    public HoldingDto addToTraderHoldings(HoldingDto holdingDto) {

        if(cacheDataMap.getTraderIdNameMap().get(holdingDto.getTraderId()) == null) {
            logger.error("Trader with id {} not found", holdingDto.getTraderId());
            throw new DataNotFoundException("Trader with id " + holdingDto.getTraderId() + " not found");
        }
        if(cacheDataMap.getStockIdNameMap().get(holdingDto.getStockId()) == null){
            logger.error("Stock with id {} not found", holdingDto.getStockId());
            throw new DataNotFoundException("Stock with id " + holdingDto.getStockId() + " not found");
        }

        Holding holding = getHoldingFromHoldingDto(holdingDto);
        if(!holdingRepository.existsByTraderIdAndStockId(holdingDto.getTraderId(), holdingDto.getStockId())) {
            holding =  holdingRepository.save(holding);
            logger.info("Holdings with id {} added successfully", holding.getId());
            return convertToDto(holding);
        }
        else {
            logger.error("Holdings for trader id {} and stock id {} already exists", holdingDto.getTraderId(), holdingDto.getStockId());
            throw new DataNotFoundException("Holdings for trader id " + holdingDto.getTraderId() + " and stock id " + holdingDto.getStockId() + " already exists");
        }
    }

    @Override
    public TradersHoldingsDto getHoldingsByTraderIdAndStockId(Long traderId, Long stockId) {
        Optional<Holding> holding = holdingRepository.findByTraderIdAndStockId(traderId, stockId);
        List<Holding> holdings = holding.isPresent() ? List.of(holding.get()) : List.of();
        return getHoldingDtoFromHolding(holdings, traderId);
    }

    @Override
    public TradersHoldingsDto getHoldingsByTraderId(Long traderId) {
        return getHoldingDtoFromHolding(holdingRepository.findByTraderId(traderId), traderId);
    }

    private Holding getHoldingFromHoldingDto(HoldingDto holdingDto) {
        Holding holding = new Holding();
        holding.setId(holdingDto.getId());
        holding.setTraderId(holdingDto.getTraderId());
        holding.setStockId(holdingDto.getStockId());
        holding.setSectorId(holdingDto.getSectorId());
        holding.setQuantity(holdingDto.getQuantity());
        holding.setUpdatedAt(holdingDto.getUpdatedAt());
        return holding;
    }

    private HoldingDto convertToDto(Holding holding) {
        HoldingDto holdingDto = new HoldingDto();
        holdingDto.setId(holding.getId());
        holdingDto.setTraderId(holding.getTraderId());
        holdingDto.setStockId(holding.getStockId());
        holdingDto.setSectorId(holding.getSectorId());
        holdingDto.setQuantity(holding.getQuantity());
        holdingDto.setUpdatedAt(holding.getUpdatedAt());
        return holdingDto;
    }

    private TradersHoldingsDto getHoldingDtoFromHolding(List<Holding> holdings, Long traderId) {
        List<HoldingStock> holdingStocks = holdings.stream().map(holding ->{
            HoldingStock holdingStock = new HoldingStock();
            holdingStock.setId(holding.getId());
            holdingStock.setStockId(holding.getStockId());
            holdingStock.setStockName(cacheDataMap.getStockIdNameMap().get(holding.getStockId()));
            holdingStock.setQuantity(holding.getQuantity());
            holdingStock.setUpdatedAt(holding.getUpdatedAt());
            holdingStock.setSectorId(holding.getSectorId());
            holdingStock.setSectorName(cacheDataMap.getSectorIdNameMap().get(holding.getSectorId()));
            return holdingStock;
        }).toList();

        TradersHoldingsDto holdingsDto = new TradersHoldingsDto();
        holdingsDto.setTraderName(cacheDataMap.getTraderIdNameMap().get(traderId));
        holdingsDto.setTraderId(traderId);
        holdingsDto.setHoldings(holdingStocks);

        return holdingsDto;
    }

}
