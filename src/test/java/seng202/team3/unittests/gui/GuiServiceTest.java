package seng202.team3.unittests.gui;

import org.junit.jupiter.api.Test;
import seng202.team3.gui.GuiService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GuiServiceTest {
    @Test
    public void getContentFromFileTest() {
        String testText = GuiService.getContentFromFile("/text/test.txt");
        String expectedText = "This is a Junit test.\n\n\nHello World!\n";
        assertEquals(testText, expectedText);
    }

    @Test
    public void getContentFromEmptyFileTest() {
        String testText = GuiService.getContentFromFile("/text/empty.txt");
        String expectedText = "";
        assertEquals(testText, expectedText);
    }

    @Test
    public void getContentFromNonExistentFileTest() {
        assertThrows(NullPointerException.class, () -> GuiService.getContentFromFile("/text/idonotexist.txt"));
    }
}

