package de.dittel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        automaton = new KruemelmonsterAutomaton(100, 100, false);

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
}
