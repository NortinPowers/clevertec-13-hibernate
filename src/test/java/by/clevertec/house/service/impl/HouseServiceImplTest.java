package by.clevertec.house.service.impl;

import static by.clevertec.house.util.TestConstant.CORRECT_UUID;
import static by.clevertec.house.util.TestConstant.INCORRECT_UUID;
import static by.clevertec.house.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.exception.ConditionalException;
import by.clevertec.house.exception.CustomEntityNotFoundException;
import by.clevertec.house.exception.CustomNoContentException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    @Mock
    private HouseMapper mapper;

    @Mock
    private HouseRepository houseRepository;

    @InjectMocks
    private HouseServiceImpl houseService;

    @Captor
    private ArgumentCaptor<House> captor;

    @Nested
    class GetAllTest {

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

            when(houseRepository.getAll(PAGE_NUMBER, PAGE_SIZE))
                    .thenReturn(houses);
            when(mapper.toDto(house))
                    .thenReturn(houseResponseDto);

            List<HouseResponseDto> actual = houseService.getAll(PAGE_NUMBER, PAGE_SIZE);

            assertThat(actual)
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getAllShouldThrowCustomNoContentException_whenHousesListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(House.class);
            List<House> houses = List.of();

            when(houseRepository.getAll(PAGE_NUMBER, PAGE_SIZE))
                    .thenReturn(houses);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> houseService.getAll(PAGE_NUMBER, PAGE_SIZE));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(mapper, never()).toDto(any(House.class));
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

            when(houseRepository.getByUuid(uuid))
                    .thenReturn(house);
            when(mapper.toDto(house))
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
        void getShouldThrowCustomEntityNotFoundException_whenIncorectUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);

            when(houseRepository.getByUuid(INCORRECT_UUID))
                    .thenThrow(expectedException);

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> houseService.getByUuid(INCORRECT_UUID));

            verify(mapper, never()).toDto(any(House.class));
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

            when(mapper.toDomain(houseDto))
                    .thenReturn(house);

            houseService.save(houseDto);

            verify(houseRepository).save(house);
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

            when(mapper.toDomain(houseDto))
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
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);

            when(houseRepository.getByUuid(INCORRECT_UUID))
                    .thenThrow(exception);

            assertThrows(CustomEntityNotFoundException.class, () -> houseService.update(INCORRECT_UUID, any(HouseRequestDto.class)));
            verify(mapper, never()).toDomain(any(HouseRequestDto.class));
            verify(mapper, never()).merge(any(House.class), any(House.class));
            verify(houseRepository, never()).update(any(House.class));
        }

        @Test
        void updateShouldInvokeRepository_whenCorrectUuidAndDtoTransferred() {
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();
            HouseRequestDto houseRequestDto = HouseTestBuilder.builder()
                    .withCity("new york")
                    .build()
                    .buildHouseRequestDto();
            House updated = HouseTestBuilder.builder()
                    .withCity("new york")
                    .build()
                    .buildHouse();
            House merged = HouseTestBuilder.builder()
                    .withCity("new york")
                    .build()
                    .buildHouse();

            when(houseRepository.getByUuid(CORRECT_UUID))
                    .thenReturn(house);
            when(mapper.toDomain(houseRequestDto))
                    .thenReturn(updated);
            when(mapper.merge(house, updated))
                    .thenReturn(merged);

            houseService.update(CORRECT_UUID, houseRequestDto);

            verify(houseRepository).update(merged);
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void deleteShouldDeleteHouse_whenCorrectUuidAndEmptyResidents() {
            House house = HouseTestBuilder.builder()
                    .build()
                    .buildHouse();

            when(houseRepository.getByUuid(CORRECT_UUID))
                    .thenReturn(house);

            houseService.delete(CORRECT_UUID);

            verify(houseRepository).delete(CORRECT_UUID);
        }

        @Test
        void deleteShouldThrowConditionalException_whenCorrectUuidButNotEmptyResidents() {
            Person person = PersonTestBuilder.builder()
                    .build()
                    .buildPerson();
            House house = HouseTestBuilder.builder()
                    .withResidents(List.of(person))
                    .build()
                    .buildHouse();
            ConditionalException expectedException = new ConditionalException("Сan not delete a house in which at least 1 person lives");

            when(houseRepository.getByUuid(CORRECT_UUID))
                    .thenReturn(house);

            ConditionalException actualException = assertThrows(ConditionalException.class, () -> houseService.delete(CORRECT_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(houseRepository, never()).delete(CORRECT_UUID);
        }

        @Test
        void deleteShouldThrowCustomEntityNotFoundException_whenIncorrectUuid() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);

            when(houseRepository.getByUuid(INCORRECT_UUID))
                    .thenThrow(expectedException);

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> houseService.delete(INCORRECT_UUID));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(houseRepository, never()).delete(INCORRECT_UUID);
        }
    }
}
