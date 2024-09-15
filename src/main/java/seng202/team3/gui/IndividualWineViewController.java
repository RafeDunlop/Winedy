package seng202.team3.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng202.team3.models.Wine;

public class IndividualWineViewController {
    @FXML
    private Label wineNameLabel;
    @FXML
    private Label yearLabel;

    @FXML
    private Label countryLabel;

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

    private Wine wineToDisplay;
    public IndividualWineViewController(Wine wineToDisplay) {
        this.wineToDisplay = wineToDisplay;
    }
    @FXML
    public void initialize() {
        wineNameLabel.setText(wineToDisplay.getName());
        yearLabel.setText(String.valueOf(wineToDisplay.getYear()));
        countryLabel.setText(wineToDisplay.getCountry());
        styleLabel.setText(wineToDisplay.getStyle());
        fullnessLabel.setText(wineToDisplay.getFullness());
        priceLabel.setText("$" + wineToDisplay.getPricePerBottle());
        ABVLabel.setText(wineToDisplay.getAlcoholByVolume() + "%");
        volumeLabel.setText(wineToDisplay.getVolumeInMl() + "mL");
    }
}
