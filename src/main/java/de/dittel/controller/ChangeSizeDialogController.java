package de.dittel.controller;

import de.dittel.model.Automaton;
import de.dittel.util.ReferenceHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller-Klasse für das changeSize-Dialogfenster
 */
public class ChangeSizeDialogController {

    private final ReferenceHandler referenceHandler;
    private final ResourcesController resourcesController = ResourcesController.getResourcesController();

    @FXML @SuppressWarnings("unused")
    private Label rowLabel;
    @FXML @SuppressWarnings("unused")
    private Label columnLabel;
    @FXML @SuppressWarnings("unused")
    private TextField rowTextField;
    @FXML @SuppressWarnings("unused")
    private TextField columnTextField;
    @FXML
    private DialogPane dialogPane;

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     */
    public ChangeSizeDialogController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        referenceHandler.getMainController().getChangePopulationSizeMenuItem()
                .setOnAction(event -> changePopulationSize());
        referenceHandler.getMainController().getChangePopulationSizeButton()
                .setOnAction(event -> changePopulationSize());
    }

    /**
     * Fügt den FXML-Elementen der changeSizeDialog.fxml die Werte und Listener hinzu
     *
     * @param automaton wird benötigt, um die aktuelle Reihen- und Spaltenanzahl des Automaten darszustellen
     */
    public void init(Automaton automaton) {
        rowTextField.setText(String.valueOf(automaton.getNumberOfRows()));
        columnTextField.setText(String.valueOf(automaton.getNumberOfColumns()));
        columnTextField.textProperty().addListener(observable ->
                dialogPane.lookupButton(ButtonType.OK).setDisable(validate()));
        rowTextField.textProperty().addListener(observable2 ->
                dialogPane.lookupButton(ButtonType.OK).setDisable(validate()));
    }

    /**
     * Ändert die Größe der Population
     * <p>
     * Erstellt ein DialogPane, welches die neue Anzahl der Reihen und Spalten abfragt.
     * Die Eingaben werden benutzt, um die Attribute des Automaten zu aktualisieren.
     */
    private void changePopulationSize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/changeSizeDialog.fxml"));
            loader.setController(this);
            dialogPane = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            init(referenceHandler.getAutomaton());
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(resourcesController.getI18nValue("changeSizeDialogTitle"));
            dialog.setHeaderText(resourcesController.getI18nValue("changeSizeDialogHeader"));
            rowLabel.setText(resourcesController.getI18nValue("changeSizeDialogLabelRowText"));
            columnLabel.setText(resourcesController.getI18nValue("changeSizeDialogLabelColumnText"));
            Optional<ButtonType> clickedButton = dialog.showAndWait();

            if (clickedButton.isPresent() && clickedButton.get() == ButtonType.OK) {
                referenceHandler.getAutomaton().changeSize(Integer.parseInt(rowTextField.getText()),
                        Integer.parseInt(columnTextField.getText()));
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    resourcesController.getI18nValue("changeSizeDialogError"));
            alert.showAndWait();
            e.printStackTrace();
        }
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
