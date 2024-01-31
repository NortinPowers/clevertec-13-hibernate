package by.clevertec.house.util;

import static by.clevertec.house.util.ResponseUtils.getSuccessResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import by.clevertec.house.model.ExceptionResponse;
import by.clevertec.house.model.MessageResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

    @Test
    void getExceptionResponseShouldReturnExceptionResponseByTransmittedError() {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Something went wrong";
        Exception exception = new NullPointerException();

        ExceptionResponse actual = ResponseUtils.getExceptionResponse(status, message, exception);

        assertNotNull(actual.getTimestamp());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actual.getStatus());
        assertEquals(message, actual.getMessage());
        assertEquals(exception.getClass().getSimpleName(), actual.getType());
    }

    @Test
    void getErrorValidationMessagesShouldReturnErrorValidationMessagesDependedOnBindingResult() {
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, new BeanPropertyBindingResult(null, "objectName"));
        exception.getBindingResult().addError(new FieldError("objectName", "fieldName", "Field is required"));
        exception.getBindingResult().addError(new FieldError("objectName", "otherField", "Other field is invalid"));

        List<String> errorMessages = ResponseUtils.getErrorValidationMessages(exception);

        assertThat(errorMessages)
                .containsExactly("Field is required", "Other field is invalid");
    }
}
