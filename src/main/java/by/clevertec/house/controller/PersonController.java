package by.clevertec.house.controller;

import static by.clevertec.house.util.CheckerUtil.checkIllegalArgument;
import static by.clevertec.house.util.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.getSuccessResponse;

import by.clevertec.house.domain.Person;
import by.clevertec.house.dto.request.PersonPathRequestDto;
import by.clevertec.house.dto.request.PersonPutRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.model.BaseResponse;
import by.clevertec.house.service.PersonService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
@Validated
public class PersonController {

    private final PersonService personService;

    /**
     * Получает список всех персон с учетом параметров страницы и размера страницы.
     *
     * @param pageNumber Номер страницы (по умолчанию - 1).
     * @param pageSize   Размер страницы (по умолчанию - 15).
     * @return {@link ResponseEntity} с объектом {@link List} содержащим информацию о персонах.
     */
    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getAll(@RequestParam(defaultValue = "1") int pageNumber,
                                                          @RequestParam(defaultValue = "15") int pageSize) {
        List<PersonResponseDto> persons = personService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok(persons);
    }

    /**
     * Получает информацию о персоне по её уникальному идентификатору.
     *
     * @param uuid Уникальный идентификатор персоны.
     * @return {@link ResponseEntity} с объектом {@link PersonResponseDto}.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<PersonResponseDto> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(personService.getByUuid(uuid));
    }

    /**
     * Получает информацию о персоне по её имени (регистронезависимо).
     *
     * @param name Имя персоны для поиска.
     * @return {@link ResponseEntity} с объектом {@link PersonResponseDto}.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<PersonResponseDto> getByUuid(@PathVariable String name) {
        return ResponseEntity.ok(personService.getByName(name.toLowerCase()));
    }

    /**
     * Сохраняет новую персону на основе предоставленных данных.
     *
     * @param person Объект {@link PersonPutRequestDto}, содержащий данные новой персоны.
     * @return {@link ResponseEntity} с объектом {@link BaseResponse} для успешного ответа.
     */
    @PostMapping
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
    public ResponseEntity<List<HouseResponseDto>> getOwnedHouses(@PathVariable UUID uuid) {
        List<HouseResponseDto> ownedHouses = personService.getOwnedHouses(uuid);
        return ResponseEntity.ok(ownedHouses);
    }
}
