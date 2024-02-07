package by.clevertec.house.controller;

import static by.clevertec.house.util.ResponseUtils.ADDED_HOUSE_OWNER_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_HOUSE_OWNER_EXIST_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_HOUSE_OWNER_NOT_EXIST_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.DELETED_HOUSE_OWNER_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.HTTP_NOT_READABLE_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.getSuccessResponse;
import static by.clevertec.house.util.TestConstant.HOUSE_UUID;
import static by.clevertec.house.util.TestConstant.INCORRECT_UUID;
import static by.clevertec.house.util.TestConstant.PERSON_UUID;
import static by.clevertec.util.ResponseUtils.getExceptionResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.exception.ConditionalException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.exception.CustomNoContentException;
import by.clevertec.house.AbstractTest;
import by.clevertec.house.config.TestContainerConfig;
import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.model.MessageResponse;
import by.clevertec.house.proxy.HouseCacheableAspect;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
import by.clevertec.model.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(classes = TestContainerConfig.class)
@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class HouseControllerTest extends AbstractTest {

    private final ObjectMapper mapper;
    private final MockMvc mockMvc;
    private final HouseCacheableAspect cacheableAspect;

    @Nested
    class TestGetAll {

        private final String url = "/houses";

        @Test
        void getAllShouldReturnPageWithHouseResponseDtosList() throws Exception {
            HouseResponseDto house = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].uuid").value(house.getUuid().toString()),
                            jsonPath("$.content[0].area").value(house.getArea()),
                            jsonPath("$.content[0].country").value(house.getCountry()),
                            jsonPath("$.content[0].city").value(house.getCity()),
                            jsonPath("$.content[0].street").value(house.getStreet()),
                            jsonPath("$.content[0].created").value(house.getCreated()));
        }

        @Test
        @Sql(value = "classpath:sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getAllShouldReturnExceptionResponse_whenHousesListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(House.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetByUuid {

        @Test
        void getByUuidShouldReturnHouseResponseDto() throws Exception {
            HouseResponseDto house = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();
            cacheableAspect.cacheableDelete(HOUSE_UUID);
            String url = "/houses/" + HOUSE_UUID;

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(house)));
        }

        @Test
        void getByUuidShouldReturnExceptionResponse_whenIncorrectUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );
            String url = "/houses/" + INCORRECT_UUID;

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestSave {

        private final String url = "/houses";

        @Test
        void saveShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            HouseRequestDto requestDto = HouseTestBuilder.builder()
                    .withResidentUuids(null)
                    .withOwnerUuids(null)
                    .build()
                    .buildHouseRequestDto();
            MessageResponse response = getSuccessResponse(CREATION_MESSAGE, House.class);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void saveShouldReturnExceptionResponse_whenInvalidRequestBodySend() throws Exception {
            String requestDto = "some value";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("not matter", new MockHttpInputMessage("not matter".getBytes()));
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    HTTP_NOT_READABLE_EXCEPTION_MESSAGE,
                    exception);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestUpdate {

        private final String url = "/houses/{uuid}";
        private final HouseRequestDto correctRequestDto = HouseTestBuilder.builder()
                .withArea("new area")
                .withCountry("new country")
                .withCity("new city")
                .withStreet("new street")
                .withNumber("new number")
                .withResidentUuids(null)
                .withOwnerUuids(null)
                .build()
                .buildHouseRequestDto();

        @Test
        void updateShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            MessageResponse response = getSuccessResponse(UPDATE_MESSAGE, House.class);

            mockMvc.perform(patch(url, HOUSE_UUID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(correctRequestDto)))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenIcorrectUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(patch(url, INCORRECT_UUID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(correctRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenInvalidRequestBodySend() throws Exception {
            String requestDto = "some value";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("not matter", new MockHttpInputMessage("not matter".getBytes()));
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    HTTP_NOT_READABLE_EXCEPTION_MESSAGE,
                    exception);

            mockMvc.perform(patch(url, HOUSE_UUID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestDelete {

        private final String url = "/houses/{uuid}";

        @Test
        @Sql(value = {"classpath:sql/insert-before.sql", "classpath:sql/delete-persons.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void deleteShouldReturnSuccessResponse_whenValidUuidAndHouseResidentsListIsEmpty() throws Exception {
            MessageResponse response = getSuccessResponse(DELETION_MESSAGE, House.class);

            mockMvc.perform(delete(url, HOUSE_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void deleteShouldReturnExceptionResponse_whenValidUuidAndHouseResidentsListIsNotEmpty() throws Exception {
            ConditionalException exception = new ConditionalException(CONDITIONAL_EXCEPTION_MESSAGE);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_ACCEPTABLE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(delete(url, HOUSE_UUID))
                    .andExpect(status().isNotAcceptable())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void deleteShouldReturnExceptionResponse_whenInvalidUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(delete(url, INCORRECT_UUID))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetResidents {

        private final String url = "/houses/{uuid}/residents";

        @Test
        void getResidentsReturnPersonResponseDtosList_whenValidUuidAndHouseResidentsSetIsNotEmpty() throws Exception {
            PersonResponseDto responseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();

            mockMvc.perform(get(url, HOUSE_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$[0].uuid").value(responseDto.getUuid().toString()),
                            jsonPath("$[0].name").value(responseDto.getName()),
                            jsonPath("$[0].surname").value(responseDto.getSurname()),
                            jsonPath("$[0].gender").value(responseDto.getGender().toString()),
                            jsonPath("$[0].passport").value(responseDto.getPassport()),
                            jsonPath("$[0].created").value(responseDto.getCreated()),
                            jsonPath("$[0].updated").value(responseDto.getUpdated()),
                            jsonPath("$[0].houseUuid").value(responseDto.getHouseUuid().toString()))
                    .andExpect(content().json(mapper.writeValueAsString(List.of(responseDto))));
        }

        @Test
        @Sql(value = {"classpath:sql/insert-before.sql", "classpath:sql/delete-persons.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getResidentsReturnExceptionResponse_whenValidUuidAndHouseResidentsSetIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(Person.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, HOUSE_UUID))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void getResidentsReturnExceptionResponse_whenInvalidUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, INCORRECT_UUID))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestAddOwner {

        private final String url = "/houses/{uuid}/add/owner/{personUuid}";

        @Test
        @Sql(value = {"classpath:sql/insert-before.sql", "classpath:sql/delete-owners.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void addOwnerSuccessResponse_whenAllSendDataIsValid() throws Exception {
            MessageResponse response = getSuccessResponse(ADDED_HOUSE_OWNER_MESSAGE, HOUSE_UUID.toString());

            mockMvc.perform(patch(url, HOUSE_UUID, PERSON_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void addOwnerExceptionResponse_whenInvalidHouseUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(patch(url, INCORRECT_UUID, PERSON_UUID))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void addOwnerExceptionResponse_whenInvalidPersonUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(patch(url, HOUSE_UUID, INCORRECT_UUID))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void addOwnerExceptionResponse_whenPersonIsAlreadyOwner() throws Exception {
            ConditionalException exception = new ConditionalException(CONDITIONAL_HOUSE_OWNER_EXIST_EXCEPTION_MESSAGE);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_ACCEPTABLE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(patch(url, HOUSE_UUID, PERSON_UUID))
                    .andExpect(status().isNotAcceptable())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestDeleteOwner {

        private final String url = "/houses/{uuid}/delete/owner/{personUuid}";

        @Test
        void deleteOwnerSuccessResponse_whenAllSendDataIsValid() throws Exception {
            MessageResponse response = getSuccessResponse(DELETED_HOUSE_OWNER_MESSAGE, HOUSE_UUID.toString());

            mockMvc.perform(patch(url, HOUSE_UUID, PERSON_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void deleteOwnerExceptionResponse_whenInvalidHouseUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(patch(url, INCORRECT_UUID, PERSON_UUID))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void deleteOwnerExceptionResponse_whenInvalidPersonUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(patch(url, HOUSE_UUID, INCORRECT_UUID))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        @Sql(value = {"classpath:sql/insert-before.sql", "classpath:sql/delete-owners.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void deleteOwnerExceptionResponse_whenPersonIsNotOwner() throws Exception {
            ConditionalException exception = new ConditionalException(CONDITIONAL_HOUSE_OWNER_NOT_EXIST_EXCEPTION_MESSAGE);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_ACCEPTABLE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(patch(url, HOUSE_UUID, PERSON_UUID))
                    .andExpect(status().isNotAcceptable())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetHouseSearchResult {

        private final String url = "/houses/search/{condition}";

        @Test
        void getAllShouldReturnPageWithHouseResponseDtosList() throws Exception {
            HouseResponseDto house = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            mockMvc.perform(get(url, house.getCity()))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].uuid").value(house.getUuid().toString()),
                            jsonPath("$.content[0].area").value(house.getArea()),
                            jsonPath("$.content[0].country").value(house.getCountry()),
                            jsonPath("$.content[0].city").value(house.getCity()),
                            jsonPath("$.content[0].street").value(house.getStreet()),
                            jsonPath("$.content[0].created").value(house.getCreated()));
        }

        @Test
        void getAllShouldReturnExceptionResponse_whenHousesListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(House.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, "none"))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }
}
