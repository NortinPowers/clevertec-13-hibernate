package by.clevertec.house.service.impl;

import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_RESIDENT_EXIST_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.TestConstant.CORRECT_UUID;
import static by.clevertec.house.util.TestConstant.HOUSE_UUID;
import static by.clevertec.house.util.TestConstant.INCORRECT_UUID;
import static by.clevertec.house.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static by.clevertec.house.util.TestConstant.PERSON_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.exception.ConditionalException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.exception.CustomNoContentException;
import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.PersonRequestDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonMapper personMapper;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HouseRepository houseRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    @Captor
    private ArgumentCaptor<Person> captor;

    @Nested
    class GetAllTest {

        private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        @Test
        void getAllShouldReturnPersonResponseDtosList_whenPersonsListIsNotEmpty() {
            PersonResponseDto personResponseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();
            List<PersonResponseDto> expected = List.of(personResponseDto);
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            List<Person> people = List.of(person);
            PageImpl<Person> page = new PageImpl<>(people);

            when(personRepository.findAll(pageRequest))
                    .thenReturn(page);
            when(personMapper.toDto(person))
                    .thenReturn(personResponseDto);

            Page<PersonResponseDto> actual = personService.getAll(pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getAllShouldThrowCustomNoContentException_whenPersonsListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(Person.class);
            List<Person> people = List.of();
            PageImpl<Person> page = new PageImpl<>(people);

            when(personRepository.findAll(pageRequest))
                    .thenReturn(page);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> personService.getAll(pageRequest));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(personMapper, never()).toDto(any(Person.class));
        }
    }

    @Nested
    class GetByUuidTest {

        @Test
        void getShouldReturnPersonResponseDto_whenCorrectUuid() {
            PersonResponseDto expected = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            Optional<Person> optionalPerson = Optional.of(person);
            UUID uuid = person.getUuid();

            when(personRepository.findByUuid(uuid))
                    .thenReturn(optionalPerson);
            when(personMapper.toDto(person))
                    .thenReturn(expected);

            PersonResponseDto actual = personService.getByUuid(uuid);

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
        void getShouldThrowCustomEntityNotFoundException_whenIncorectUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);

            when(personRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> personService.getByUuid(INCORRECT_UUID));

            verify(personMapper, never()).toDto(any(Person.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @Nested
    class SaveTest {

        @Test
        void saveShouldInvokeRepositoryAndNotSaveHouses_whenPersonRequestDtoTransferredWithEmptyHousesOwners() {
            PersonRequestDto personRequestDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonRequestDto();
            Person person = PersonTestBuilder.builder()
                    .withUuid(null)
                    .build()
                    .buildPerson();
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            Optional<House> optionalHouse = Optional.of(house);

            when(personMapper.toDomain(personRequestDto))
                    .thenReturn(person);
            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(optionalHouse);

            personService.save(personRequestDto);

            verify(personRepository).save(person);
            verify(houseRepository, never()).save(any(House.class));
        }

        @Test
        void saveShouldInvokeRepositoryAndSaveHouse_whenPersonRequestDtoTransferredWithHousesOwners() {
            List<UUID> ownedHouseUuids = List.of(CORRECT_UUID);
            PersonRequestDto personRequestDto = PersonTestBuilder.builder()
                    .withOwnedHouseUuids(ownedHouseUuids)
                    .build()
                    .buildPersonRequestDto();
            Person person = PersonTestBuilder.builder()
                    .withUuid(null)
                    .build()
                    .buildPerson();
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            Optional<House> optionalHouse = Optional.of(house);
            House ownedHouse = HouseTestBuilder.builder()
                    .withUuid(CORRECT_UUID)
                    .build()
                    .buildHouse();
            Optional<House> optionalOwnedHouse = Optional.of(ownedHouse);

            when(personMapper.toDomain(personRequestDto))
                    .thenReturn(person);
            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(optionalHouse);
            when(houseRepository.findByUuid(CORRECT_UUID))
                    .thenReturn(optionalOwnedHouse);

            personService.save(personRequestDto);

            verify(personRepository).save(person);
            verify(houseRepository).save(ownedHouse);
        }

        @Test
        void createShouldSetPersonUuidAndNotSaveHouses_whenInvokeRepositoryWithEmptyHousesOwners() {
            PersonRequestDto personDto = PersonTestBuilder.builder()
                    .withUuid(null)
                    .build()
                    .buildPersonRequestDto();
            Person person = PersonTestBuilder.builder()
                    .withUuid(null)
                    .build()
                    .buildPerson();
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            Optional<House> optionalHouse = Optional.of(house);

            when(personMapper.toDomain(personDto))
                    .thenReturn(person);
            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(optionalHouse);

            personService.save(personDto);

            verify(houseRepository, never()).save(any(House.class));
            verify(personRepository).save(captor.capture());
            assertNotNull(captor.getValue());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void updateShouldThrowCustomEntityNotFoundException_whenIncorrectUuid() {
            when(personRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            assertThrows(CustomEntityNotFoundException.class, () -> personService.update(INCORRECT_UUID, any(PersonRequestDto.class)));
            verify(personMapper, never()).toDomain(any(PersonRequestDto.class));
            verify(personMapper, never()).merge(any(Person.class), any(Person.class));
        }

        @Test
        void updateShouldInvokeRepository_whenCorrectUuidAndDtoTransferred() {
            String name = "newman";
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            Optional<Person> optionalPerson = Optional.of(person);
            PersonRequestDto personRequestDto = PersonTestBuilder.builder()
                    .withName(name)
                    .build()
                    .buildPersonRequestDto();
            Person updated = PersonTestBuilder.builder()
                    .withName(name)
                    .build()
                    .buildPerson();
            Person merged = PersonTestBuilder.builder()
                    .withName(name)
                    .build()
                    .buildPerson();

            when(personRepository.findByUuid(CORRECT_UUID))
                    .thenReturn(optionalPerson);
            when(personMapper.toDomain(personRequestDto))
                    .thenReturn(updated);
            when(personMapper.merge(person, updated))
                    .thenReturn(merged);

            personService.update(CORRECT_UUID, personRequestDto);
        }
    }

    @Nested
    class UpdatePathTest {

        @Test
        void updateShouldThrowCustomEntityNotFoundException_whenIncorrectUuid() {
            when(personRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            assertThrows(CustomEntityNotFoundException.class, () -> personService.update(INCORRECT_UUID, any(PersonRequestDto.class)));
            verify(personMapper, never()).toDomain(any(PersonRequestDto.class));
            verify(personMapper, never()).merge(any(Person.class), any(Person.class));
        }

        @Test
        void updateShouldInvokeRepository_whenCorrectUuidAndDtoTransferred() {
            String name = "newman";
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            Optional<Person> optionalPerson = Optional.of(person);
            PersonRequestDto personRequestDto = PersonTestBuilder.builder()
                    .withName(name)
                    .build()
                    .buildPersonRequestDto();
            Person updated = PersonTestBuilder.builder()
                    .withName(name)
                    .build()
                    .buildPerson();
            Person merged = PersonTestBuilder.builder()
                    .withName(name)
                    .build()
                    .buildPerson();

            when(personRepository.findByUuid(CORRECT_UUID))
                    .thenReturn(optionalPerson);
            when(personMapper.toDomain(personRequestDto))
                    .thenReturn(updated);
            when(personMapper.merge(person, updated))
                    .thenReturn(merged);

            personService.update(CORRECT_UUID, personRequestDto);
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void deleteShouldDeletePerson_whenCorrectUuid() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            Optional<Person> optionalPerson = Optional.of(person);

            when(personRepository.findByUuid(CORRECT_UUID))
                    .thenReturn(optionalPerson);

            personService.delete(CORRECT_UUID);

            verify(houseRepository, never()).save(any(House.class));
        }

        @Test
        void deleteShouldThrowCustomEntityNotFoundException_whenIncorrectUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);

            when(personRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> personService.delete(INCORRECT_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(personRepository, never()).delete(any(Person.class));
        }
    }

    @Nested
    class TestGetPersonSearchResult {

        private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        @Test
        void getPersonSearchResultShouldReturnPageWithPersonResponseDto_whenValidConditionPassed() {
            PersonResponseDto personResponseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();
            List<PersonResponseDto> expected = List.of(personResponseDto);
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            List<Person> persons = List.of(person);
            PageImpl<Person> page = new PageImpl<>(persons);
            String condition = "valid condition";

            when(personRepository.getPersonSearchResult(condition, pageRequest))
                    .thenReturn(page);
            when(personMapper.toDto(person))
                    .thenReturn(personResponseDto);

            Page<PersonResponseDto> actual = personService.getPersonSearchResult(condition, pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getPersonSearchResultShouldThrowCustomNoContentException_whenFoundedPersonsListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(Person.class);
            List<Person> houses = List.of();
            PageImpl<Person> page = new PageImpl<>(houses);
            String condition = "invalid condition";

            when(personRepository.getPersonSearchResult(condition, pageRequest))
                    .thenReturn(page);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> personService.getPersonSearchResult(condition, pageRequest));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(personMapper, never()).toDto(any(Person.class));
        }
    }

    @Nested
    class TestChangeHome {

        @Test
        void changeHomeShouldSetNewHouseInPersonHouseField() {
            House oldHouse = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            House newHouse = HouseTestBuilder.builder()
                    .withCity("newCity")
                    .build()
                    .buildHouse();
            Person person = PersonTestBuilder.builder()
                    .withHouse(oldHouse)
                    .build()
                    .buildPerson();

            when(personRepository.findByUuid(PERSON_UUID))
                    .thenReturn(Optional.of(person));
            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(newHouse));

            personService.changeHome(PERSON_UUID, HOUSE_UUID);

            assertThat(person.getHouse())
                    .isEqualTo(newHouse);
        }

        @Test
        void changeHomeShouldThrowCustomEntityNotFoundException_whenIncorrectPersonUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);

            when(personRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> personService.changeHome(INCORRECT_UUID, HOUSE_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

        @Test
        void changeHomeShouldThrowCustomEntityNotFoundException_whenIncorrectHouseUuid() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);

            when(personRepository.findByUuid(PERSON_UUID))
                    .thenReturn(Optional.of(person));
            when(houseRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> personService.changeHome(PERSON_UUID, INCORRECT_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

        @Test
        void changeHomeShouldThrowConditionalException_whenPersonIsNorOwner() {
            House house = HouseTestBuilder.builder()
                    .withOwners(Collections.emptySet())
                    .build()
                    .buildHouse();
            Person person = PersonTestBuilder.builder()
                    .withHouse(house)
                    .build()
                    .buildPerson();
            ConditionalException expectedException = new ConditionalException(CONDITIONAL_RESIDENT_EXIST_EXCEPTION_MESSAGE);

            when(personRepository.findByUuid(PERSON_UUID))
                    .thenReturn(Optional.of(person));
            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(house));

            ConditionalException actualException = assertThrows(ConditionalException.class, () -> personService.changeHome(PERSON_UUID, HOUSE_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }
}
