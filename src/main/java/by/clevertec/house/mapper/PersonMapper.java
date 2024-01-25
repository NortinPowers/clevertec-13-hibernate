package by.clevertec.house.mapper;

import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonPutRequestDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.mapper.annotation.CreateDateMapping;
import by.clevertec.house.mapper.annotation.UpdateDateMapping;
import by.clevertec.house.mapper.util.DateConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = PassportMapper.class)
public interface PersonMapper extends DateConverter {

    /**
     * Преобразует объект типа {@link Person} в объект {@link PersonResponseDto} с использованием
     * маппинга, определенного аннотациями {@link CreateDateMapping}, {@link UpdateDateMapping} и
     * аннотацией {@link Mapping} для преобразования поля "house.uuid" в "houseUuid".
     *
     * @param entity Объект типа {@link Person}, который требуется преобразовать в {@link PersonResponseDto}.
     * @return Объект {@link PersonResponseDto}, созданный на основе данных из объекта {@link Person}.
     */
    @CreateDateMapping
    @UpdateDateMapping
    @Mapping(source = "house.uuid", target = "houseUuid")
    PersonResponseDto toDto(Person entity);

    /**
     * Преобразует объект типа {@link PersonPutRequestDto} в объект {@link Person}.
     *
     * @param dto Объект типа {@link PersonPutRequestDto}, который требуется преобразовать в {@link Person}.
     * @return Объект {@link Person}, созданный на основе данных из объекта {@link PersonPutRequestDto}.
     */
    Person toDomain(PersonPutRequestDto dto);

    /**
     * Преобразует объект типа {@link PersonPathRequestDto} в объект {@link Person}.
     *
     * @param dto Объект типа {@link PersonPathRequestDto}, который требуется преобразовать в {@link Person}.
     * @return Объект {@link Person}, созданный на основе данных из объекта {@link PersonPathRequestDto}.
     */
    Person toDomain(PersonPathRequestDto dto);

    /**
     * Обновляет существующий объект типа {@link Person} данными из объекта {@link Person updated}.
     *
     * @param person  Объект типа {@link Person}, который требуется обновить.
     * @param updated Объект типа {@link Person}, содержащий обновленные данные.
     * @return Обновленный объект типа {@link Person}.
     */
    Person merge(@MappingTarget Person person, Person updated);
}
