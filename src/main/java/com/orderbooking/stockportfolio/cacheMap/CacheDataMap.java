package com.orderbooking.stockportfolio.cacheMap;

import com.orderbooking.stockportfolio.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Data
public class CacheDataMap {

    private Map<Long, String> sectorIdNameMap = new ConcurrentHashMap<>();
    private Map<Long, String> stockIdNameMap = new ConcurrentHashMap<>();
    private Map<Long, String> traderIdNameMap = new ConcurrentHashMap<>();
    private Map<Long, String> basketIdNameMap = new ConcurrentHashMap<>();

    private final SectorRepository sectorRepository;
    private final StockRepository stockRepository;
    private final TraderRepository traderRepository;
    private final BasketRepository basketRepository;

    CacheDataMap(SectorRepository sectorRepository, StockRepository stockRepository, TraderRepository traderRepository, BasketRepository basketRepository){
        this.basketRepository= basketRepository;
        this.sectorRepository= sectorRepository;
        this.traderRepository= traderRepository;
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    public void init() {
        sectorIdNameMap = sectorRepository.findAll().stream()
                .collect(ConcurrentHashMap::new, (m, v) -> m.put(v.getId(), v.getName()), ConcurrentHashMap::putAll);

        stockIdNameMap = stockRepository.findAll().stream()
                .collect(ConcurrentHashMap::new, (m, v) -> m.put(v.getId(), v.getName()), ConcurrentHashMap::putAll);

        traderIdNameMap = traderRepository.findAll().stream()
                .collect(ConcurrentHashMap::new, (m, v) -> m.put(v.getId(), v.getName()), ConcurrentHashMap::putAll);

        basketIdNameMap = basketRepository.findAll().stream()
                .collect(ConcurrentHashMap::new, (m, v) -> m.put(v.getId(), v.getName()), ConcurrentHashMap::putAll);





        System.out.println("sectorIdNameMap: " + sectorIdNameMap);
        System.out.println("stockIdNameMap: " + stockIdNameMap);
        System.out.println("traderIdNameMap: " + traderIdNameMap);
        System.out.println("basketIdNameMap: " + basketIdNameMap);
    }





}
