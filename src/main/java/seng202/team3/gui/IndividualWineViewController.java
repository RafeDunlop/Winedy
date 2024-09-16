package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng202.team3.models.Wine;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IndividualWineViewController {
    @FXML
    private Label wineNameLabel;

    @FXML
    private Label wineCountryLabel;
    @FXML
    private Label countryLabel;

    @FXML
    private Label wineStyleLabel;

    @FXML
    private Label styleLabel;

    @FXML
    private Label fullnessLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label ABVLabel;

    @FXML
    private Label volumeLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label wineDescriptionLabel;

    @FXML
    private Label awardsLabel;

    @FXML
    private Label wineAwardsLabel;
    private Wine wineToDisplay;
    public IndividualWineViewController(Wine wineToDisplay) {
        this.wineToDisplay = wineToDisplay;
    }
    @FXML
    public void initialize() {
        wineNameLabel.setText(wineToDisplay.getName());
        fullnessLabel.setText(wineToDisplay.getFullness());
        priceLabel.setText("$" + wineToDisplay.getPricePerBottle());
        ABVLabel.setText(wineToDisplay.getAlcoholByVolume() + "%");
        volumeLabel.setText(wineToDisplay.getVolumeInMl() + "mL");
        if (!wineToDisplay.getStyle().isEmpty()) {
            wineStyleLabel.setVisible(true);
            wineStyleLabel.setText(wineToDisplay.getStyle());
            styleLabel.setVisible(true);
        }
        if (!wineToDisplay.getCountry().isEmpty()) {
            wineCountryLabel.setVisible(true);
            wineCountryLabel.setText(wineToDisplay.getCountry());
            countryLabel.setVisible(true);
        }
        if (!wineToDisplay.getLongDescription().isEmpty()) {
            descriptionLabel.setVisible(true);
            wineDescriptionLabel.setText(wineToDisplay.getLongDescription());
        } else {
            wineDescriptionLabel.setText("This wine does not have a description");
        }
        if (!(Arrays.stream(wineToDisplay.getAwards()).allMatch(award -> award == null || award == ""))) {
            awardsLabel.setVisible(true);
            System.out.println(wineToDisplay.getAwards()[0]);
            String awards = Arrays.asList(wineToDisplay.getAwards())
                                                    .stream()
                                                    .filter(award -> award != null)
                                                    .collect(Collectors.joining("\n"));
            wineAwardsLabel.setText(awards);
        } else {
            wineAwardsLabel.setText("This wine does not have any awards");
        }

    }
}
