package com.mithwick93.stocks.dal.repository;

import com.mithwick93.stocks.modal.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void save_whenStockPassed_thenReturnSavedStock() {
        Stock stock = new Stock();
        stock.setName("Test_create");
        stock.setCurrentPrice(BigDecimal.ONE);
        Stock saveResult = stockRepository.save(stock);

        assertEquals(stock.getName(), saveResult.getName());
        assertEquals(stock.getCurrentPrice(), saveResult.getCurrentPrice());
    }

    @Test
    public void findAll_whenPagePassed_thenReturnPageWithStocks() {
        Stock stock = new Stock();
        stock.setName("Test_findAll");
        stock.setCurrentPrice(BigDecimal.TEN);
        stockRepository.save(stock);

        Pageable pageable = PageRequest.of(0, 100);

        Page<Stock> allStocksResult = stockRepository.findAll(pageable);

        assertEquals(stock.getName(), allStocksResult.getContent().get(0).getName());
    }

    @Test
    public void findById_whenIdPassed_thenReturnStock() {
        Stock stock = new Stock();
        stock.setName("Test_findById");
        stock.setCurrentPrice(BigDecimal.TEN);
        Stock saveResult = stockRepository.save(stock);

        Optional<Stock> getStockByIdResult = stockRepository.findById(saveResult.getId());

        assertFalse(getStockByIdResult.isEmpty());
        assertEquals(saveResult.getId(), getStockByIdResult.get().getId());
        assertEquals(stock.getName(), getStockByIdResult.get().getName());
        assertEquals(stock.getCurrentPrice(), getStockByIdResult.get().getCurrentPrice());
    }

    @Test
    public void findById_whenWrongIdPassed_thenReturnNoData() {
        Long id = -1L;
        Optional<Stock> getStockByIdResult = stockRepository.findById(id);

        assertTrue(getStockByIdResult.isEmpty());
    }

    @Test
    public void update_whenUpdatePassedForExistingStock_thenReturnUpdatedStock() {
        Stock stock = new Stock();
        stock.setName("Test_update");
        stock.setCurrentPrice(BigDecimal.TEN);
        Stock saveResult = stockRepository.save(stock);

        saveResult.setCurrentPrice(BigDecimal.valueOf(100.6));

        Stock updateStockResult = stockRepository.save(saveResult);

        assertEquals(saveResult.getId(), updateStockResult.getId());
        assertEquals(stock.getName(), updateStockResult.getName());
        assertEquals(saveResult.getCurrentPrice(), updateStockResult.getCurrentPrice());
    }

    @Test
    public void deleteById_whenIdPassed_thenReturnNothing() {
        Stock stock = new Stock();
        stock.setName("Test_deleteById");
        stock.setCurrentPrice(BigDecimal.TEN);
        Stock saveResult = stockRepository.save(stock);

        stockRepository.deleteById(saveResult.getId());
    }

    @Test
    public void deleteById_whenInvalidIdPassed_thenReturnException() {
        Long id = -1L;
        assertThrows(EmptyResultDataAccessException.class, () -> stockRepository.deleteById(id));
    }

}