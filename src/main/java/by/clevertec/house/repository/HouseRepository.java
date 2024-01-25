package by.clevertec.house.repository;

import by.clevertec.house.domain.House;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HouseRepository extends JpaRepository<House, Long> {

    /**
     * Получает объект {@link House} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) дома.
     * @return Объект {@link Optional<House>}, соответствующий указанному идентификатору.
     */
    @EntityGraph(attributePaths = {"residents", "owners"})
    Optional<House> findByUuid(UUID uuid);

    /**
     * Удаляет объект {@link House} по уникальному идентификатору (UUID).
     *
     * @param uuid Уникальный идентификатор (UUID) дома, который требуется удалить.
     */
    void deleteByUuid(UUID uuid);

    @Query("SELECT DISTINCT h FROM House h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "JOIN FETCH Person p ON hh.person = p "
            + "WHERE p.uuid = :personUuid AND hh.status = 'TENANT'")
    List<House> findAllByResidentUuid(UUID personUuid);

    @Query("SELECT DISTINCT h FROM House h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "JOIN FETCH Person p ON hh.person = p "
            + "WHERE p.uuid = :personUuid AND hh.status = 'OWNER'")
    List<House> findAllByOwnersUuid(UUID personUuid);

    @Query("SELECT DISTINCT h FROM House h "
            + "WHERE (LOWER(h.area) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(h.country) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(h.city) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(h.street) LIKE LOWER(CONCAT('%', :condition, '%')))"
            + "ORDER BY h.createDate DESC")
    Page<House> getHouseSearchResult(String condition, Pageable pageable);
}
