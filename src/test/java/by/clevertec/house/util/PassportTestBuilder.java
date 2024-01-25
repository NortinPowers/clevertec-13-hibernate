package by.clevertec.house.util;

import static by.clevertec.house.util.TestConstant.PASSPORT_NUMBER;
import static by.clevertec.house.util.TestConstant.PASSPORT_SERIES;

import by.clevertec.house.domain.Passport;
import by.clevertec.house.dto.PassportDto;
import lombok.Builder;
import lombok.Data;

@Builder(setterPrefix = "with")
@Data
public class PassportTestBuilder {

    @Builder.Default
    private String passportSeries = PASSPORT_SERIES;

    @Builder.Default
    private String passportNumber = PASSPORT_NUMBER;

    public Passport buildPassport() {
        Passport passport = new Passport();
        passport.setPassportSeries(passportSeries);
        passport.setPassportNumber(passportNumber);
        return passport;
    }

    public PassportDto buildPassportDto() {
        PassportDto passport = new PassportDto();
        passport.setPassportSeries(passportSeries);
        passport.setPassportNumber(passportNumber);
        return passport;
    }
}
