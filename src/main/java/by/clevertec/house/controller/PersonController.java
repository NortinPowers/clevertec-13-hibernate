package by.clevertec.house.controller;

import static by.clevertec.house.util.CheckerUtil.checkIllegalArgument;
import static by.clevertec.house.util.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.getSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonPutRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.model.BaseResponse;
import by.clevertec.house.model.ErrorValidationResponse;
import by.clevertec.house.model.ExceptionResponse;
import by.clevertec.house.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
@Tag(name = "Person", description = "People management API")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    @Operation(
            summary = "Retrieves a page of people from the list of all houses depending on the page",
            description = "Collect people from the list of all people. Default page size - 15 elements. The answer is an array of people with uuid, name, surname, gender, passport (array of passportSeries and passportNumber), created, updated and houseUuid for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "410", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<Page<PersonResponseDto>> getAll(@Parameter(name = "Pageable parameters", example = "page=0&size=15&sort=created,asc")
                                                          @PageableDefault(size = 15)
                                                          @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(personService.getAll(pageable));
    }

    /**
     * Получает информацию о персоне по её уникальному идентификатору.
     *
     * @param uuid Уникальный идентификатор персоны.
     * @return {@link ResponseEntity} с объектом {@link PersonResponseDto}.
     */
    @GetMapping("/{uuid}")
    @Operation(
            summary = "Retrieve the person by uuid",
            description = "Get person by specifying its uuid. The response is a person with uuid, area, country, city, street, number and created",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = PersonResponseDto.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})

    public ResponseEntity<PersonResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(personService.getByUuid(uuid));
    }

    /**
     * Сохраняет новую персону на основе предоставленных данных.
     *
     * @param person Объект {@link PersonPutRequestDto}, содержащий данные новой персоны.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @PostMapping
    @Operation(
            summary = "Create new person",
            description = "Create new person. The response is a message about the successful creation of a person",
            tags = "post"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "409", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> save(@Valid @RequestBody PersonPutRequestDto person) {
        personService.save(person);
        return ResponseEntity.ok(getSuccessResponse(CREATION_MESSAGE, Person.class));
    }

    /**
     * Обновляет данные персоны по её уникальному идентификатору.
     *
     * @param uuid   Уникальный идентификатор персоны, которую требуется обновить.
     * @param person Объект {@link PersonPutRequestDto}, содержащий обновленные данные персоны.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @PutMapping("/{uuid}")
    @Operation(
            summary = "Update the person by uuid",
            description = "Update the person by specifying its uuid. The response is a message about the successful update a person",
            tags = "put"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> update(@PathVariable UUID uuid,
                                               @Valid @RequestBody PersonPutRequestDto person) {
        personService.update(uuid, person);
        return ResponseEntity.ok(getSuccessResponse(UPDATE_MESSAGE, Person.class));
    }

    /**
     * Частично обновляет данные персоны по её уникальному идентификатору.
     *
     * @param uuid   Уникальный идентификатор персоны, которую требуется частично обновить.
     * @param person Объект {@link PersonPathRequestDto}, содержащий частично обновленные данные персоны.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     * @throws IllegalArgumentException Если переданный объект {@code null}.
     */
    @PatchMapping("/{uuid}")
    @Operation(
            summary = "Update the person by uuid",
            description = "Update the person by specifying its uuid. The response is a message about the successful update a person",
            tags = "patch"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorValidationResponse.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> updatePath(@PathVariable UUID uuid,
                                                   @RequestBody PersonPathRequestDto person) {
        checkIllegalArgument(person, "Incorrect person data");
        personService.updatePath(uuid, person);
        return ResponseEntity.ok(getSuccessResponse(UPDATE_MESSAGE, Person.class));
    }

    /**
     * Удаляет персону по её уникальному идентификатору.
     *
     * @param uuid Уникальный идентификатор персоны, которую требуется удалить.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @DeleteMapping("/{uuid}")
    @Operation(
            summary = "Delete the person by uuid",
            description = "Delete the person by specifying its uuid. The response is a message about the successful deletion a person",
            tags = "delete"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = BaseResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "406", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<BaseResponse> delete(@PathVariable UUID uuid) {
        personService.delete(uuid);
        return ResponseEntity.ok(getSuccessResponse(DELETION_MESSAGE, Person.class));
    }

    /**
     * Получает список домов, которыми владеет указанная персона по её уникальному идентификатору.
     *
     * @param uuid Уникальный идентификатор персоны.
     * @return {@link ResponseEntity} с объектом {@link List} содержащим информацию о домах, которыми владеет персона.
     */
    @GetMapping("/houses/owned/{uuid}")
    @Operation(
            summary = "Retrieves a list of all houses owned by a person by specifying their uuid",
            description = "Collect houses in the possession of a person. The answer is an array of houses with uuid, area, country, city, street, number and created for each of the array element",
            tags = "get"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = HouseResponseDto.class)), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE)})})
    public ResponseEntity<List<HouseResponseDto>> getOwnedHouses(@PathVariable UUID uuid) {
        List<HouseResponseDto> ownedHouses = personService.getOwnedHouses(uuid);
        return ResponseEntity.ok(ownedHouses);
    }

    @PatchMapping("/{uuid}/house/change/{houseUuid}")
    public ResponseEntity<BaseResponse> changeHome(@PathVariable UUID uuid,
                                                   @PathVariable UUID houseUuid) {
        personService.changeHome(uuid, houseUuid);
        return ResponseEntity.ok(getSuccessResponse("The place of residence for the person (uuid:%s) has been successfully changed", uuid.toString()));
    }

    @GetMapping("/search/{condition}")
    public ResponseEntity<Page<PersonResponseDto>> getPersonSearchResult(@PathVariable String condition,
                                                                         Pageable pageable) {
        return ResponseEntity.ok(personService.getPersonSearchResult(condition, pageable));
    }
}
