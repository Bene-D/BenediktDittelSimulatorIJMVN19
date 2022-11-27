package de.dittel.controller;

import de.dittel.model.Automaton;
import de.dittel.util.Pair;
import de.dittel.view.PopulationPanel;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Controller-Klasse für das PopulationPanel
 */
public class PopulationPanelController {

    private final PopulationPanel populationPanel;
    private final MainController mainController;
    private final Automaton automaton;
    private Pair<Integer> rowColStart;

    /**
     * Konstruktor
     *
     * @param populationPanel Model des Controllers
     * @param mainController Hauptcontroller der View, zum Verwalten der FXML-Elemente
     */
    public PopulationPanelController(PopulationPanel populationPanel, MainController mainController) {
        this.populationPanel = populationPanel;
        this.mainController = mainController;
        this.automaton = mainController.getAutomaton();
        mainController.setPopulationPanel(populationPanel);
        populationPanel.setOnScroll(this::zoom);
        populationPanel.getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, this::canvasPressed);
        populationPanel.getCanvas().addEventHandler(MouseEvent.MOUSE_DRAGGED, this::canvasMouseDragged);
        populationPanel.addEventHandler(ScrollEvent.SCROLL, this::zoom);
        mainController.getZoomOutButton().setOnAction(this::zoomOut);
        mainController.getZoomInButton().setOnAction(this::zoomIn);
        mainController.getPopulationScrollPane().setContent(populationPanel);
        mainController.getPopulationScrollPane().viewportBoundsProperty()
                .addListener((observable, oldValue, newValue) -> populationPanel.center(newValue));
    }

    /**
     * Vergrößert mit Hilfe der zoomIn-Methode des PopulationPanels die Population
     * <p>
     * Das neugezeichnete Canvas ist somit größer und suggeriert einen optischen Zoom.
     */
    public void zoomIn(ActionEvent actionEvent) {
        mainController.getZoomOutButton().setDisable(false);
        if (populationPanel.zoomIn()) {
            mainController.getZoomInButton().setDisable(true);
        }
        automaton.notifyObserver();
    }

    /**
     * Verkleinert mit Hilfe der zoomOut-Methode des PopulationPanels die Population
     * <p>
     * Das neugezeichnete Canvas ist somit kleiner und suggeriert einen optischen Zoom.
     */
    public void zoomOut(ActionEvent actionEvent) {
        mainController.getZoomInButton().setDisable(false);
        if (populationPanel.zoomOut()) {
            mainController.getZoomOutButton().setDisable(true);
        }
        automaton.notifyObserver();
    }

    /**
     * Methode zum Zoomen mit dem Scrollrad er Maus
     * <p>
     * Verwendet dabei die zoomIn- und zoomOut-Methode dieser Klasse
     *
     * @param scrollEvent wird benötigt, um die Scrollrichtung zu bestimmen
     */
    public void zoom(ScrollEvent scrollEvent) {
        double deltaY = scrollEvent.getDeltaY();

        if (scrollEvent.isControlDown()) {
            if (deltaY < 0) {
                zoomOut(new ActionEvent());
            } else {
                zoomIn(new ActionEvent());
            }
        }
    }

    /**
     * Ändert den Zustand/Farbe der Zelle, auf die geklickt wurde
     * <p>
     * Verwendet dabei den aktuell ausgewählten RadioButton, um den neuen Zustand/Farbe zu bestimmen.
     * Speichert zusätzlich die Koordinaten der Zelle als Anfangspunkt für einen möglichen MouseDrag.
     *
     * @param mouseEvent zum Bestimmen der X- und Y-Koordinaten
     */
    public void canvasPressed(MouseEvent mouseEvent) {
        populationPanel.getRowAndCol(mouseEvent.getX(), mouseEvent.getY()).ifPresent(rowCol -> {
            rowColStart = rowCol;
            int state = (int) mainController.getRadioButtonToggleGroup().getSelectedToggle().getUserData();
            automaton.setState(rowColStart.value1(), rowColStart.value2(), state);
        });
    }

    /**
     * Bestimmt die Endkoordinaten eines Bereichs beim Loslassen der Maus auf dem Canvas
     *
     * @param mouseEvent zum Bestimmen der X- und Y-Koordinaten
     */
    public void canvasMouseDragged(MouseEvent mouseEvent) {
        populationPanel.getRowAndCol(mouseEvent.getX(), mouseEvent.getY()).ifPresent(rowColEnd -> {
            int fromRow = rowColStart.value1();
            int fromColumn = rowColStart.value2();

            int toRow = rowColEnd.value1();
            int toColumn = rowColEnd.value2();

            if (toRow >= automaton.getNumberOfRows()) {
                toRow = automaton.getNumberOfRows()-1;
            }
            if (toColumn >= automaton.getNumberOfColumns()) {
                toColumn = automaton.getNumberOfColumns()-1;
            }

            int lowRowIndex = Math.min(fromRow, toRow);
            int highRowIndex = Math.max(fromRow, toRow);
            int lowColumnIndex = Math.min(fromColumn, toColumn);
            int highColumnIndex = Math.max(fromColumn, toColumn);
            int state = (int) mainController.getRadioButtonToggleGroup().getSelectedToggle().getUserData();
            automaton.setState(lowRowIndex, lowColumnIndex, highRowIndex, highColumnIndex, state);
            });
        }
}
