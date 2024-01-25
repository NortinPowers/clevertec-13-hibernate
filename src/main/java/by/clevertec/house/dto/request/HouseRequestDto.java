package by.clevertec.house.dto.request;

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
public class HouseRequestDto {

    private UUID uuid;
    private String area;
    private String country;
    private String city;
    private String street;
    private String number;
    private List<UUID> residentUuids;
    private List<UUID> ownerUuids;
}
