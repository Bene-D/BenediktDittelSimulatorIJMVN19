package de.dittel;

import de.dittel.controller.PopulationPanelController;
import de.dittel.model.Automaton;
import de.dittel.model.KruemelmonsterAutomaton;
import de.dittel.controller.MainController;
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
        MainController mainController = new MainController();
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/main2.fxml"));
        mainLoader.setController(mainController);
        Parent root = mainLoader.load();
        mainController.initialize(automaton);
        PopulationPanel populationPanel = new PopulationPanel(automaton, mainController.getColorPickerList());
        new PopulationPanelController(populationPanel, mainController);

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