package by.clevertec.house.dto.request;

import by.clevertec.house.domain.Gender;
import by.clevertec.house.dto.PassportDto;
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
public class PersonPathRequestDto {

    private UUID uuid;
    private String name;
    private String surname;
    private Gender gender;
    private PassportDto passport;
    private UUID houseUuid;
    private List<UUID> ownedHouseUuids;
}
