package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.StockDto;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.entity.Sector;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.DuplicateDataException;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.repository.SectorRepository;
import com.orderbooking.stockportfolio.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final SectorRepository sectorRepository;
    private final CacheDataMap cacheDataMap;
    private final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);
    public StockServiceImpl(StockRepository stockRepository, SectorRepository sectorRepository, CacheDataMap cacheDataMap){
        this.stockRepository = stockRepository;
        this.sectorRepository = sectorRepository;
        this.cacheDataMap = cacheDataMap;
    }

    @Override
    public StockDto addStock(StockDto stockDto) throws Exception {

        if(cacheDataMap.getStockIdNameMap().containsValue(stockDto.getName())) {
            logger.error("Stock with name {} already exists", stockDto.getName());
            throw new DuplicateDataException("Stock with name " + stockDto.getName() + " already exists");
        }
        else{
            Stock stock = getStockFromStockDto(stockDto);
            stock.setCreatedAt(new Date());
            stock.setUpdatedAt(new Date());
            stock = stockRepository.save(stock);
            cacheDataMap.getStockIdNameMap().put(stock.getId(), stock.getName());
            logger.info("Stock {} added successfully", stock.getName());
            return convertToDto(stock);
        }
    }

    @Override
    public StockDto updateStock(Long stockId, StockDto stockDto) {

        if(cacheDataMap.getStockIdNameMap().containsKey(stockId)
                ) {
            Stock stock = this.stockRepository.findById(stockId).get();

            if(stockDto.getName() != null) {
                stock.setName(stockDto.getName());
            }
            if(stockDto.getSectorId() != null) {
                Sector sector = sectorRepository.findById(stockDto.getSectorId()).orElseThrow(() ->
                    new DataNotFoundException("Sector with id " + stockDto.getSectorId() + " not found"));
                stock.setSector(sector);
            }
            if(stockDto.getPrice() != null) {
                stock.setPrice(stockDto.getPrice());
            }
            stock.setUpdatedAt(new Date());
            stock = stockRepository.save(stock);
            logger.info("Stock with name {} updated successfully", stock.getName());
            return convertToDto(stock);
        }
        else{
            logger.error("Stock with id {} not found", stockDto.getId());
            throw new DataNotFoundException("Stock " + stockDto.getId() + " not found");
        }
    }

    @Override
    public void removeStock(Long stockId) {
        if(cacheDataMap.getStockIdNameMap().containsKey(stockId)) {
            stockRepository.deleteById(stockId);
            cacheDataMap.getStockIdNameMap().remove(stockId);
            logger.info("Stock with id {} removed successfully", stockId);
        }
        else{
            logger.error("Stock with id {} not found", stockId);
            throw new DataNotFoundException("Stock not found");
        }
    }

    @Override
    public List<StockDto> getAllStocks() {
        return stockRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public StockDto getStockDetailsById(Long stockId) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> {
            logger.error("Stock with id {} not found", stockId);
            return new DataNotFoundException("Stock with id " + stockId + " not found");
        });
        return convertToDto(stock);
    }

    @Override
    public StockDto getStockDetailsByName(String name) {
        Stock stock = stockRepository.findByName(name).orElseThrow(() -> {
            logger.error("Stock with name {} not found", name);
            return new DataNotFoundException("Stock with name " + name + " not found");
        });
        return convertToDto(stock);
    }

    private Stock getStockFromStockDto(StockDto stockDto) {
        Stock stock = new Stock();
        stock.setId(stockDto.getId());
        stock.setName(stockDto.getName());
        stock.setPrice(stockDto.getPrice());
        if(stockDto.getSectorId() != null) {
            Sector sector = sectorRepository.findById(stockDto.getSectorId()).orElseThrow(() ->
                new DataNotFoundException("Sector with id " + stockDto.getSectorId() + " not found"));
            stock.setSector(sector);
        }
        return stock;
    }

    private StockDto convertToDto(Stock stock) {

        StockDto stockDto = new StockDto();
        stockDto.setId(stock.getId());
        stockDto.setName(stock.getName());
        stockDto.setPrice(stock.getPrice());
        if(stock.getSector() != null) {
            stockDto.setSectorId(stock.getSector().getId());
            stockDto.setSectorName(stock.getSector().getName());
        }
        stockDto.setCreatedAt(stock.getCreatedAt());
        stockDto.setUpdatedAt(stock.getUpdatedAt());
        return stockDto;
    }
}
