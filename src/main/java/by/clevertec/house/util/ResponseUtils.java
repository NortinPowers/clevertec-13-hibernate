package by.clevertec.house.util;

import by.clevertec.house.model.MessageResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class ResponseUtils {

    public static final String CREATION_MESSAGE = "The %s have been successful created";
    public static final String UPDATE_MESSAGE = "The %s have been successful updated";
    public static final String ADDED_HOUSE_OWNER_MESSAGE = "The new owner has been successfully added to the house (uuid:%s)";
    public static final String DELETED_HOUSE_OWNER_MESSAGE = "The owner has been successfully deleted to the house (uuid:%s)";
    public static final String PERSON_CHANGE_HOUSE_MESSAGE = "The place of residence for the person (uuid:%s) has been successfully changed";
    public static final String DELETION_MESSAGE = "The %s have been successful deleted";
    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE = "The transmitted data did not pass verification";
    public static final String HTTP_NOT_READABLE_EXCEPTION_MESSAGE = "The entered data is incorrect and leads to an error";
    public static final String CONDITIONAL_EXCEPTION_MESSAGE = "Сan not delete a house in which at least 1 person lives";
    public static final String CONDITIONAL_HOUSE_OWNER_EXIST_EXCEPTION_MESSAGE = "The person is already the owner of the house";
    public static final String CONDITIONAL_RESIDENT_EXIST_EXCEPTION_MESSAGE = "The person already lives in this house";
    public static final String CONDITIONAL_HOUSE_OWNER_NOT_EXIST_EXCEPTION_MESSAGE = "The person is not the owner of the house";

    /**
     * Возвращает объект {@link MessageResponse} для успешного ответа с указанным сообщением,
     * кодом состояния HTTP OK и именем класса, полученным из переданного класса.
     *
     * @param message Сообщение для включения в ответ.
     * @param clazz   Класс, используемый для формирования имени в ответе.
     * @return Объект {@link MessageResponse} для успешного ответа.
     */
    public static MessageResponse getSuccessResponse(String message, Class<?> clazz) {
        return new MessageResponse(HttpStatus.OK.value(), String.format(message, clazz.getSimpleName().toLowerCase()), clazz);
    }

    /**
     * Возвращает объект {@link MessageResponse} для успешного ответа с указанным сообщением и
     * кодом состояния HTTP OK, предоставленным в виде строки.
     *
     * @param message Сообщение для включения в ответ.
     * @param value   Имя, используемое для формирования ответа.
     * @return Объект {@link MessageResponse} для успешного ответа.
     */
    public static MessageResponse getSuccessResponse(String message, String value) {
        return new MessageResponse(HttpStatus.OK.value(), String.format(message, value.toLowerCase()), value);
    }
}
