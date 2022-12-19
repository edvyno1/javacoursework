import courseSystem.fxControllers.SignupController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class hashPasswordTest extends SignupController {
    @Test
    public void testHashPasswordResult() throws NoSuchAlgorithmException {
        String password = "test";

        String hashedPassword = hashPassword(password);
        Boolean result = BCrypt.checkpw(password, hashedPassword);

        Assertions.assertTrue(result);
    }
}
