package com.mithwick93.stocks.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Null
    private Long id;

    @NotEmpty(message = "Stock name is required")
    private String name;

    @NotNull(message = "Stock price is required")
    @Positive(message = "Stock price requires a positive number")
    private BigDecimal currentPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Null
    private Date lastUpdate;

}
