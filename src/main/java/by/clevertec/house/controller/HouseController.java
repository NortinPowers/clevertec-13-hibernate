package by.clevertec.house.controller;

import static by.clevertec.house.util.ResponseUtils.ADDED_HOUSE_OWNER_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.DELETED_HOUSE_OWNER_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.getSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.house.domain.House;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.model.BaseResponse;
import by.clevertec.house.service.HouseService;
import by.clevertec.model.ErrorValidationResponse;
import by.clevertec.model.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
@Tag(name = "House", description = "Houses management API")
public class HouseController {

    private final HouseService houseService;

    @GetMapping
    @Operation(
            summary = "Retrieves a page of houses from the list of all houses depending on the page",
            description = "Collect houses from the list of all houses. Default page size - 15 elements. The answer is an array of houses with uuid, area, country, city, street, number and created for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = HouseResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<Page<HouseResponseDto>> getAll(@Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                         @PageableDefault(size = 15)
                                                         @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(houseService.getAll(pageable));
    }

    /**
     * Получает информацию о доме по его уникальному идентификатору.
     *
     * @param uuid Уникальный идентификатор дома.
     * @return {@link ResponseEntity} с объектом {@link HouseResponseDto}.
     */
    @GetMapping("/{uuid}")
    @Operation(
            summary = "Retrieve the house by uuid",
            description = "Get house by specifying its uuid. The response is a house with uuid, area, country, city, street, number and created",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = HouseResponseDto.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<HouseResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(houseService.getByUuid(uuid));
    }

    /**
     * Сохраняет новый дом на основе предоставленных данных.
     *
     * @param house Объект {@link HouseRequestDto}, содержащий данные нового дома.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @PostMapping
    @Operation(
            summary = "Create new house",
            description = "Create new house. The response is a message about the successful creation of a house",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> save(@RequestBody HouseRequestDto house) {
        houseService.save(house);
        return ResponseEntity.ok(getSuccessResponse(CREATION_MESSAGE, House.class));

    }

    /**
     * Обновляет данные дома по его уникальному идентификатору.
     *
     * @param uuid  Уникальный идентификатор дома, который требуется обновить.
     * @param house Объект {@link HouseRequestDto}, содержащий обновленные данные дома.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @PatchMapping("/{uuid}")
    @Operation(
            summary = "Update the house by uuid",
            description = "Update the house by specifying its uuid. The response is a message about the successful update a house",
            tags = "patch"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> update(@PathVariable UUID uuid,
                                               @RequestBody HouseRequestDto house) {
        houseService.update(uuid, house);
        return ResponseEntity.ok(getSuccessResponse(UPDATE_MESSAGE, House.class));

    }

    /**
     * Удаляет дом по его уникальному идентификатору.
     *
     * @param uuid Уникальный идентификатор дома, который требуется удалить.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @DeleteMapping("/{uuid}")
    @Operation(
            summary = "Delete the house by uuid",
            description = "Delete the house by specifying its uuid. The response is a message about the successful deletion a house",
            tags = "delete"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> delete(@PathVariable UUID uuid) {
        houseService.delete(uuid);
        return ResponseEntity.ok(getSuccessResponse(DELETION_MESSAGE, House.class));
    }

    /**
     * Получает список жителей дома по его уникальному идентификатору.
     *
     * @param uuid Уникальный идентификатор дома.
     * @return {@link ResponseEntity} с объектом {@link List} содержащим информацию о жителях дома.
     */
    @GetMapping("/{uuid}/residents")
    @Operation(
            summary = "Retrieves a list of all people living in the house specifying its uuid",
            description = "Collect people living in the house. The answer is an array of people with uuid, name, surname, gender, passport (array of passportSeries and passportNumber), created, updated and houseUuid for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<PersonResponseDto>> getResidents(@PathVariable UUID uuid) {
        List<PersonResponseDto> residents = houseService.getResidents(uuid);
        return ResponseEntity.ok(residents);
    }

    @PatchMapping("/{uuid}/add/owner/{personUuid}")
    @Operation(
            summary = "Adds a new owner (by its uuid) to the house (by its uuid)",
            description = "Add an owner (by its uuid) to the house by specifying its uuid. The response is a message about the successful addition of the owner",
            tags = "patch"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> addOwner(@PathVariable UUID uuid,
                                                 @PathVariable UUID personUuid) {
        houseService.addOwner(uuid, personUuid);
        return ResponseEntity.ok(getSuccessResponse(ADDED_HOUSE_OWNER_MESSAGE, uuid.toString()));
    }

    @PatchMapping("/{uuid}/delete/owner/{personUuid}")
    @Operation(
            summary = "Delete owner (by its uuid) to the house (by its uuid)",
            description = "Delete an owner (by its uuid) to the house by specifying its uuid. The response is a message about the successful deletion of the owner",
            tags = "patch"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> deleteOwner(@PathVariable UUID uuid,
                                                    @PathVariable UUID personUuid) {
        houseService.deleteOwner(uuid, personUuid);
        return ResponseEntity.ok(getSuccessResponse(DELETED_HOUSE_OWNER_MESSAGE, uuid.toString()));
    }

    @GetMapping("/search/{condition}")
    @Operation(
            summary = "Retrieves a page of houses from the list of all houses found by the search condition depending on the page",
            description = "Collect houses from the list of all houses according to the search condition. The default page size is 15 elements. The answer is an array of houses with uuid, area, country, city, street, number and created for each element of the array",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = HouseResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<Page<HouseResponseDto>> getHouseSearchResult(@PathVariable String condition,
                                                                       @Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                                       @PageableDefault(size = 15)
                                                                       @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(houseService.getHouseSearchResult(condition, pageable));
    }
}
