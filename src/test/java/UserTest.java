import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    @Test
    void shouldValidatePasswordCorrectly() {
        User u = new User("amin", "password123");
        assertTrue(u.checkPassword("password123"));
        assertFalse(u.checkPassword("wrongpass"));
    }
}
