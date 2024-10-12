package seng202.team3.services;

import javafx.scene.control.TextFormatter;
import seng202.team3.models.Wine;

import java.time.LocalDate;

/**
 * Service class for the add personal wine popup
 * @author Krishna Sridhar (nsr36)
 */
public class PersonalWinePopupService {

    final int MAXABV = 25;
    final int MAXVOLUME = 1000;
    final int MAXYEARSINPAST = 200;

    Wine personalWine;

    public PersonalWinePopupService(Wine personalWine) {
        this.personalWine = personalWine;
    }

    public boolean validatePersonalWineName() {
        return !this.personalWine.getName().isEmpty();
    }

    public void validatePersonalWineColour() {
        if (this.personalWine.getColour() != null) {
            personalWine.setColour("Red");
        }
    }

    public boolean validatePersonalWineABV() {
        return 0 <= this.personalWine.getAlcoholByVolume() && this.personalWine.getAlcoholByVolume() <= MAXABV;
    }

    public boolean validatePersonalWineVolume() {
        return 0 <= this.personalWine.getVolumeInMl() && this.personalWine.getVolumeInMl() <= MAXVOLUME;
    }

    public boolean validatePersonalWineYear() {
        return LocalDate.now().getYear()-MAXYEARSINPAST <= this.personalWine.getYear() && this.personalWine.getYear() <= LocalDate.now().getYear();
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
     * This text formatter makes sure the input is a float
     * Used for TextFields representing float attributes
     *
     * @return a String text formatter for validating the input
     */
    public static TextFormatter<String> getFloatFormatter() {
        return getFormatter("\\d*(\\.)?\\d*");
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
            if (!newText.matches(regex) && !newText.isEmpty()) return null;
            return change;
        });
    }
}
