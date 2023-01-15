package com.mithwick93.stocks.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Stock DTO used to send stock response to clients.
 *
 * @author mithwick93
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDto extends RepresentationModel<StockResponseDto> {
    private Long id;
    private String name;
    private BigDecimal currentPrice;
    private Date createdAt;
    private Date lastUpdate;
}
