package com.orderbooking.stockportfolio.support;

import com.orderbooking.stockportfolio.entity.Basket;
import com.orderbooking.stockportfolio.entity.Holding;
import com.orderbooking.stockportfolio.entity.Order;
import com.orderbooking.stockportfolio.entity.Sector;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.entity.StockBasketMap;
import com.orderbooking.stockportfolio.entity.Trader;
import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;

import java.util.Date;

public final class EntityTestBuilders {

    private EntityTestBuilders() {
    }

    public static Sector sector(long id) {
        Sector s = new Sector();
        s.setId(id);
        s.setName("sector-" + id);
        return s;
    }

    public static Trader trader(long id) {
        Trader t = new Trader();
        t.setId(id);
        t.setName("trader-" + id);
        return t;
    }

    public static Basket basket(long id) {
        Basket b = new Basket();
        b.setId(id);
        b.setName("basket-" + id);
        return b;
    }

    public static Stock stock(long id, long sectorId) {
        Stock st = new Stock();
        st.setId(id);
        st.setName("stock-" + id);
        st.setPrice(10f);
        st.setSector(sector(sectorId));
        return st;
    }

    public static Stock stock(long id, String name, float price, long sectorId, Date createdAt, Date updatedAt) {
        Stock st = stock(id, sectorId);
        st.setName(name);
        st.setPrice(price);
        st.setCreatedAt(createdAt);
        st.setUpdatedAt(updatedAt);
        return st;
    }

    public static Holding holding(long id, long traderId, long stockId, long sectorId, int qty, Date updatedAt) {
        return new Holding(id, trader(traderId), stock(stockId, sectorId), sector(sectorId), null, qty, updatedAt);
    }

    public static StockBasketMap stockBasketMap(Long id, long basketId, long stockId, Date createdAt, Date updatedAt) {
        return new StockBasketMap(id, basket(basketId), stock(stockId, 1L), createdAt, updatedAt);
    }

    public static Order order(long id, long traderId, long stockId, long sectorId, int qty, float rate, float total,
                              TradeSide side, OrderStatus status, Date createdAt, Date updatedAt) {
        Sector sec = sector(sectorId);
        return new Order(id, trader(traderId), stock(stockId, sectorId), sec, qty, rate, total, side, status, createdAt, updatedAt);
    }
}
