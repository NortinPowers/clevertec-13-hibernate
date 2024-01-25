package by.clevertec.house.util;

import by.clevertec.house.domain.Gender;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstant {

    public static final UUID HOUSE_UUID = UUID.fromString("76dbb74c-2f08-4bc0-8029-aed02147e737");
    public static final UUID PERSON_UUID = UUID.fromString("21281ae1-a9cd-4d69-96b6-2451472d5bc9");
    public static final UUID INCORRECT_UUID = UUID.fromString("010a71b3-1de3-43ac-ab98-130910fd8130");
    public static final UUID CORRECT_UUID = UUID.fromString("c05c4c2b-2510-4dda-99da-c0070a224cf8");
    public static final String HOUSE_AREA = "minsk region";
    public static final String HOUSE_COUNTRY = "belarus";
    public static final String HOUSE_CITY = "minsk";
    public static final String HOUSE_STREET = "first";
    public static final String HOUSE_NUMBER = "1";
    public static final LocalDateTime HOUSE_CREATED_DATE = LocalDateTime.of(2024, 1, 28, 11, 17, 0);
    public static final String HOUSE_CREATED = "2024-01-28T11:17:00";
    public static final int PAGE_NUMBER = 1;
    public static final int PAGE_SIZE = 15;
    public static final String PERSON_NAME = "john";
    public static final String PERSON_SURNAME = "gold";
    public static final Gender PERSON_GENDER = Gender.MALE;
    public static final String PASSPORT_SERIES = "mt";
    public static final String PASSPORT_NUMBER = "12fh45if1";
    public static final LocalDateTime PERSON_CREATE_DATE = LocalDateTime.of(2024, 1, 28, 11, 17, 0);
    public static final LocalDateTime PERSON_UPDATE_DATE = LocalDateTime.of(2024, 1, 28, 11, 17, 0);
    public static final String PERSON_CREATED = "14.01.24";
    public static final String PERSON_UPDATED = "14.01.24";
}
