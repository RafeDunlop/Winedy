package seng202.team3.unittests;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import seng202.team3.exceptions.IllegalWineDrinkerException;
import seng202.team3.services.SignInScreenService;

/**
 * Junit tests for the sign in screen service
 */

public class SignInScreenServiceTest {

    private SignInScreenService signInScreenService = new SignInScreenService();

    //Note that the following test has two assert statements as it checks the exception message and that the exception is thrown
    @Test
    public void testValidateRegisteringPasswordsMismatchRegexLowerBound(){
        IllegalWineDrinkerException e = assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateRegisteringPasswords("1234", "1234"));
        assertEquals("Passwords must be between 5 and 16 characters and must be alpha-numeric", e.getMessage());
    }
    @Test
    public void testValidateRegisteringPasswordsMismatchRegexUpperBound(){
        assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateRegisteringPasswords("12345678123456789", "12345678123456789"));
    }
    @Test
    public void testValidateRegisteringPasswordsMismatchRegexSpecialChar(){
        assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateRegisteringPasswords("abc def", "abc def"));
    }
    @Test
    public void testValidateRegisteringPasswordsDifferentPasswords(){
        IllegalWineDrinkerException e = assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateRegisteringPasswords("abcdef", "abcde"));
        assertEquals("Passwords do not match", e.getMessage());
    }

    @Test
    public void testValidateRegisteringPasswordsPass(){
        assertDoesNotThrow(() -> signInScreenService.validateRegisteringPasswords("12345abc", "12345abc"));
    }
}
