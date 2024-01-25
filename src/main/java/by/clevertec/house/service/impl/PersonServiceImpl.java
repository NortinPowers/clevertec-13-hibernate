package by.clevertec.house.service.impl;

import static by.clevertec.house.util.CheckerUtil.checkIllegalArgument;
import static by.clevertec.house.util.CheckerUtil.checkList;

import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonPutRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.exception.CustomEntityNotFoundException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.JdbcPersonRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.service.PersonService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final JdbcPersonRepository jdbcPersonRepository;
    private final HouseRepository houseRepository;
    private final PersonMapper personMapper;
    private final HouseMapper houseMapper;

    @Override
    public List<PersonResponseDto> getAll(int pageNumber, int pageSize) {
        List<Person> people = personRepository.getAll(pageNumber, pageSize);
        checkList(people, Person.class);
        return people.stream()
                .map(personMapper::toDto)
                .toList();
    }

    @Override
    public PersonResponseDto getByUuid(UUID uuid) {
        return personMapper.toDto(personRepository.getByUuid(uuid));
    }

    @Override
    public void save(PersonPutRequestDto personDto) {
        Person person = personMapper.toDomain(personDto);
        if (person.getUuid() == null) {
            person.setUuid(UUID.randomUUID());
        }
        House house = getResidentHouse(personDto);
        person.setHouse(house);
        List<House> houses = getHouseOwners(personDto);
        person.setOwnedHouses(houses);
        for (House currentHouse : houses) {
            currentHouse.getOwners().add(person);
            houseRepository.save(currentHouse);
        }
        personRepository.save(person);
    }

    @Override
    public void update(UUID uuid, PersonPutRequestDto personDto) {
        Person person = personRepository.getByUuid(uuid);
        Person updated = personMapper.toDomain(personDto);
        if (!person.equals(updated)) {
            Person merged = personMapper.merge(person, updated);
            merged.setCreateDate(person.getCreateDate());
            merged.setUpdateDate(LocalDateTime.now());
            if (updated.getOwnedHouses() == null) {
                merged.setOwnedHouses(Collections.emptyList());
            }
            personRepository.update(merged);
        }
    }

    @Override
    public void updatePath(UUID uuid, PersonPathRequestDto personDto) {
        Person person = personRepository.getByUuid(uuid);
        Person updated = personMapper.toDomain(personDto);
        if (!person.equals(updated)) {
            Person merged = personMapper.merge(person, updated);
            merged.setCreateDate(person.getCreateDate());
            merged.setUpdateDate(LocalDateTime.now());
            personRepository.update(merged);
        }
    }

    @Override
    public void delete(UUID uuid) {
        Person person = personRepository.getByUuid(uuid);
        person.getOwnedHouses().forEach(house -> {
            house.getOwners().remove(person);
            houseRepository.save(house);
        });
        person.getOwnedHouses().clear();
        personRepository.delete(uuid);
    }

    @Override
    public List<HouseResponseDto> getOwnedHouses(UUID uuid) {
        checkIllegalArgument(uuid, "UUID cannot be null");
        List<House> houses = houseRepository.getOwnedHousesByUuid(uuid);
        if (houses.isEmpty()) {
            throw new CustomEntityNotFoundException("No houses owned by the person with UUID " + uuid);
        }
        return houses.stream()
                .map(houseMapper::toDto)
                .toList();
    }

    @Override
    public PersonResponseDto getByName(String name) {
        checkIllegalArgument(name, "Name cannot be null");
        Optional<Person> person = jdbcPersonRepository.getByName(name);
        return person.map(personMapper::toDto)
                .orElseThrow(() -> new CustomEntityNotFoundException("Person with name " + name + " not found"));
    }

    private House getResidentHouse(PersonPutRequestDto personDto) {
        UUID uuid = personDto.getHouseUuid();
        checkIllegalArgument(uuid, "Uuid of the person`s house is missing");
        return houseRepository.getByUuid(uuid);
    }

    private List<House> getHouseOwners(PersonPutRequestDto personDto) {
        List<UUID> ownedHouseUuids = personDto.getOwnedHouseUuids();
        List<House> houses;
        if (ownedHouseUuids == null || ownedHouseUuids.isEmpty()) {
            houses = Collections.emptyList();
        } else {
            houses = ownedHouseUuids.stream()
                    .map(houseRepository::getByUuid)
                    .distinct()
                    .toList();
        }
        return houses;
    }
}
