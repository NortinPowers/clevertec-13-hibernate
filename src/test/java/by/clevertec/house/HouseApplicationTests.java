package by.clevertec.house;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.clevertec.house.controller.HouseController;
import by.clevertec.house.controller.HouseHistoryController;
import by.clevertec.house.controller.PersonController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class HouseApplicationTests extends AbstractTest {

    private final HouseController houseController;
    private final HouseHistoryController houseHistoryController;
    private final PersonController personController;

    @Test
    void houseControllerMustBeNotNull_whenContextLoaded() {
        assertThat(houseController).isNotNull();
    }

    @Test
    void houseHistoryControllerMustBeNotNull_whenContextLoaded() {
        assertThat(houseHistoryController).isNotNull();
    }

    @Test
    void personControllerMustBeNotNull_whenContextLoaded() {
        assertThat(personController).isNotNull();
    }
}
