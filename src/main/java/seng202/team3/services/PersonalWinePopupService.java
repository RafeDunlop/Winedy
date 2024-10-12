package seng202.team3.services;

import javafx.scene.control.TextFormatter;
import seng202.team3.models.Wine;

import java.time.LocalDate;

/**
 * Service class for the add personal wine popup
 * @author Krishna Sridhar (nsr36)
 */
public class PersonalWinePopupService {

    final float MAXABV = 25f;
    final float MAXVOLUME = 1000f;
    final int MAXYEARSINPAST = 200;
    final String DEFAULTCOLOUR = "Red";

    private Wine personalWine;

    /**
     * Constructor for PersonalWinePopupService
     * @param personalWine wine object created using the user's inputs
     */
    public PersonalWinePopupService(Wine personalWine) {
        this.personalWine = personalWine;
    }

    /**
     * Check if the wine has a name
     * @return true if wine has a name
     */
    public boolean validatePersonalWineName() {
        return !this.personalWine.getName().isEmpty();
    }

    /**
     * Default wine colour to red if colour is not set
     * Default style and fullness to empty strings if not set
     */
    public void validatePersonalWineColourStyleFullness() {
        if (this.personalWine.getColour() == null) {
            personalWine.setColour(DEFAULTCOLOUR);
        }
        if (this.personalWine.getStyle() == null) {
            personalWine.setStyle("");
        }
        if (this.personalWine.getFullness() == null) {
            personalWine.setFullness("");
        }
    }

    /**
     * Check if the ABV is set to a reasonable value
     * @return true if ABV is a reasonable value
     */
    public boolean validatePersonalWineABV() {
        return 0 <= this.personalWine.getAlcoholByVolume() && this.personalWine.getAlcoholByVolume() <= MAXABV;
    }

    /**
     * Check if the volume is set to a reasonable value
     * @return true if volume is a reasonable value
     */
    public boolean validatePersonalWineVolume() {
        return 0 <= this.personalWine.getVolumeInMl() && this.personalWine.getVolumeInMl() <= MAXVOLUME;
    }

    /**
     * Check if the year is set to a reasonable value
     * @return true if year is a reasonable value
     */
    public boolean validatePersonalWineYear() {
        return this.personalWine.getYear() == 0 ||
                (LocalDate.now().getYear()-MAXYEARSINPAST <= this.personalWine.getYear() && this.personalWine.getYear() <= LocalDate.now().getYear());
    }

    /**
     * Return the final personal wine after validation
     * @return the personal wine
     */
    public Wine getPersonalWine() {
        return this.personalWine;
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
