package com.mithwick93.stocks.core;

import com.mithwick93.stocks.modal.Stock;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TestUtils {
    private TestUtils() {

    }

    public static Stock creatStock() {
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setName("MSW");
        stock.setCurrentPrice(BigDecimal.valueOf(12.4));
        stock.setCreatedAt(new Timestamp(12345));
        stock.setLastUpdate(new Timestamp(67890));

        return stock;
    }

    public static Stock creatStock(long id) {
        Stock stock = creatStock();
        stock.setId(id);

        return stock;
    }

    public static Stock creatRequestStock() {
        Stock stock = new Stock();
        stock.setName("MSW");
        stock.setCurrentPrice(BigDecimal.valueOf(12456));

        return stock;
    }
}
