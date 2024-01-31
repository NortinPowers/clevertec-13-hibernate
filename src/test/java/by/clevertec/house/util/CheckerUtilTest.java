package by.clevertec.house.util;

import static by.clevertec.house.util.CheckerUtil.checkIllegalArgument;
import static by.clevertec.house.util.CheckerUtil.checkList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import by.clevertec.house.exception.CustomNoContentException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CheckerUtilTest {

    @Test
    public void checkIllegalArgumentShouldThrowIllegalArgumentException_whenPassedNull() {
        String message = "Object cannot be null";
        try {
            checkIllegalArgument(null, message);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void checkListShouldThrowCustomNoContentException_whenPassedListIsEmpty() {
        List<String> list = new ArrayList<>();
        try {
            checkList(list, String.class);
            fail("Expected CustomNoContentException to be thrown");
        } catch (CustomNoContentException exception) {
            assertEquals("There are no " + String.class.getSimpleName().toLowerCase() + " objects", exception.getMessage());
        }
    }
}
