package by.clevertec.house.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@FieldNameConstants
@EqualsAndHashCode
public class Passport {

    @Column(name = "passport_series", nullable = false)
    private String passportSeries;

    @Column(name = "passport_number", nullable = false)
    private String passportNumber;
}
