package by.clevertec.house.repository.impl;

import by.clevertec.house.domain.House;
import by.clevertec.house.exception.CustomEntityNotFoundException;
import by.clevertec.house.repository.HouseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@RequiredArgsConstructor
public class HouseRepositoryImpl implements HouseRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public List<House> getAll(int pageNumber, int pageSize) {
        return entityManager.createQuery("FROM House", House.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public House getByUuid(UUID uuid) {
        try {
            return entityManager.createQuery("FROM House h WHERE h.uuid = :uuid", House.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw CustomEntityNotFoundException.of(House.class, uuid);
        }
    }

    @Override
    public void save(House house) {
        entityManager.persist(house);
    }

    @Override
    public void update(House house) {
        entityManager.merge(house);
    }

    @Override
    public void delete(UUID uuid) {
        House house = getByUuid(uuid);
        if (house != null) {
            entityManager.remove(house);
        }
    }

    @Override
    public List<House> getOwnedHousesByUuid(UUID uuid) {
        return entityManager.createQuery("FROM House h JOIN h.owners o WHERE o.uuid = :ownerUuid", House.class)
                .setParameter("ownerUuid", uuid)
                .getResultList();
    }
}
