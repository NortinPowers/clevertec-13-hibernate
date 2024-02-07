package by.clevertec.house.service.impl;

import static by.clevertec.house.util.CheckerUtil.checkList;
import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_HOUSE_OWNER_EXIST_EXCEPTION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CONDITIONAL_HOUSE_OWNER_NOT_EXIST_EXCEPTION_MESSAGE;

import by.clevertec.exception.ConditionalException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.proxy.Cacheable;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.repository.PersonRepository;
import by.clevertec.house.service.HouseService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;
    private final PersonRepository personRepository;

    @Override
    public Page<HouseResponseDto> getAll(Pageable pageable) {
        Page<HouseResponseDto> housesPage = houseRepository.findAll(pageable)
                .map(houseMapper::toDto);
        checkList(housesPage.getContent(), House.class);
        return housesPage;
    }

    @Override
    @Cacheable
    public HouseResponseDto getByUuid(UUID uuid) {
        return houseRepository.findByUuid(uuid)
                .map(houseMapper::toDto)
                .orElseThrow(() -> CustomEntityNotFoundException.of(House.class, uuid));
    }

    @Override
    @Cacheable
    @Transactional
    public UUID save(HouseRequestDto houseDto) {
        House house = houseMapper.toDomain(houseDto);
        house.setCreateDate(LocalDateTime.now());
        if (house.getUuid() == null) {
            house.setUuid(UUID.randomUUID());
        }
        return houseRepository.save(house).getUuid();
    }

    @Override
    @Transactional
    public void save(House house) {
        houseRepository.save(house);
    }

    @Override
    @Cacheable
    @Transactional
    public void update(UUID uuid, HouseRequestDto houseDto) {
        Optional<House> houseOptional = houseRepository.findByUuid(uuid);
        if (houseOptional.isPresent()) {
            House updated = houseMapper.toDomain(houseDto);
            houseMapper.merge(houseOptional.get(), updated);
        } else {
            throw CustomEntityNotFoundException.of(House.class, uuid);
        }
    }

    @Override
    @Cacheable
    @Transactional
    public void delete(UUID uuid) {
        Optional<House> houseOptional = houseRepository.findByUuid(uuid);
        if (houseOptional.isPresent()) {
            if (houseOptional.get().getResidents().isEmpty()) {
                houseRepository.deleteByUuid(uuid);
            } else {
                throw new ConditionalException(CONDITIONAL_EXCEPTION_MESSAGE);
            }
        } else {
            throw CustomEntityNotFoundException.of(House.class, uuid);
        }
    }

    @Override
    public List<PersonResponseDto> getResidents(UUID uuid) {
        Optional<House> houseOptional = houseRepository.findByUuid(uuid);
        if (houseOptional.isPresent()) {
            Set<Person> residents = houseOptional.get().getResidents();
            checkList(residents, Person.class);
            return residents.stream()
                    .map(personMapper::toDto)
                    .distinct()
                    .toList();
        } else {
            throw CustomEntityNotFoundException.of(House.class, uuid);
        }
    }

    @Override
    @Transactional
    public void addOwner(UUID uuid, UUID personUuid) {
        Optional<House> houseOptional = houseRepository.findByUuid(uuid);
        if (houseOptional.isPresent()) {
            Optional<Person> optionalPerson = personRepository.findByUuid(personUuid);
            if (optionalPerson.isPresent()) {
                House house = houseOptional.get();
                Person person = optionalPerson.get();
                Set<Person> owners = house.getOwners();
                if (owners.stream().anyMatch(p -> p.equals(person))) {
                    throw new ConditionalException(CONDITIONAL_HOUSE_OWNER_EXIST_EXCEPTION_MESSAGE);
                }
                owners.add(optionalPerson.get());
                houseRepository.save(house);
            } else {
                throw CustomEntityNotFoundException.of(Person.class, personUuid);
            }
        } else {
            throw CustomEntityNotFoundException.of(House.class, uuid);
        }
    }

    @Override
    @Transactional
    public void deleteOwner(UUID uuid, UUID personUuid) {
        Optional<House> houseOptional = houseRepository.findByUuid(uuid);
        if (houseOptional.isPresent()) {
            Optional<Person> optionalPerson = personRepository.findByUuid(personUuid);
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                Set<Person> owners = houseOptional.get().getOwners();
                owners.stream()
                        .filter(p -> p.equals(person))
                        .findAny()
                        .orElseThrow(() -> new ConditionalException(CONDITIONAL_HOUSE_OWNER_NOT_EXIST_EXCEPTION_MESSAGE));
                owners.removeIf(owner -> owner.getUuid().equals(optionalPerson.get().getUuid()));
            } else {
                throw CustomEntityNotFoundException.of(Person.class, personUuid);
            }
        } else {
            throw CustomEntityNotFoundException.of(House.class, uuid);
        }
    }

    @Override
    public Page<HouseResponseDto> getHouseSearchResult(String condition, Pageable pageable) {
        Page<HouseResponseDto> houses = houseRepository.getHouseSearchResult(condition, pageable)
                .map(houseMapper::toDto);
        checkList(houses.getContent(), House.class);
        return houses;
    }
}
