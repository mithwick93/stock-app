package com.mithwick93.stocks.controller;

import com.mithwick93.stocks.controller.dto.StockRequestDto;
import com.mithwick93.stocks.controller.dto.StockResponseDto;
import com.mithwick93.stocks.core.IntegrationTest;
import com.mithwick93.stocks.modal.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static com.mithwick93.stocks.core.TestUtils.creatRequestStock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StockControllerIT extends IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    @Test
    public void createStock_whenCalledWithValidRequest_thenReturnCreatedResponseWithStock() {
        Stock stock = creatRequestStock();
        ResponseEntity<StockResponseDto> createResponse = createStock(stock);

        assertEquals(HttpStatusCode.valueOf(HttpStatus.CREATED.value()), createResponse.getStatusCode());
        assertEquals(stock.getName(), createResponse.getBody().getName());
        assertEquals(stock.getCurrentPrice(), createResponse.getBody().getCurrentPrice());
    }

    @Test
    public void getStocks_whenCalled_thenReturnOkResponseWithStocks() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ParameterizedTypeReference<PagedModel<StockResponseDto>> typeRef = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PagedModel<StockResponseDto>> response = restTemplate.exchange(createURLWithPort("/api/v1/stocks?page=0&size=10"), HttpMethod.GET, entity, typeRef);

        assertEquals(HttpStatusCode.valueOf(HttpStatus.OK.value()), response.getStatusCode());
        assertFalse(response.getBody().getContent().isEmpty());
    }

    @Test
    public void getStockById_whenCalledWithValidId_thenReturnOkResponseWithStock() {
        Stock stock = creatRequestStock();
        ResponseEntity<StockResponseDto> createResponse = createStock(stock);

        long id = createResponse.getBody().getId();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<StockResponseDto> getByIdResponse = restTemplate.exchange(createURLWithPort("/api/v1/stocks/" + id), HttpMethod.GET, entity, StockResponseDto.class);

        assertEquals(HttpStatusCode.valueOf(HttpStatus.OK.value()), getByIdResponse.getStatusCode());
        assertEquals(id, getByIdResponse.getBody().getId());
    }

    @Test
    public void getStockById_whenCalledWithInValidId_thenReturnNotFoundResponseWithNoBody() {
        long id = -1L;
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ProblemDetail> getByIdResponse = restTemplate.exchange(createURLWithPort("/api/v1/stocks/" + id), HttpMethod.GET, entity, ProblemDetail.class);

        assertEquals(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()), getByIdResponse.getStatusCode());
    }

    @Test
    public void updateStock_whenCalledWithValidRequest_thenReturnOkResponseWithUpdatedStock() {
        Stock stock = creatRequestStock();
        ResponseEntity<StockResponseDto> createResponse = createStock(stock);

        long id = createResponse.getBody().getId();

        StockRequestDto updateStockRequestDto = new StockRequestDto("new name", BigDecimal.ONE);

        HttpEntity<StockRequestDto> entity = new HttpEntity<>(updateStockRequestDto, headers);
        ResponseEntity<StockResponseDto> updateStockResponse = restTemplate.exchange(createURLWithPort("/api/v1/stocks/" + id), HttpMethod.PUT, entity, StockResponseDto.class);

        assertEquals(HttpStatusCode.valueOf(HttpStatus.OK.value()), updateStockResponse.getStatusCode());
        assertEquals(id, updateStockResponse.getBody().getId());
        assertEquals(updateStockRequestDto.getName(), updateStockResponse.getBody().getName());
        assertEquals(updateStockRequestDto.getCurrentPrice(), updateStockResponse.getBody().getCurrentPrice());

    }

    @Test
    public void deleteStock_whenCalledWithValidId_thenReturnNoContentResponseWithNoBody() {
        Stock stock = creatRequestStock();
        ResponseEntity<StockResponseDto> createResponse = createStock(stock);

        long id = createResponse.getBody().getId();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<?> deleteStockResponse = restTemplate.exchange(createURLWithPort("/api/v1/stocks/" + id), HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatusCode.valueOf(HttpStatus.NO_CONTENT.value()), deleteStockResponse.getStatusCode());
    }

    private ResponseEntity<StockResponseDto> createStock(Stock stock) {
        StockRequestDto stockRequestDto = new StockRequestDto(stock.getName(), stock.getCurrentPrice());

        HttpEntity<StockRequestDto> entity = new HttpEntity<>(stockRequestDto, headers);
        return restTemplate.exchange(createURLWithPort("/api/v1/stocks"), HttpMethod.POST, entity, StockResponseDto.class);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
