package by.clevertec.house.repository;

import by.clevertec.house.domain.Person;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Получает список объектов {@link Person} с учетом параметров страницы и размера страницы.
     *
     * @param pageable Объект Pageable.
     * @return Список объектов {@link Page<Person>} для текущей страницы и размера страницы обернутый в Page.
     */
    @Override
    @EntityGraph(attributePaths = "house")
    Page<Person> findAll(Pageable pageable);

    /**
     * Получает объект {@link Person} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) объекта {@link Person}.
     * @return Объект {@link Optional<Person>}, соответствующий указанному идентификатору.
     */
    @EntityGraph(attributePaths = "house")
    Optional<Person> findByUuid(UUID uuid);

    /**
     * Удаляет объект {@link Person} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) объекта {@link Person}, который требуется удалить.
     */
    void deleteByUuid(UUID uuid);

    @Query("SELECT DISTINCT p FROM Person p "
            + "JOIN FETCH House h ON p.house = h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "WHERE h.uuid = :houseUuid AND hh.status = 'TENANT'")
    List<Person> findAllResidentsByHouseUuid(UUID houseUuid);

    @Query("SELECT DISTINCT p FROM Person p "
            + "JOIN FETCH House h ON p.house = h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "WHERE h.uuid = :houseUuid AND hh.status = 'OWNER'")
    List<Person> findAllOwnersByHouseUuid(UUID houseUuid);

    @Query("SELECT DISTINCT p FROM Person p "
            + "WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(p.surname) LIKE LOWER(CONCAT('%', :condition, '%'))) "
            + "ORDER BY p.createDate DESC")
    Page<Person> getPersonSearchResult(String condition, Pageable pageable);
}
