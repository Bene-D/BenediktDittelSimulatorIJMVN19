package de.dittel.controller;

import de.dittel.util.Pair;
import de.dittel.util.ReferenceHandler;
import de.dittel.view.PopulationPanelContextMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Controller-Klasse für das PopulationPanel
 */
public class PopulationPanelController {

    private final MainController mainController;
    private final ReferenceHandler referenceHandler;
    private Pair<Integer> rowColStart;

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen (Model)
     * @param mainController Hauptcontroller der View, zum Verwalten der FXML-Elemente
     */
    public PopulationPanelController(ReferenceHandler referenceHandler, MainController mainController) {
        this.mainController = mainController;
        this.referenceHandler = referenceHandler;
        mainController.setPopulationPanel(referenceHandler.getPopulationPanel());
        referenceHandler.getPopulationPanel().setOnScroll(this::zoom);
        referenceHandler.getPopulationPanel().getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, this::canvasPressed);
        referenceHandler.getPopulationPanel().getCanvas().addEventHandler(MouseEvent.MOUSE_DRAGGED, this::canvasMouseDragged);
        referenceHandler.getPopulationPanel().addEventHandler(ScrollEvent.SCROLL, this::zoom);
        referenceHandler.getPopulationPanel().getCanvas().setOnContextMenuRequested(this::contextMenu);
        mainController.getZoomOutButton().setOnAction(this::zoomOut);
        mainController.getZoomInButton().setOnAction(this::zoomIn);
        mainController.getPopulationScrollPane().setContent(referenceHandler.getPopulationPanel());
        mainController.getPopulationScrollPane().viewportBoundsProperty()
                .addListener((observable, oldValue, newValue) -> referenceHandler.getPopulationPanel().center(newValue));
    }

    /**
     * Öffnet ein ContextMenu für das Canvas
     * <p>
     * Es werden über Reflection ausführbare Methoden für den Automaten zum ContextMenu hinzugefügt
     *
     * @param event zum Bestimmen der X- und Y-Koordinaten
     */
    @FXML
    private void contextMenu(ContextMenuEvent event) {
        PopulationPanelContextMenu contextMenu = new PopulationPanelContextMenu(referenceHandler);
        List<Method> contextMenuItems = contextMenu.getValidMethods();
        referenceHandler.getPopulationPanel().getRowAndCol(event.getX(), event.getY()).ifPresent(rowCol -> rowColStart = rowCol);

        for (int i = 0; i < contextMenuItems.size(); i++) {
            Method method = contextMenuItems.get(i);
            contextMenu.getItems().get(i).setOnAction(actionEvent -> {
                try {
                    method.invoke(referenceHandler.getAutomaton(), rowColStart.value1(), rowColStart.value2());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Beim Ausführen der Methode ist ein Fehler aufgetreten!", ButtonType.OK);
                    alert.showAndWait();
                }
            });
        }
        contextMenu.show(referenceHandler.getPopulationPanel().getScene().getWindow(), event.getScreenX(), event.getScreenY());
    }

    /**
     * Vergrößert mit Hilfe der zoomIn-Methode des PopulationPanels die Population
     * <p>
     * Das neugezeichnete Canvas ist somit größer und suggeriert einen optischen Zoom.
     */
    @FXML
    public void zoomIn(ActionEvent actionEvent) {
        mainController.getZoomOutButton().setDisable(false);
        if (referenceHandler.getPopulationPanel().zoomIn()) {
            mainController.getZoomInButton().setDisable(true);
        }
        referenceHandler.getPopulationPanel().update();

    }

    /**
     * Verkleinert mit Hilfe der zoomOut-Methode des PopulationPanels die Population
     * <p>
     * Das neugezeichnete Canvas ist somit kleiner und suggeriert einen optischen Zoom.
     */
    @FXML
    public void zoomOut(ActionEvent actionEvent) {
        mainController.getZoomInButton().setDisable(false);
        if (referenceHandler.getPopulationPanel().zoomOut()) {
            mainController.getZoomOutButton().setDisable(true);
        }
        referenceHandler.getPopulationPanel().update();
    }

    /**
     * Methode zum Zoomen mit dem Scrollrad er Maus
     * <p>
     * Verwendet dabei die zoomIn- und zoomOut-Methode dieser Klasse.
     *
     * @param scrollEvent wird benötigt, um die Scrollrichtung zu bestimmen
     */
    @FXML
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
    @FXML
    public void canvasPressed(MouseEvent mouseEvent) {
        referenceHandler.getPopulationPanel().getRowAndCol(mouseEvent.getX(), mouseEvent.getY()).ifPresent(rowCol -> {
            rowColStart = rowCol;
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                int state = (int) mainController.getRadioButtonToggleGroup().getSelectedToggle().getUserData();
                referenceHandler.getAutomaton().setState(rowColStart.value1(), rowColStart.value2(), state);
            }
        });
    }

    /**
     * Bestimmt die Endkoordinaten eines Bereichs beim Loslassen der Maus auf dem Canvas
     *
     * @param mouseEvent zum Bestimmen der X- und Y-Koordinaten
     */
    @FXML
    public void canvasMouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            referenceHandler.getPopulationPanel().getRowAndCol(mouseEvent.getX(), mouseEvent.getY()).ifPresent(rowColEnd -> {
                int fromRow = rowColStart.value1();
                int fromColumn = rowColStart.value2();

                int toRow = rowColEnd.value1();
                int toColumn = rowColEnd.value2();

                if (toRow >= referenceHandler.getAutomaton().getNumberOfRows()) {
                    toRow = referenceHandler.getAutomaton().getNumberOfRows() - 1;
                }
                if (toColumn >= referenceHandler.getAutomaton().getNumberOfColumns()) {
                    toColumn = referenceHandler.getAutomaton().getNumberOfColumns() - 1;
                }

                int lowRowIndex = Math.min(fromRow, toRow);
                int highRowIndex = Math.max(fromRow, toRow);
                int lowColumnIndex = Math.min(fromColumn, toColumn);
                int highColumnIndex = Math.max(fromColumn, toColumn);
                int state = (int) mainController.getRadioButtonToggleGroup().getSelectedToggle().getUserData();
                referenceHandler.getAutomaton().setState(lowRowIndex, lowColumnIndex, highRowIndex, highColumnIndex, state);
            });
        }
    }
}
