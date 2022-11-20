package de.dittel.controller;

import de.dittel.PopulationPanel;
import de.dittel.automaton.Automaton;
import de.dittel.automaton.KruemelmonsterAutomaton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

/**
 * Controller-Klasse zur Steuerung der GUI
 */
public class Controller {

    private Automaton automaton;
    Random random = new Random();
    private List<ColorPicker> colorPickers;
    private double xDragDetected;
    private double yDragDetected;

    @FXML
    private VBox colorPickersVBox;
    @FXML
    private VBox radioButtonsVBox;
    @FXML
    private ScrollPane populationScrollPane;
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
    @FXML
    private ToggleGroup radioButtonToggleGroup;
    @FXML
    private RadioButton radioButtonZero;
    @FXML
    private RadioButton radioButtonOne;


    public void initialize() {
        automaton = new KruemelmonsterAutomaton(10, 10, 10, true);
        colorPickers = new ArrayList<>(Arrays.asList(new ColorPicker(), new ColorPicker(Color.BLACK)));
        colorPickers.get(1).setId(String.valueOf(1));
        setUpColorPickers();
        setUpRadioButtons();
        torusCheckMenuItem.setSelected(automaton.isTorus());
        changeTorusToggleButton.setSelected(automaton.isTorus());
        populationPanel = new PopulationPanel(automaton, colorPickers);
        populationPanel.setOnScroll(this::zoom);
        populationPanel.getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, this::canvasPressed);
        populationPanel.getCanvas().addEventHandler(MouseEvent.DRAG_DETECTED, this::canvasDragDetected);
        populationPanel.getCanvas().addEventHandler(MouseEvent.MOUSE_RELEASED, this::canvasMouseReleased);
        populationScrollPane.setContent(populationPanel);
        populationScrollPane.viewportBoundsProperty()
                .addListener((observable, oldValue, newValue) -> populationPanel.center(newValue));
        radioButtonZero.setToggleGroup(radioButtonToggleGroup);
        radioButtonOne.setToggleGroup(radioButtonToggleGroup);
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
            radioButton.setToggleGroup(radioButtonToggleGroup);
            radioButtonsVBox.getChildren().add(radioButton);
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
            colorPicker.setOnAction(this::changeColor);
            colorPickersVBox.getChildren().add(colorPicker);
            colorPickers.add(colorPicker);
        }
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
     * <p>
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
                populationPanel.center(populationScrollPane.getViewportBounds());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vergrößert mit Hilfe der zoomIn-Methode des PopulationPanels die Population
     * <p>
     * Das neugezeichnete Canvas ist somit größer und suggeriert einen optischen Zoom.
     */
    public void zoomIn() {
        zoomOutButton.setDisable(false);
        if (populationPanel.zoomIn()) {
            zoomInButton.setDisable(true);
        }
        populationPanel.paintCanvas();
        populationPanel.center(populationScrollPane.getViewportBounds());
    }

    /**
     * Verkleinert mit Hilfe der zoomOut-Methode des PopulationPanels die Population
     * <p>
     * Das neugezeichnete Canvas ist somit kleiner und suggeriert einen optischen Zoom.
     */
    public void zoomOut() {
        zoomInButton.setDisable(false);
        if (populationPanel.zoomOut()) {
            zoomOutButton.setDisable(true);
        }
        populationPanel.paintCanvas();
        populationPanel.center(populationScrollPane.getViewportBounds());

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
                zoomOut();
            } else {
                zoomIn();
            }
        }
    }

    /**
     * Ändert die Farbe eines Zustands des Automaten
     * <p>
     * Es ändert sich die Farbe aller Zellen, die sich in diesem Zustand befinden.
     * Das Canvas des PopulationsPanels wird deshalb neu gezeichnet.
     *
     * @param actionEvent wird benötigt, um den ColorPicker zu wählen, der das Event ausgelöst hat
     */
    public void changeColor(ActionEvent actionEvent) {
        ColorPicker colorPicker = (ColorPicker) actionEvent.getSource();
        int id;

        if (colorPicker.getId() == null) {
            id = 0;
        } else {
            id = Integer.parseInt(colorPicker.getId());
        }

        Color color = colorPicker.getValue();
        colorPickers.get(id).setValue(color);
        populationPanel.paintCanvas();
    }
    /**
     * Ändert den Zustand der Zelle der Population, auf die geklickt wurde
     * <p>
     * Verwendet dabei den aktuell ausgewählten RadioButton, um den neuen Zustand/Farbe zu bestimmen
     * @param mouseEvent, zum Bestimmen der X- und Y-Koordinaten
     */
    public void canvasPressed(MouseEvent mouseEvent) {
        int state = getActiveRadioButton();
        populationPanel.canvasPaintPressed(mouseEvent, state);
    }

    /**
     * Bestimmt die Anfangskoordinaten eines Bereichs beim Ziehen mit der Maus auf dem Canvas
     *
     * @param mouseEvent, zum Bestimmen der X- und Y-Koordinaten
     */
    public void canvasDragDetected(MouseEvent mouseEvent) {
        xDragDetected = mouseEvent.getX();
        yDragDetected = mouseEvent.getY();
    }

    /**
     * Bestimmt die Endkoordinaten eines Bereichs beim Loslassen der Maus auf dem Canvas
     *
     * @param mouseEvent, zum Bestimmen der X- und Y-Koordinaten
     */
    public void canvasMouseReleased(MouseEvent mouseEvent) {
        double xMouseReleased = mouseEvent.getX();
        double yMouseReleased = mouseEvent.getY();
        int state = getActiveRadioButton();

        populationPanel.canvasPaintDragAndRelease(xDragDetected, yDragDetected, xMouseReleased, yMouseReleased, state);
    }

    /**
     * Helfermethode zum Bestimmen des aktuell ausgewählten RadioButtons
     *
     * @return Zustand des aktuell ausgewählten RadioButtons
     */
    private int getActiveRadioButton() {
        RadioButton radioButton = (RadioButton) radioButtonToggleGroup.getSelectedToggle();
        int state;
        if (radioButton.getText().equals("0")) {
            state = 0;
        } else {
            state = Integer.parseInt(radioButton.getText());
        }
        return state;
    }
}
