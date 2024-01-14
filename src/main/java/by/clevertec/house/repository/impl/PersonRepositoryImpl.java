package by.clevertec.house.repository.impl;

import by.clevertec.house.domain.Person;
import by.clevertec.house.exception.CustomEntityNotFoundException;
import by.clevertec.house.repository.PersonRepository;
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
public class PersonRepositoryImpl implements PersonRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Person> getAll(int pageNumber, int pageSize) {
        return entityManager.createQuery("FROM Person", Person.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Person getByUuid(UUID uuid) {
        try {
            return entityManager.createQuery("FROM Person p WHERE p.uuid = :uuid", Person.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException exception) {
            throw CustomEntityNotFoundException.of(Person.class, uuid);
        }
    }

    @Override
    public void save(Person person) {
        entityManager.persist(person);
    }

    @Override
    public void update(Person person) {
        entityManager.merge(person);
    }

    @Override
    public void delete(UUID uuid) {
        Person person = getByUuid(uuid);
        if (person != null) {
            entityManager.remove(person);
        }
    }
}
