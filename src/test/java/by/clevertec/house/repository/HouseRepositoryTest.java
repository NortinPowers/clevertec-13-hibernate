package by.clevertec.house.repository;

import static by.clevertec.house.util.TestConstant.HOUSE_CITY;
import static by.clevertec.house.util.TestConstant.HOUSE_UUID;
import static by.clevertec.house.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.house.util.TestConstant.PAGE_SIZE;
import static by.clevertec.house.util.TestConstant.PERSON_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.clevertec.house.config.TestContainerConfig;
import by.clevertec.house.domain.House;
import by.clevertec.house.util.HouseTestBuilder;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Transactional
@RequiredArgsConstructor
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class HouseRepositoryTest {

    private final HouseRepository houseRepository;

    @Test
    void findByUuidShouldReturnPageWithHouse_whenHousesTableIsNotEmpty() {
        House expected = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        Optional<House> actual = houseRepository.findByUuid(HOUSE_UUID);

        assertTrue(actual.isPresent());
        assertThat(actual.get())
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber())
                .hasFieldOrPropertyWithValue(House.Fields.createDate, expected.getCreateDate());
    }

    @Test
    @Sql(value = "classpath:sql/house/create-empty-house.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteByUuidDeleteHouse_whenHouseExistInTable() {
        Optional<House> before = houseRepository.findByUuid(HOUSE_UUID);
        assertTrue(before.isPresent());

        houseRepository.deleteByUuid(HOUSE_UUID);

        Optional<House> after = houseRepository.findByUuid(HOUSE_UUID);
        assertFalse(after.isPresent());
    }

    @Test
    void findAllByResidentUuidShouldReturnHousesList_whenHouseContainsResident() {
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        List<House> expected = List.of(house);

        List<House> actual = houseRepository.findAllByResidentUuid(PERSON_UUID);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getUuid(), actual.get(0).getUuid());
    }

    @Test
    void findAllByOwnersUuidReturnHousesList_whenHouseContainsOwner() {
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        List<House> expected = List.of(house);

        List<House> actual = houseRepository.findAllByOwnersUuid(PERSON_UUID);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getUuid(), actual.get(0).getUuid());
    }

    @Test
    void getHouseSearchResultPageOfHouses_whenHousesWithSearchConditionExistInTable() {
        House expected = HouseTestBuilder.builder()
                .build()
                .buildHouse();
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<House> actual = houseRepository.getHouseSearchResult(HOUSE_CITY, pageRequest);

        assertThat(actual.getContent().get(0))
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber())
                .hasFieldOrPropertyWithValue(House.Fields.createDate, expected.getCreateDate());
    }

    @Test
    void getHouseSearchResultReturnPageWithEmptyOptional_whenHousesWithSearchConditionNotExistInTable() {
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<House> actual = houseRepository.getHouseSearchResult("some", pageRequest);

        Optional<House> personOptional = actual.getContent().stream()
                .findFirst();
        assertThat(personOptional).isEmpty();
    }
}
