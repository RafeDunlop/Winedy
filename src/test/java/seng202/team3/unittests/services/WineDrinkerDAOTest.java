package seng202.team3.unittests.services;

import org.junit.jupiter.api.Test;
import seng202.team3.models.WineDrinker;
import seng202.team3.services.WineDrinkerDAO;

public class WineDrinkerDAOTest {
    WineDrinkerDAO wineDrinkerDAO = new WineDrinkerDAO("jdbc:sqlite:./src/test/resources/test_database.db");
    private String username = "Username";
    private String password = "Password";

    private WineDrinker wineDrinker = new WineDrinker(username, password, null, null, null,null,0);
    @Test
    public void testGetNonExistingUser() {

    }
}
