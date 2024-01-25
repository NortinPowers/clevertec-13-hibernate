package by.clevertec.house.service;

import by.clevertec.house.domain.House;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HouseService {

    /**
     * Получает список объектов {@link HouseResponseDto} с учетом параметров страницы и размера страницы.
     *
     * @param pageable Объект Pageable.
     * @return Список объектов {@link HouseResponseDto} для текущей страницы и размера страницы обернутых в Page.
     */
    Page<HouseResponseDto> getAll(Pageable pageable);

    /**
     * Получает объект {@link HouseResponseDto} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) объекта {@link HouseResponseDto}.
     * @return Объект {@link HouseResponseDto}, соответствующий указанному идентификатору.
     */
    HouseResponseDto getByUuid(UUID uuid);

    /**
     * Сохраняет объект {@link HouseRequestDto} в виде нового объекта {@link House}.
     *
     * @param houseDto Объект {@link HouseRequestDto}, который требуется сохранить в виде нового дома.
     */
    UUID save(HouseRequestDto houseDto);

    void save(House house);

    /**
     * Обновляет данные объекта {@link House} по уникальному идентификатору (UUID).
     *
     * @param uuid     Уникальный идентификатор (UUID) объекта {@link House}, который требуется обновить.
     * @param houseDto Объект {@link HouseRequestDto}, содержащий обновленные данные.
     */
    void update(UUID uuid, HouseRequestDto houseDto);

    /**
     * Удаляет объект {@link House} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) объекта {@link House}, который требуется удалить.
     */
    void delete(UUID uuid);

    /**
     * Получает список объектов {@link PersonResponseDto}, представляющих жителей дома по его уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) дома, для которого требуется получить жителей.
     * @return Список объектов {@link PersonResponseDto}, представляющих жителей указанного дома.
     */
    List<PersonResponseDto> getResidents(UUID uuid);

    void addOwner(UUID uuid, UUID personUuid);

    void deleteOwner(UUID uuid, UUID personUuid);

    Page<HouseResponseDto> getHouseSearchResult(String condition, Pageable pageable);
}
