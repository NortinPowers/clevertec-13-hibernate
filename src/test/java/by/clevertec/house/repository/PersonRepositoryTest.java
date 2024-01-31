package by.clevertec.house.repository;

import static by.clevertec.house.util.TestConstant.HOUSE_UUID;
import static by.clevertec.house.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static by.clevertec.house.util.TestConstant.PERSON_SURNAME;
import static by.clevertec.house.util.TestConstant.PERSON_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.clevertec.house.config.TestContainerConfig;
import by.clevertec.house.domain.Passport;
import by.clevertec.house.domain.Person;
import by.clevertec.house.util.PassportTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Transactional
@RequiredArgsConstructor
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PersonRepositoryTest {

    private final PersonRepository personRepository;

    @Test
    void findAllShouldReturnPageWithPerson_whenPersonsTableIsNotEmpty() {
        Person expected = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<Person> actual = personRepository.findAll(pageRequest);

        assertThat(actual.getContent().get(0))
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.gender, expected.getGender())
                .hasFieldOrPropertyWithValue(Person.Fields.passport, expected.getPassport())
                .hasFieldOrPropertyWithValue(Person.Fields.createDate, expected.getCreateDate())
                .hasFieldOrPropertyWithValue(Person.Fields.updateDate, expected.getUpdateDate());
        assertEquals(expected.getHouse().getUuid(), actual.getContent().get(0).getHouse().getUuid());
    }

    @Test
    void findByUuidShouldReturnPerson_whenPersonExistInTable() {
        Person expected = PersonTestBuilder.builder()
                .build()
                .buildPerson();

        Optional<Person> actual = personRepository.findByUuid(PERSON_UUID);

        assertTrue(actual.isPresent());
        assertThat(actual.get())
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.gender, expected.getGender())
                .hasFieldOrPropertyWithValue(Person.Fields.passport, expected.getPassport())
                .hasFieldOrPropertyWithValue(Person.Fields.createDate, expected.getCreateDate())
                .hasFieldOrPropertyWithValue(Person.Fields.updateDate, expected.getUpdateDate());
    }

    @Test
    @Sql(value = {"classpath:sql/house/create-house-without-owner.sql", "classpath:sql/delete-owners.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteByUuidShouldDeletePerson_whenPersonExistInTable() {
        Optional<Person> before = personRepository.findByUuid(PERSON_UUID);
        assertTrue(before.isPresent());

        personRepository.deleteByUuid(PERSON_UUID);

        Optional<Person> after = personRepository.findByUuid(PERSON_UUID);
        assertFalse(after.isPresent());
    }

    @Test
    void findAllResidentsByHouseUuidShouldReturnPersonsList_whenResidentExistInTable() {
        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        List<Person> expected = List.of(person);

        List<Person> actual = personRepository.findAllResidentsByHouseUuid(HOUSE_UUID);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getUuid(), actual.get(0).getUuid());
    }

    @Test
    void findAllOwnersByHouseUuidShouldReturnPersonsList_whenOwnersExistInTable() {
        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        List<Person> expected = List.of(person);

        List<Person> actual = personRepository.findAllOwnersByHouseUuid(HOUSE_UUID);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getUuid(), actual.get(0).getUuid());
    }

    @Test
    void getPersonSearchResultReturnPageOfPersons_whenPersonsWithSearchConditionExistInTable() {
        Person expected = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<Person> actual = personRepository.getPersonSearchResult(PERSON_SURNAME, pageRequest);

        assertThat(actual.getContent().get(0))
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.gender, expected.getGender())
                .hasFieldOrPropertyWithValue(Person.Fields.passport, expected.getPassport())
                .hasFieldOrPropertyWithValue(Person.Fields.createDate, expected.getCreateDate())
                .hasFieldOrPropertyWithValue(Person.Fields.updateDate, expected.getUpdateDate());
        assertEquals(expected.getHouse().getUuid(), actual.getContent().get(0).getHouse().getUuid());
    }

    @Test
    void getPersonSearchResultReturnPageWithEmptyOptional_whenPersonsWithSearchConditionNotExistInTable() {
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<Person> actual = personRepository.getPersonSearchResult("some", pageRequest);

        Optional<Person> personOptional = actual.getContent().stream()
                .findFirst();
        assertThat(personOptional).isEmpty();
    }

    @Test
    public void saveShouldThrowInvalidDataAccessApiUsageException_whenPassportSeriesIsNull() {
        Passport passport = PassportTestBuilder.builder()
                .withPassportSeries(null)
                .build()
                .buildPassport();
        Person person = PersonTestBuilder.builder()
                .withPassport(passport)
                .build()
                .buildPerson();

        assertThrows(InvalidDataAccessApiUsageException.class, () -> personRepository.save(person));
    }

    @Test
    public void saveShouldThrowInvalidDataAccessApiUsageException_whenPassportNumberIsNull() {
        Passport passport = PassportTestBuilder.builder()
                .withPassportNumber(null)
                .build()
                .buildPassport();
        Person person = PersonTestBuilder.builder()
                .withPassport(passport)
                .build()
                .buildPerson();

        assertThrows(InvalidDataAccessApiUsageException.class, () -> personRepository.save(person));
    }
}
