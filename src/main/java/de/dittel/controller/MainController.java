package de.dittel.controller;

import de.dittel.model.Automaton;
import de.dittel.view.PopulationPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

/**
 * Controller-Klasse für die Verbindung zwischen mainView und Automaton
 */
public class MainController {

    private final Automaton automaton;
    Random random = new Random();
    private final List<ColorPicker> colorPickerList;

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
    private CheckMenuItem changeTorusCheckMenuItem;
    @FXML
    private MenuItem singleStepSimulationMenuItem;
    @FXML
    private MenuItem startSimulationMenuItem;
    @FXML
    private MenuItem stopSimulationMenuItem;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Button singleStepSimulationButton;
    @FXML
    private Button startSimulationButton;
    @FXML
    private Button stopSimulationButton;
    @FXML
    private Slider simulationSpeedSlider;
    @FXML
    private ToggleGroup radioButtonToggleGroup;
    @FXML
    private RadioButton radioButtonZero;
    @FXML
    private RadioButton radioButtonOne;

    /**
     * Konstruktor
     *
     * @param automaton Model des Controllers
     */
    public MainController(Automaton automaton) {
        this.automaton = automaton;
        colorPickerList = new ArrayList<>(Arrays.asList(new ColorPicker(), new ColorPicker(Color.BLACK)));
        colorPickerList.get(1).setId(String.valueOf(1));

    }

    /**
     * Getter für automaton
     */
    public Automaton getAutomaton() {
        return automaton;
    }

    /**
     * Getter für colorPickersList
     */
    public List<ColorPicker> getColorPickerList() {
        return colorPickerList;
    }

    /**
     * Getter für populationScrollPane
     */
    public ScrollPane getPopulationScrollPane() {
        return populationScrollPane;
    }

    /**
     * Getter für singleStepSimulationMenuItem
     */
    public MenuItem getSingleStepSimulationMenuItem() {
        return singleStepSimulationMenuItem;
    }

    /**
     * Getter für startSimulationMenuItem
     */
    public MenuItem getStartSimulationMenuItem() {
        return startSimulationMenuItem;
    }

    /**
     * Getter für stopSimulationMenuItem
     */
    public MenuItem getStopSimulationMenuItem() {
        return stopSimulationMenuItem;
    }

    /**
     * Getter für zoomInButton
     */
    public Button getZoomInButton() {
        return zoomInButton;
    }

    /**
     * Getter für zoomOutButton
     */
    public Button getZoomOutButton() {
        return zoomOutButton;
    }

    /**
     * Getter für singleStepSimulationButton
     */
    public Button getSingleStepSimulationButton() {
        return singleStepSimulationButton;
    }

    /**
     * Getter für startSimulationButton
     */
    public Button getStartSimulationButton() {
        return startSimulationButton;
    }

    /**
     * Getter für stopSimulationButton
     */
    public Button getStopSimulationButton() {
        return stopSimulationButton;
    }

    /**
     * Getter für simulationSpeedSlider
     */
    public Slider getSimulationSpeedSlider() {
        return simulationSpeedSlider;
    }

    /**
     * Getter für radioButtonToggleGroup
     */
    public ToggleGroup getRadioButtonToggleGroup() {
        return radioButtonToggleGroup;
    }

    /**
     * Setter für populationPanel
     */
    public void setPopulationPanel(PopulationPanel populationPanel) {
        this.populationPanel = populationPanel;
    }

    /**
     * Initialisiert die View mit den aktuellen Werten des Automaten
     */
    public void init() {
        setUpColorPickers();
        setUpRadioButtons();
        radioButtonZero.setUserData(0);
        radioButtonOne.setUserData(1);
        radioButtonZero.setToggleGroup(radioButtonToggleGroup);
        radioButtonOne.setToggleGroup(radioButtonToggleGroup);
        changeTorusCheckMenuItem.setSelected(automaton.isTorus());
        changeTorusToggleButton.setSelected(automaton.isTorus());
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
            colorPickerList.add(colorPicker);
        }
    }

    /**
     * Generiert EINE neue Population
     *
     * @throws Throwable möglicherweise wirft die Methode eine Exception
     */
    public void singleStep() throws Throwable {
        automaton.nextGeneration();
    }

    /**
     * Ändert die Torus-Einstellung des Automaten und aktualisiert den Button in der View
     */
    public void changeTorus() {
        automaton.setTorus(!automaton.isTorus());
        changeTorusToggleButton.setSelected(automaton.isTorus());
        changeTorusCheckMenuItem.setSelected(automaton.isTorus());
    }

    /**
     * Erzeugt eine Random-Population des Automaten
     */
    public void randomPopulation() {
        automaton.randomPopulation();
    }

    /**
     * Setzt den Zustand aller Zellen des Automaten auf den Wert 0
     */
    public void resetPopulation() {
        automaton.clearPopulation();
    }

    /**
     * Ändert die Größe der Population
     * <p>
     * Erstellt ein DialogPane, welches die neue Anzahl der Reihen und Spalten abfragt.
     * Die Eingaben werden benutzt, um die Attribute des Automaten zu aktualisieren.
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ändert die Farbe eines Zustands des Automaten
     * <p>
     * Es ändert sich die Farbe aller Zellen, die sich in diesem Zustand befinden.
     *
     * @param actionEvent wird benötigt, um den ColorPicker zu wählen, der das Event ausgelöst hat.
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
        colorPickerList.get(id).setValue(color);
        populationPanel.paintCanvas();
        populationPanel.center(populationScrollPane.getViewportBounds());
    }
}
