package by.clevertec.house.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Mapping;

/**
 * Аннотация для маппинга даты создания (created) с использованием выражения Java.
 * Используется для аннотирования методов в мапперах или других компонентах.
 *
 * <p>Пример использования:
 * <pre>
 * {@literal @}CreateDateMapping
 * public String mapCreateDateToIsoDate(Entity entity) {
 *     return convertToIsoDate(entity.getCreateDate());
 * }
 * </pre>
 *
 * <p>Аннотация применяется к методам.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Mapping(target = "created", expression = "java(convertToIsoDate(entity.getCreateDate()))")
public @interface CreateDateMapping {
}
