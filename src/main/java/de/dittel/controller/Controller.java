package de.dittel.controller;

import de.dittel.automaton.Automaton;
import de.dittel.automaton.GameOfLifeAutomaton;
import de.dittel.PopulationPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller-Klasse zur Steuerung der GUI
 */
public class Controller {

    private Automaton automaton;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private PopulationPanel populationPanel;
    @FXML
    private ToggleButton changeTorusButton;
    @FXML
    private CheckMenuItem torusCheckMenuItem;

    public void initialize() {
        automaton = new GameOfLifeAutomaton(15, 15, true);
        torusCheckMenuItem.setSelected(automaton.isTorus());
        changeTorusButton.setSelected(automaton.isTorus());
        populationPanel = new PopulationPanel(automaton);
        scrollPane.setPannable(true);
        scrollPane.setContent(populationPanel);
        scrollPane.viewportBoundsProperty()
                .addListener((observable, oldValue, newValue) -> populationPanel.center(newValue));
    }

    /**
     * Generiert EINE neue Population und zeichnet diese in das Canvas
     *
     * @throws Throwable möglicherweise wirft die Methode eine Exception
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
        changeTorusButton.setSelected(automaton.isTorus());
        torusCheckMenuItem.setSelected(automaton.isTorus());
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

    public void changePopulationSize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialog.fxml"));
            DialogPane dialogPane = loader.load();
            DialogController dialogController = loader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialogController.initialize(automaton);
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Größe der Population ändern");
            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                    automaton.changeSize(Integer.parseInt(dialogController.getRowTextField().getText()),
                            Integer.parseInt(dialogController.getColumnTextField().getText()));
                populationPanel.paintCanvas();
                populationPanel.center(scrollPane.getViewportBounds());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
