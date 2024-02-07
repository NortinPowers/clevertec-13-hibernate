package by.clevertec.house.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.service.HouseHistoryService;
import by.clevertec.model.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Retrieves the list of houses in which the person lives from the list of all houses",
            description = "Collect houses in which the person lives from the list of all houses. The answer is an array of houses with uuid, area, country, city, street, number and created for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = HouseResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<HouseResponseDto>> getHistoryPersonHousesOfResidence(@PathVariable UUID uuid) {
        return ResponseEntity.ok(houseHistoryService.getPersonHousesOfResidence(uuid));
    }

    @GetMapping("/houses/owner/{uuid}")
    @Operation(
            summary = "Retrieves the list of houses owned by a person from the list of all houses",
            description = "Collect houses owned by a person from the list of all houses. The answer is an array of houses with uuid, area, country, city, street, number and created for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = HouseResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<HouseResponseDto>> getHistoryPersonOwnershipOfHouses(@PathVariable UUID uuid) {
        return ResponseEntity.ok(houseHistoryService.getPersonOwnershipOfHouses(uuid));
    }

    @GetMapping("/residents/house/{uuid}")
    @Operation(
            summary = "Retrieves a list of people who lived in the house from the list of all houses",
            description = "Collect people who lived in the house from the list of all people. The answer is an array of people with uuid, name, surname, gender, passport (array of passportSeries and passportNumber), created, updated and houseUuid for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<PersonResponseDto>> getHistoryPeopleLivingInHouse(@PathVariable UUID uuid) {
        return ResponseEntity.ok(houseHistoryService.getPeopleLivingInHouse(uuid));

    }

    @GetMapping("/owners/house/{uuid}")
    @Operation(
            summary = "Retrieves a list of people  who owned a house from the list of all houses",
            description = "Collect people who lived in the house from the list of all people. The answer is an array of people with uuid, name, surname, gender, passport (array of passportSeries and passportNumber), created, updated and houseUuid for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<PersonResponseDto>> getHistoryPeopleWhoOwnHouse(@PathVariable UUID uuid) {
        return ResponseEntity.ok(houseHistoryService.getPeopleWhoOwnHouse(uuid));
    }
}
