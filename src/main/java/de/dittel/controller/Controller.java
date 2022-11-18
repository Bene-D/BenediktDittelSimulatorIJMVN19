package de.dittel.controller;

import de.dittel.PopulationPanel;
import de.dittel.automaton.Automaton;
import de.dittel.automaton.GameOfLifeAutomaton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;

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
    private ToggleButton changeTorusToggleButton;
    @FXML
    private CheckMenuItem torusCheckMenuItem;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;

    public void initialize() {
        automaton = new GameOfLifeAutomaton(10, 10, true);
        torusCheckMenuItem.setSelected(automaton.isTorus());
        changeTorusToggleButton.setSelected(automaton.isTorus());
        populationPanel = new PopulationPanel(automaton);
        populationPanel.setOnScroll(this::zoom);
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
        changeTorusToggleButton.setSelected(automaton.isTorus());
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

    /**
     * Ändert die Größe der Population
     *
     * Es wird ein DialogPane erstellt und angezeigt, welches die neue Anzahl der Reihen und Spalten abfragt.
     * Die Eingaben werden benutzt, um die Attribute des Automaten zu aktualisieren.
     * Anschließend wird das Canvas neu gezeichnet, um die Änderungen anzuzeigen.
     */
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

    /**
     * Vergrößert mit Hilfe der zoomIn-Methode des PopulationPanels die Population
     *
     * Das neugezeichnete Canvas ist somit größer und suggeriert einen optischen Zoom.
     */
    public void zoomIn() {
        zoomOutButton.setDisable(false);
        if (populationPanel.zoomIn()) {
            zoomInButton.setDisable(true);
        }
        populationPanel.paintCanvas();
        populationPanel.center(scrollPane.getViewportBounds());
    }

    /**
     * Verkleinert mit Hilfe der zoomOut-Methode des PopulationPanels die Population
     *
     * Das neugezeichnete Canvas ist somit kleiner und suggeriert einen optischen Zoom.
     */
    public void zoomOut() {
        zoomInButton.setDisable(false);
        if (populationPanel.zoomOut()) {
            zoomOutButton.setDisable(true);
        }
        populationPanel.paintCanvas();
        populationPanel.center(scrollPane.getViewportBounds());

    }

    /**
     * Methode zum Zoomen mit dem Scrollrad er Maus
     *
     * Verwendet dabei die zoomIn- und zoomOut-Methode dieser Klasse
     *
     * @param scrollEvent wird benötigt, um die Scrollrichtung zu bestimmen
     */
    public void zoom(ScrollEvent scrollEvent) {
        double deltaY = scrollEvent.getDeltaY();

        if (scrollEvent.isControlDown()) {
            if (deltaY < 0) {
                zoomOut();
            } else {
                zoomIn();
            }
        }
    }
}
