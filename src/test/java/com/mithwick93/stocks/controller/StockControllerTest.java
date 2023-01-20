package com.mithwick93.stocks.controller;

import com.mithwick93.stocks.controller.dto.StockRequestDto;
import com.mithwick93.stocks.controller.dto.StockResponseDto;
import com.mithwick93.stocks.controller.mapper.StockMapper;
import com.mithwick93.stocks.modal.Stock;
import com.mithwick93.stocks.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.mithwick93.stocks.core.TestUtils.creatRequestStock;
import static com.mithwick93.stocks.core.TestUtils.creatStock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StockControllerTest {
    @InjectMocks
    StockController stockController;

    @Mock
    private StockService stockService;

    @Mock
    private StockMapper stockMapper;

    @Mock
    private PagedResourcesAssembler<Stock> stockPagedResourcesAssembler;

    @Test
    public void getStocks_whenCorrectParametersPassed_thenReturnResponseEntity() {
        int page = 0;
        int size = 10;

        List<Stock> stocks = new ArrayList<>();
        Stock stock = creatStock();
        stocks.add(stock);
        Page<Stock> stocksPage = new PageImpl<>(stocks);

        StockResponseDto stockDto = new StockResponseDto(
                stock.getId(),
                stock.getName(),
                stock.getCurrentPrice(),
                stock.getCreatedAt().getTime(),
                stock.getLastUpdate().getTime()
        );
        List<StockResponseDto> stockDtos = new ArrayList<>();
        stockDtos.add(stockDto);
        PagedModel<StockResponseDto> stockResponseDtos = PagedModel.of(stockDtos, new PagedModel.PageMetadata(1, 0, 1, 1));

        Mockito.when(stockService.findAllStocks(page, size)).thenReturn(stocksPage);
        Mockito.when(stockPagedResourcesAssembler.toModel(stocksPage, stockMapper)).thenReturn(stockResponseDtos);

        ResponseEntity<PagedModel<StockResponseDto>> response = stockController.getStocks(page, size);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());

        Mockito.verify(stockService, times(1)).findAllStocks(page, size);
        Mockito.verify(stockPagedResourcesAssembler, times(1)).toModel(stocksPage, stockMapper);
    }

    @Test
    public void getStockById_whenCorrectParametersPassed_thenReturnResponseEntity() {
        long id = 1;

        Stock stock = creatStock();
        StockResponseDto stockDto = new StockResponseDto(
                stock.getId(),
                stock.getName(),
                stock.getCurrentPrice(),
                stock.getCreatedAt().getTime(),
                stock.getLastUpdate().getTime()
        );

        Mockito.when(stockService.findStockById(id)).thenReturn(stock);
        Mockito.when(stockMapper.toModel(stock)).thenReturn(stockDto);

        ResponseEntity<StockResponseDto> response = stockController.getStockById(id);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(stockDto, response.getBody());

        Mockito.verify(stockService, times(1)).findStockById(id);
        Mockito.verify(stockMapper, times(1)).toModel(stock);
    }

    @Test
    public void createStock_whenCorrectParametersPassed_thenReturnResponseEntity() {
        Stock stock = creatRequestStock();
        StockRequestDto stockRequestDto = new StockRequestDto(stock.getName(), stock.getCurrentPrice());

        Stock savedStock = creatStock();
        savedStock.setName(stock.getName());
        savedStock.setCurrentPrice(stock.getCurrentPrice());

        StockResponseDto stockDto = new StockResponseDto(
                savedStock.getId(),
                savedStock.getName(),
                savedStock.getCurrentPrice(),
                savedStock.getCreatedAt().getTime(),
                savedStock.getLastUpdate().getTime()
        );

        Mockito.when(stockMapper.toEntity(stockRequestDto)).thenReturn(stock);
        Mockito.when(stockService.createStock(stock)).thenReturn(savedStock);
        Mockito.when(stockMapper.toModel(savedStock)).thenReturn(stockDto);

        ResponseEntity<StockResponseDto> response = stockController.createStock(stockRequestDto);

        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertEquals(stockDto, response.getBody());

        Mockito.verify(stockMapper, times(1)).toEntity(stockRequestDto);
        Mockito.verify(stockService, times(1)).createStock(stock);
        Mockito.verify(stockMapper, times(1)).toModel(savedStock);
    }

    @Test
    public void updateStock_whenCorrectParametersPassed_thenReturnResponseEntity() {
        long id = 2;
        Stock stock = creatRequestStock();
        StockRequestDto stockRequestDto = new StockRequestDto(stock.getName(), stock.getCurrentPrice());

        Stock updatedStock = creatStock();
        updatedStock.setName(stock.getName());
        updatedStock.setCurrentPrice(stock.getCurrentPrice());

        StockResponseDto stockDto = new StockResponseDto(
                updatedStock.getId(),
                updatedStock.getName(),
                updatedStock.getCurrentPrice(),
                updatedStock.getCreatedAt().getTime(),
                updatedStock.getLastUpdate().getTime()
        );

        Mockito.when(stockMapper.toEntity(stockRequestDto)).thenReturn(stock);
        Mockito.when(stockService.updateStock(id, stock)).thenReturn(updatedStock);
        Mockito.when(stockMapper.toModel(updatedStock)).thenReturn(stockDto);

        ResponseEntity<StockResponseDto> response = stockController.updateStock(id, stockRequestDto);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(stockDto, response.getBody());

        Mockito.verify(stockMapper, times(1)).toEntity(stockRequestDto);
        Mockito.verify(stockService, times(1)).updateStock(id, stock);
        Mockito.verify(stockMapper, times(1)).toModel(updatedStock);
    }

    @Test
    public void deleteStock_whenCorrectParametersPassed_thenReturnResponseEntity() {
        long id = 3;

        ResponseEntity<?> response = stockController.deleteStock(id);

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());

    }

}