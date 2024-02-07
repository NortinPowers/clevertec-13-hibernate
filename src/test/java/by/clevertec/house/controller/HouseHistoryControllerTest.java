package by.clevertec.house.controller;

import static by.clevertec.house.util.TestConstant.HOUSE_UUID;
import static by.clevertec.house.util.TestConstant.INCORRECT_UUID;
import static by.clevertec.house.util.TestConstant.PERSON_UUID;
import static by.clevertec.util.ResponseUtils.getExceptionResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.exception.CustomNoContentException;
import by.clevertec.house.AbstractTest;
import by.clevertec.house.config.TestContainerConfig;
import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import by.clevertec.model.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(classes = TestContainerConfig.class)
@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class HouseHistoryControllerTest extends AbstractTest {

    private final MockMvc mockMvc;

    @Nested
    class TestGetHistoryPersonHousesOfResidence {

        private final String url = "/history/houses/resident/{uuid}";

        @Test
        void getHistoryPersonHousesOfResidenceShouldReturnNotEmptyListOfHouseResponseDtos() throws Exception {
            HouseResponseDto house = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            mockMvc.perform(get(url, PERSON_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$").isArray(),
                            jsonPath("$.[0].uuid").value(house.getUuid().toString()),
                            jsonPath("$.[0].area").value(house.getArea()),
                            jsonPath("$.[0].country").value(house.getCountry()),
                            jsonPath("$.[0].city").value(house.getCity()),
                            jsonPath("$.[0].street").value(house.getStreet()),
                            jsonPath("$.[0].created").value(house.getCreated()));
        }

        @Test
        void getHistoryPersonHousesOfResidenceShouldReturnExceptionResponse_whenHousesListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(House.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, INCORRECT_UUID))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetHistoryPersonOwnershipOfHouses {

        private final String url = "/history/houses/owner/{uuid}";

        @Test
        void getHistoryPersonOwnershipOfHousesShouldReturnNotEmptyListOfHouseResponseDtos() throws Exception {
            HouseResponseDto house = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            mockMvc.perform(get(url, PERSON_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$").isArray(),
                            jsonPath("$.[0].uuid").value(house.getUuid().toString()),
                            jsonPath("$.[0].area").value(house.getArea()),
                            jsonPath("$.[0].country").value(house.getCountry()),
                            jsonPath("$.[0].city").value(house.getCity()),
                            jsonPath("$.[0].street").value(house.getStreet()),
                            jsonPath("$.[0].created").value(house.getCreated()));
        }

        @Test
        void getHistoryPersonOwnershipOfHousesShouldReturnExceptionResponse_whenHousesListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(House.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, INCORRECT_UUID))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetHistoryPeopleLivingInHouse {

        private final String url = "/history/residents/house/{uuid}";

        @Test
        void getHistoryPeopleLivingInHouseShouldReturnNotEmptyListOfPersonResponseDtos() throws Exception {
            PersonResponseDto responseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();

            mockMvc.perform(get(url, HOUSE_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$").isArray(),
                            jsonPath("$.[0].uuid").value(responseDto.getUuid().toString()),
                            jsonPath("$.[0].name").value(responseDto.getName()),
                            jsonPath("$.[0].surname").value(responseDto.getSurname()),
                            jsonPath("$.[0].gender").value(responseDto.getGender().toString()),
                            jsonPath("$.[0].passport").value(responseDto.getPassport()),
                            jsonPath("$.[0].created").value(responseDto.getCreated()),
                            jsonPath("$.[0].updated").value(responseDto.getUpdated()),
                            jsonPath("$.[0].houseUuid").value(responseDto.getHouseUuid().toString()));
        }

        @Test
        void getHistoryPeopleLivingInHouseShouldReturnExceptionResponse_whenPersonsListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(Person.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, INCORRECT_UUID))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetHistoryPeopleWhoOwnHouse {

        private final String url = "/history/owners/house/{uuid}";

        @Test
        void getHistoryPeopleWhoOwnHouseShouldReturnNotEmptyListOfPersonResponseDtos() throws Exception {
            PersonResponseDto responseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();

            mockMvc.perform(get(url, HOUSE_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$").isArray(),
                            jsonPath("$.[0].uuid").value(responseDto.getUuid().toString()),
                            jsonPath("$.[0].name").value(responseDto.getName()),
                            jsonPath("$.[0].surname").value(responseDto.getSurname()),
                            jsonPath("$.[0].gender").value(responseDto.getGender().toString()),
                            jsonPath("$.[0].passport").value(responseDto.getPassport()),
                            jsonPath("$.[0].created").value(responseDto.getCreated()),
                            jsonPath("$.[0].updated").value(responseDto.getUpdated()),
                            jsonPath("$.[0].houseUuid").value(responseDto.getHouseUuid().toString()));
        }

        @Test
        void getHistoryPeopleWhoOwnHouseShouldReturnExceptionResponse_whenPersonsListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(Person.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, INCORRECT_UUID))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }
}
