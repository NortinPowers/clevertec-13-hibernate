package by.clevertec.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class PassportDto {

    @NotBlank(message = "Enter passport series")
    @Schema(description = "passportSeries", example = "mp")
    private String passportSeries;

    @NotBlank(message = "Enter passport number")
    @Schema(description = "passportNumber", example = "15df65sad")
    private String passportNumber;
}
