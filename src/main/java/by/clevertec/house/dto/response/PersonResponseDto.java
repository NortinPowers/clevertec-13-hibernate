package by.clevertec.house.dto.response;

import by.clevertec.house.domain.Gender;
import by.clevertec.house.dto.PassportDto;
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

    private UUID uuid;
    private String name;
    private String surname;
    private Gender gender;
    private PassportDto passport;
    private String created;
    private String updated;
    private UUID houseUuid;
}
