package by.clevertec.house.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import by.clevertec.house.AbstractTest;
import by.clevertec.house.domain.House;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.util.HouseTestBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class HouseMapperTest extends AbstractTest {

    private final HouseMapper houseMapper;

    @Test
    void toDtoShouldReturnHouseResponseDto_whenHousePassed() {
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        HouseResponseDto expected = HouseTestBuilder.builder()
                .build()
                .buildHouseResponseDto();

        HouseResponseDto actual = houseMapper.toDto(house);

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
    void toDomainShouldReturnHouse_whenHouseRequestDtoPassed() {
        HouseRequestDto houseRequestDto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();
        House expected = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        House actual = houseMapper.toDomain(houseRequestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }

    @Test
    void mergeShouldReturnUpdatedHouse_whenHouseForUpdatePassed() {
        String country = "usa";
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        House updated = HouseTestBuilder.builder()
                .withCountry(country)
                .build()
                .buildHouse();
        House expected = HouseTestBuilder.builder()
                .withCountry(country)
                .build()
                .buildHouse();

        House actual = houseMapper.merge(house, updated);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber());
    }
}
