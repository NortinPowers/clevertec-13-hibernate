package by.clevertec.house.controller;

import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.service.HouseHistoryService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HouseHistoryController {

    private final HouseHistoryService houseHistoryService;

    @GetMapping("/houses/resident/{uuid}")
    public ResponseEntity<List<HouseResponseDto>> getHistoryPersonHousesOfResidence(@PathVariable UUID uuid) {
        return ResponseEntity.ok(houseHistoryService.getPersonHousesOfResidence(uuid));
    }

    @GetMapping("/houses/owner/{uuid}")
    public ResponseEntity<List<HouseResponseDto>> getHistoryPersonOwnershipOfHouses(@PathVariable UUID uuid) {
        return ResponseEntity.ok(houseHistoryService.getPersonOwnershipOfHouses(uuid));
    }

    @GetMapping("/residents/house/{uuid}")
    public ResponseEntity<List<PersonResponseDto>> getHistoryPeopleLivingInHouse(@PathVariable UUID uuid) {
        return ResponseEntity.ok(houseHistoryService.getPeopleLivingInHouse(uuid));

    }

    @GetMapping("/owners/house/{uuid}")
    public ResponseEntity<List<PersonResponseDto>> getHistoryPeopleWhoOwnHouse(@PathVariable UUID uuid) {
        return ResponseEntity.ok(houseHistoryService.getPeopleWhoOwnHouse(uuid));
    }
}
