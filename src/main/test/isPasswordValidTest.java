import courseSystem.fxControllers.SignupController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class isPasswordValidTest extends SignupController {
    @Test
    public void testIsPasswordValidIncorrect(){
        String password = "1234567";
        Assertions.assertFalse(isPasswordValid(password));
    }
    @Test
    public void testIsPasswordValidCorrect(){
        String password = "12345678";
        Assertions.assertTrue(isPasswordValid(password));
    }
}