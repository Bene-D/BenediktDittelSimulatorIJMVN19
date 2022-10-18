package de.nachname;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {

    // VM Args: --module-path "\path\to\javafx-sdk-19\lib" --add-modules javafx.controls,javafx.fxml
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        final TextField nameText = new TextField();
        nameText.setText("dibo");
        nameText.setLayoutX(10);
        nameText.setLayoutY(10);

        final Button button = new Button();
        button.setLayoutX(160);
        button.setLayoutY(10);
        button.setText("Greet me!");

        final Label greetingLabel = new Label();
        greetingLabel.setLayoutX(10);
        greetingLabel.setLayoutY(40);

        button.setOnAction(event -> greetingLabel.setText("Hello " + nameText.getText() + "!"));

        final Group root = new Group();
        root.getChildren().addAll(nameText, button, greetingLabel);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 240, 70));
        primaryStage.show();
    }
}