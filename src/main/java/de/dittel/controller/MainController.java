package de.dittel.controller;

import de.dittel.Main;
import de.dittel.model.Automaton;
import de.dittel.util.FileManager;
import de.dittel.util.ReferenceHandler;
import de.dittel.view.PopulationPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller-Klasse für die mainView des Programms
 */
@SuppressWarnings("unused")
public class MainController {

    private ReferenceHandler referenceHandler;
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
    private MenuBar menuBar;
    @FXML
    private MenuItem createNewAutomatonMenuItem;
    @FXML
    private MenuItem changePopulationSizeMenuItem;
    @FXML
    private MenuItem openEditorMenuItem;
    @FXML
    private CheckMenuItem changeTorusCheckMenuItem;
    @FXML
    private MenuItem xmlSerializationMenuItem;
    @FXML
    private MenuItem serializationMenuItem;
    @FXML
    private MenuItem xmlDeserializationMenuItem;
    @FXML
    private MenuItem deserializationMenuItem;
    @FXML
    private MenuItem editorMenuItem;
    @FXML
    private MenuItem singleStepSimulationMenuItem;
    @FXML
    private MenuItem startSimulationMenuItem;
    @FXML
    private MenuItem stopSimulationMenuItem;
    @FXML
    private MenuItem jdbcSaveConfigMenuItem;
    @FXML
    private MenuItem jdbcLoadConfigMenuItem;
    @FXML
    private MenuItem jdbcDeleteConfigMenuItem;
    @FXML
    private Button createNewAutomatonButton;
    @FXML
    private Button changePopulationSizeButton;
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

    /**
     * Konstruktor
     */
    public MainController() {
        colorPickerList = new ArrayList<>(Arrays.asList(new ColorPicker(), new ColorPicker(Color.BLACK)));
        colorPickerList.get(1).setId(String.valueOf(1));
        FileManager.loadAutomaton(new File("automata/DefaultAutomaton.java"));
    }

    /**
     * Getter für colorPickersList
     */
    public List<ColorPicker> getColorPickerList() {
        return colorPickerList;
    }

    /**
     * Getter für colorPickersVBox
     */
    public VBox getColorPickersVBox() {
        return colorPickersVBox;
    }

    /**
     * Getter für radioButtonsVBox
     */
    public VBox getRadioButtonsVBox() {
        return radioButtonsVBox;
    }

    /**
     * Getter für populationScrollPane
     */
    public ScrollPane getPopulationScrollPane() {
        return populationScrollPane;
    }

    /**
     * Getter für createNewAutomatonMenuItem
     */
    public MenuItem getCreateNewAutomatonMenuItem() {
        return createNewAutomatonMenuItem;
    }

    /**
     * Getter für changePopulationSizeMenuItem
     */
    public MenuItem getChangePopulationSizeMenuItem() {
        return changePopulationSizeMenuItem;
    }

    /**
     * Getter für openEditorMenuItem
     */
    public MenuItem getOpenEditorMenuItem() {
        return openEditorMenuItem;
    }

    /**
     * Getter für xmlSerializationMenuItem
     */
    public MenuItem getXmlSerializationMenuItem() {
        return xmlSerializationMenuItem;
    }

    /**
     * Getter für serializationMenuItem
     */
    public MenuItem getSerializationMenuItem() {
        return serializationMenuItem;
    }

    /**
     * Getter für xmlDeserializationMenuItem
     */
    public MenuItem getXmlDeserializationMenuItem() {
        return xmlDeserializationMenuItem;
    }

    /**
     * Getter für deserializationMenuItem
     */
    public MenuItem getDeserializationMenuItem() {
        return deserializationMenuItem;
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
     * Getter für jdbcSaveConfigMenuItem
     */
    public MenuItem getJdbcSaveConfigMenuItem() {
        return jdbcSaveConfigMenuItem;
    }

    /**
     * Getter für jdbcLoadConfigMenuItem
     */
    public MenuItem getJdbcLoadConfigMenuItem() {
        return jdbcLoadConfigMenuItem;
    }

    /**
     * Getter für jdbcDeleteConfigMenuItem
     */
    public MenuItem getJdbcDeleteConfigMenuItem() {
        return jdbcDeleteConfigMenuItem;
    }

    /**
     * Getter für createNewAutomatonButton
     */
    public Button getCreateNewAutomatonButton() {
        return createNewAutomatonButton;
    }

    /**
     * Getter für changePopulationSizeButton
     */
    public Button getChangePopulationSizeButton() {
        return changePopulationSizeButton;
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
     * Initialisiert die View mit den benötigten Attributen
     */
    public void init(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        setTorus();
        for (ColorPicker colorPicker : colorPickerList) {
            colorPicker.setOnAction(this::changeColor);
        }
    }

    /**
     * Setzt den aktuellen Torus-Status des Automaten
     */
    public void setTorus() {
        changeTorusCheckMenuItem.setSelected(referenceHandler.getAutomaton().isTorus());
        changeTorusToggleButton.setSelected(referenceHandler.getAutomaton().isTorus());
    }

    /**
     * Generiert EINE neue Population
     *
     * @throws Throwable möglicherweise wirft die Methode eine Exception
     */
    @FXML
    public void singleStep() throws Throwable {
        referenceHandler.getAutomaton().nextGeneration();
    }

    /**
     * Ändert die Torus-Einstellung des Automaten und aktualisiert den Button in der View
     */
    @FXML
    public void changeTorus() {
        referenceHandler.getAutomaton().setTorus(!referenceHandler.getAutomaton().isTorus());
        changeTorusToggleButton.setSelected(referenceHandler.getAutomaton().isTorus());
        changeTorusCheckMenuItem.setSelected(referenceHandler.getAutomaton().isTorus());
    }

    /**
     * Erzeugt eine Random-Population des Automaten
     */
    @FXML
    public void randomPopulation() {
        referenceHandler.getAutomaton().randomPopulation();
    }

    /**
     * Setzt den Zustand aller Zellen des Automaten auf den Wert 0
     */
    @FXML
    public void resetPopulation() {
        referenceHandler.getAutomaton().clearPopulation();
    }

    /**
     * Ändert die Farbe eines Zustands des Automaten
     * <p>
     * Es ändert sich die Farbe aller Zellen, die sich in diesem Zustand befinden.
     *
     * @param actionEvent wird benötigt, um den ColorPicker zu wählen, der das Event ausgelöst hat.
     */
    @FXML
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

    /**
     * Lädt und compiliert eine Automaten-Datei und öffnet diese in einem neuen Fenster
     * <p>
     * Zum Auswählen der Datei wird ein FileChooser verwendet, welcher alle ".java" Dateien im Ordner "automata" anzeigt.
     */
    @FXML
    public void loadAndCompileAutomaton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Automaten auswählen");
        fileChooser.setInitialDirectory(new File("automata"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java-Datei (*.java)", "*.java"));

        File automatonFile = fileChooser.showOpenDialog(new Stage());

        if (automatonFile != null) {
            String automatonClass = String.valueOf(Path.of(automatonFile.getPath()));
            File file = new File(automatonClass.replace(".java", ".class"));
            if (!file.exists()) {
                FileManager.compile(automatonFile);
            }
                try {
                    // Klasse laden
                    URL classUrl = automatonFile.getParentFile().toURI().toURL();
                    URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
                    Class<?> newAutomatonClass = Class.forName(automatonFile.getName().replace(".java", ""),
                            true, classLoader);
                    Automaton automaton = (Automaton) newAutomatonClass.getDeclaredConstructor().newInstance();
                    Main.newAutomaton(null, automaton, automaton.getClass().getName());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Ups, da ist etwas schief gelaufen:\n" + e);
                    alert.show();
                    e.printStackTrace();
                }
            }
    }

    /**
     * Schließt das aktuelle Fenster
     */
    @FXML
    public void closeWindow() {
        stopSimulationButton.fire();
        referenceHandler.getDatabaseController().shutdown();
        Stage stageToClose = (Stage) menuBar.getScene().getWindow();
        stageToClose.close();
    }
}
