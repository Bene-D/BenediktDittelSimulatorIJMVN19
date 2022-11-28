package de.dittel;

import de.dittel.controller.MainController;
import de.dittel.controller.PopulationPanelController;
import de.dittel.controller.SimulationController;
import de.dittel.model.Automaton;
import de.dittel.model.KruemelmonsterAutomaton;
import de.dittel.view.PopulationPanel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // VM Args: --module-path "\path\to\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml

    @Override
    public void start(Stage stage) throws Exception {
        Automaton automaton = new KruemelmonsterAutomaton(10, 10, 10, true);
        MainController mainController = new MainController(automaton);
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        mainLoader.setController(mainController);
        Parent root = mainLoader.load();
        mainController.init();
        PopulationPanel populationPanel = new PopulationPanel(automaton, mainController);
        new PopulationPanelController(populationPanel, mainController);
        new SimulationController(automaton, mainController);

//        new KruemelmonsterConsole(automaton);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/style.css");
        stage.setScene(scene);
        stage.setTitle("Zellulaerer Automat");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}