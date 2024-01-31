package by.clevertec.house.controller;

import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_RESIDENT_EXIST_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.HTTP_NOT_READABLE_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.PERSON_CHANGE_HOUSE_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.getExceptionResponse;
import static by.clevertec.house.util.ResponseUtils.getSuccessResponse;
import static by.clevertec.house.util.TestConstant.ANOUTHER_HOUSE_UUID;
import static by.clevertec.house.util.TestConstant.HOUSE_UUID;
import static by.clevertec.house.util.TestConstant.INCORRECT_UUID;
import static by.clevertec.house.util.TestConstant.PERSON_UUID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.house.AbstractTest;
import by.clevertec.house.config.TestContainerConfig;
import by.clevertec.house.domain.Gender;
import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.PassportDto;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.exception.ConditionalException;
import by.clevertec.house.exception.CustomEntityNotFoundException;
import by.clevertec.house.exception.CustomNoContentException;
import by.clevertec.house.model.ErrorValidationResponse;
import by.clevertec.house.model.ExceptionResponse;
import by.clevertec.house.model.MessageResponse;
import by.clevertec.house.util.HouseTestBuilder;
import by.clevertec.house.util.PassportTestBuilder;
import by.clevertec.house.util.PersonTestBuilder;
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
class PersonControllerTest extends AbstractTest {

    private final ObjectMapper mapper;
    private final MockMvc mockMvc;

    @Nested
    class TestGetAll {

        private final String url = "/persons";

        @Test
        void getAllShouldReturnPageWithPersonResponseDtosList() throws Exception {
            PersonResponseDto responseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].uuid").value(responseDto.getUuid().toString()),
                            jsonPath("$.content[0].name").value(responseDto.getName()),
                            jsonPath("$.content[0].surname").value(responseDto.getSurname()),
                            jsonPath("$.content[0].gender").value(responseDto.getGender().toString()),
                            jsonPath("$.content[0].passport").value(responseDto.getPassport()),
                            jsonPath("$.content[0].created").value(responseDto.getCreated()),
                            jsonPath("$.content[0].updated").value(responseDto.getUpdated()),
                            jsonPath("$.content[0].houseUuid").value(responseDto.getHouseUuid().toString()));
        }

        @Test
        @Sql(value = "classpath:sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getAllShouldReturnExceptionResponse_whenPersonsListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(Person.class);
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
        void getByUuidShouldReturnPersonResponseDto() throws Exception {
            PersonResponseDto responseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();
            String url = "/persons/" + PERSON_UUID;

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(responseDto)));
        }

        @Test
        void getByUuidShouldReturnExceptionResponse_whenIncorrectUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );
            String url = "/persons/" + INCORRECT_UUID;

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

        private final String url = "/persons";

        @Test
        void saveShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            PassportDto passport = PassportTestBuilder.builder()
                    .withPassportSeries("kl")
                    .withPassportNumber("15678sddf")
                    .build()
                    .buildPassportDto();
            PersonRequestDto requestDto = PersonTestBuilder.builder()
                    .withPassportDto(passport)
                    .build()
                    .buildPersonRequestDto();
            MessageResponse response = getSuccessResponse(CREATION_MESSAGE, Person.class);

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

        @Test
        void saveShouldReturnExceptionResponse_whenInvalidValueInRequestBodySend() throws Exception {
            PersonRequestDto requestDto = PersonTestBuilder.builder()
                    .withName(null)
                    .build()
                    .buildPersonRequestDto();
            List<String> errors = List.of("Enter name");
            ErrorValidationResponse response = new ErrorValidationResponse(
                    HttpStatus.BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.errors[0]").value(response.getErrors().get(0)));
        }

        @Test
        void saveShouldReturnExceptionResponse_whenIncorrectValueInRequestBodySend() throws Exception {
            PersonRequestDto requestDto = PersonTestBuilder.builder()
                    .withHouseUuid(INCORRECT_UUID)
                    .build()
                    .buildPersonRequestDto();
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void saveShouldReturnExceptionResponse_whenInvalidHouseUuidInRequestBodySend() throws Exception {
            PersonRequestDto requestDto = PersonTestBuilder.builder()
                    .withHouseUuid(null)
                    .build()
                    .buildPersonRequestDto();
            List<String> errors = List.of("Invalid uuid");
            ErrorValidationResponse response = new ErrorValidationResponse(
                    HttpStatus.BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.errors[0]").value(response.getErrors().get(0)));
        }
    }

    @Nested
    class TestPathUpdate {

        private final String url = "/persons/{uuid}";
        private final PersonPathRequestDto correctRequestDto = PersonTestBuilder.builder()
                .withName("new area")
                .withSurname("new country")
                .withGender(Gender.MALE)
                .withPassportDto(PersonTestBuilder.builder().build().getPassportDto())
                .withHouseUuid(PERSON_UUID)
                .withOwnedHouseUuids(null)
                .build()
                .buildPersonPathRequestDto();

        @Test
        void updateShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            MessageResponse response = getSuccessResponse(UPDATE_MESSAGE, Person.class);

            mockMvc.perform(patch(url, PERSON_UUID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(correctRequestDto)))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void updateShouldReturnSuccessResponse_whenValidRequestSendWithOneValue() throws Exception {
            PersonOneFieldValue requestDto = new PersonOneFieldValue("new name");
            MessageResponse response = getSuccessResponse(UPDATE_MESSAGE, Person.class);

            mockMvc.perform(patch(url, PERSON_UUID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenIcorrectUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);
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

            mockMvc.perform(patch(url, PERSON_UUID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        record PersonOneFieldValue(String name) {
        }
    }

    @Nested
    class TestPutUpdate {

        private final String url = "/persons/{uuid}";
        private final PersonRequestDto correctRequestDto = PersonTestBuilder.builder()
                .withName("new area")
                .withSurname("new country")
                .withGender(Gender.MALE)
                .withPassportDto(
                        PassportTestBuilder.builder()
                                .withPassportSeries("nn")
                                .build()
                                .buildPassportDto())
                .withHouseUuid(HOUSE_UUID)
                .withOwnedHouseUuids(null)
                .build()
                .buildPersonRequestDto();

        @Test
        void updateShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            MessageResponse response = getSuccessResponse(UPDATE_MESSAGE, Person.class);

            mockMvc.perform(put(url, PERSON_UUID)
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
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(put(url, INCORRECT_UUID)
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

            mockMvc.perform(put(url, PERSON_UUID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenInvalidValueInRequestBodySend() throws Exception {
            PersonRequestDto requestDto = PersonTestBuilder.builder()
                    .withName(null)
                    .withSurname("new country")
                    .withGender(Gender.MALE)
                    .withPassportDto(
                            PassportTestBuilder.builder()
                                    .withPassportSeries("nn")
                                    .build()
                                    .buildPassportDto())
                    .withHouseUuid(HOUSE_UUID)
                    .withOwnedHouseUuids(null)
                    .build()
                    .buildPersonRequestDto();
            List<String> errors = List.of("Enter name");
            ErrorValidationResponse response = new ErrorValidationResponse(
                    HttpStatus.BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(put(url, PERSON_UUID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.errors[0]").value(response.getErrors().get(0)));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenInvalidHouseUuidInRequestBodySend() throws Exception {
            PersonRequestDto requestDto = PersonTestBuilder.builder()
                    .withName("new area")
                    .withSurname("new country")
                    .withGender(Gender.MALE)
                    .withPassportDto(
                            PassportTestBuilder.builder()
                                    .withPassportSeries("nn")
                                    .build()
                                    .buildPassportDto())
                    .withHouseUuid(null)
                    .withOwnedHouseUuids(null)
                    .build()
                    .buildPersonRequestDto();
            List<String> errors = List.of("Invalid uuid");
            ErrorValidationResponse response = new ErrorValidationResponse(
                    HttpStatus.BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(put(url, PERSON_UUID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.errors[0]").value(response.getErrors().get(0)));
        }
    }

    @Nested
    class TestDelete {

        private final String url = "/persons/{uuid}";

        @Test
        void deleteShouldReturnSuccessResponse_whenValidUuid() throws Exception {
            MessageResponse response = getSuccessResponse(DELETION_MESSAGE, Person.class);

            mockMvc.perform(delete(url, PERSON_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void deleteShouldReturnExceptionResponse_whenInvalidUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);
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
    class TestGetOwnerHouses {

        private final String url = "/persons/houses/owned/{uuid}";

        @Test
        void getOwnerHousesShouldReturnPageWithHouseResponseDtosList() throws Exception {
            HouseResponseDto responseDto = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();

            mockMvc.perform(get(url, PERSON_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$").isArray(),
                            jsonPath("$.[0].uuid").value(responseDto.getUuid().toString()),
                            jsonPath("$.[0].area").value(responseDto.getArea()),
                            jsonPath("$.[0].country").value(responseDto.getCountry()),
                            jsonPath("$.[0].city").value(responseDto.getCity()),
                            jsonPath("$.[0].street").value(responseDto.getStreet()),
                            jsonPath("$.[0].number").value(responseDto.getNumber()),
                            jsonPath("$.[0].created").value(responseDto.getCreated()));
        }

        @Test
        @Sql(value = {"classpath:sql/insert-before.sql", "classpath:sql/delete-owners.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getOwnerHousesShouldReturnExceptionResponse_whenOwnedHousesListIsEmpty() throws Exception {
            CustomEntityNotFoundException exception = new CustomEntityNotFoundException("No houses owned by the person with UUID " + PERSON_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, PERSON_UUID))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void getOwnerHousesShouldReturnExceptionResponse_whenInvalidUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);
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
    class TestChangeHome {
        private final String url = "/persons/{uuid}/house/change/{houseUuid}";

        @Test
        @Sql(value = {"classpath:sql/insert-before.sql", "classpath:sql/person/create-new-house.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void changeHomeShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            MessageResponse response = getSuccessResponse(PERSON_CHANGE_HOUSE_MESSAGE, PERSON_UUID.toString());

            mockMvc.perform(patch(url, PERSON_UUID, ANOUTHER_HOUSE_UUID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void changeHomeShouldReturnExceptionResponse_whenInvalidPersonUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Person.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(patch(url, INCORRECT_UUID, ANOUTHER_HOUSE_UUID))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void changeHomeShouldReturnExceptionResponse_whenInvalidHouseUuid() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(House.class, INCORRECT_UUID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(patch(url, PERSON_UUID, INCORRECT_UUID))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void changeHomeShouldReturnExceptionResponse_whenPersonAlreadyLivesInHouse() throws Exception {
            ConditionalException exception = new ConditionalException(CONDITIONAL_RESIDENT_EXIST_EXCEPTION_MESSAGE);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_ACCEPTABLE,
                    exception.getMessage(),
                    exception
            );
            mockMvc.perform(patch(url, PERSON_UUID, HOUSE_UUID))
                    .andExpect(status().isNotAcceptable())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetPersonSearchResult {

        private final String url = "/persons/search/{condition}";

        @Test
        void getAllShouldReturnPageWithPersonResponseDtosList() throws Exception {
            PersonResponseDto responseDto = PersonTestBuilder.builder()
                    .build()
                    .buildPersonResponseDto();

            mockMvc.perform(get(url, responseDto.getName()))

                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].uuid").value(responseDto.getUuid().toString()),
                            jsonPath("$.content[0].name").value(responseDto.getName()),
                            jsonPath("$.content[0].surname").value(responseDto.getSurname()),
                            jsonPath("$.content[0].gender").value(responseDto.getGender().toString()),
                            jsonPath("$.content[0].passport").value(responseDto.getPassport()),
                            jsonPath("$.content[0].created").value(responseDto.getCreated()),
                            jsonPath("$.content[0].updated").value(responseDto.getUpdated()),
                            jsonPath("$.content[0].houseUuid").value(responseDto.getHouseUuid().toString()));
        }

        @Test
        void getAllShouldReturnExceptionResponse_whenPersonsListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(Person.class);
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
