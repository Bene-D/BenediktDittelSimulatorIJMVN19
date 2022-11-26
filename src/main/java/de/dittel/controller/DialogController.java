package de.dittel.controller;

import de.dittel.model.Automaton;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class DialogController {

    @FXML
    private TextField rowTextField;
    @FXML
    private TextField columnTextField;
    @FXML
    private DialogPane dialogPane;

    public TextField getRowTextField() {
        return rowTextField;
    }

    public TextField getColumnTextField() {
        return columnTextField;
    }

    public void initialize(Automaton automaton) {
        rowTextField.setText(String.valueOf(automaton.getNumberOfRows()));
        columnTextField.setText(String.valueOf(automaton.getNumberOfColumns()));
        columnTextField.textProperty().addListener(observable ->
                dialogPane.lookupButton(ButtonType.OK).setDisable(validate()));
        rowTextField.textProperty().addListener(observable2 ->
                dialogPane.lookupButton(ButtonType.OK).setDisable(validate()));
    }

    /**
     * Überprüft den Input der TextFields für die Änderung der Reihen- und Spaltenanzahl
     *
     * @return boolean, ob die Eingabe nur aus Zahlen besteht und in der gewählten Range ist
     */
    private boolean validate() {
        if (!rowTextField.getText().matches("\\d+") || !columnTextField.getText().matches("\\d+")) {
            return true;
        }
        return !(Integer.parseInt(rowTextField.getText()) >= 3 && Integer.parseInt(columnTextField.getText()) >= 3 &&
                Integer.parseInt(rowTextField.getText()) <= 100 && Integer.parseInt(columnTextField.getText()) <= 100);
    }
}
