package com.mithwick93.stocks.exception;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockNotFoundExceptionTest {
    @Test
    public void StockNotFoundException_whenStockIdPassed_thenReturnStockNotFoundException() {
        long inputId = 12345L;
        String expectedResult = "Stock 12345 not found";

        StockNotFoundException resultStockNotFoundException = new StockNotFoundException(inputId);

        assertEquals(expectedResult, resultStockNotFoundException.getMessage());
    }

    @Test
    public void supplier_whenStockIdPassed_thenReturnStockNotFoundExceptionSupplier() {
        long inputId = 45678L;
        String expectedResult = "Stock 45678 not found";

        Supplier<StockNotFoundException> resultStockNotFoundExceptionSupplier = StockNotFoundException.supplier(inputId);

        assertEquals(expectedResult, resultStockNotFoundExceptionSupplier.get().getMessage());
    }
}