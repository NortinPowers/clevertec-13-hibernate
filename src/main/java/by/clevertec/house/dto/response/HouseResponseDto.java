package by.clevertec.house.dto.response;

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
public class HouseResponseDto {

    @Schema(description = "identifier", example = "64a6c8d9-975a-47c2-8871-69cd7192b278")
    private UUID uuid;

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

    @Schema(description = "created", example = "2024-01-22T10:02:48.17907")
    private String created;
}
