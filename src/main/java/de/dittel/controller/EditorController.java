package de.dittel.controller;

import de.dittel.util.FileManager;
import de.dittel.util.ReferenceHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
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

    private ReferenceHandler referenceHandler;
    private String automatonPath;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TextArea automatonClassTextArea;

    /**
     * Initialisiert den Controller mit den benötigten Attributen
     *
     * @param referenceHandler verwaltet alle verwendeten Referenzen
     */
    public void init(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        automatonPath = "automata/" + referenceHandler.getAutomaton().getClass().getName() + ".java";
        loadAutomatonClassText();
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
            e.printStackTrace();
        }
    }

    /**
     * Speichert den aktuellen Inhalt der TextArea in das File des Automaten
     * <p>
     * Bestätigt die Aktion in einem Alert-Fenster.
     */
    @FXML
    public void saveFileChanges() {
        try {
            Path automatonClass = Paths.get(automatonPath);
            String content = automatonClassTextArea.getText();
            Files.writeString(automatonClass, content, StandardCharsets.UTF_8);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Änderungen gespeichert!");
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compiliert das aktuelle File des Automaten und tauscht den neu erzeugten Automaten im ReferenceHandler aus
     * <p>
     * Durch Setzen des neuen Automaten im ReferenceHandler wird die View neu gezeichnet und der neue Automat angezeigt.
     */
    @FXML
    public void compileFile() {
        File currentAutomaton = new File(automatonPath);
        if (FileManager.compile(currentAutomaton)) {
            referenceHandler.setAutomaton(FileManager.loadAutomaton(currentAutomaton));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compilieren erfolgreich!");
            alert.show();
        }
    }

    /**
     * Schließt das aktuelle Fenster
     */
    @FXML
    public void closeWindow() {
        Stage stageToClose = (Stage) menuBar.getScene().getWindow();
        stageToClose.close();
    }
}
