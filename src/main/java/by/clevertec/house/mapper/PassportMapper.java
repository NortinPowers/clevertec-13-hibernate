package by.clevertec.house.mapper;

import by.clevertec.house.domain.Passport;
import by.clevertec.house.dto.PassportDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PassportMapper {

    /**
     * Преобразует объект типа {@link Passport} в объект {@link PassportDto}.
     *
     * @param passport Объект типа {@link Passport}, который требуется преобразовать в {@link PassportDto}.
     * @return Объект {@link PassportDto}, созданный на основе данных из объекта {@link Passport}.
     */
    PassportDto toDto(Passport passport);

    /**
     * Преобразует объект типа {@link PassportDto} в объект {@link Passport}.
     *
     * @param dto Объект типа {@link PassportDto}, который требуется преобразовать в {@link Passport}.
     * @return Объект {@link Passport}, созданный на основе данных из объекта {@link PassportDto}.
     */
    Passport toDomain(PassportDto dto);

}
