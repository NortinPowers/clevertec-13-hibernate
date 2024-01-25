package by.clevertec.house.service;

import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import java.util.List;
import java.util.UUID;

public interface HouseHistoryService {

    List<HouseResponseDto> getPersonHousesOfResidence(UUID uuid);

    List<HouseResponseDto> getPersonOwnershipOfHouses(UUID uuid);

    List<PersonResponseDto> getPeopleLivingInHouse(UUID uuid);

    List<PersonResponseDto> getPeopleWhoOwnHouse(UUID uuid);
}
