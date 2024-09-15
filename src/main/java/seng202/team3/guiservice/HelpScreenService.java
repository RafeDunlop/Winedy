package seng202.team3.guiservice;

import seng202.team3.gui.HelpScreenController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A final service class for the Help Screen.
 * Used by HelpScreenController.
 * Contains a collection of static methods.
 * @author Hannah Botting (hbo51)
 */
public final class HelpScreenService {

    /**
     * Returns the content of the file at the given path as a String
     *
     * @param filePath
     * @return A String of the file content at the given path
     */
    public static String getContentFromFile(String filePath) {
        return new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(HelpScreenController.class.getResourceAsStream(filePath)), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
