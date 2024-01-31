package by.clevertec.house.service.impl;

import static by.clevertec.house.util.TestConstant.HOUSE_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import by.clevertec.house.AbstractTest;
import by.clevertec.house.cache.Cache;
import by.clevertec.house.domain.House;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.proxy.HouseCacheableAspect;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.util.HouseTestBuilder;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@RequiredArgsConstructor
public class HouseServiceImplCacheIntegrationTest extends AbstractTest {

    private final HouseCacheableAspect cacheableAspect;
    private final HouseServiceImpl houseService;

    @MockBean
    private HouseRepository houseRepository;

    @MockBean
    private HouseMapper houseMapper;

    @MockBean
    private Cache<UUID, Object> cache;

    @MockBean
    private ProceedingJoinPoint joinPoint;

    @Test
    void getShouldReturnHouseResponseDtoFromAop_whenHouseResponseDtoWithCurrentUuidIsNotInCacheButIsInRepository() throws Throwable {
        HouseResponseDto expected = HouseTestBuilder.builder()
                .build()
                .buildHouseResponseDto();

        lenient()
                .when(joinPoint.getArgs())
                .thenReturn(new Object[]{HOUSE_UUID});
        lenient()
                .when(cache.get(HOUSE_UUID))
                .thenReturn(null);
        when(joinPoint.proceed())
                .thenReturn(expected);

        HouseResponseDto actual = (HouseResponseDto) cacheableAspect.cacheableGet(joinPoint);

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
    void getShouldReturnHouseResponseDtoFromRepository_whenHouseWithCurrentUuidExist() {
        HouseResponseDto expected = HouseTestBuilder.builder()
                .build()
                .buildHouseResponseDto();
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        when(houseRepository.findByUuid(HOUSE_UUID))
                .thenReturn(Optional.of(house));
        when(houseMapper.toDto(house))
                .thenReturn(expected);

        HouseResponseDto actual = houseService.getByUuid(HOUSE_UUID);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(House.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(House.Fields.area, expected.getArea())
                .hasFieldOrPropertyWithValue(House.Fields.country, expected.getCountry())
                .hasFieldOrPropertyWithValue(House.Fields.city, expected.getCity())
                .hasFieldOrPropertyWithValue(House.Fields.street, expected.getStreet())
                .hasFieldOrPropertyWithValue(House.Fields.number, expected.getNumber())
                .hasFieldOrPropertyWithValue("created", expected.getCreated());
    }
}
