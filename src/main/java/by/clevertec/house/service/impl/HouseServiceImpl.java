package by.clevertec.house.service.impl;

import static by.clevertec.house.util.CheckerUtil.checkList;

import by.clevertec.house.domain.House;
import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.exception.ConditionalException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.mapper.PersonMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.service.HouseService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;

    @Override
    public List<HouseResponseDto> getAll(int pageNumber, int pageSize) {
        List<House> houses = houseRepository.getAll(pageNumber, pageSize);
        checkList(houses, House.class);
        return houses.stream()
                .map(houseMapper::toDto)
                .toList();
    }

    @Override
    public HouseResponseDto getByUuid(UUID uuid) {
        return houseMapper.toDto(houseRepository.getByUuid(uuid));
    }

    @Override
    public void save(HouseRequestDto houseDto) {
        House house = houseMapper.toDomain(houseDto);
        house.setCreateDate(LocalDateTime.now());
        if (house.getUuid() == null) {
            house.setUuid(UUID.randomUUID());
        }
        houseRepository.save(house);
    }

    @Override
    public void update(UUID uuid, HouseRequestDto houseDto) {
        House house = houseRepository.getByUuid(uuid);
        House updated = houseMapper.toDomain(houseDto);
        House merged = houseMapper.merge(house, updated);
        houseRepository.update(merged);
    }

    @Override
    public void delete(UUID uuid) {
        House house = houseRepository.getByUuid(uuid);
        if (house.getResidents().isEmpty()) {
            houseRepository.delete(uuid);
        } else {
            throw new ConditionalException("Сan not delete a house in which at least 1 person lives");
        }
    }

    @Override
    public List<PersonResponseDto> getResidents(UUID uuid) {
        House house = houseRepository.getByUuid(uuid);
        List<Person> residents = house.getResidents();
        checkList(residents, Person.class);
        return residents.stream()
                .map(personMapper::toDto)
                .distinct()
                .toList();
    }
}
