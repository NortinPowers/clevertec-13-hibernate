package by.clevertec.house.repository;

import by.clevertec.house.domain.House;
import java.util.List;
import java.util.UUID;

public interface HouseRepository {

    /**
     * Получает список домов с учетом параметров страницы и размера страницы.
     *
     * @param pageNumber Номер страницы (начиная с 0).
     * @param pageSize   Размер страницы, количество элементов на странице.
     * @return Список объектов {@link House} для текущей страницы и размера страницы.
     */
    List<House> getAll(int pageNumber, int pageSize);

    /**
     * Получает объект {@link House} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) дома.
     * @return Объект {@link House}, соответствующий указанному идентификатору.
     */
    House getByUuid(UUID uuid);

    /**
     * Сохраняет объект {@link House}.
     *
     * @param house Объект {@link House}, который требуется сохранить.
     */
    void save(House house);

    /**
     * Обновляет данные объекта {@link House}.
     *
     * @param house Объект {@link House}, который требуется обновить.
     */
    void update(House house);

    /**
     * Удаляет объект {@link House} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) дома, который требуется удалить.
     */
    void delete(UUID uuid);

    /**
     * Получает список домов, принадлежащих пользователю с указанным уникальным идентификатором (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) пользователя.
     * @return Список объектов {@link House}, принадлежащих указанному пользователю.
     */
    List<House> getOwnedHousesByUuid(UUID uuid);
}
