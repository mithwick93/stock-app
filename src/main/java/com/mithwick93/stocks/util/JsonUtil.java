package com.mithwick93.stocks.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Class to hold json related utils.
 *
 * @author mithwick93
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtil {
    private static final ObjectMapper Obj = new ObjectMapper();

    public static String getJsonString(Object object) {
        try {
            return Obj.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
