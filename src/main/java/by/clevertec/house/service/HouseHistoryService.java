package by.clevertec.house.service;

import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import java.util.List;
import java.util.UUID;

public interface HouseHistoryService {

    /**
     * Получает список объектов DTO (Data Transfer Object) домов, в которых проживает человек с указанным идентификатором.
     *
     * @param uuid Идентификатор человека.
     * @return Список DTO домов, в которых проживает указанный человек.
     */
    List<HouseResponseDto> getPersonHousesOfResidence(UUID uuid);

    /**
     * Получает список объектов DTO домов, которые принадлежат человеку с указанным идентификатором.
     *
     * @param uuid Идентификатор человека.
     * @return Список DTO домов, принадлежащих указанному человеку.
     */
    List<HouseResponseDto> getPersonOwnershipOfHouses(UUID uuid);

    /**
     * Получает список объектов DTO людей, проживающих в доме с указанным идентификатором.
     *
     * @param uuid Идентификатор дома.
     * @return Список DTO людей, проживающих в указанном доме.
     */
    List<PersonResponseDto> getPeopleLivingInHouse(UUID uuid);

    /**
     * Получает список объектов DTO людей, которые являются владельцами дома с указанным идентификатором.
     *
     * @param uuid Идентификатор дома.
     * @return Список DTO людей, владеющих указанным домом.
     */
    List<PersonResponseDto> getPeopleWhoOwnHouse(UUID uuid);
}
