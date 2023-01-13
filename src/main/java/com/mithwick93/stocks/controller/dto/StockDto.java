package com.mithwick93.stocks.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Stock DTO used to communicate with clients.
 *
 * @author mithwick93
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {
    private static final int STOCK_MIN_PRICE_VALUE = 0;
    @Null
    private Long id;

    @NotEmpty(message = "Stock name is required")
    private String name;

    @NotNull(message = "Stock price is required")
    @Min(value = STOCK_MIN_PRICE_VALUE, message = "Stock price requires a positive number")
    private BigDecimal currentPrice;

    @Null
    private Date lastUpdate;

}
