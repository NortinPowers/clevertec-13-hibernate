package by.clevertec.house.util;

import static by.clevertec.house.util.TestConstant.HOUSE_UUID;
import static by.clevertec.house.util.TestConstant.PERSON_CREATED;
import static by.clevertec.house.util.TestConstant.PERSON_CREATE_DATE;
import static by.clevertec.house.util.TestConstant.PERSON_GENDER;
import static by.clevertec.house.util.TestConstant.PERSON_NAME;
import static by.clevertec.house.util.TestConstant.PERSON_SURNAME;
import static by.clevertec.house.util.TestConstant.PERSON_UPDATED;
import static by.clevertec.house.util.TestConstant.PERSON_UPDATE_DATE;
import static by.clevertec.house.util.TestConstant.PERSON_UUID;

import by.clevertec.house.domain.Gender;
import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Passport;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.PassportDto;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonRequestDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class PersonTestBuilder {

    @Builder.Default
    private UUID uuid = PERSON_UUID;

    @Builder.Default
    private String name = PERSON_NAME;

    @Builder.Default
    private String surname = PERSON_SURNAME;

    @Builder.Default
    private Gender gender = PERSON_GENDER;

    @Builder.Default
    private Passport passport = PassportTestBuilder.builder().build().buildPassport();

    @Builder.Default
    private LocalDateTime createDate = PERSON_CREATE_DATE;

    @Builder.Default
    private LocalDateTime updateDate = PERSON_UPDATE_DATE;

    @Builder.Default
    private House house = HouseTestBuilder.builder().build().buildHouse();

    @Builder.Default
    private List<House> ownedHouses = new ArrayList<>();

    @Builder.Default
    private PassportDto passportDto = PassportTestBuilder.builder().build().buildPassportDto();

    @Builder.Default
    private UUID houseUuid = HOUSE_UUID;

    @Builder.Default
    private List<UUID> ownedHouseUuids = new ArrayList<>();

    @Builder.Default
    private String created = PERSON_CREATED;

    @Builder.Default
    private String updated = PERSON_UPDATED;

    public Person buildPerson() {
        Person person = new Person();
        person.setUuid(uuid);
        person.setName(name);
        person.setSurname(surname);
        person.setGender(gender);
        person.setPassport(passport);
        person.setCreateDate(createDate);
        person.setUpdateDate(updateDate);
        person.setHouse(house);
        person.setOwnedHouses(ownedHouses);
        return person;
    }

    public PersonPathRequestDto buildPersonPathRequestDto() {
        PersonPathRequestDto person = new PersonPathRequestDto();
        person.setName(name);
        person.setSurname(surname);
        person.setGender(gender);
        person.setPassport(passportDto);
        person.setHouseUuid(houseUuid);
        person.setOwnedHouseUuids(ownedHouseUuids);
        return person;
    }

    public PersonRequestDto buildPersonRequestDto() {
        PersonRequestDto person = new PersonRequestDto();
        person.setName(name);
        person.setSurname(surname);
        person.setGender(gender);
        person.setPassport(passportDto);
        person.setHouseUuid(houseUuid);
        person.setOwnedHouseUuids(ownedHouseUuids);
        return person;
    }

    public PersonResponseDto buildPersonResponseDto() {
        PersonResponseDto person = new PersonResponseDto();
        person.setUuid(uuid);
        person.setName(name);
        person.setSurname(surname);
        person.setGender(gender);
        person.setPassport(passportDto);
        person.setCreated(created);
        person.setUpdated(updated);
        person.setHouseUuid(houseUuid);
        return person;
    }
}
