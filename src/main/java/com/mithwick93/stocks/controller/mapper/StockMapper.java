package com.mithwick93.stocks.controller.mapper;

import com.mithwick93.stocks.controller.dto.StockDto;
import com.mithwick93.stocks.modal.Stock;
import org.springframework.stereotype.Component;

/**
 * Mapper class to map Stock model to Stock DTO and vise versa.
 *
 * @author mithwick93
 */
@Component
public class StockMapper {

    /**
     * Map modal to dto.
     *
     * @param stock {@link Stock} to convert.
     * @return {@link StockDto}
     */
    public StockDto toDto(Stock stock) {
        return new StockDto(stock.getId(), stock.getName(), stock.getCurrentPrice(), stock.getLastUpdate());
    }

    /**
     * Map dto to modal.
     *
     * @param stockDto {@link StockDto} to convert.
     * @return {@link Stock}
     */
    public Stock toModal(StockDto stockDto) {
        return new Stock(stockDto.getId(), stockDto.getName(), stockDto.getCurrentPrice(), stockDto.getLastUpdate());
    }
}
