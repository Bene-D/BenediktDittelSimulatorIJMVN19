package de.dittel.controller;

import de.dittel.automaton.Automaton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DialogController {

    @FXML
    private TextField rowTextField;
    @FXML
    private TextField columnTextField;

    public void initialize(Automaton automaton) {
        rowTextField.setText(String.valueOf(automaton.getNumberOfRows()));
        columnTextField.setText(String.valueOf(automaton.getNumberOfColumns()));
    }

    public TextField getRowTextField() {
        return rowTextField;
    }

    public TextField getColumnTextField() {
        return columnTextField;
    }

    public boolean validate() {
        if (!rowTextField.getText().matches("\\d+") || !columnTextField.getText().matches("\\d+")) {
            return true;
        }
        return !(Integer.parseInt(rowTextField.getText()) >= 3 && Integer.parseInt(columnTextField.getText()) >= 3 &&
                Integer.parseInt(rowTextField.getText()) <= 150 && Integer.parseInt(columnTextField.getText()) <= 150);
    }
}
