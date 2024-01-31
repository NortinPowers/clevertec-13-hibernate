package by.clevertec.house.service.impl;

import static by.clevertec.house.util.TestConstant.HOUSE_UUID;
import static by.clevertec.house.util.TestConstant.PERSON_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.house.AbstractTest;
import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.exception.CustomNoContentException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.service.HouseHistoryService;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@RequiredArgsConstructor
class HouseHistoryServiceImplIntegrationTest extends AbstractTest {

    private final HouseHistoryService historyService;

    @MockBean
    private HouseRepository houseRepository;

    @MockBean
    private HouseMapper houseMapper;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PersonMapper personMapper;

    @Nested
    class TestGetHistoryPersonHousesOfResidence {

        @Test
        void getPersonHousesOfResidenceShouldReturnNotEmptyListOfHouseResponseDtos() {
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            HouseResponseDto houseResponseDto = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();
            List<HouseResponseDto> expected = List.of(houseResponseDto);

            when(houseRepository.findAllByResidentUuid(PERSON_UUID))
                    .thenReturn(List.of(house));
            when(houseMapper.toDto(house))
                    .thenReturn(houseResponseDto);

            List<HouseResponseDto> actual = historyService.getPersonHousesOfResidence(PERSON_UUID);

            assertThat(actual)
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getPersonHousesOfResidenceShouldThrowCustomNoContentException_whenHousesListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(House.class);

            when(houseRepository.findAllByResidentUuid(PERSON_UUID))
                    .thenReturn(Collections.emptyList());

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> historyService.getPersonHousesOfResidence(PERSON_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(houseMapper, never()).toDto(any(House.class));
        }
    }

    @Nested
    class TestGetPersonOwnershipOfHouses {
        @Test
        void getPersonOwnershipOfHousesShouldReturnNotEmptyListOfHouseResponseDtos() {
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            HouseResponseDto houseResponseDto = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();
            List<HouseResponseDto> expected = List.of(houseResponseDto);

            when(houseRepository.findAllByOwnersUuid(PERSON_UUID))
                    .thenReturn(List.of(house));
            when(houseMapper.toDto(house))
                    .thenReturn(houseResponseDto);

            List<HouseResponseDto> actual = historyService.getPersonOwnershipOfHouses(PERSON_UUID);

            assertThat(actual)
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getPersonOwnershipOfHousesShouldThrowCustomNoContentException_whenHousesListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(House.class);

            when(houseRepository.findAllByOwnersUuid(PERSON_UUID))
                    .thenReturn(Collections.emptyList());

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> historyService.getPersonOwnershipOfHouses(PERSON_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(houseMapper, never()).toDto(any(House.class));
        }
    }

    @Nested
    class TestGetPeopleLivingInHouse {

        @Test
        void getPeopleLivingInHouseShouldReturnNotEmptyListOfPersonResponseDtos() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            PersonResponseDto personResponseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();
            List<PersonResponseDto> expected = List.of(personResponseDto);

            when(personRepository.findAllResidentsByHouseUuid(HOUSE_UUID))
                    .thenReturn(List.of(person));
            when(personMapper.toDto(person))
                    .thenReturn(personResponseDto);

            List<PersonResponseDto> actual = historyService.getPeopleLivingInHouse(HOUSE_UUID);

            assertThat(actual)
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getPeopleLivingInHouseShouldThrowCustomNoContentException_whenPersonsListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(Person.class);

            when(personRepository.findAllResidentsByHouseUuid(HOUSE_UUID))
                    .thenReturn(Collections.emptyList());

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> historyService.getPeopleLivingInHouse(HOUSE_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(personMapper, never()).toDto(any(Person.class));
        }
    }

    @Nested
    class TestGetPeopleWhoOwnHouse {

        @Test
        void getPeopleWhoOwnHouseShouldReturnNotEmptyListOfPersonResponseDtos() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            PersonResponseDto personResponseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();
            List<PersonResponseDto> expected = List.of(personResponseDto);

            when(personRepository.findAllOwnersByHouseUuid(HOUSE_UUID))
                    .thenReturn(List.of(person));
            when(personMapper.toDto(person))
                    .thenReturn(personResponseDto);

            List<PersonResponseDto> actual = historyService.getPeopleWhoOwnHouse(HOUSE_UUID);

            assertThat(actual)
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getPeopleWhoOwnHouseShouldThrowCustomNoContentException_whenPersonsListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(Person.class);

            when(personRepository.findAllOwnersByHouseUuid(HOUSE_UUID))
                    .thenReturn(Collections.emptyList());

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> historyService.getPeopleWhoOwnHouse(HOUSE_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(personMapper, never()).toDto(any(Person.class));
        }
    }
}
