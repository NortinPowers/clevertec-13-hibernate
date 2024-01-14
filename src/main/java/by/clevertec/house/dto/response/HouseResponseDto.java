package by.clevertec.house.dto.response;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class HouseResponseDto {

    private UUID uuid;
    private String area;
    private String country;
    private String city;
    private String street;
    private String number;
    private String created;
}
