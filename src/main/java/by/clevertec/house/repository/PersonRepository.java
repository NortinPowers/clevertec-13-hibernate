package by.clevertec.house.repository;

import by.clevertec.house.domain.Person;
import java.util.List;
import java.util.UUID;

public interface PersonRepository {

    /**
     * Получает список объектов {@link Person} с учетом параметров страницы и размера страницы.
     *
     * @param pageNumber Номер страницы (начиная с 0).
     * @param pageSize   Размер страницы, количество элементов на странице.
     * @return Список объектов {@link Person} для текущей страницы и размера страницы.
     */
    List<Person> getAll(int pageNumber, int pageSize);

    /**
     * Получает объект {@link Person} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) объекта {@link Person}.
     * @return Объект {@link Person}, соответствующий указанному идентификатору.
     */
    Person getByUuid(UUID uuid);

    /**
     * Сохраняет объект {@link Person}.
     *
     * @param person Объект {@link Person}, который требуется сохранить.
     */
    void save(Person person);

    /**
     * Обновляет данные объекта {@link Person}.
     *
     * @param person Объект {@link Person}, который требуется обновить.
     */
    void update(Person person);

    /**
     * Удаляет объект {@link Person} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) объекта {@link Person}, который требуется удалить.
     */
    void delete(UUID uuid);
}
