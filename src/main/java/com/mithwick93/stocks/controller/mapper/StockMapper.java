package com.mithwick93.stocks.controller.mapper;

import com.mithwick93.stocks.Constants;
import com.mithwick93.stocks.controller.StockController;
import com.mithwick93.stocks.controller.dto.StockRequestDto;
import com.mithwick93.stocks.controller.dto.StockResponseDto;
import com.mithwick93.stocks.modal.Stock;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Mapper class to map Stock model to Stock DTO and vise versa.
 *
 * @author mithwick93
 */
@Component
public class StockMapper extends RepresentationModelAssemblerSupport<Stock, StockResponseDto> {
    public StockMapper() {
        super(StockController.class, StockResponseDto.class);
    }

    /**
     * Map modal to DTO.
     *
     * @param stock {@link Stock} to convert.
     * @return {@link StockResponseDto}
     */
    @Override
    public StockResponseDto toModel(Stock stock) {
        StockResponseDto stockDto = new StockResponseDto(
                stock.getId(),
                stock.getName(),
                stock.getCurrentPrice(),
                stock.getCreatedAt().getTime(),
                stock.getLastUpdate().getTime()
        );

        stockDto.add(linkTo(methodOn(StockController.class).getStockById(stock.getId())).withSelfRel());
        stockDto.add(linkTo(methodOn(StockController.class).getStocks(Integer.parseInt(Constants.DEFAULT_PAGE_NUMBER), Integer.parseInt(Constants.DEFAULT_PAGE_SIZE))).withRel("stocks"));
        return stockDto;
    }

    /**
     * Map DTO to entity.
     *
     * @param stockRequestDto {@link StockRequestDto} to convert.
     * @return {@link Stock}
     */
    public Stock toEntity(StockRequestDto stockRequestDto) {
        Stock stock = new Stock();
        stock.setName(stockRequestDto.getName());
        stock.setCurrentPrice(stockRequestDto.getCurrentPrice());

        return stock;
    }
}
