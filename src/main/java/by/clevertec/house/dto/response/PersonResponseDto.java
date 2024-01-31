package by.clevertec.house.dto.response;

import by.clevertec.house.domain.Gender;
import by.clevertec.house.dto.PassportDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class PersonResponseDto {

    @Schema(description = "identifier", example = "c370a441-208d-46b4-902b-19de3e6d795b")
    private UUID uuid;

    @Schema(description = "name", example = "john")
    private String name;

    @Schema(description = "surname", example = "gold")
    private String surname;

    @Schema(description = "gender", example = "MALE")
    private Gender gender;

    @Schema(description = "passport")
    private PassportDto passport;

    @Schema(description = "created", example = "2024-01-22T10:02:48.17907")
    private String created;

    @Schema(description = "updated", example = "2024-01-22T10:02:48.17907")
    private String updated;

    @Schema(description = "house identifier", example = "fa730904-9d24-42e7-ab5a-55029b1bfd69")
    private UUID houseUuid;
}
