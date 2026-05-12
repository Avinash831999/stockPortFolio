package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.*;
import com.orderbooking.stockportfolio.entity.Order;
import com.orderbooking.stockportfolio.entity.Sector;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.entity.Trader;
import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.IllegalOrderStateException;
import com.orderbooking.stockportfolio.exceptions.MaxPendingOrdersCountException;
import com.orderbooking.stockportfolio.exceptions.NotEnoughSharesException;
import com.orderbooking.stockportfolio.repository.OrderRepository;
import com.orderbooking.stockportfolio.repository.SectorRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.repository.TraderRepository;
import com.orderbooking.stockportfolio.service.HoldingsService;
import com.orderbooking.stockportfolio.service.OrderCancellationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

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

    @Mock
    private OrderCancellationService orderCancellationService;

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    void testPlaceOrderSuccess() {

        OrderRepository orderRepository = mock(OrderRepository.class);
        StockRepository stockRepository = mock(StockRepository.class);
        SectorRepository sectorRepository = mock(SectorRepository.class);
        TraderRepository traderRepository = mock(TraderRepository.class);
        CacheDataMap cacheDataMap = mock(CacheDataMap.class);
        HoldingsService holdingsService = mock(HoldingsService.class);
        OrderCancellationService orderCancellationService = mock(OrderCancellationService.class);

        OrderServiceImpl service = new OrderServiceImpl(
                orderRepository,
                stockRepository,
                sectorRepository,
                traderRepository,
                cacheDataMap,
                holdingsService,
                orderCancellationService
        );

        PlaceOrderDto dto = new PlaceOrderDto();
        dto.setTraderId(1L);
        dto.setStockId(1L);
        dto.setSectorId(1L);
        dto.setQuantity(5);
        dto.setSide("BUY");

        Trader trader = new Trader();
        trader.setId(1L);

        Stock stock = new Stock();
        stock.setId(1L);
        stock.setPrice(100f);

        Sector sector = new Sector();
        sector.setId(1L);
        sector.setName("IT");

        Order saved = new Order();
        saved.setId(1L);
        saved.setTrader(trader);
        saved.setStock(stock);
        saved.setSector(sector);
        saved.setQuantity(5);
        saved.setSide(TradeSide.BUY);
        saved.setStatus(OrderStatus.PENDING);
        saved.setRate(100f);
        saved.setTotal(500f);
        saved.setCreatedAt(new Date());
        saved.setUpdatedAt(new Date());

        Map<Long, String> traderMap = new HashMap<>();
        traderMap.put(1L, "Trader");

        Map<Long, String> stockMap = new HashMap<>();
        stockMap.put(1L, "TCS");

        when(orderRepository.findByTrader_IdAndStatus(1L, OrderStatus.PENDING))
                .thenReturn(Collections.emptyList());

        when(traderRepository.findById(1L))
                .thenReturn(Optional.of(trader));

        when(stockRepository.findById(1L))
                .thenReturn(Optional.of(stock));

        when(sectorRepository.findById(1L))
                .thenReturn(Optional.of(sector));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(saved);

        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);

        OrderDto result = service.placeOrder(dto);

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getOrderStatus());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testPlaceOrderMaxPendingOrdersException() {

        OrderRepository orderRepository = mock(OrderRepository.class);

        OrderServiceImpl service = new OrderServiceImpl(
                orderRepository,
                mock(StockRepository.class),
                mock(SectorRepository.class),
                mock(TraderRepository.class),
                mock(CacheDataMap.class),
                mock(HoldingsService.class),
                mock(OrderCancellationService.class)
        );

        PlaceOrderDto dto = new PlaceOrderDto();
        dto.setTraderId(1L);

        when(orderRepository.findByTrader_IdAndStatus(1L, OrderStatus.PENDING))
                .thenReturn(List.of(new Order(), new Order(), new Order()));

        assertThrows(MaxPendingOrdersCountException.class,
                () -> service.placeOrder(dto));
    }

    @Test
    void testGetOrderDetailsSuccess() {

        Order order = buildOrder();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        Map<Long, String> traderMap = new HashMap<>();
        traderMap.put(1L, "Trader");

        Map<Long, String> stockMap = new HashMap<>();
        stockMap.put(1L, "TCS");

        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);

        OrderDto dto = orderService.getOrderDetails(1L);

        assertEquals(1L, dto.getId());
    }

    @Test
    void testGetOrderDetailsNotFound() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class,
                () -> orderService.getOrderDetails(1L));
    }

    @Test
    void testFillOrderSuccessBuy() {

        Order order = buildOrder();

        TradersHoldingsDto holdingsDto = new TradersHoldingsDto();
        holdingsDto.setHoldings(Collections.emptyList());

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 1L))
                .thenReturn(holdingsDto);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        Map<Long, String> traderMap = new HashMap<>();
        traderMap.put(1L, "Trader");

        Map<Long, String> stockMap = new HashMap<>();
        stockMap.put(1L, "TCS");

        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);

        OrderDto dto = orderService.fillOrder(1L);

        assertEquals(OrderStatus.FILLED, dto.getOrderStatus());

        verify(holdingsService).addToTraderHoldings(any(HoldingDto.class));
    }

    @Test
    void testFillOrderNotEnoughShares() {

        Order order = buildOrder();
        order.setSide(TradeSide.SELL);

        TradersHoldingsDto holdingsDto = new TradersHoldingsDto();
        holdingsDto.setHoldings(Collections.emptyList());

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 1L))
                .thenReturn(holdingsDto);

        Map<Long, String> traderMap = new HashMap<>();
        traderMap.put(1L, "Trader");

        Map<Long, String> stockMap = new HashMap<>();
        stockMap.put(1L, "TCS");

        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);

        assertThrows(NotEnoughSharesException.class,
                () -> orderService.fillOrder(1L));

        verify(orderCancellationService)
                .cancelOrderInternal(1L);
    }

    @Test
    void testFillOrderIllegalState() {

        Order order = buildOrder();
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(IllegalOrderStateException.class,
                () -> orderService.fillOrder(1L));
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

        OrderDto dto = orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, dto.getOrderStatus());
    }

    @Test
    void testCancelOrderIllegalState() {

        Order order = buildOrder();
        order.setStatus(OrderStatus.FILLED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(IllegalOrderStateException.class,
                () -> orderService.cancelOrder(1L));
    }

    @Test
    void testCancelOrderNotFound() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class,
                () -> orderService.cancelOrder(1L));
    }

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
    void testFillOrderBuyExistingHoldings() {

        Order order = buildOrder();
        order.setSide(TradeSide.BUY);

        HoldingStock holdingStock = new HoldingStock();
        holdingStock.setId(1L);
        holdingStock.setQuantity(10);

        TradersHoldingsDto holdingsDto = new TradersHoldingsDto();
        holdingsDto.setHoldings(List.of(holdingStock));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 1L))
                .thenReturn(holdingsDto);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        Map<Long, String> traderMap = new HashMap<>();
        traderMap.put(1L, "Trader");

        Map<Long, String> stockMap = new HashMap<>();
        stockMap.put(1L, "TCS");

        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);

        orderService.fillOrder(1L);

        verify(holdingsService).updateHoldings(any(HoldingDto.class));
    }

    @Test
    void testFillOrderSellEnoughShares() {

        Order order = buildOrder();
        order.setSide(TradeSide.SELL);
        order.setQuantity(5);

        HoldingStock holdingStock = new HoldingStock();
        holdingStock.setId(1L);
        holdingStock.setQuantity(10);

        TradersHoldingsDto holdingsDto = new TradersHoldingsDto();
        holdingsDto.setHoldings(List.of(holdingStock));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 1L))
                .thenReturn(holdingsDto);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        Map<Long, String> traderMap = new HashMap<>();
        traderMap.put(1L, "Trader");

        Map<Long, String> stockMap = new HashMap<>();
        stockMap.put(1L, "TCS");

        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);

        orderService.fillOrder(1L);

        verify(holdingsService).updateHoldings(any(HoldingDto.class));
    }
    @Test
    void testFillOrderSellExactQuantityDeleteHolding() {

        Order order = buildOrder();
        order.setSide(TradeSide.SELL);
        order.setQuantity(10);

        HoldingStock holdingStock = new HoldingStock();
        holdingStock.setId(1L);
        holdingStock.setQuantity(10);

        TradersHoldingsDto holdingsDto = new TradersHoldingsDto();
        holdingsDto.setHoldings(List.of(holdingStock));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 1L))
                .thenReturn(holdingsDto);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        Map<Long, String> traderMap = new HashMap<>();
        traderMap.put(1L, "Trader");

        Map<Long, String> stockMap = new HashMap<>();
        stockMap.put(1L, "TCS");

        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);

        orderService.fillOrder(1L);

        verify(holdingsService).deleteHolding(any(HoldingDto.class));
    }

    @Test
    void testFillOrderSellInsufficientShares() {

        Order order = buildOrder();
        order.setSide(TradeSide.SELL);
        order.setQuantity(20);

        HoldingStock holdingStock = new HoldingStock();
        holdingStock.setId(1L);
        holdingStock.setQuantity(5);

        TradersHoldingsDto holdingsDto = new TradersHoldingsDto();
        holdingsDto.setHoldings(List.of(holdingStock));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 1L))
                .thenReturn(holdingsDto);

        Map<Long, String> traderMap = new HashMap<>();
        traderMap.put(1L, "Trader");

        Map<Long, String> stockMap = new HashMap<>();
        stockMap.put(1L, "TCS");

        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);

        assertThrows(NotEnoughSharesException.class,
                () -> orderService.fillOrder(1L));

        verify(orderCancellationService)
                .cancelOrderInternal(1L);
    }


}