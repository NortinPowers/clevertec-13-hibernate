package by.clevertec.house.repository;

import static by.clevertec.house.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import by.clevertec.house.AbstractTest;
import by.clevertec.house.domain.Person;
import by.clevertec.house.util.PersonTestBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@Transactional
@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PersonRepositoryTest extends AbstractTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void findAllShouldReturnPageWithPerson_whenPersonTableIsNotEmpty() {
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
    void findByUuid() {
    }

    @Test
    void deleteByUuid() {
    }

    @Test
    void findAllResidentsByHouseUuid() {
    }

    @Test
    void findAllOwnersByHouseUuid() {
    }
}
