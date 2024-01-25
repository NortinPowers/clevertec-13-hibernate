package by.clevertec.house.service;

import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonPutRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {

    /**
     * Получает список объектов {@link PersonResponseDto} с учетом параметров страницы и размера страницы.
     *
     * @param pageable Объект Pageable.
     * @return Список объектов {@link PersonResponseDto} для текущей страницы и размера страницы обернутых в Page.
     */
    Page<PersonResponseDto> getAll(Pageable pageable);

    /**
     * Получает объект {@link PersonResponseDto} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) объекта {@link PersonResponseDto}.
     * @return Объект {@link PersonResponseDto}, соответствующий указанному идентификатору.
     */
    PersonResponseDto getByUuid(UUID uuid);

    /**
     * Сохраняет объект {@link PersonPutRequestDto} в виде нового объекта {@link Person}.
     *
     * @param person Объект {@link PersonPutRequestDto}, который требуется сохранить в виде нового пользователя.
     */
    void save(PersonPutRequestDto person);

    /**
     * Обновляет данные объекта {@link Person} по уникальному идентификатору (UUID).
     *
     * @param uuid   Уникальный идентификатор (UUID) объекта {@link Person}, который требуется обновить.
     * @param person Объект {@link PersonPutRequestDto}, содержащий обновленные данные.
     */
    void update(UUID uuid, PersonPutRequestDto person);

    /**
     * Обновляет данные объекта {@link Person} по уникальному идентификатору (UUID) с использованием
     * данных из объекта {@link PersonPathRequestDto}.
     *
     * @param uuid      Уникальный идентификатор (UUID) объекта {@link Person}, который требуется обновить.
     * @param personDto Объект {@link PersonPathRequestDto}, содержащий обновленные данные.
     */
    void updatePath(UUID uuid, PersonPathRequestDto personDto);

    /**
     * Удаляет объект {@link Person} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) объекта {@link Person}, который требуется удалить.
     */
    void delete(UUID uuid);

    /**
     * Получает список объектов {@link HouseResponseDto}, представляющих дома, принадлежащих пользователю
     * с указанным уникальным идентификатором (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) пользователя.
     * @return Список объектов {@link HouseResponseDto}, представляющих дома, принадлежащих указанному пользователю.
     */
    List<HouseResponseDto> getOwnedHouses(UUID uuid);

    void changeHome(UUID uuid, UUID homeUuid);

    Page<PersonResponseDto> getPersonSearchResult(String condition, Pageable pageable);
}
