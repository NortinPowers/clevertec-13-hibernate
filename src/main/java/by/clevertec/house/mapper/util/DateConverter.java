package by.clevertec.house.mapper.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface DateConverter {

    /**
     * Конвертирует объект типа LocalDateTime в строку в формате ISO-8601.
     *
     * <p>Пример использования:
     * <pre>
     * LocalDateTime dateTime = LocalDateTime.now();
     * String isoDateString = convertToIsoDate(dateTime);
     * </pre>
     *
     * @param date Объект типа {@link LocalDateTime}, который требуется конвертировать.
     * @return Строка, представляющая дату в формате ISO-8601.
     */
    default String convertToIsoDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
