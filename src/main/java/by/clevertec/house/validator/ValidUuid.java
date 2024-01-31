package by.clevertec.house.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Пользовательская аннотация для проверки валидности значения типа UUID.
 * Применяется к полям класса и использует валидатор {@link UuidValidator}.
 *
 * <p>Пример использования:
 * <pre>
 * {@code @ValidUuid}
 * private UUID myUuid;
 * </pre>
 *
 * <p>Данная аннотация помечает поле, которое должно содержать корректный идентификатор UUID.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
public @interface ValidUuid {

    String message() default "Invalid uuid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
