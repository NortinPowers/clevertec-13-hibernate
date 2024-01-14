package by.clevertec.house.controller;

import static by.clevertec.house.util.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.house.util.ResponseUtils.getSuccessResponse;

import by.clevertec.house.domain.House;
import by.clevertec.house.dto.request.HouseRequestDto;
import by.clevertec.house.dto.response.HouseResponseDto;
import by.clevertec.house.dto.response.PersonResponseDto;
import by.clevertec.house.model.BaseResponse;
import by.clevertec.house.service.HouseService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    /**
     * Получает список всех домов с учетом параметров страницы и размера страницы.
     *
     * @param pageNumber Номер страницы (по умолчанию - 1).
     * @param pageSize   Размер страницы (по умолчанию - 15).
     * @return {@link ResponseEntity} с объектом {@link List} содержащим информацию о домах.
     */
    @GetMapping
    public ResponseEntity<List<HouseResponseDto>> getAll(@RequestParam(defaultValue = "1") int pageNumber,
                                                         @RequestParam(defaultValue = "15") int pageSize) {
        List<HouseResponseDto> houses = houseService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok(houses);
    }

    /**
     * Получает информацию о доме по его уникальному идентификатору.
     *
     * @param uuid Уникальный идентификатор дома.
     * @return {@link ResponseEntity} с объектом {@link HouseResponseDto}.
     */
    @GetMapping("/{uuid}")
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
    public ResponseEntity<List<PersonResponseDto>> getResidents(@PathVariable UUID uuid) {
        List<PersonResponseDto> residents = houseService.getResidents(uuid);
        return ResponseEntity.ok(residents);
    }
}
