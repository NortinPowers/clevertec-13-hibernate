package by.clevertec.house.exception;

import static by.clevertec.house.util.ResponseUtils.DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.NOT_FOUND_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.OTHER_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.UNIQUE_CONSTRAINT_VIOLATION_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.getErrorValidationMessages;
import static by.clevertec.house.util.ResponseUtils.getExceptionResponse;

import by.clevertec.house.model.BaseResponse;
import by.clevertec.house.model.ErrorValidationResponse;
import by.clevertec.house.model.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение {@link EntityNotFoundException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link EntityNotFoundException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP NOT_FOUND.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse> handleException(EntityNotFoundException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.NOT_FOUND,
                NOT_FOUND_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение {@link CustomEntityNotFoundException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link CustomEntityNotFoundException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP NOT_FOUND.
     */
    @ExceptionHandler(CustomEntityNotFoundException.class)
    public ResponseEntity<BaseResponse> handleException(CustomEntityNotFoundException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение {@link CustomNoContentException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link CustomNoContentException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP GONE.
     */
    @ExceptionHandler(CustomNoContentException.class)
    public ResponseEntity<BaseResponse> handleException(CustomNoContentException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.GONE,
                exception.getMessage(),
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.GONE);
    }

    /**
     * Обрабатывает исключение {@link IllegalArgumentException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link IllegalArgumentException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP BAD_REQUEST.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse> handleException(IllegalArgumentException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<BaseResponse> handleException(MethodArgumentNotValidException exception) {
        ErrorValidationResponse errorValidationResponse = new ErrorValidationResponse(
                HttpStatus.BAD_REQUEST,
                getErrorValidationMessages(exception),
                METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE
        );
        return new ResponseEntity<>(errorValidationResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключение {@link MethodArgumentNotValidException} и возвращает соответствующий ResponseEntity с {@link ErrorValidationResponse}.
     *
     * @param exception Исключение {@link MethodArgumentNotValidException}, которое требуется обработать.
     * @return ResponseEntity с {@link ErrorValidationResponse} и кодом состояния HTTP BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse> handleException(MethodArgumentTypeMismatchException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid UUID format: " + exception.getValue(),
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключение {@link PersistenceException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link PersistenceException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP CONFLICT.
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<BaseResponse> handleException(PersistenceException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.CONFLICT,
                UNIQUE_CONSTRAINT_VIOLATION_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Обрабатывает исключение {@link ConditionalException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link ConditionalException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP NOT_ACCEPTABLE.
     */
    @ExceptionHandler(ConditionalException.class)
    public ResponseEntity<BaseResponse> handleException(ConditionalException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.NOT_ACCEPTABLE,
                exception.getMessage(),
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Обрабатывает исключение {@link DataIntegrityViolationException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link DataIntegrityViolationException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<BaseResponse> handleException(DataIntegrityViolationException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обрабатывает исключение {@link Exception} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link Exception}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleException(Exception exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                OTHER_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
