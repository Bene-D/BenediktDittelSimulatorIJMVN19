package de.dittel.controller;

import de.dittel.util.FileManager;
import de.dittel.util.ReferenceHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller-Klasse für das Editor-Fenster
 */
@SuppressWarnings("unused")
public class EditorController {

    private final ReferenceHandler referenceHandler;
    private final String automatonPath;
    private boolean editorOpen = false;
    private final ResourcesController resourcesController = ResourcesController.getResourcesController();

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem saveFileMenuItem;
    @FXML
    private MenuItem compileFileMenuItem;
    @FXML
    private MenuItem quitEditorMenuItem;
    @FXML
    private Button saveFileButton;
    @FXML
    private Tooltip editorSaveFileTooltip;
    @FXML
    private Button compileFileButton;
    @FXML
    private Tooltip editorCompileFileTooltip;
    @FXML
    private TextArea automatonClassTextArea;
    @FXML
    private Label editorWelcomeLabel;

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     */
    public EditorController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        automatonPath = "automata/" + referenceHandler.getAutomaton().getClass().getName() + ".java";
        referenceHandler.getMainController().getOpenEditorMenuItem().setOnAction(event -> newEditor());
    }

    /**
     * Helfermethode zum Binden der FXML-Elemente an die entsprechenden Methoden
     */
    private void bindFxmlMethods() {
        saveFileMenuItem.setOnAction(event -> saveFileChanges());
        saveFileButton.setOnAction(event -> saveFileChanges());
        compileFileMenuItem.setOnAction(event -> compileFile());
        compileFileButton.setOnAction(event -> compileFile());
        quitEditorMenuItem.setOnAction(event -> closeWindow());
    }

    /**
     * Bindet die Properties der GUI-Elemente an die i18n-Ressourcen
     * <p>
     * Erlaubt ein einfaches Wechseln der Sprache aller gebundenen Elemente.
     */
    private void bindLanguageProperties() {
        saveFileMenuItem.textProperty().bind(resourcesController.i18n("saveFileMenuItem"));
        compileFileMenuItem.textProperty().bind(resourcesController.i18n("compileFileMenuItem"));
        quitEditorMenuItem.textProperty().bind(resourcesController.i18n("quitEditorMenuItem"));
        editorSaveFileTooltip.textProperty().bind(resourcesController.i18n("editorSaveFileTooltip"));
        editorCompileFileTooltip.textProperty().bind(resourcesController.i18n("editorCompileFileTooltip"));
        editorWelcomeLabel.textProperty().bind(resourcesController.i18n("editorWelcomeLabel"));
    }

    /**
     * Öffnet einen Editor für die Automaton-Datei in einem neuen Fenster
     */
    private void newEditor() {
        if (!editorOpen) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editor.fxml"));
                loader.setController(this);
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                loadAutomatonClassText();
                stage.setTitle("Editor - " + referenceHandler.getAutomaton().getClass().getName());
                stage.initOwner(referenceHandler.getMainStage());
                stage.setOnCloseRequest(event ->  closeWindow());
                bindFxmlMethods();
                bindLanguageProperties();
                stage.show();
                editorOpen = true;
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, resourcesController.getI18nValue("newEditorError"));
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    /**
     * Lädt den aktuellen Inhalt der Klasse in die TextArea des Editor-Fensters
     */
    private void loadAutomatonClassText() {
        try {
            Path automatonClass = Paths.get(automatonPath);
            String content = Files.readString(automatonClass, StandardCharsets.UTF_8);
            automatonClassTextArea.setText(content);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    resourcesController.getI18nValue("loadAutomatonTextError"));
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Speichert den aktuellen Inhalt der TextArea in das File des Automaten
     * <p>
     * Bestätigt die Aktion in einem Alert-Fenster.
     */
    private void saveFileChanges() {
        try {
            Path automatonClass = Paths.get(automatonPath);
            String content = automatonClassTextArea.getText();
            Files.writeString(automatonClass, content, StandardCharsets.UTF_8);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, resourcesController.getI18nValue("saveFileInfo"));
            alert.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, resourcesController.getI18nValue("saveFileError"));
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Compiliert das aktuelle File des Automaten und tauscht den neu erzeugten Automaten im ReferenceHandler aus
     * <p>
     * Durch Setzen des neuen Automaten im ReferenceHandler wird die View neu gezeichnet und der neue Automat angezeigt.
     */
    private void compileFile() {
        File currentAutomaton = new File(automatonPath);
        if (FileManager.compile(currentAutomaton)) {
            referenceHandler.setAutomaton(FileManager.loadAutomaton(currentAutomaton));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, resourcesController.getI18nValue("compileInfo"));
            alert.show();
        }
    }

    /**
     * Schließt das aktuelle Fenster
     */
    private void closeWindow() {
        Stage stageToClose = (Stage) menuBar.getScene().getWindow();
        stageToClose.close();
        editorOpen = false;
    }
}
