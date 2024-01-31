package by.clevertec.house.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.clevertec.house.AbstractTest;
import by.clevertec.house.domain.Passport;
import by.clevertec.house.dto.PassportDto;
import by.clevertec.house.util.PassportTestBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class PassportMapperTest extends AbstractTest {

    private final PassportMapper passportMapper;

    @Test
    void toDtoShouldReturnPassportDto_whenPassportPassed() {
        Passport passport = PassportTestBuilder.builder()
                .build()
                .buildPassport();
        PassportDto expected = PassportTestBuilder.builder()
                .build()
                .buildPassportDto();

        PassportDto actual = passportMapper.toDto(passport);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Passport.Fields.passportSeries, expected.getPassportSeries())
                .hasFieldOrPropertyWithValue(Passport.Fields.passportNumber, expected.getPassportNumber());
    }

    @Test
    void toDomainShouldReturnPassport_whenPassportDtoPassed() {
        PassportDto passportDto = PassportTestBuilder.builder()
                .build()
                .buildPassportDto();
        Passport expected = PassportTestBuilder.builder()
                .build()
                .buildPassport();

        Passport actual = passportMapper.toDomain(passportDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Passport.Fields.passportSeries, expected.getPassportSeries())
                .hasFieldOrPropertyWithValue(Passport.Fields.passportNumber, expected.getPassportNumber());
    }
}
