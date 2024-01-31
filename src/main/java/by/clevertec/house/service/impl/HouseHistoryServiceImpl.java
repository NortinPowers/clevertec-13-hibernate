package by.clevertec.house.service.impl;

import static by.clevertec.house.util.CheckerUtil.checkList;

import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.service.HouseHistoryService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseHistoryServiceImpl implements HouseHistoryService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public List<HouseResponseDto> getPersonHousesOfResidence(UUID personUuid) {
        List<House> houses = houseRepository.findAllByResidentUuid(personUuid);
        checkList(houses, House.class);
        return houses.stream()
                .map(houseMapper::toDto)
                .toList();
    }

    @Override
    public List<HouseResponseDto> getPersonOwnershipOfHouses(UUID personUuid) {
        List<House> houses = houseRepository.findAllByOwnersUuid(personUuid);
        checkList(houses, House.class);
        return houses.stream()
                .map(houseMapper::toDto)
                .toList();
    }

    @Override
    public List<PersonResponseDto> getPeopleLivingInHouse(UUID houseUuid) {
        List<Person> residents = personRepository.findAllResidentsByHouseUuid(houseUuid);
        checkList(residents, Person.class);
        return residents.stream()
                .map(personMapper::toDto)
                .toList();
    }

    @Override
    public List<PersonResponseDto> getPeopleWhoOwnHouse(UUID houseUuid) {
        List<Person> owners = personRepository.findAllOwnersByHouseUuid(houseUuid);
        checkList(owners, Person.class);
        return owners.stream()
                .map(personMapper::toDto)
                .toList();
    }
}
