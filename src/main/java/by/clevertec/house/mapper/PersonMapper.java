package by.clevertec.house.mapper;

import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonRequestDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.mapper.annotation.CreateDateMapping;
import by.clevertec.house.mapper.annotation.UpdateDateMapping;
import by.clevertec.house.mapper.util.DateConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CustomMapperConfig.class,
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
     * Преобразует объект типа {@link PersonRequestDto} в объект {@link Person}.
     *
     * @param dto Объект типа {@link PersonRequestDto}, который требуется преобразовать в {@link Person}.
     * @return Объект {@link Person}, созданный на основе данных из объекта {@link PersonRequestDto}.
     */
    Person toDomain(PersonRequestDto dto);

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
