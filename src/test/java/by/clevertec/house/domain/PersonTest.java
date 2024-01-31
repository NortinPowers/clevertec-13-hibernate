package by.clevertec.house.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void preUpdateShouldSetDateToCreatedEntity() {
        Person entity = new Person();

        entity.preUpdate();

        LocalDateTime expectedUpdateDate = LocalDateTime.now();

        assertEquals(expectedUpdateDate, entity.getUpdateDate());
    }
}
