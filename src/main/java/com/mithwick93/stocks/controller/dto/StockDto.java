package com.mithwick93.stocks.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
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
    @Null(message = "Stock id not allowed in request")
    private Long id;

    @NotEmpty(message = "Stock name is required")
    @Size(max = 255, message = "Stock name max length should be <= 255")
    private String name;

    @NotNull(message = "Stock price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Stock price requires a positive number")
    @Digits(integer = 19, fraction = 4, message = "Stock price is beyond accepted range of decimal(19, 4)")
    private BigDecimal currentPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Null(message = "Stock last update not allowed in request")
    private Date lastUpdate;

}
