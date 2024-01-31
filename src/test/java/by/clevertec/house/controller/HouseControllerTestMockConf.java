package by.clevertec.house.controller;

import static by.clevertec.house.util.ResponseUtils.getExceptionResponse;
import static by.clevertec.house.util.TestConstant.CORRECT_UUID;
import static by.clevertec.house.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.house.AbstractTest;
import by.clevertec.house.domain.House;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.exception.CustomNoContentException;
import by.clevertec.house.model.ExceptionResponse;
import by.clevertec.house.service.HouseService;
import by.clevertec.house.util.HouseTestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class HouseControllerTestMockConf extends AbstractTest {

    private final ObjectMapper mapper;
    private final MockMvc mockMvc;

    @MockBean
    private HouseService houseService;

    @Test
    void getByUuidShouldReturnHouseResponseDto() throws Exception {
        HouseResponseDto house = HouseTestBuilder.builder()
                .build()
                .buildHouseResponseDto();
        String url = "/houses/" + CORRECT_UUID;

        when(houseService.getByUuid(CORRECT_UUID))
                .thenReturn(house);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(house)));
    }

    @Nested
    class TestGetAll {

        private final String url = "/houses";

        @Test
        void getAllShouldReturnPageWithHouseResponseDtosList() throws Exception {
            HouseResponseDto house = HouseTestBuilder.builder()
                    .build()
                    .buildHouseResponseDto();
            List<HouseResponseDto> houses = List.of(house);
            PageImpl<HouseResponseDto> page = new PageImpl<>(houses);
            PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

            when(houseService.getAll(pageRequest))
                    .thenReturn(page);

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
        void getAllShouldReturnExceptionResponse_whenHousesListIsEmpty() throws Exception {
            PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
            CustomNoContentException exception = CustomNoContentException.of(House.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            when(houseService.getAll(pageRequest))
                    .thenThrow(exception);

            mockMvc.perform(get(url))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }
}
