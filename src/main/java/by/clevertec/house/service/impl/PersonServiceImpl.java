package by.clevertec.house.service.impl;

import static by.clevertec.house.util.CheckerUtil.checkList;
import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_RESIDENT_EXIST_EXCEPTION_MESSAGE;

import by.clevertec.exception.ConditionalException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.service.PersonService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final HouseRepository houseRepository;
    private final PersonMapper personMapper;
    private final HouseMapper houseMapper;

    @Override
    public Page<PersonResponseDto> getAll(Pageable pageable) {
        Page<PersonResponseDto> people = personRepository.findAll(pageable)
                .map(personMapper::toDto);
        checkList(people.getContent(), Person.class);
        return people;
    }

    @Override
    @Cacheable("persons")
    public PersonResponseDto getByUuid(UUID uuid) {
        return personRepository.findByUuid(uuid)
                .map(personMapper::toDto)
                .orElseThrow(() -> CustomEntityNotFoundException.of(Person.class, uuid));
    }

    @Override
    public void save(PersonRequestDto personDto) {
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
    public void update(UUID uuid, PersonRequestDto personDto) {
        Optional<Person> personOptional = personRepository.findByUuid(uuid);
        if (personOptional.isPresent()) {
            Person updated = personMapper.toDomain(personDto);
            Person person = personOptional.get();
            if (!person.equals(updated)) {
                Person merged = personMapper.merge(person, updated);
                merged.setCreateDate(person.getCreateDate());
                merged.setUpdateDate(LocalDateTime.now());
                if (updated.getOwnedHouses() == null) {
                    merged.setOwnedHouses(Collections.emptyList());
                }
            }
        } else {
            throw CustomEntityNotFoundException.of(Person.class, uuid);
        }
    }

    @Override
    public void updatePath(UUID uuid, PersonPathRequestDto personDto) {
        Optional<Person> personOptional = personRepository.findByUuid(uuid);
        if (personOptional.isPresent()) {
            Person updated = personMapper.toDomain(personDto);
            Person person = personOptional.get();
            if (!person.equals(updated)) {
                Person merged = personMapper.merge(person, updated);
                merged.setCreateDate(person.getCreateDate());
                merged.setUpdateDate(LocalDateTime.now());
            }
        } else {
            throw CustomEntityNotFoundException.of(Person.class, uuid);
        }
    }

    @Override
    public void delete(UUID uuid) {
        Optional<Person> personOptional = personRepository.findByUuid(uuid);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            person.getOwnedHouses().forEach(house -> {
                house.getOwners().remove(person);
                houseRepository.save(house);
            });
            person.getOwnedHouses().clear();
            personRepository.deleteByUuid(uuid);
        } else {
            throw CustomEntityNotFoundException.of(Person.class, uuid);
        }
    }

    @Override
    public List<HouseResponseDto> getOwnedHouses(UUID uuid) {
        Optional<Person> personOptional = personRepository.findByUuid(uuid);
        if (personOptional.isPresent()) {
            List<House> houses = personOptional.get().getOwnedHouses();
            if (houses.isEmpty()) {
                throw new CustomEntityNotFoundException("No houses owned by the person with UUID " + uuid);
            }
            return houses.stream()
                    .map(houseMapper::toDto)
                    .toList();
        } else {
            throw CustomEntityNotFoundException.of(Person.class, uuid);
        }
    }

    @Override
    public void changeHome(UUID uuid, UUID homeUuid) {
        Optional<Person> optionalPerson = personRepository.findByUuid(uuid);
        if (optionalPerson.isPresent()) {
            Optional<House> optionalHouse = houseRepository.findByUuid(homeUuid);
            if (optionalHouse.isPresent()) {
                House updated = optionalHouse.get();
                Person person = optionalPerson.get();
                if (!person.getHouse().equals(updated)) {
                    person.setHouse(updated);
                } else {
                    throw new ConditionalException(CONDITIONAL_RESIDENT_EXIST_EXCEPTION_MESSAGE);
                }
            } else {
                throw CustomEntityNotFoundException.of(House.class, homeUuid);
            }
        } else {
            throw CustomEntityNotFoundException.of(Person.class, uuid);
        }
    }

    @Override
    public Page<PersonResponseDto> getPersonSearchResult(String condition, Pageable pageable) {
        Page<PersonResponseDto> people = personRepository.getPersonSearchResult(condition, pageable)
                .map(personMapper::toDto);
        checkList(people.getContent(), Person.class);
        return people;
    }

    private House getResidentHouse(PersonRequestDto personDto) {
        UUID uuid = personDto.getHouseUuid();
        return houseRepository.findByUuid(uuid).orElseThrow(() -> CustomEntityNotFoundException.of(House.class, uuid));
    }

    private List<House> getHouseOwners(PersonRequestDto personDto) {
        List<UUID> ownedHouseUuids = personDto.getOwnedHouseUuids();
        List<House> houses;
        if (ownedHouseUuids == null || ownedHouseUuids.isEmpty()) {
            houses = Collections.emptyList();
        } else {
            houses = ownedHouseUuids.stream()
                    .map(houseRepository::findByUuid)
                    .flatMap(Optional::stream)
                    .distinct()
                    .toList();
        }
        return houses;
    }
}
