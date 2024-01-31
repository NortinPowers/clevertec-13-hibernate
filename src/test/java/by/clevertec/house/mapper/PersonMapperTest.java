package by.clevertec.house.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import by.clevertec.house.AbstractTest;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonRequestDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.util.PersonTestBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class PersonMapperTest extends AbstractTest {

    private final PersonMapper personMapper;

    @Test
    void toDtoShouldReturnPersonResponseDto_whenPersonPassed() {
        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        PersonResponseDto expected = PersonTestBuilder.builder()
                .build()
                .buildPersonResponseDto();

        PersonResponseDto actual = personMapper.toDto(person);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.gender, expected.getGender())
                .hasFieldOrPropertyWithValue(Person.Fields.passport, expected.getPassport())
                .hasFieldOrPropertyWithValue("created", expected.getCreated())
                .hasFieldOrPropertyWithValue("updated", expected.getUpdated())
                .hasFieldOrPropertyWithValue("houseUuid", expected.getHouseUuid());
    }

    @Test
    void personRequestDtoToDomainShouldReturnPerson_whenPersonRequestDtoPassed() {
        PersonRequestDto personRequestDto = PersonTestBuilder.builder()
                .build()
                .buildPersonRequestDto();
        Person expected = PersonTestBuilder.builder()
                .build()
                .buildPerson();

        Person actual = personMapper.toDomain(personRequestDto);
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.gender, expected.getGender())
                .hasFieldOrPropertyWithValue(Person.Fields.passport, expected.getPassport());
    }

    @Test
    void personPathRequestDtoToDomainShouldReturnPerson_whenPersonPathRequestDtoPassed() {
        PersonPathRequestDto personPathRequestDto = PersonTestBuilder.builder()
                .build()
                .buildPersonPathRequestDto();
        Person expected = PersonTestBuilder.builder()
                .build()
                .buildPerson();

        Person actual = personMapper.toDomain(personPathRequestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.gender, expected.getGender())
                .hasFieldOrPropertyWithValue(Person.Fields.passport, expected.getPassport());
    }

    @Test
    void mergeShouldReturnUpdatedPerson_whenPersonForUpdatePassed() {
        String name = "new name";
        Person person = PersonTestBuilder.builder()
                .build()
                .buildPerson();
        Person updated = PersonTestBuilder.builder()
                .withName(name)
                .build()
                .buildPerson();
        Person expected = PersonTestBuilder.builder()
                .withName(name)
                .build()
                .buildPerson();

        Person actual = personMapper.merge(person, updated);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Person.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Person.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Person.Fields.surname, expected.getSurname())
                .hasFieldOrPropertyWithValue(Person.Fields.gender, expected.getGender())
                .hasFieldOrPropertyWithValue(Person.Fields.passport, expected.getPassport())
                .hasFieldOrPropertyWithValue(Person.Fields.createDate, expected.getCreateDate())
                .hasFieldOrPropertyWithValue(Person.Fields.updateDate, expected.getUpdateDate())
                .hasFieldOrPropertyWithValue(Person.Fields.house, expected.getHouse())
                .hasFieldOrPropertyWithValue(Person.Fields.ownedHouses, expected.getOwnedHouses());
    }
}
