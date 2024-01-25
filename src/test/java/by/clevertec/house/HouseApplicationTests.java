package by.clevertec.house;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.clevertec.house.controller.HouseController;
import by.clevertec.house.controller.HouseHistoryController;
import by.clevertec.house.controller.PersonController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HouseApplicationTests {

    @Autowired
    private HouseController houseController;
    @Autowired
    private HouseHistoryController houseHistoryController;
    @Autowired
    private PersonController personController;

    @Test
    void houseControllerMustBeNotNull() {
        assertThat(houseController).isNotNull();
    }

    @Test
    void houseHistoryControllerMustBeNotNull() {
        assertThat(houseHistoryController).isNotNull();
    }

    @Test
    void personControllerMustBeNotNull() {
        assertThat(personController).isNotNull();
    }
}
