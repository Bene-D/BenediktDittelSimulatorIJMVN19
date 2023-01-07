package de.dittel.view;

import de.dittel.controller.MainController;
import de.dittel.util.ReferenceHandler;
import javafx.application.Platform;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Klasse zur Darstellung der ColorPicker und RadioButtons eines Automaten in der View
 */
public class StatePanel {

    private final ReferenceHandler referenceHandler;
    private final MainController mainController;
    Random random = new Random();

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     * @param mainController Hauptcontroller der View, zum Verwalten der FXML-Elemente
     */
    public StatePanel(ReferenceHandler referenceHandler, MainController mainController) {
        this.referenceHandler = referenceHandler;
        this.mainController = mainController;
        setUpRadioButtons();
        setUpColorPickers();
    }

    /**
     * Helfermethode zum Erstellen von RadioButtons
     * <p>
     * Erzeugt für jeden Zustand eines Automaten einen RadioButton und fügt diesen der RadioButtonVBox der View hinzu.
     * Die Nummerierung der RadioButtons ist fortlaufend.
     */
    private void setUpRadioButtons() {
        mainController.getRadioButtonsVBox().getChildren().clear();
        for (int i=0; i<referenceHandler.getAutomaton().getNumberOfStates(); i++) {
            RadioButton radioButton = new RadioButton(String.valueOf(i));
            radioButton.setUserData(i);
            radioButton.setToggleGroup(mainController.getRadioButtonToggleGroup());
            mainController.getRadioButtonsVBox().getChildren().add(radioButton);
            if (i==1) {
                radioButton.setSelected(true);
            }
        }
    }

    /**
     * Helfermethode zum Erstellen von ColorPickern
     * <p>
     * Erzeugt für jeden Zustand eines Automaten einen ColorPicker und fügt diesen der ColorPickerVBox der View hinzu.
     * Die Farben werden dabei zufällig erzeugt.
     * Jeder ColorPicker bekommt eine fortlaufende ID-Nummer.
     */
    private void setUpColorPickers() {
        mainController.getColorPickerList().clear();
        mainController.getColorPickersVBox().getChildren().clear();
        for (int i=0; i<referenceHandler.getAutomaton().getNumberOfStates(); i++) {
            ColorPicker colorPicker;

            if (i == 0) {
                colorPicker = new ColorPicker(Color.WHITE);
            } else if (i == 1) {
                colorPicker = new ColorPicker(Color.BLACK);
            }
            else {
                double red = random.nextDouble();
                double green = random.nextDouble();
                double blue = random.nextDouble();
                colorPicker = new ColorPicker(Color.color(red, green, blue));
            }

            colorPicker.setId(String.valueOf(i));
            mainController.getColorPickersVBox().getChildren().add(colorPicker);
            mainController.getColorPickerList().add(colorPicker);
        }
    }

    /**
     * Aktualisiert das StatePanel und dessen ColorPicker und RadioButtons
     */
    public void update() {
        if (Platform.isFxApplicationThread()) {
            setUpColorPickers();
            setUpRadioButtons();
        } else {
            Platform.runLater(() -> {
                setUpColorPickers();
                setUpRadioButtons();
            });
        }
    }
}
