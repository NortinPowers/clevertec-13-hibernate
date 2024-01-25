package by.clevertec.house.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UuidValidator implements ConstraintValidator<ValidUuid, UUID> {

    /**
     * Переопределенный метод для проверки валидности значения типа {@link UUID}.
     *
     * @param value   Значение типа {@link UUID}, которое требуется проверить на валидность.
     * @param context Контекст ограничения, предоставляющий информацию о текущем исполнении.
     * @return {@code true}, если значение не является {@code null}, иначе {@code false}.
     */
    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        return value != null;
    }
}
