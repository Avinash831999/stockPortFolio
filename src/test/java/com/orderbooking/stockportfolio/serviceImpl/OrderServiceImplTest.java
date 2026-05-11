package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.HoldingStock;
import com.orderbooking.stockportfolio.dto.OrderDto;
import com.orderbooking.stockportfolio.dto.PlaceOrderDto;
import com.orderbooking.stockportfolio.dto.TradersHoldingsDto;
import com.orderbooking.stockportfolio.entity.Order;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.IllegalOrderStateException;
import com.orderbooking.stockportfolio.exceptions.MaxPendingOrdersCountException;
import com.orderbooking.stockportfolio.exceptions.NotEnoughSharesException;
import com.orderbooking.stockportfolio.repository.HoldingRepository;
import com.orderbooking.stockportfolio.repository.OrderRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.service.HoldingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private CacheDataMap cacheDataMap;
    @Mock
    private HoldingsService holdingsService;
    @Mock
    private HoldingRepository holdingRepository;

    private OrderServiceImpl service;

    @BeforeEach
    void setUp() {

    }

    @Test
    void placeOrder_success_whenPendingAboveTwo() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        Order pending = new Order(11L, 1L, 2L, 3L, 1, 10f, 10f, TradeSide.BUY, OrderStatus.PENDING, new Date(), new Date());
        when(orderRepository.findByTraderId(1L)).thenReturn(List.of(pending, pending, pending));
        when(stockRepository.findById(2L)).thenReturn(Optional.of(new Stock(2L, "Stock2", 50f, 3L, new Date(), new Date())));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setId(99L);
            return o;
        });

        PlaceOrderDto dto = new PlaceOrderDto(1L, 2L, 3L, 4, "BUY");
        OrderDto result = service.placeOrder(dto);

        assertEquals(99L, result.getId());
        assertEquals(200f, result.getTotal());
    }

    @Test
    void placeOrder_pendingNotAboveTwo_throws() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        Order pending = new Order(11L, 1L, 2L, 3L, 1, 10f, 10f, TradeSide.BUY, OrderStatus.PENDING, new Date(), new Date());
        when(orderRepository.findByTraderId(1L)).thenReturn(List.of(pending));
        PlaceOrderDto dto = new PlaceOrderDto(1L, 2L, 3L, 4, "BUY");
        assertThrows(MaxPendingOrdersCountException.class, () -> service.placeOrder(dto));
    }

    @Test
    void getOrderDetailsOfTrader_mapsAll() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        when(orderRepository.findByTraderId(1L)).thenReturn(List.of(
                new Order(1L, 1L, 2L, 3L, 2, 20f, 40f, TradeSide.BUY, OrderStatus.PENDING, new Date(), new Date())
        ));
        List<OrderDto> result = service.getOrderDetailsOfTrader(1L);
        assertEquals(1, result.size());
        assertEquals("Trader1", result.get(0).getTraderName());
    }

    @Test
    void getOrderDetails_success() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        Order order = new Order(1L, 1L, 2L, 3L, 2, 20f, 40f, TradeSide.BUY, OrderStatus.PENDING, new Date(), new Date());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderDto result = service.getOrderDetails(1L, 1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getOrderDetails_notFound_throws() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.getOrderDetails(1L, 1L));
    }

    @Test
    void fillOrder_buy_withNoHolding_addsHolding() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        Order order = new Order(1L, 1L, 2L, 3L, 5, 20f, 100f, TradeSide.BUY, OrderStatus.PENDING, new Date(), new Date());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 2L))
                .thenReturn(new TradersHoldingsDto(1L, "Trader1", List.of()));

        OrderDto result = service.fillOrder(1L, 1L);
        assertEquals(OrderStatus.FILLED, result.getOrderStatus());
        verify(holdingsService).addToTraderHoldings(any());
    }

    @Test
    void fillOrder_sell_withEnoughHolding_updatesHolding() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        Order order = new Order(1L, 1L, 2L, 3L, 5, 20f, 100f, TradeSide.SELL, OrderStatus.PENDING, new Date(), new Date());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        HoldingStock holding = new HoldingStock(7L, 2L, "Stock2", 3L, "Sector3", 10, new Date());
        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 2L))
                .thenReturn(new TradersHoldingsDto(1L, "Trader1", List.of(holding)));

        service.fillOrder(1L, 1L);
        verify(holdingsService).updateHoldings(any());
    }

    @Test
    void fillOrder_sell_withoutEnoughHolding_throws() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        Order order = new Order(1L, 1L, 2L, 3L, 15, 20f, 300f, TradeSide.SELL, OrderStatus.PENDING, new Date(), new Date());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        HoldingStock holding = new HoldingStock(7L, 2L, "Stock2", 3L, "Sector3", 10, new Date());
        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 2L))
                .thenReturn(new TradersHoldingsDto(1L, "Trader1", List.of(holding)));

        assertThrows(NotEnoughSharesException.class, () -> service.fillOrder(1L, 1L));
    }

    @Test
    void fillOrder_invalidState_throws() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        Order order = new Order(1L, 1L, 2L, 3L, 5, 20f, 100f, TradeSide.BUY, OrderStatus.CANCELLED, new Date(), new Date());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(IllegalOrderStateException.class, () -> service.fillOrder(1L, 1L));
    }

    @Test
    void fillOrder_wrongTrader_throws() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        Order order = new Order(1L, 9L, 2L, 3L, 5, 20f, 100f, TradeSide.BUY, OrderStatus.PENDING, new Date(), new Date());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(DataNotFoundException.class, () -> service.fillOrder(1L, 1L));
    }

    @Test
    void fillOrder_notFound_throws() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.fillOrder(1L, 1L));
    }

    @Test
    void cancelOrder_success() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        Order order = new Order(1L, 1L, 2L, 3L, 5, 20f, 100f, TradeSide.BUY, OrderStatus.PENDING, new Date(), new Date());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        OrderDto result = service.cancelOrder(1L, 1L);
        assertEquals(OrderStatus.CANCELLED, result.getOrderStatus());
    }

    @Test
    void cancelOrder_invalidState_throws() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        Order order = new Order(1L, 1L, 2L, 3L, 5, 20f, 100f, TradeSide.BUY, OrderStatus.FILLED, new Date(), new Date());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(IllegalOrderStateException.class, () -> service.cancelOrder(1L, 1L));
    }

    @Test
    void cancelOrder_wrongTrader_throws() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        Order order = new Order(1L, 9L, 2L, 3L, 5, 20f, 100f, TradeSide.BUY, OrderStatus.PENDING, new Date(), new Date());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(DataNotFoundException.class, () -> service.cancelOrder(1L, 1L));
    }

    @Test
    void cancelOrder_notFound_throws() {
        service = new OrderServiceImpl(orderRepository, stockRepository, cacheDataMap, holdingsService, holdingRepository);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        traderMap.put(1L, "Trader1");
        stockMap.put(2L, "Stock2");
        sectorMap.put(3L, "Sector3");
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.cancelOrder(1L, 1L));
    }
}
