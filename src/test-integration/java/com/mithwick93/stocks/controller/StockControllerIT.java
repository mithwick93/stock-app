package com.mithwick93.stocks.controller;

import com.mithwick93.stocks.controller.dto.StockRequestDto;
import com.mithwick93.stocks.controller.dto.StockResponseDto;
import com.mithwick93.stocks.core.IntegrationTest;
import com.mithwick93.stocks.modal.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static com.mithwick93.stocks.core.TestUtils.creatRequestStock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StockControllerIT extends IntegrationTest {

    @Autowired
    StockController stockController;

    @Test
    public void createStock_whenCalledWithValidRequest_thenReturnCreatedResponseWithStock() {
        Stock stock = creatRequestStock();
        ResponseEntity<StockResponseDto> createResponse = createStock(stock);

        assertEquals(HttpStatusCode.valueOf(201), createResponse.getStatusCode());
        assertEquals(stock.getName(), createResponse.getBody().getName());
        assertEquals(stock.getCurrentPrice(), createResponse.getBody().getCurrentPrice());
    }

    @Test
    public void getStocks_whenCalled_thenReturnOkResponseWithStocks() {
        ResponseEntity<PagedModel<StockResponseDto>> response = stockController.getStocks(0, 100);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertFalse(response.getBody().getContent().isEmpty());
    }

    @Test
    public void getStockById_whenCalledWithValidId_thenReturnOkResponseWithStock() {
        Stock stock = creatRequestStock();
        ResponseEntity<StockResponseDto> createResponse = createStock(stock);

        long id = createResponse.getBody().getId();
        ResponseEntity<StockResponseDto> getByIdResponse = stockController.getStockById(id);

        assertEquals(HttpStatusCode.valueOf(200), getByIdResponse.getStatusCode());
        assertEquals(id, getByIdResponse.getBody().getId());
    }

    @Test
    public void updateStock_whenCalledWithValidRequest_thenReturnOkResponseWithUpdatedStock() {
        Stock stock = creatRequestStock();
        ResponseEntity<StockResponseDto> createResponse = createStock(stock);

        long id = createResponse.getBody().getId();

        StockRequestDto updateStockRequestDto = new StockRequestDto("new name", BigDecimal.ONE);

        ResponseEntity<StockResponseDto> updateStockResponse = stockController.updateStock(id, updateStockRequestDto);

        assertEquals(HttpStatusCode.valueOf(200), updateStockResponse.getStatusCode());
        assertEquals(id, updateStockResponse.getBody().getId());
        assertEquals(updateStockRequestDto.getName(), updateStockResponse.getBody().getName());
        assertEquals(updateStockRequestDto.getCurrentPrice(), updateStockResponse.getBody().getCurrentPrice());

    }

    @Test
    public void deleteStock_whenCalledWithValidId_thenReturnNoContentResponseWithNoBody() {
        Stock stock = creatRequestStock();
        ResponseEntity<StockResponseDto> createResponse = createStock(stock);

        long id = createResponse.getBody().getId();
        ResponseEntity<?> deleteStockResponse = stockController.deleteStock(id);

        assertEquals(HttpStatusCode.valueOf(204), deleteStockResponse.getStatusCode());
    }

    private ResponseEntity<StockResponseDto> createStock(Stock stock) {
        StockRequestDto stockRequestDto = new StockRequestDto(stock.getName(), stock.getCurrentPrice());

        return stockController.createStock(stockRequestDto);
    }
}
