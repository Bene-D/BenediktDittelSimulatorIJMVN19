package de.dittel.controller;

import de.dittel.Main;
import de.dittel.model.Automaton;
import de.dittel.util.FileManager;
import de.dittel.util.ReferenceHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Controller-Klasse für das NewAutomaton-Fenster
 */
public class NewAutomatonDialogController {

    private final ResourcesController resourcesController = ResourcesController.getResourcesController();

    @FXML
    private DialogPane dialogPane;
    @FXML @SuppressWarnings("unused")
    private TextField newAutomatonTextField;

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     */
    public NewAutomatonDialogController(ReferenceHandler referenceHandler) {
        referenceHandler.getMainController().getNewAutomatonMenuItem().setOnAction(event -> createNewAutomaton());
        referenceHandler.getMainController().getCreateNewAutomatonButton().setOnAction(event -> createNewAutomaton());
    }

    /**
     * Erzeugt einen neuen Automaten und legt eine Datei für diesen im Ordner "automata" an
     * <p>
     * Für das Erzeugen wird eine Dummy-Datei verwendet und der Name an den notwendigen Stellen durch den neuen ersetzt.
     */
    private void createNewAutomaton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newAutomatonDialog.fxml"));
            loader.setController(this);
            dialogPane = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            newAutomatonTextField.textProperty().addListener(observable ->
                    dialogPane.lookupButton(ButtonType.OK).setDisable(!validate()));
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(resourcesController.getI18nValue("createNewAutomatonDialogTitle"));
            dialog.setHeaderText(resourcesController.getI18nValue("createNewAutomatonDialogHeader"));
            newAutomatonTextField.setPromptText
                    (resourcesController.getI18nValue("createNewAutomatonDialogPromptText"));
            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                String automatonName = newAutomatonTextField.getText();
                newAutomatonTextField.setText(automatonName.substring(0,1).toUpperCase() +
                        automatonName.substring(1).toLowerCase());
                File newAutomatonFile = FileManager.createNewAutomatonFile(automatonName);

                if (FileManager.compile(newAutomatonFile)) {
                    Automaton automaton = FileManager.loadAutomaton(newAutomatonFile);
                    assert automaton != null;
                    Main.newAutomaton(null, automaton, automaton.getClass().getName());
                }
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    resourcesController.getI18nValue("createNewAutomatonDialogError"));
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Überprüft den Input der TextFields für die Erstellung einer Java-Klasse
     *
     * @return boolean, ob die Eingabe den Java-Klassen-Namensgebung entspricht und ob der Name bereits vergeben ist
     */
    private boolean validate() {
        boolean isJavaIdentifier = true;
        String userInput = newAutomatonTextField.getText();

        if (userInput == null || userInput.equals("") || !Character.isJavaIdentifierStart(userInput.charAt(0))) {
            return false;
        }

        // Check if class with this name is already existing
        String checkForDuplicates = userInput.substring(0,1).toUpperCase() +
                userInput.substring(1).toLowerCase();
        File temp = new File("automata/" + checkForDuplicates + ".java");
        if (temp.isFile()) {
            return false;
        }

        String subStringUserInput = userInput.substring(1);
        char[] userInputAsCharacters = subStringUserInput.toCharArray();

        for (Character character : userInputAsCharacters) {
            if (!Character.isJavaIdentifierPart(character)) {
                isJavaIdentifier = false;
            }
        }
        return isJavaIdentifier;
    }
}
