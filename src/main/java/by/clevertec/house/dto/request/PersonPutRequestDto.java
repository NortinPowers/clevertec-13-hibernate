package by.clevertec.house.dto.request;

import by.clevertec.house.domain.Gender;
import by.clevertec.house.dto.PassportDto;
import by.clevertec.house.validator.ValidUuid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class PersonPutRequestDto {

    @Schema(description = "identifier", example = "c370a441-208d-46b4-902b-19de3e6d795b")
    private UUID uuid;

    @NotBlank(message = "Enter name")
    @Schema(description = "name", example = "john")
    private String name;

    @NotBlank(message = "Enter surname")
    @Schema(description = "surname", example = "gold")
    private String surname;

    @Enumerated
    @NotNull(message = "Enter gender")
    @Schema(description = "gender", example = "MALE")
    private Gender gender;

    @Schema(description = "passport")
    private PassportDto passport;

    @ValidUuid
    @Schema(description = "house identifier", example = "fa730904-9d24-42e7-ab5a-55029b1bfd69")
    private UUID houseUuid;

    @Schema(description = "ownedHouseUuids")
    private List<UUID> ownedHouseUuids;
}
