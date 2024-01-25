package by.clevertec.house.util;

import static by.clevertec.house.util.TestConstant.HOUSE_AREA;
import static by.clevertec.house.util.TestConstant.HOUSE_CITY;
import static by.clevertec.house.util.TestConstant.HOUSE_COUNTRY;
import static by.clevertec.house.util.TestConstant.HOUSE_CREATED;
import static by.clevertec.house.util.TestConstant.HOUSE_CREATED_DATE;
import static by.clevertec.house.util.TestConstant.HOUSE_NUMBER;
import static by.clevertec.house.util.TestConstant.HOUSE_STREET;
import static by.clevertec.house.util.TestConstant.HOUSE_UUID;

import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder(setterPrefix = "with")
@Data
public class HouseTestBuilder {

    @Builder.Default
    private UUID uuid = HOUSE_UUID;

    @Builder.Default
    private String area = HOUSE_AREA;

    @Builder.Default
    private String country = HOUSE_COUNTRY;

    @Builder.Default
    private String city = HOUSE_CITY;

    @Builder.Default
    private String street = HOUSE_STREET;

    @Builder.Default
    private String number = HOUSE_NUMBER;

    @Builder.Default
    private String created = HOUSE_CREATED;

    @Builder.Default
    private LocalDateTime createDate = HOUSE_CREATED_DATE;

    @Builder.Default
    private List<UUID> residentUuids = new ArrayList<>();

    @Builder.Default
    private List<UUID> ownerUuids = new ArrayList<>();

    @Builder.Default
    private List<Person> residents = new ArrayList<>();

    @Builder.Default
    private Set<Person> owners = new HashSet<>();

    public House buildHouse() {
        House house = new House();
        house.setUuid(uuid);
        house.setArea(area);
        house.setCountry(country);
        house.setCity(city);
        house.setStreet(street);
        house.setNumber(number);
        house.setCreateDate(createDate);
        house.setResidents(residents);
        house.setOwners(owners);
        return house;
    }

    public HouseRequestDto buildHouseRequestDto() {
        HouseRequestDto houseRequestDto = new HouseRequestDto();
        houseRequestDto.setUuid(uuid);
        houseRequestDto.setArea(area);
        houseRequestDto.setCountry(country);
        houseRequestDto.setCity(city);
        houseRequestDto.setStreet(street);
        houseRequestDto.setNumber(number);
        houseRequestDto.setResidentUuids(residentUuids);
        houseRequestDto.setOwnerUuids(ownerUuids);
        return houseRequestDto;
    }

    public HouseResponseDto buildHouseResponseDto() {
        HouseResponseDto houseResponseDto = new HouseResponseDto();
        houseResponseDto.setUuid(uuid);
        houseResponseDto.setArea(area);
        houseResponseDto.setCountry(country);
        houseResponseDto.setCity(city);
        houseResponseDto.setStreet(street);
        houseResponseDto.setNumber(number);
        houseResponseDto.setCreated(created);
        return houseResponseDto;
    }
}
