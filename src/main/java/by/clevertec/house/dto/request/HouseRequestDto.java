package by.clevertec.house.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "area", example = "pennsylvania region")
    private String area;

    @Schema(description = "country", example = "usa")
    private String country;

    @Schema(description = "city", example = "philadelphia")
    private String city;

    @Schema(description = "street", example = "elfreth’s alley")
    private String street;

    @Schema(description = "number", example = "42")
    private String number;
    private List<UUID> residentUuids;
    private List<UUID> ownerUuids;
}
