package by.clevertec.house.dto;

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
    private String passportSeries;

    @NotBlank(message = "Enter passport number")
    private String passportNumber;
}
