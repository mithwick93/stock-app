package com.mithwick93.stocks.exception;

import com.mithwick93.stocks.modal.Stock;

import java.util.function.Supplier;

/**
 * Exception to indicate {@link Stock} was not found.
 *
 * @author mithwick93
 */
public class StockNotFoundException extends RuntimeException {

    private static final String STOCK_NOT_FOUND_MESSAGE = "Stock %d not found";

    public StockNotFoundException(long id) {
        super(String.format(STOCK_NOT_FOUND_MESSAGE, id));
    }

    public static Supplier<StockNotFoundException> supplier(long id) {
        return () -> new StockNotFoundException(id);
    }

}
