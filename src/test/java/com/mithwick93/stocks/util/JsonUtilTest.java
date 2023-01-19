package com.mithwick93.stocks.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mithwick93.stocks.controller.dto.StockResponseDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonUtilTest {
    @Test
    public void getJsonString_whenCorrectObjectPassed_thenReturnString() throws JsonProcessingException {
        Object inputObject = new StockResponseDto();
        String expectedResult = "{\"id\":null,\"name\":null,\"currentPrice\":null,\"createdAt\":0,\"lastUpdate\":0,\"links\":[]}";

        String result = JsonUtil.getJsonString(inputObject);

        assertEquals(expectedResult, result);
    }
}