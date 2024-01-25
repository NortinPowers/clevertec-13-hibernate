package by.clevertec.house.repository;

import by.clevertec.house.domain.Person;
import java.util.Optional;

public interface JdbcPersonRepository {

    /**
     * Получает объект типа {@link Person} по имени.
     *
     * @param name Имя, по которому осуществляется поиск объекта {@link Person}.
     * @return {@link Optional}, содержащий объект {@link Person}, если найден, или пустой, если не найден.
     */
    Optional<Person> getByName(String name);
}
