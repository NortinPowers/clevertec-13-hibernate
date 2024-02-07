package by.clevertec.house.util;

import static by.clevertec.house.util.ResponseUtils.getSuccessResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import by.clevertec.house.model.MessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ResponseUtilsTest {

    @Test
    void getSuccessResponseWithClassParameterShouldReturnNewResponse() {
        String message = "Success for %s";
        Class<?> clazz = Object.class;

        MessageResponse actual = getSuccessResponse(message, clazz);

        assertEquals(HttpStatus.OK.value(), actual.getStatus());
        assertEquals(String.format(message, clazz.getSimpleName().toLowerCase()), actual.getMessage());
        assertEquals(clazz, actual.getObject());

    }

    @Test
    void getSuccessResponseWithStringParameterShouldReturnNewResponse() {
        String message = "Success for %s!";
        String value = "Object";

        MessageResponse actual = ResponseUtils.getSuccessResponse(message, value);

        assertEquals(HttpStatus.OK.value(), actual.getStatus());
        assertEquals(String.format(message, value.toLowerCase()), actual.getMessage());
        assertEquals(value, actual.getObject());
    }
}
