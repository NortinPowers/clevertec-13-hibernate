package by.clevertec.house.service.impl;

import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_HOUSE_OWNER_EXIST_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_HOUSE_OWNER_NOT_EXIST_EXCEPTION_MESSAGE;
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

import by.clevertec.house.AbstractTest;
import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.exception.ConditionalException;
import by.clevertec.house.exception.CustomEntityNotFoundException;
import by.clevertec.house.exception.CustomNoContentException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.service.HouseService;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
class HouseServiceImplIntegrationTest extends AbstractTest {

    private final HouseService houseService;

    @MockBean
    private HouseMapper houseMapper;

    @MockBean
    private PersonMapper personMapper;

    @MockBean
    private HouseRepository houseRepository;

    @MockBean
    private PersonRepository personRepository;

    @Captor
    private ArgumentCaptor<House> captor;

    @Test
    void saveShouldCallRepositorySaveMethod() {
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        houseService.save(house);

        verify(houseRepository).save(house);
    }

    @Nested
    class GetAllTest {

        private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        @Test
        void getAllShouldReturnHouseResponseDtosList_whenHousesListIsNotEmpty() {
            HouseResponseDto houseResponseDto = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();
            List<HouseResponseDto> expected = List.of(houseResponseDto);
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            List<House> houses = List.of(house);
            PageImpl<House> page = new PageImpl<>(houses);

            when(houseRepository.findAll(pageRequest))
                    .thenReturn(page);
            when(houseMapper.toDto(house))
                    .thenReturn(houseResponseDto);

            Page<HouseResponseDto> actual = houseService.getAll(pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getAllShouldThrowCustomNoContentException_whenHousesListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(House.class);
            List<House> houses = List.of();
            PageImpl<House> page = new PageImpl<>(houses);

            when(houseRepository.findAll(pageRequest))
                    .thenReturn(page);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> houseService.getAll(pageRequest));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(houseMapper, never()).toDto(any(House.class));
        }
    }

    @Nested
    class GetByUuidTest {

        @Test
        void getShouldReturnHouseResponseDto_whenCorrectUuid() {
            HouseResponseDto expected = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            UUID uuid = house.getUuid();
            Optional<House> optionalHouse = Optional.of(house);

            when(houseRepository.findByUuid(uuid))
                    .thenReturn(optionalHouse);
            when(houseMapper.toDto(house))
                    .thenReturn(expected);

            HouseResponseDto actual = houseService.getByUuid(uuid);

            assertThat(actual)
                    .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                    .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                    .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                    .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                    .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                    .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber())
                    .hasFieldOrPropertyWithValue("created", expected.getCreated());
        }

        @Test
        void getShouldThrowCustomEntityNotFoundException_whenIncorrectUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);

            when(houseRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> houseService.getByUuid(INCORRECT_UUID));

            verify(houseMapper, never()).toDto(any(House.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

    }

    @Nested
    class SaveTest {

        @Test
        void saveShouldInvokeRepository_whenHouseRequestDtoTransferred() {
            HouseRequestDto houseDto = HouseTestBuilder.builder()
                    .build()
                    .buildHouseRequestDto();
            House house = HouseTestBuilder.builder()
                    .withUuid(null)
                    .build()
                    .buildHouse();

            when(houseMapper.toDomain(houseDto))
                    .thenReturn(house);
            when(houseRepository.save(house))
                    .thenReturn(house);

            houseService.save(houseDto);
        }

        @Test
        void createShouldSetHouseUuid_whenInvokeRepository() {
            HouseRequestDto houseDto = HouseTestBuilder.builder()
                    .withUuid(null)
                    .build()
                    .buildHouseRequestDto();
            House house = HouseTestBuilder.builder()
                    .withUuid(null)
                    .build()
                    .buildHouse();

            when(houseMapper.toDomain(houseDto))
                    .thenReturn(house);
            when(houseRepository.save(house))
                    .thenReturn(house);

            houseService.save(houseDto);

            verify(houseRepository).save(captor.capture());
            assertNotNull(captor.getValue());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void updateShouldThrowCustomEntityNotFoundException_whenIncorrectUuid() {
            when(houseRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            assertThrows(CustomEntityNotFoundException.class, () -> houseService.update(INCORRECT_UUID, any(HouseRequestDto.class)));
            verify(houseMapper, never()).toDomain(any(HouseRequestDto.class));
            verify(houseMapper, never()).merge(any(House.class), any(House.class));
        }

        @Test
        void updateShouldInvokeRepository_whenCorrectUuidAndDtoTransferred() {
            String city = "new york";
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            Optional<House> optionalHouse = Optional.of(house);
            HouseRequestDto houseRequestDto = HouseTestBuilder.builder()
                    .withCity(city)
                    .build()
                    .buildHouseRequestDto();
            House updated = HouseTestBuilder.builder()
                    .withCity(city)
                    .build()
                    .buildHouse();
            House merged = HouseTestBuilder.builder()
                    .withCity(city)
                    .build()
                    .buildHouse();

            when(houseRepository.findByUuid(CORRECT_UUID))
                    .thenReturn(optionalHouse);
            when(houseMapper.toDomain(houseRequestDto))
                    .thenReturn(updated);
            when(houseMapper.merge(house, updated))
                    .thenReturn(merged);

            houseService.update(CORRECT_UUID, houseRequestDto);
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void deleteShouldDeleteHouse_whenCorrectUuidAndEmptyResidents() {
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            Optional<House> optionalHouse = Optional.of(house);

            when(houseRepository.findByUuid(CORRECT_UUID))
                    .thenReturn(optionalHouse);

            houseService.delete(CORRECT_UUID);
        }

        @Test
        void deleteShouldThrowConditionalException_whenCorrectUuidButNotEmptyResidents() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            House house = HouseTestBuilder.builder()
                    .withResidents(Set.of(person))
                    .build()
                    .buildHouse();
            Optional<House> optionalHouse = Optional.of(house);
            ConditionalException expectedException = new ConditionalException(CONDITIONAL_EXCEPTION_MESSAGE);

            when(houseRepository.findByUuid(CORRECT_UUID))
                    .thenReturn(optionalHouse);

            ConditionalException actualException = assertThrows(ConditionalException.class, () -> houseService.delete(CORRECT_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(houseRepository, never()).delete(any(House.class));
        }

        @Test
        void deleteShouldThrowCustomEntityNotFoundException_whenIncorrectUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);

            when(houseRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> houseService.delete(INCORRECT_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(houseRepository, never()).delete(any(House.class));
        }
    }

    @Nested
    class TestGetResidents {
        @Test
        void getResidentsShouldReturnPersonDtosList_whenHouseHasResident() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            PersonResponseDto expected = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();
            House house = HouseTestBuilder.builder()
                    .withResidents(Set.of(person))
                    .build()
                    .buildHouse();

            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(house));
            when(personMapper.toDto(person))
                    .thenReturn(expected);

            List<PersonResponseDto> actual = houseService.getResidents(HOUSE_UUID);

            assertThat(actual.get(0))
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
        void getResidentsShouldThrowCustomEntityNotFoundException_whenIncorrectUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);

            when(houseRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> houseService.getResidents(INCORRECT_UUID));

            verify(personMapper, never()).toDto(any(Person.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

        @Test
        void getResidentsThrowCustomNoContentException_whenPersonsSetIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(Person.class);
            House house = HouseTestBuilder.builder()
                    .withResidents(Collections.emptySet())
                    .build()
                    .buildHouse();

            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(house));

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> houseService.getResidents(HOUSE_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(personMapper, never()).toDto(any(Person.class));
        }
    }

    @Nested
    class TestAddOwner {

        @Test
        void addOwnerShouldCallSaveOnHouse_whenValidDataPassed() {
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();

            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(house));
            when(personRepository.findByUuid(PERSON_UUID))
                    .thenReturn(Optional.of(person));

            houseService.addOwner(HOUSE_UUID, PERSON_UUID);

            verify(houseRepository).save(house);
        }

        @Test
        void addOwnerShouldThrowCustomEntityNotFoundException_whenIncorrectHouseUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);

            when(houseRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> houseService.addOwner(INCORRECT_UUID, PERSON_UUID));

            verify(personRepository, never()).findByUuid(any(UUID.class));
            verify(houseRepository, never()).save(any(House.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

        @Test
        void addOwnerShouldThrowCustomEntityNotFoundException_whenIncorrectPersonUuid() {
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);

            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(house));
            when(personRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> houseService.addOwner(HOUSE_UUID, INCORRECT_UUID));

            verify(houseRepository, never()).save(any(House.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

        @Test
        void addOwnerShouldThrowConditionalException_whenPersonIsAlreadyOwner() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            House house = HouseTestBuilder.builder()
                    .withOwners(Set.of(person))
                    .build()
                    .buildHouse();
            ConditionalException expectedException = new ConditionalException(CONDITIONAL_HOUSE_OWNER_EXIST_EXCEPTION_MESSAGE);

            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(house));
            when(personRepository.findByUuid(PERSON_UUID))
                    .thenReturn(Optional.of(person));

            ConditionalException actualException = assertThrows(ConditionalException.class, () -> houseService.addOwner(HOUSE_UUID, PERSON_UUID));

            verify(houseRepository, never()).save(any(House.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @Nested
    class TestDeleteOwner {

        @Test
        void deleteOwnerShouldRemovePersonFromSet_whenValidDataPassed() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            Set<Person> persons = new HashSet<>();
            persons.add(person);
            House house = HouseTestBuilder.builder()
                    .withOwners(persons)
                    .build()
                    .buildHouse();

            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(house));
            when(personRepository.findByUuid(PERSON_UUID))
                    .thenReturn(Optional.of(person));

            houseService.deleteOwner(HOUSE_UUID, PERSON_UUID);

            assertThat(house.getOwners())
                    .isEmpty();
        }

        @Test
        void deleteOwnerShouldThrowCustomEntityNotFoundException_whenIncorrectHouseUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);

            when(houseRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> houseService.deleteOwner(INCORRECT_UUID, PERSON_UUID));

            verify(personRepository, never()).findByUuid(any(UUID.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

        @Test
        void deleteOwnerShouldThrowCustomEntityNotFoundException_whenIncorrectPersonUuid() {
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);

            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(house));
            when(personRepository.findByUuid(INCORRECT_UUID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> houseService.deleteOwner(HOUSE_UUID, INCORRECT_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }

        @Test
        void deleteOwnerShouldThrowConditionalException_whenPersonIsNorOwner() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            House house = HouseTestBuilder.builder()
                    .withOwners(Collections.emptySet())
                    .build()
                    .buildHouse();
            ConditionalException expectedException = new ConditionalException(CONDITIONAL_HOUSE_OWNER_NOT_EXIST_EXCEPTION_MESSAGE);

            when(houseRepository.findByUuid(HOUSE_UUID))
                    .thenReturn(Optional.of(house));
            when(personRepository.findByUuid(PERSON_UUID))
                    .thenReturn(Optional.of(person));

            ConditionalException actualException = assertThrows(ConditionalException.class, () -> houseService.deleteOwner(HOUSE_UUID, PERSON_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @Nested
    class TestGetHouseSearchResult {

        private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        @Test
        void getHouseSearchResultShouldReturnPageWithHouseResponseDto_whenValidConditionPassed() {
            HouseResponseDto houseResponseDto = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();
            List<HouseResponseDto> expected = List.of(houseResponseDto);
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            List<House> houses = List.of(house);
            PageImpl<House> page = new PageImpl<>(houses);
            String condition = "valid condition";

            when(houseRepository.getHouseSearchResult(condition, pageRequest))
                    .thenReturn(page);
            when(houseMapper.toDto(house))
                    .thenReturn(houseResponseDto);

            Page<HouseResponseDto> actual = houseService.getHouseSearchResult(condition, pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getHouseSearchResultShouldThrowCustomNoContentException_whenFoundedHousesListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(House.class);
            List<House> houses = List.of();
            PageImpl<House> page = new PageImpl<>(houses);
            String condition = "invalid condition";

            when(houseRepository.getHouseSearchResult(condition, pageRequest))
                    .thenReturn(page);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> houseService.getHouseSearchResult(condition, pageRequest));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(houseMapper, never()).toDto(any(House.class));
        }
    }
}
