package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.OrderDto;
import com.orderbooking.stockportfolio.entity.Order;
import com.orderbooking.stockportfolio.entity.Sector;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.entity.Trader;
import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.IllegalOrderStateException;
import com.orderbooking.stockportfolio.repository.OrderRepository;
import com.orderbooking.stockportfolio.repository.SectorRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.repository.TraderRepository;
import com.orderbooking.stockportfolio.service.HoldingsService;
import com.orderbooking.stockportfolio.service.OrderCancellationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderCancellationServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private TraderRepository traderRepository;

    @Mock
    private CacheDataMap cacheDataMap;

    @Mock
    private HoldingsService holdingsService;


    @InjectMocks
    private OrderCancellationServiceImpl orderService;

    private Order buildOrder() {

        Trader trader = new Trader();
        trader.setId(1L);

        Stock stock = new Stock();
        stock.setId(1L);

        Sector sector = new Sector();
        sector.setId(1L);
        sector.setName("IT");

        Order order = new Order();
        order.setId(1L);
        order.setTrader(trader);
        order.setStock(stock);
        order.setSector(sector);
        order.setQuantity(5);
        order.setSide(TradeSide.BUY);
        order.setStatus(OrderStatus.PENDING);
        order.setRate(100f);
        order.setTotal(500f);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        return order;
    }
    @Test
    void testCancelOrderSuccess() {

        Order order = buildOrder();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        Map<Long, String> traderMap = new HashMap<>();
        traderMap.put(1L, "Trader");

        Map<Long, String> stockMap = new HashMap<>();
        stockMap.put(1L, "TCS");

        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);

        OrderDto dto = orderService.cancelOrderInternal(1L);

        assertEquals(OrderStatus.CANCELLED, dto.getOrderStatus());
    }

    @Test
    void testCancelOrderIllegalState() {

        Order order = buildOrder();
        order.setStatus(OrderStatus.FILLED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(IllegalOrderStateException.class,
                () -> orderService.cancelOrderInternal(1L));
    }



    @Test
    void testCancelOrderNotFound() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class,
                () -> orderService.cancelOrderInternal(1L));
    }

}