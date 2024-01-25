package by.clevertec.house.mapper;

import by.clevertec.house.domain.House;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.mapper.annotation.CreateDateMapping;
import by.clevertec.house.mapper.util.DateConverter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CustomMapperConfig.class)
public interface HouseMapper extends DateConverter {

    /**
     * Преобразует объект типа {@link House} в объект {@link HouseResponseDto} с использованием
     * маппинга, определенного аннотацией {@link CreateDateMapping}.
     *
     * @param entity Объект типа {@link House}, который требуется преобразовать в {@link HouseResponseDto}.
     * @return Объект {@link HouseResponseDto}, созданный на основе данных из объекта {@link House}.
     */
    @CreateDateMapping
    HouseResponseDto toDto(House entity);

    /**
     * Преобразует объект типа {@link HouseRequestDto} в объект {@link House} с учетом
     * необходимых преобразований и валидации данных.
     *
     * @param dto Объект типа {@link HouseRequestDto}, который требуется преобразовать в {@link House}.
     * @return Объект {@link House}, созданный на основе данных из объекта {@link HouseRequestDto}.
     */
    House toDomain(HouseRequestDto dto);

    /**
     * Обновляет существующий объект типа {@link House} данными из объекта {@link House updated}.
     *
     * @param house   Объект типа {@link House}, который требуется обновить.
     * @param updated Объект типа {@link House}, содержащий обновленные данные.
     * @return Обновленный объект типа {@link House}.
     */
    House merge(@MappingTarget House house, House updated);
}
