package by.clevertec.util;

import by.clevertec.model.ExceptionResponse;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@UtilityClass
public class ResponseUtils {

    public static final String NOT_FOUND_EXCEPTION_MESSAGE = "Specify the entered data";
    public static final String OTHER_EXCEPTION_MESSAGE = "Unexpected error";
    public static final String DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE = "The input data does not correspond to the required";
    public static final String UNIQUE_CONSTRAINT_VIOLATION_EXCEPTION_MESSAGE = "Unique constraint violation";
    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE = "The transmitted data did not pass verification";
    public static final String HTTP_NOT_READABLE_EXCEPTION_MESSAGE = "The entered data is incorrect and leads to an error";

    /**
     * Возвращает объект {@link ExceptionResponse} для ответа с ошибкой, содержащий
     * указанный статус, сообщение и имя класса исключения.
     *
     * @param status    Статус ответа с ошибкой.
     * @param message   Сообщение для включения в ответ.
     * @param exception Исключение, используемое для получения имени класса.
     * @return Объект {@link ExceptionResponse} для ответа с ошибкой.
     */
    public static ExceptionResponse getExceptionResponse(HttpStatus status, String message, Exception exception) {
        return new ExceptionResponse(status, message, exception.getClass().getSimpleName());
    }

    /**
     * Возвращает список строк, представляющих сообщения об ошибках валидации
     * для объекта {@link MethodArgumentNotValidException}.
     *
     * @param exception Исключение типа {@link MethodArgumentNotValidException}.
     * @return Список строк с сообщениями об ошибках валидации.
     */
    public static List<String> getErrorValidationMessages(MethodArgumentNotValidException exception) {
        return exception.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }
}
