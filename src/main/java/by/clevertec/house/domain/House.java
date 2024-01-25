package by.clevertec.house.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@FieldNameConstants
@Table(name = "houses")
public class House {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    private String area;
    private String country;
    private String city;
    private String street;
    private String number;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "house",
            fetch = FetchType.LAZY)
    private Set<Person> residents;

    @ManyToMany
    @BatchSize(size = 10)
    @EqualsAndHashCode.Exclude
    @JoinTable(
            name = "house_owner",
            joinColumns = @JoinColumn(name = "house_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id"))
    private Set<Person> owners;
}
