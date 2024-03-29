package de.dittel.controller;

import de.dittel.model.Automaton;
import de.dittel.model.Cell;
import de.dittel.util.ReferenceHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.*;

/**
 * Controller-Klasse für den Serialisierungsvorgang
 */
public class SerializationController {

    private final ReferenceHandler referenceHandler;
    private final ResourcesController resourcesController = ResourcesController.getResourcesController();
    private static final FileChooser fileChooser;

    static {
        fileChooser = new FileChooser();
        File dir = new File(".");
        fileChooser.setInitialDirectory(dir);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.ser", "*.ser");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     */
    public SerializationController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        referenceHandler.getMainController().getSerializationMenuItem().setOnAction(e -> savePopulation());
        referenceHandler.getMainController().getDeserializationMenuItem().setOnAction(e -> loadPopulation());
    }

    /**
     * Speichert die aktuelle Population als Serialisierung ab
     */
    private void savePopulation() {
        fileChooser.setTitle(resourcesController.getI18nValue("serializationFileChooserTitle"));
        File file = fileChooser.showSaveDialog(referenceHandler.getMainStage());

        if (file == null) {
            return;
        }

        if (!file.getName().endsWith(".ser")) {
            file = new File(file.getAbsolutePath().concat(".ser"));
        }

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file))) {
            synchronized (referenceHandler.getAutomaton()) {
                os.writeInt(referenceHandler.getAutomaton().getNumberOfStates());
                os.writeObject(referenceHandler.getAutomaton().getPopulation());
            }
        } catch (Exception exc) {
			exc.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, resourcesController.getI18nValue("serializationError"), ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Lädt eine serialisierte Populationsdatei und erzeugt daraus eine neue Population
     */
    private void loadPopulation() {
        Automaton automaton = referenceHandler.getAutomaton();
        fileChooser.setTitle(resourcesController.getI18nValue("deserializationFileChooserTitle"));
        File file = fileChooser.showOpenDialog(referenceHandler.getMainStage());

        if (file != null) {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {
                if (automaton.getNumberOfStates() < is.readInt()) {
                    throw new Exception(resourcesController.getI18nValue("invalidNumberOfStatesError"));
                }
                automaton.changeCells((Cell[][]) is.readObject());
            } catch (Exception exc) {
				exc.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        resourcesController.getI18nValue("deserializationError"), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
}