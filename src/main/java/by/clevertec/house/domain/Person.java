package by.clevertec.house.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@FieldNameConstants
@Table(name = "persons", uniqueConstraints = @UniqueConstraint(columnNames = {"passport_series", "passport_number"}))
public class Person {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID uuid;

    private String name;
    private String surname;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Embedded
    private Passport passport;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    //    @ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @EqualsAndHashCode.Exclude
    @ManyToMany(
            mappedBy = "owners",
            fetch = FetchType.LAZY)
    private List<House> ownedHouses;

    @PrePersist
    public void prePersist() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
            this.updateDate = createDate;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
