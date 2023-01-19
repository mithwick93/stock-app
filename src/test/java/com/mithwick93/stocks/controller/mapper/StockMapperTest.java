package com.mithwick93.stocks.controller.mapper;

import com.mithwick93.stocks.controller.dto.StockRequestDto;
import com.mithwick93.stocks.controller.dto.StockResponseDto;
import com.mithwick93.stocks.modal.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockMapperTest {
    private StockMapper stockMapper;

    @BeforeEach
    public void init() {
        stockMapper = new StockMapper();
    }

    @Test
    public void toModel_WhenStockPassed_thenReturnCorrectStockResponseDto() {
        Stock inputStock = new Stock();
        inputStock.setId(1L);
        inputStock.setName("MSW");
        inputStock.setCurrentPrice(BigDecimal.valueOf(12.4));
        inputStock.setCreatedAt(new Timestamp(12345));
        inputStock.setLastUpdate(new Timestamp(67890));

        StockResponseDto expectedStockResponseDto = new StockResponseDto();
        expectedStockResponseDto.setId(inputStock.getId());
        expectedStockResponseDto.setName(inputStock.getName());
        expectedStockResponseDto.setCurrentPrice(inputStock.getCurrentPrice());
        expectedStockResponseDto.setCreatedAt(inputStock.getCreatedAt().getTime());
        expectedStockResponseDto.setLastUpdate(inputStock.getLastUpdate().getTime());

        StockResponseDto resultStockResponseDto = stockMapper.toModel(inputStock);

        assertEquals(expectedStockResponseDto, resultStockResponseDto);
    }

    @Test
    public void toEntity_WhenStockRequestDtoPassed_thenReturnCorrectStock() {
        StockRequestDto inputStockRequestDto = new StockRequestDto();
        inputStockRequestDto.setName("MSW");
        inputStockRequestDto.setCurrentPrice(BigDecimal.valueOf(1045.6));

        Stock expectedStock = new Stock();
        expectedStock.setName(inputStockRequestDto.getName());
        expectedStock.setCurrentPrice(inputStockRequestDto.getCurrentPrice());

        Stock resultStock = stockMapper.toEntity(inputStockRequestDto);

        assertEquals(expectedStock, resultStock);
    }
}