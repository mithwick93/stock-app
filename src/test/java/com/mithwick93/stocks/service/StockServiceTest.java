package com.mithwick93.stocks.service;

import com.mithwick93.stocks.dal.repository.StockRepository;
import com.mithwick93.stocks.exception.StockNotFoundException;
import com.mithwick93.stocks.modal.Stock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mithwick93.stocks.core.TestUtils.creatRequestStock;
import static com.mithwick93.stocks.core.TestUtils.creatStock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @InjectMocks
    StockService stockService;

    @Mock
    StockRepository stockRepository;

    @Test
    public void findAllStocks_whenPageParametersPassed_thenReturnPageableResult() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        List<Stock> stocks = new ArrayList<>();
        stocks.add(creatStock());
        Page<Stock> stocksPage = new PageImpl<>(stocks);

        Mockito.when(stockRepository.findAll(pageable)).thenReturn(stocksPage);

        Page<Stock> resultStocksPage = stockService.findAllStocks(page, size);

        Mockito.verify(stockRepository, times(1)).findAll(pageable);
        assertEquals(stocks.size(), resultStocksPage.getContent().size());
    }

    @Test
    public void findStockById_whenCorrectIdPassed_thenReturnStockResult() {
        long id = 1234L;
        Stock expectedStock = creatStock(id);
        Optional<Stock> stockOptional = Optional.of(expectedStock);

        Mockito.when(stockRepository.findById(id)).thenReturn(stockOptional);

        Stock resultStock = stockService.findStockById(id);

        Mockito.verify(stockRepository, times(1)).findById(id);
        assertEquals(expectedStock, resultStock);
    }

    @Test
    public void findStockById_whenIncorrectIdPassed_thenThrowStockNotFoundException() {
        long id = -1L;
        String expectedErrorMessage = "Stock -1 not found";
        Optional<Stock> stockOptional = Optional.empty();

        Mockito.when(stockRepository.findById(id)).thenReturn(stockOptional);

        StockNotFoundException thrown = assertThrows(StockNotFoundException.class, () -> {
            stockService.findStockById(id);
        });

        Mockito.verify(stockRepository, times(1)).findById(id);
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }

    @Test
    public void createStock_whenCorrectStockRequestPassed_thenReturnCreatedStock() {
        long id = 1234L;
        Stock requestStock = creatRequestStock();
        Stock expectedStock = creatStock(id);
        expectedStock.setName(requestStock.getName());
        expectedStock.setCurrentPrice(requestStock.getCurrentPrice());

        Mockito.when(stockRepository.save(requestStock)).thenReturn(expectedStock);

        Stock resultStock = stockService.createStock(requestStock);

        Mockito.verify(stockRepository, times(1)).save(requestStock);
        assertEquals(expectedStock, resultStock);
    }

    @Test
    void updateStock_whenStockExist_thenReturnUpdatedStock() {
        long id = 1234L;
        Stock existingStock = creatStock(id);
        Optional<Stock> stockOptional = Optional.of(existingStock);

        Stock requestStock = creatRequestStock();

        Stock expectedStock = new Stock(existingStock.getId(), requestStock.getName(), requestStock.getCurrentPrice(), existingStock.getCreatedAt(), new Timestamp(789536));

        Mockito.when(stockRepository.findById(id)).thenReturn(stockOptional);
        Mockito.when(stockRepository.save(Mockito.any())).thenReturn(expectedStock);

        Stock resultStock = stockService.updateStock(id, requestStock);

        assertEquals(expectedStock, resultStock);
    }

    @Test
    void updateStock_whenStockNotExist_thenThrowStockNotFoundException() {
        long id = -2L;
        String expectedErrorMessage = "Stock -2 not found";
        Optional<Stock> stockOptional = Optional.empty();
        Stock requestStock = creatRequestStock();

        Mockito.when(stockRepository.findById(id)).thenReturn(stockOptional);

        StockNotFoundException thrown = assertThrows(StockNotFoundException.class, () -> {
            stockService.updateStock(id, requestStock);
        });

        Mockito.verify(stockRepository, times(1)).findById(id);
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }

    @Test
    void deleteStock_whenStockExist_thenReturnNothing() {
        long id = 1234L;
        Stock existingStock = creatStock(id);
        Optional<Stock> stockOptional = Optional.of(existingStock);

        Mockito.when(stockRepository.findById(id)).thenReturn(stockOptional);

        stockService.deleteStock(id);

        Mockito.verify(stockRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteStock_whenStockNotExist_thenThrowStockNotFoundException() {
        long id = -3L;
        String expectedErrorMessage = "Stock -3 not found";
        Optional<Stock> stockOptional = Optional.empty();

        Mockito.when(stockRepository.findById(id)).thenReturn(stockOptional);

        StockNotFoundException thrown = assertThrows(StockNotFoundException.class, () -> {
            stockService.deleteStock(id);
        });

        Mockito.verify(stockRepository, times(1)).findById(id);
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}