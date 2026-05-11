package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.TraderDto;
import com.orderbooking.stockportfolio.entity.Trader;
import com.orderbooking.stockportfolio.enums.TraderStatus;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.TraderRepository;
import com.orderbooking.stockportfolio.service.TradersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradersServiceImpl implements TradersService {

    private final TraderRepository traderRepository;
    private final CacheDataMap cacheDataMap;

    public TradersServiceImpl(TraderRepository traderRepository, CacheDataMap cacheDataMap){
        this.traderRepository = traderRepository;
        this.cacheDataMap = cacheDataMap;
    }

    private final Logger logger = LoggerFactory.getLogger(TradersServiceImpl.class);

    @Override
    public TraderDto addTrader(TraderDto traderDto) {
        Trader trader = getTraderFromTraderDto(traderDto);
        trader.setCreatedAt(new Date());
        trader.setUpdatedAt(new Date());

        trader = traderRepository.save(trader);
        cacheDataMap.getTraderIdNameMap().put(trader.getId(), trader.getName());
        logger.info("Trader with id {} added successfully", trader.getId());
        return convertToDto(trader);
    }

    @Override
    public TraderDto updateTrader(Long traderId, TraderDto traderDto) {
        if(cacheDataMap.getTraderIdNameMap().containsKey(traderId)){
            Trader trader = this.traderRepository.findById(traderId).get();

            if(traderDto.getTraderStatus() != null){
                trader.setTraderStatus(TraderStatus.valueOf(traderDto.getTraderStatus()));
            }
            if(traderDto.getEmail() != null){
                trader.setEmail(traderDto.getEmail());
            }
            if(traderDto.getName() != null){
                trader.setName(traderDto.getName());
                cacheDataMap.getTraderIdNameMap().put(traderId, traderDto.getName());
            }
            if(traderDto.getPanNumber() != null) {
                trader.setPanNumber(traderDto.getPanNumber());
            }

            trader.setUpdatedAt(traderDto.getUpdated_at());
            trader =  traderRepository.save(trader) ;
            logger.info("Trader with id {} updated successfully", trader.getId());
            return convertToDto(trader);
        }
        else {
            logger.error("Trader with id {} not found", traderId);
            throw new DataNotFoundException("Trader with id " + traderId + " not found");
        }
    }

    @Override
    public void removeTrader(Long traderId) {
        if(cacheDataMap.getTraderIdNameMap().containsKey(traderId)){
            traderRepository.deleteById(traderId);
            logger.info("Trader with id {} removed successfully", traderId);
        }
        else {
            logger.error("Trader with id {} not found", traderId);
            throw new DataNotFoundException("Trader with id " + traderId + " not found");
        }
    }

    @Override
    public List<TraderDto> getAllTraders() {
        return traderRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public TraderDto getTraderDetailsById(Long traderId) {
        if(cacheDataMap.getTraderIdNameMap().containsKey(traderId)){
            Trader trader = traderRepository.findById(traderId).get();
            return convertToDto(trader);
        }
        else{
            logger.error("Trader with id {} not found", traderId);
            throw new DataNotFoundException("Trader with id " + traderId + " not found");
        }

    }


    private Trader getTraderFromTraderDto(TraderDto traderDto){
        Trader trader = new Trader();
        trader.setId(traderDto.getId());
        trader.setName(traderDto.getName());
        trader.setEmail(traderDto.getEmail());
        trader.setPanNumber(traderDto.getPanNumber());
        trader.setTraderStatus(TraderStatus.valueOf(traderDto.getTraderStatus()));
        return  trader;
    }

    private TraderDto convertToDto(Trader trader) {
        TraderDto traderDto = new TraderDto();
        traderDto.setId(trader.getId());
        traderDto.setName(trader.getName());
        traderDto.setEmail(trader.getEmail());
        traderDto.setPanNumber(trader.getPanNumber());
        traderDto.setTraderStatus(trader.getTraderStatus().name());
        traderDto.setCreatedAt(trader.getCreatedAt());
        traderDto.setUpdated_at(trader.getUpdatedAt());
        return traderDto;
    }
}
