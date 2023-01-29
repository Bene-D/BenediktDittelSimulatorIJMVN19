package de.dittel;

import de.dittel.controller.*;
import de.dittel.model.Automaton;
import de.dittel.util.FileManager;
import de.dittel.util.ReferenceHandler;
import de.dittel.view.PopulationPanel;
import de.dittel.view.StatePanel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Main-Klasse der Anwendung
 */
public class Main extends Application {

    // VM Args: --module-path "\path\to\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml
    @Override
    public void start(Stage stage) throws Exception {
        newAutomaton(stage, null, null);
    }

    public static void main(String[] args) {
        launch(args);
        Thread dbShutdown = new Thread(() -> {
            DatabaseController databaseController = new DatabaseController(new ReferenceHandler());
            databaseController.shutdown();
        });
        Runtime.getRuntime().addShutdownHook(dbShutdown);
    }


    public static void newAutomaton(Stage stage, Automaton automaton, String name) throws IOException {
        if (stage == null) {
            stage = new Stage();
        }

        if (automaton == null) {
            FileManager.compile(FileManager.createNewAutomatonFile("DefaultAutomaton"));
            automaton = FileManager.loadAutomaton(new File("automata/DefaultAutomaton.java"));
        }

        if (name == null) {
            if (automaton != null) {
                name = automaton.getClass().getName();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ups, da ist etwas schief gelaufen.");
                alert.show();
                return;
            }
        }
        ReferenceHandler referenceHandler = new ReferenceHandler();
        referenceHandler.setMainStage(stage);
        referenceHandler.setAutomaton(automaton);
        MainController mainController = new MainController();             // controller
        FXMLLoader mainLoader = new FXMLLoader(Main.class.getResource("/fxml/main.fxml")); // view
        mainLoader.setController(mainController);
        Parent root = mainLoader.load();
        referenceHandler.setMainController(mainController);
        StatePanel statePanel = new StatePanel(referenceHandler, mainController);
        referenceHandler.setStatePanel(statePanel);
        PopulationPanel populationPanel = new PopulationPanel(referenceHandler, mainController);
        referenceHandler.setPopulationPanel(populationPanel);
        new PopulationPanelController(referenceHandler, mainController);  // controller
        new SimulationController(referenceHandler, mainController);       // controller
        new SerializationController(referenceHandler);                    // controller
        new XMLSerializationController(referenceHandler);                 // controller
        new EditorController(referenceHandler);                           // controller
        new NewAutomatonDialogController(referenceHandler);               // controller
        new LanguageController(referenceHandler);                         // controller
        new ChangeSizeDialogController(referenceHandler);                 // controller
        DatabaseController databaseController = new DatabaseController(referenceHandler); // controller
        databaseController.init();
        mainController.init(referenceHandler);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/style.css");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setTitle(name);
        stage.setOnCloseRequest(event -> mainController.getStopSimulationButton().fire());
        stage.show();
    }
}
