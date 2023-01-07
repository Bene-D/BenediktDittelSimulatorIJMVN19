package de.dittel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

import java.io.File;

/**
 * Controller-Klasse für das NewAutomaton-Fenster
 */
public class NewAutomatonDialogController {

    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField newAutomatonTextField;

    /**
     * Getter für newAutomatonTextField
     */
    public TextField getNewAutomatonTextField() {
        return newAutomatonTextField;
    }

    /**
     * Fügt den FXML-Elementen der changeSizeDialog.fxml die Werte und Listener hinzu
     */
    public void initialize() {
        newAutomatonTextField.textProperty().addListener(observable ->
                dialogPane.lookupButton(ButtonType.OK).setDisable(!validate()));
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
