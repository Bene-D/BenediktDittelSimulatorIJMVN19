package de.dittel.view;

import de.dittel.controller.MainController;
import de.dittel.model.Automaton;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Klasse zur Darstellung der ColorPicker und RadioButtons eines Automaten in der View
 */
public class StatePanel {

    private final Automaton automaton;
    private final MainController mainController;
    Random random = new Random();

    /**
     * Konstruktor
     *
     * @param automaton Automat, der abgebildet werden soll
     * @param mainController Hauptcontroller der View, zum Verwalten der FXML-Elemente
     */
    public StatePanel(Automaton automaton, MainController mainController) {
        this.automaton = automaton;
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
        for (int i=2; i<automaton.getNumberOfStates(); i++) {
            RadioButton radioButton = new RadioButton(String.valueOf(i));
            radioButton.setUserData(i);
            radioButton.setToggleGroup(mainController.getRadioButtonToggleGroup());
            mainController.getRadioButtonsVBox().getChildren().add(radioButton);
        }
    }

    /**
     * Helfermethode zum Erstellen von ColorPickern
     * <p>
     * Erzeugt für jeden Zustand eines Automaten einen ColorPicker und fügt diesen der ColorPickerVBox der View hinzu.
     * Die Farben werden dabei zufällig erzeugt.
     * Jeder ColorPicker bekommt eine fortlaufende id-Nummer.
     */
    private void setUpColorPickers() {
        for (int i=2; i<automaton.getNumberOfStates(); i++) {
            double red = random.nextDouble();
            double green = random.nextDouble();
            double blue = random.nextDouble();
            ColorPicker colorPicker = new ColorPicker(Color.color(red, green, blue));
            colorPicker.setId(String.valueOf(i));
            mainController.getColorPickersVBox().getChildren().add(colorPicker);
            mainController.getColorPickerList().add(colorPicker);
        }
    }
}
