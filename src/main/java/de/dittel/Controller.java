package de.dittel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller-Klasse zur Steuerung der GUI
 */
public class Controller implements Initializable {

    private Automaton automaton;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private PopulationPanel populationPanel;
    @FXML
    private Button changeTorusButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        automaton = new GameOfLifeAutomaton(10, 10, true);
        populationPanel = new PopulationPanel(automaton);
        scrollPane.setContent(populationPanel);
        scrollPane.viewportBoundsProperty()
                .addListener((observable, oldValue, newValue) -> populationPanel.center(newValue));
    }

    /**
     * Generiert EINE neue Population und zeichnet diese in das Canvas
     *
     * @throws Throwable moeglicherweise wirft die Methode eine Exception
     */
    public void singleStep() throws Throwable {
        automaton.nextGeneration();
        populationPanel.paintCanvas();
    }

    /**
     * Ändert die Torus-Einstellung des Automaten
     */
    public void changeTorus() {
        automaton.setTorus(!automaton.isTorus());
        if (automaton.isTorus()) {
        } else {
        }
    }

    /**
     * Erzeugt eine Random-Population des Automaten und zeichnet diese in das Canvas
     */
    public void randomPopulation() {
        automaton.randomPopulation();
        populationPanel.paintCanvas();
    }

    /**
     * Setzt den Zustand aller Zellen des Automaten auf den Wert 0
     */
    public void resetPopulation() {
        automaton.clearPopulation();
        populationPanel.paintCanvas();
    }

    public void changePopulationSize(ActionEvent actionEvent) {
    }
}
