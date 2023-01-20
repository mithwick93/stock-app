package com.mithwick93.stocks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mithwick93.stocks.controller.dto.StockRequestDto;
import com.mithwick93.stocks.controller.dto.StockResponseDto;
import com.mithwick93.stocks.controller.mapper.StockMapper;
import com.mithwick93.stocks.exception.StockNotFoundException;
import com.mithwick93.stocks.modal.Stock;
import com.mithwick93.stocks.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.mithwick93.stocks.core.TestUtils.creatRequestStock;
import static com.mithwick93.stocks.core.TestUtils.creatStock;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StockService stockService;

    @MockBean
    private StockMapper stockMapper;

    @MockBean
    private PagedResourcesAssembler<Stock> stockPagedResourcesAssembler;

    @Test
    public void getStocks_whenCorrectParametersPassed_thenReturnResponseEntity() throws Exception {

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

        Mockito.when(stockService.findAllStocks(Mockito.anyInt(), Mockito.anyInt())).thenReturn(stocksPage);
        Mockito.when(stockPagedResourcesAssembler.toModel(stocksPage, stockMapper)).thenReturn(stockResponseDtos);

        mockMvc.perform(get("/api/v1/stocks"))
                .andExpect(status().isOk());

        Mockito.verify(stockService, times(1)).findAllStocks(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void getStocks_whenIncorrectParametersPassed_thenReturnResponseEntity() throws Exception {
        int page = 0;
        int size = -10;

        mockMvc.perform(get("/api/v1/stocks?page={page}&size={size}", page, size))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getStockById_whenCorrectParametersPassed_thenReturnResponseEntity() throws Exception {
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

        mockMvc.perform(get("/api/v1/stocks/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(stockService, times(1)).findStockById(id);
    }

    @Test
    public void getStockById_whenStockNotFound_thenReturnNotFound() throws Exception {
        long id = 1;

        Mockito.when(stockService.findStockById(id)).thenThrow(new StockNotFoundException(id));

        mockMvc.perform(get("/api/v1/stocks/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(stockService, times(1)).findStockById(id);
    }

    @Test
    public void createStock_whenCorrectParametersPassed_thenReturnResponseEntity() throws Exception {
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

        mockMvc.perform(post("/api/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(stockRequestDto))
                )
                .andExpect(status().isCreated());

        Mockito.verify(stockService, times(1)).createStock(stock);
    }

    @Test
    public void createStock_whenIncorrectParametersPassed_thenReturnResponseEntity() throws Exception {
        Stock stock = creatRequestStock();
        stock.setName(null);
        StockRequestDto stockRequestDto = new StockRequestDto(stock.getName(), stock.getCurrentPrice());

        mockMvc.perform(post("/api/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(stockRequestDto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateStock_whenCorrectParametersPassed_thenReturnResponseEntity() throws Exception {
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

        mockMvc.perform(put("/api/v1/stocks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(stockRequestDto))
                )
                .andExpect(status().isOk());

        Mockito.verify(stockService, times(1)).updateStock(id, stock);
    }

    @Test
    public void deleteStock_whenCorrectParametersPassed_thenReturnResponseEntity() throws Exception {
        long id = 3;

        mockMvc.perform(delete("/api/v1/stocks/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(stockService, times(1)).deleteStock(id);

    }

}