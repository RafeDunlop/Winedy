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
    //Tests for registering password
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

    //testing validate login details
    @Test
    public void testValidateLoginDetailsMismatchRegexUsernameLowerBound(){
        IllegalWineDrinkerException e = assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateLoginDetails("1234", "passes"));
        assertEquals("Username must be between 5 and 16 characters and must be alpha-numeric", e.getMessage());
    }
    @Test
    public void testValidateLoginDetailsMismatchRegexUsernameUpperBound(){
        assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateLoginDetails("12345678123456789", "passes"));
    }
    @Test
    public void testValidateLoginDetailsMismatchRegexUsernameSpecialChar(){
        assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateLoginDetails("abc def", "passes"));
    }
    @Test
    public void testValidateLoginDetailsMismatchRegexPasswordLowerBound(){
        IllegalWineDrinkerException e = assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateLoginDetails("passes", "fail"));
        assertEquals("Password must be between 5 and 16 characters and must be alpha-numeric", e.getMessage());
    }
    @Test
    public void testValidateLoginDetailsMismatchRegexPasswordUpperBound(){
        assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateLoginDetails("passes", "123abc123abc123ab"));
    }
    @Test
    public void testValidateLoginDetailsMismatchRegexPasswordSpecialChar(){
        assertThrows(IllegalWineDrinkerException.class, () -> signInScreenService.validateLoginDetails("passes", "pass word"));
    }

    @Test
    public void testValidateLoginDetailsPass(){
        assertDoesNotThrow(() -> signInScreenService.validateLoginDetails("12345abc", "12345abc"));
    }
}
