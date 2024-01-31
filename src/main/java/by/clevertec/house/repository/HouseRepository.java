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

    /**
     * Метод для поиска всех домов, в которых проживает жилец с указанным идентификатором.
     *
     * @param personUuid Идентификатор жильца.
     * @return Список домов, в которых проживает указанный жилец.
     */
    @Query("SELECT DISTINCT h FROM House h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "JOIN FETCH Person p ON hh.person = p "
            + "WHERE p.uuid = :personUuid AND hh.status = 'TENANT'")
    List<House> findAllByResidentUuid(UUID personUuid);

    /**
     * Метод для поиска всех домов, которые принадлежат владельцу с указанным идентификатором.
     *
     * @param personUuid Идентификатор владельца.
     * @return Список домов, принадлежащих указанному владельцу.
     */
    @Query("SELECT DISTINCT h FROM House h "
            + "JOIN FETCH HouseHistory hh ON h = hh.house "
            + "JOIN FETCH Person p ON hh.person = p "
            + "WHERE p.uuid = :personUuid AND hh.status = 'OWNER'")
    List<House> findAllByOwnersUuid(UUID personUuid);

    /**
     * Метод для поиска домов по заданным критериям и сортировкой по дате создания в убывающем порядке.
     *
     * @param condition Условие поиска, которое может соответствовать части адреса дома (область, страна, город, улица).
     * @param pageable  Интерфейс для представления информации о странице результатов запроса.
     * @return Страница с результатами поиска домов.
     */
    @Query("SELECT DISTINCT h FROM House h "
            + "WHERE (LOWER(h.area) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(h.country) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(h.city) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(h.street) LIKE LOWER(CONCAT('%', :condition, '%')))"
            + "ORDER BY h.createDate DESC")
    Page<House> getHouseSearchResult(String condition, Pageable pageable);
}
