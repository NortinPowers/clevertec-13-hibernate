package by.clevertec.house.util;

import by.clevertec.house.domain.Gender;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstant {

    public static final UUID HOUSE_UUID = UUID.fromString("8e7daff2-df9a-4130-94af-1b4bc6ed3beb");
    public static final UUID ANOUTHER_HOUSE_UUID = UUID.fromString("2e7435dd-0a2c-4d87-b494-e66a228bd12b");
    public static final UUID PERSON_UUID = UUID.fromString("d7f21e40-6a65-4060-946a-600798656f68");
    public static final UUID INCORRECT_UUID = UUID.fromString("010a71b3-1de3-43ac-ab98-130910fd8130");
    public static final UUID CORRECT_UUID = UUID.fromString("c05c4c2b-2510-4dda-99da-c0070a224cf8");
    public static final String HOUSE_AREA = "minsk region";
    public static final String HOUSE_COUNTRY = "belarus";
    public static final String HOUSE_CITY = "minsk";
    public static final String HOUSE_STREET = "first";
    public static final String HOUSE_NUMBER = "1";
    public static final LocalDateTime HOUSE_CREATED_DATE = LocalDateTime.of(2024, 1, 28, 11, 17, 0);
    public static final String HOUSE_CREATED = "2024-01-28T11:17:00";
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 15;
    public static final String PERSON_NAME = "john";
    public static final String PERSON_SURNAME = "gold";
    public static final Gender PERSON_GENDER = Gender.MALE;
    public static final String PASSPORT_SERIES = "mt";
    public static final String PASSPORT_NUMBER = "12fh65ig1";
    public static final LocalDateTime PERSON_CREATE_DATE = LocalDateTime.of(2024, 1, 28, 11, 15, 0);
    public static final LocalDateTime PERSON_UPDATE_DATE = LocalDateTime.of(2024, 1, 28, 11, 16, 0);
    public static final String PERSON_CREATED = "2024-01-28T11:15:00";
    public static final String PERSON_UPDATED = "2024-01-28T11:16:00";
}
