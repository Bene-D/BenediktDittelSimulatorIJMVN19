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
    private final ResourcesController resourcesController = ResourcesController.getResourcesController();

    private final List<ColorPicker> colorPickerList;

    @FXML
    private VBox colorPickersVBox;
    @FXML
    private VBox radioButtonsVBox;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu automatonMenu;
    @FXML
    private MenuItem newAutomatonMenuItem;
    @FXML
    private MenuItem loadAutomatonMenuItem;
    @FXML
    private MenuItem openEditorMenuItem;
    @FXML
    private MenuItem quitMenuItem;
    @FXML
    private Menu populationMenu;
    @FXML
    private MenuItem changePopulationSizeMenuItem;
    @FXML
    private MenuItem clearPopulationMenuItem;
    @FXML
    private MenuItem randomPopulationMenuItem;
    @FXML
    private CheckMenuItem changeTorusCheckMenuItem;
    @FXML
    private MenuItem zoomInMenuItem;
    @FXML
    private MenuItem zoomOutMenuItem;
    @FXML
    private Menu savePopulationMenu;
    @FXML
    private MenuItem xmlSerializationMenuItem;
    @FXML
    private MenuItem serializationMenuItem;
    @FXML
    private Menu loadPopulationMenu;
    @FXML
    private MenuItem xmlDeserializationMenuItem;
    @FXML
    private MenuItem deserializationMenuItem;
    @FXML
    private MenuItem printPopulationMenuItem;
    @FXML
    private Menu simulationMenu;
    @FXML
    private MenuItem simulationStepMenuItem;
    @FXML
    private MenuItem startSimulationMenuItem;
    @FXML
    private MenuItem stopSimulationMenuItem;
    @FXML
    private Menu settingsMenu;
    @FXML
    private MenuItem saveSettingsMenuItem;
    @FXML
    private MenuItem loadSettingsMenuItem;
    @FXML
    private MenuItem deleteSettingsMenuItem;
    @FXML
    private Menu languageMenu;
    @FXML
    private ToggleGroup languageToggleGroup;
    @FXML
    private RadioMenuItem languageEnglishMenuItem;
    @FXML
    private RadioMenuItem languageGermanMenuItem;
    @FXML
    private Button createNewAutomatonButton;
    @FXML
    private Tooltip createAutomatonTooltip;
    @FXML
    private Tooltip loadAutomatonTooltip;
    @FXML
    private Button changePopulationSizeButton;
    @FXML
    private Tooltip changePopulationSizeTooltip;
    @FXML
    private Tooltip clearPopulationTooltip;
    @FXML
    private Tooltip randomPopulationTooltip;
    @FXML
    private ToggleButton changeTorusToggleButton;
    @FXML
    private Tooltip changeTorusTooltip;
    @FXML
    private Tooltip printPopulationTooltip;
    @FXML
    private Button zoomInButton;
    @FXML
    private Tooltip zoomInTooltip;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Tooltip zoomOutTooltip;
    @FXML
    private Button simulationStepButton;
    @FXML
    private Tooltip simulationStepTooltip;
    @FXML
    private Button startSimulationButton;
    @FXML
    private Tooltip startSimulationTooltip;
    @FXML
    private Button stopSimulationButton;
    @FXML
    private Tooltip stopSimulationTooltip;
    @FXML
    private Slider simulationSpeedSlider;
    @FXML
    private Tooltip sliderTooltip;
    @FXML
    private ToggleGroup radioButtonToggleGroup;
    @FXML
    private ScrollPane populationScrollPane;
    @FXML
    private PopulationPanel populationPanel;
    @FXML
    private Label welcomeLabel;

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
    public MenuItem getNewAutomatonMenuItem() {
        return newAutomatonMenuItem;
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
    public MenuItem getSimulationStepMenuItem() {
        return simulationStepMenuItem;
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
    public MenuItem getSaveSettingsMenuItem() {
        return saveSettingsMenuItem;
    }

    /**
     * Getter für jdbcLoadConfigMenuItem
     */
    public MenuItem getLoadSettingsMenuItem() {
        return loadSettingsMenuItem;
    }

    /**
     * Getter für jdbcDeleteConfigMenuItem
     */
    public MenuItem getDeleteSettingsMenuItem() {
        return deleteSettingsMenuItem;
    }

    public ToggleGroup getLanguageToggleGroup() {
        return languageToggleGroup;
    }

    /**
     * Getter für languageEnglishMenuItem
     */
    public RadioMenuItem getLanguageEnglishMenuItem() {
        return languageEnglishMenuItem;
    }

    /**
     * Getter für languageGermanMenuItem
     */
    public RadioMenuItem getLanguageGermanMenuItem() {
        return languageGermanMenuItem;
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
    public Button getSimulationStepButton() {
        return simulationStepButton;
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
        initLanguageMenuItems();
        bindLanguageProperties();
    }

    private void bindLanguageProperties() {
        // Automat
        automatonMenu.textProperty().bind(resourcesController.i18n("automatonMenu"));
        newAutomatonMenuItem.textProperty().bind(resourcesController.i18n("newAutomatonMenuItem"));
        loadAutomatonMenuItem.textProperty().bind(resourcesController.i18n("loadAutomatonMenuItem"));
        openEditorMenuItem.textProperty().bind(resourcesController.i18n("openEditorMenuItem"));
        quitMenuItem.textProperty().bind(resourcesController.i18n("quitMenuItem"));
        // Population
        populationMenu.textProperty().bind(resourcesController.i18n("populationMenu"));
        changePopulationSizeMenuItem.textProperty().bind(resourcesController.i18n("changePopulationSizeMenuItem"));
        clearPopulationMenuItem.textProperty().bind(resourcesController.i18n("clearPopulationMenuItem"));
        randomPopulationMenuItem.textProperty().bind(resourcesController.i18n("randomPopulationMenuItem"));
        changeTorusCheckMenuItem.textProperty().bind(resourcesController.i18n("changeTorusCheckMenuItem"));
        zoomInMenuItem.textProperty().bind(resourcesController.i18n("zoomInMenuItem"));
        zoomOutMenuItem.textProperty().bind(resourcesController.i18n("zoomOutMenuItem"));
        savePopulationMenu.textProperty().bind(resourcesController.i18n("savePopulationMenu"));
        xmlSerializationMenuItem.textProperty().bind(resourcesController.i18n("xmlSerializationMenuItem"));
        serializationMenuItem.textProperty().bind(resourcesController.i18n("serializationMenuItem"));
        loadPopulationMenu.textProperty().bind(resourcesController.i18n("loadPopulationMenu"));
        xmlDeserializationMenuItem.textProperty().bind(resourcesController.i18n("xmlDeserializationMenuItem"));
        deserializationMenuItem.textProperty().bind(resourcesController.i18n("deserializationMenuItem"));
        printPopulationMenuItem.textProperty().bind(resourcesController.i18n("printPopulationMenuItem"));
        // Simulation
        simulationMenu.textProperty().bind(resourcesController.i18n("simulationMenu"));
        simulationStepMenuItem.textProperty().bind(resourcesController.i18n("simulationStepMenuItem"));
        startSimulationMenuItem.textProperty().bind(resourcesController.i18n("startSimulationMenuItem"));
        stopSimulationMenuItem.textProperty().bind(resourcesController.i18n("stopSimulationMenuItem"));
        // Einstellungen
        settingsMenu.textProperty().bind(resourcesController.i18n("settingsMenu"));
        saveSettingsMenuItem.textProperty().bind(resourcesController.i18n("saveSettingsMenuItem"));
        loadSettingsMenuItem.textProperty().bind(resourcesController.i18n("loadSettingsMenuItem"));
        deleteSettingsMenuItem.textProperty().bind(resourcesController.i18n("deleteSettingsMenuItem"));
        // Sprache
        languageMenu.textProperty().bind(resourcesController.i18n("languageMenu"));
        languageEnglishMenuItem.textProperty().bind(resourcesController.i18n("languageEnglishMenuItem"));
        languageGermanMenuItem.textProperty().bind(resourcesController.i18n("languageGermanMenuItem"));
        // Tooltips
        createAutomatonTooltip.textProperty().bind(resourcesController.i18n("createAutomatonTooltip"));
        loadAutomatonTooltip.textProperty().bind(resourcesController.i18n("loadAutomatonTooltip"));
        changePopulationSizeTooltip.textProperty().bind(resourcesController.i18n("changePopulationSizeTooltip"));
        clearPopulationTooltip.textProperty().bind(resourcesController.i18n("clearPopulationTooltip"));
        randomPopulationTooltip.textProperty().bind(resourcesController.i18n("randomPopulationTooltip"));
        changeTorusTooltip.textProperty().bind(resourcesController.i18n("changeTorusTooltip"));
        printPopulationTooltip.textProperty().bind(resourcesController.i18n("printPopulationTooltip"));
        zoomInTooltip.textProperty().bind(resourcesController.i18n("zoomInTooltip"));
        zoomOutTooltip.textProperty().bind(resourcesController.i18n("zoomOutTooltip"));
        simulationStepTooltip.textProperty().bind(resourcesController.i18n("simulationStepTooltip"));
        startSimulationTooltip.textProperty().bind(resourcesController.i18n("startSimulationTooltip"));
        stopSimulationTooltip.textProperty().bind(resourcesController.i18n("stopSimulationTooltip"));
        sliderTooltip.textProperty().bind(resourcesController.i18n("sliderTooltip"));
        // Gruß Label
        welcomeLabel.textProperty().bind(resourcesController.i18n("welcomeLabel"));
    }

    private void initLanguageMenuItems() {
        languageEnglishMenuItem
                .setSelected(ResourcesController.getResourcesController().getLocale().getLanguage().equals("en"));
        languageGermanMenuItem
                .setSelected(ResourcesController.getResourcesController().getLocale().getLanguage().equals("de"));
        languageEnglishMenuItem.setToggleGroup(languageToggleGroup);
        languageGermanMenuItem.setToggleGroup(languageToggleGroup);
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
        Stage stageToClose = (Stage) menuBar.getScene().getWindow();
        stageToClose.close();
    }
}
