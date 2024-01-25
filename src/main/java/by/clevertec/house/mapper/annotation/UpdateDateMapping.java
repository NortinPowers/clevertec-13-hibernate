package by.clevertec.house.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Mapping;

/**
 * Аннотация для маппинга даты обновления (updated) с использованием выражения Java.
 * Используется для аннотирования методов в мапперах или других компонентах.
 *
 * <p>Пример использования:
 * <pre>
 * {@literal @}UpdateDateMapping
 * public String mapUpdateDateToIsoDate(Entity entity) {
 *     return convertToIsoDate(entity.getUpdateDate());
 * }
 * </pre>
 *
 * <p>Аннотация применяется к методам.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Mapping(target = "updated", expression = "java(convertToIsoDate(entity.getUpdateDate()))")
public @interface UpdateDateMapping {
}
