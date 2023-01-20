package com.mithwick93.stocks.controller.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockRequestDtoTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void name_whenNull_thenThrowError() {
        StockRequestDto stockRequestDto = new StockRequestDto(null, BigDecimal.TEN);

        Set<ConstraintViolation<StockRequestDto>> violations = validator.validate(stockRequestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void name_whenEmpty_thenThrowError() {
        StockRequestDto stockRequestDto = new StockRequestDto("", BigDecimal.TEN);

        Set<ConstraintViolation<StockRequestDto>> violations = validator.validate(stockRequestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void name_whenMoreThanMaxLength_thenThrowError() {
        StockRequestDto stockRequestDto = new StockRequestDto(
                "qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm1234567890",
                BigDecimal.TEN
        );

        Set<ConstraintViolation<StockRequestDto>> violations = validator.validate(stockRequestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void name_whenValidNameAndPrice_thenNoErrors() {
        StockRequestDto stockRequestDto = new StockRequestDto("MSW", BigDecimal.TEN);

        Set<ConstraintViolation<StockRequestDto>> violations = validator.validate(stockRequestDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void currentPrice_whenNull_thenThrowError() {
        StockRequestDto stockRequestDto = new StockRequestDto("MSW", null);

        Set<ConstraintViolation<StockRequestDto>> violations = validator.validate(stockRequestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void currentPrice_whenLessThanMin_thenThrowError() {
        StockRequestDto stockRequestDto = new StockRequestDto("MSW", BigDecimal.valueOf(-4.5));

        Set<ConstraintViolation<StockRequestDto>> violations = validator.validate(stockRequestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void currentPrice_whenPriceIsZero_thenThrowError() {
        StockRequestDto stockRequestDto = new StockRequestDto("MSW", BigDecimal.valueOf(0.0));

        Set<ConstraintViolation<StockRequestDto>> violations = validator.validate(stockRequestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void currentPrice_whenOutSizePrecision_thenThrowError() {
        StockRequestDto stockRequestDto = new StockRequestDto("MSW", BigDecimal.valueOf(4.53646356));

        Set<ConstraintViolation<StockRequestDto>> violations = validator.validate(stockRequestDto);
        assertFalse(violations.isEmpty());
    }
}