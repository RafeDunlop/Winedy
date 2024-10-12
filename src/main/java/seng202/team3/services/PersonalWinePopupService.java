package seng202.team3.services;

import javafx.scene.control.TextFormatter;
import seng202.team3.models.Wine;

import java.time.LocalDate;

/**
 * Service class for the add personal wine popup
 * @author Krishna Sridhar (nsr36)
 */
public class PersonalWinePopupService {

    final int MAXYEARSINPAST = 200;

    Wine personalWine;

    public PersonalWinePopupService(Wine personalWine) {
        this.personalWine = personalWine;
    }

    public boolean validatePersonalWine() {
        return validatePersonalWineName() && validatePersonalWineColour();
    }

    public boolean validatePersonalWineName() {
        return this.personalWine.getName() != null;
    }

    public boolean validatePersonalWineColour() {
        if (this.personalWine.getColour() != null) {
            personalWine.setColour("Red");
        }
        return true;
    }

    public boolean validatePersonalWineYear() {
        System.out.println(this.personalWine.getYear());
        if (this.personalWine.getYear() < LocalDate.now().getYear()-MAXYEARSINPAST || this.personalWine.getYear() > LocalDate.now().getYear()) {
            return false;
        }
        return true;
    }

    /**
     * This text formatter prevents the input of the text fields from being non-alphabetic
     * Used for TextFields representing string attributes
     *
     * @return a String text formatter for validating the input
     */
    public static TextFormatter<String> getAlphabeticalFormatter() {
        return getFormatter("[a-zA-Z]*");
    }

    /**
     * This text formatter makes sure the input is an integer
     * Used for TextFields representing integer attributes
     *
     * @return a String text formatter for validating the input
     */
    public static TextFormatter<String> getIntegerFormatter() {
        return getFormatter("\\d*");
    }

    /**
     * This text formatter makes sure the input is either an integer or double
     * Used for TextFields representing double attributes
     *
     * @return a String text formatter for validating the input
     */
    public static TextFormatter<String> getDoubleFormatter() {
        return getFormatter("\\d*\\.?\\d+");
    }

    /**
     * Formatter which uses a given regex
     *
     * @param regex regex expression to perform formatting
     * @return a String text formatter for validating the input
     */
    public static TextFormatter<String> getFormatter(String regex) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (!newText.matches(regex)) return null;
            return change;
        });
    }
}
