package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.HoldingDto;
import com.orderbooking.stockportfolio.dto.HoldingStock;
import com.orderbooking.stockportfolio.dto.TradersHoldingsDto;
import com.orderbooking.stockportfolio.entity.Basket;
import com.orderbooking.stockportfolio.entity.Holding;
import com.orderbooking.stockportfolio.entity.Sector;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.entity.Trader;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.repository.HoldingRepository;
import com.orderbooking.stockportfolio.repository.SectorRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.repository.TraderRepository;
import com.orderbooking.stockportfolio.service.HoldingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HoldingsServiceImpl implements HoldingsService {

    private final HoldingRepository holdingRepository;
    private final SectorRepository sectorRepository;
    private final TraderRepository traderRepository;
    private final StockRepository stockRepository;
    private final BasketRepository basketRepository;
    private final CacheDataMap cacheDataMap;
    private final Logger logger = LoggerFactory.getLogger(HoldingsServiceImpl.class);

    public HoldingsServiceImpl(HoldingRepository holdingRepository, SectorRepository sectorRepository,
                               TraderRepository traderRepository, StockRepository stockRepository,
                               BasketRepository basketRepository, CacheDataMap cacheDataMap) {
        this.holdingRepository = holdingRepository;
        this.sectorRepository = sectorRepository;
        this.traderRepository = traderRepository;
        this.stockRepository = stockRepository;
        this.basketRepository = basketRepository;
        this.cacheDataMap = cacheDataMap;
    }

    @Override
    @Transactional
    public HoldingDto updateHoldings(HoldingDto holdingDto) {
        Holding holding = getHoldingFromHoldingDto(holdingDto);
        if (holdingRepository.existsById(holding.getId())) {
            holding = holdingRepository.save(holding);
            logger.info("Holdings with id {} updated successfully", holding.getId());
            return convertToDto(holding);
        }
        logger.error("Holdings with id {} not found", holding.getId());
        throw new DataNotFoundException("Holdings with id " + holding.getId() + " not found");
    }

    @Override
    @Transactional
    public HoldingDto addToTraderHoldings(HoldingDto holdingDto) {

        if (cacheDataMap.getTraderIdNameMap().get(holdingDto.getTraderId()) == null) {
            logger.error("Trader with id {} not found", holdingDto.getTraderId());
            throw new DataNotFoundException("Trader with id " + holdingDto.getTraderId() + " not found");
        }
        if (cacheDataMap.getStockIdNameMap().get(holdingDto.getStockId()) == null) {
            logger.error("Stock with id {} not found", holdingDto.getStockId());
            throw new DataNotFoundException("Stock with id " + holdingDto.getStockId() + " not found");
        }

        Holding holding = getHoldingFromHoldingDto(holdingDto);
        if (!holdingRepository.existsByTrader_IdAndStock_Id(holdingDto.getTraderId(), holdingDto.getStockId())) {
            holding = holdingRepository.save(holding);
            logger.info("Holdings with id {} added successfully", holding.getId());
            return convertToDto(holding);
        }
        logger.error("Holdings for trader id {} and stock id {} already exists", holdingDto.getTraderId(), holdingDto.getStockId());
        throw new DataNotFoundException("Holdings for trader id " + holdingDto.getTraderId() + " and stock id " + holdingDto.getStockId() + " already exists");
    }

    @Override
    public TradersHoldingsDto getHoldingsByTraderIdAndStockId(Long traderId, Long stockId) {
        Optional<Holding> holding = holdingRepository.findByTrader_IdAndStock_Id(traderId, stockId);
        List<Holding> holdings = holding.map(List::of).orElseGet(List::of);
        return getHoldingDtoFromHolding(holdings, traderId);
    }

    @Override
    public TradersHoldingsDto getHoldingsByTraderId(Long traderId) {
        return getHoldingDtoFromHolding(holdingRepository.findByTrader_Id(traderId), traderId);
    }

    @Override
    public void deleteHolding(HoldingDto holdingDto){

        this.holdingRepository.deleteById(holdingDto.getId());
    }

    private Holding getHoldingFromHoldingDto(HoldingDto holdingDto) {
        Holding holding = new Holding();
        holding.setId(holdingDto.getId());
        Trader trader = traderRepository.findById(holdingDto.getTraderId()).orElseThrow(() ->
                new DataNotFoundException("Trader with id " + holdingDto.getTraderId() + " not found"));
        Stock stock = stockRepository.findById(holdingDto.getStockId()).orElseThrow(() ->
                new DataNotFoundException("Stock with id " + holdingDto.getStockId() + " not found"));
        Sector sector = sectorRepository.findById(holdingDto.getSectorId()).orElseThrow(() ->
                new DataNotFoundException("Sector with id " + holdingDto.getSectorId() + " not found"));
        holding.setTrader(trader);
        holding.setStock(stock);
        holding.setSector(sector);

        Basket basket = null;
        if (holdingDto.getBasketId() != null) {
            basket = basketRepository.findById(holdingDto.getBasketId()).orElseThrow(() ->
                    new DataNotFoundException("Basket with id " + holdingDto.getBasketId() + " not found"));
        } else if (holdingDto.getId() != null) {
            basket = holdingRepository.findById(holdingDto.getId()).map(Holding::getBasket).orElse(null);
        }
        holding.setBasket(basket);

        holding.setQuantity(holdingDto.getQuantity());
        holding.setUpdatedAt(holdingDto.getUpdatedAt());
        return holding;
    }

    private HoldingDto convertToDto(Holding holding) {
        HoldingDto holdingDto = new HoldingDto();
        holdingDto.setId(holding.getId());
        holdingDto.setTraderId(holding.getTrader().getId());
        holdingDto.setStockId(holding.getStock().getId());
        holdingDto.setSectorId(holding.getSector().getId());
        if (holding.getBasket() != null) {
            holdingDto.setBasketId(holding.getBasket().getId());
        }
        holdingDto.setQuantity(holding.getQuantity());
        holdingDto.setUpdatedAt(holding.getUpdatedAt());
        return holdingDto;
    }

    private TradersHoldingsDto getHoldingDtoFromHolding(List<Holding> holdings, Long traderId) {
        List<HoldingStock> holdingStocks = holdings.stream().map(holding -> {
            HoldingStock holdingStock = new HoldingStock();
            holdingStock.setId(holding.getId());
            holdingStock.setStockId(holding.getStock().getId());
            holdingStock.setStockName(cacheDataMap.getStockIdNameMap().get(holding.getStock().getId()));
            holdingStock.setQuantity(holding.getQuantity());
            holdingStock.setUpdatedAt(holding.getUpdatedAt());
            holdingStock.setSectorId(holding.getSector().getId());
            holdingStock.setSectorName(holding.getSector().getName());
            return holdingStock;
        }).toList();

        TradersHoldingsDto holdingsDto = new TradersHoldingsDto();
        holdingsDto.setTraderName(cacheDataMap.getTraderIdNameMap().get(traderId));
        holdingsDto.setTraderId(traderId);
        holdingsDto.setHoldings(holdingStocks);

        return holdingsDto;
    }

}
