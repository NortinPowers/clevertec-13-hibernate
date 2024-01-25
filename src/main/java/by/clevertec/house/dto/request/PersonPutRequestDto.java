package by.clevertec.house.dto.request;

import by.clevertec.house.domain.Gender;
import by.clevertec.house.dto.PassportDto;
import by.clevertec.house.validator.ValidUuid;
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

    private UUID uuid;

    @NotBlank(message = "Enter name")
    private String name;

    @NotBlank(message = "Enter surname")
    private String surname;

    @NotNull(message = "Enter gender")
    @Enumerated
    private Gender gender;
    private PassportDto passport;

    @ValidUuid
    private UUID houseUuid;
    private List<UUID> ownedHouseUuids;
}
