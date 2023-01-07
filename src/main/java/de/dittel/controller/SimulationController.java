package de.dittel.controller;

import de.dittel.util.ReferenceHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.StringConverter;

/**
 * Controller-Klasse für die Simulation eines Automaten
 */
public class SimulationController {

    private volatile int speed;
    private SimulationThread simulationThread;
    private final ReferenceHandler referenceHandler;
    private final MainController mainController;

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen (Model)
     * @param mainController Hauptcontroller der View, zum Verwalten der FXML-Elemente
     */
    public SimulationController(ReferenceHandler referenceHandler, MainController mainController) {
        this.referenceHandler = referenceHandler;
        this.mainController = mainController;
        this.speed = 1400;

        mainController.getStartSimulationButton().setOnAction(e -> startSimulation());
        mainController.getStopSimulationButton().setOnAction(e -> stopSimulation());
        mainController.getStartSimulationMenuItem().setOnAction(e -> startSimulation());
        mainController.getStopSimulationMenuItem().setOnAction(e -> stopSimulation());

        mainController.getSimulationSpeedSlider().setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double sliderSpeed) {
                if (sliderSpeed < 500) {
                    return "Schnell";
                } else if (sliderSpeed < 1100) {
                    return "Schneller";
                } else if (sliderSpeed < 1700) {
                    return "Standard";
                } else if(sliderSpeed < 2300) {
                    return "Langsamer";
                } else {
                    return "Langsam";
                }
            }

            @Override
            public Double fromString(String sliderOption) {
                return switch (sliderOption) {
                    case "Langsam" -> 2600d;
                    case "Langsamer" -> 2000d;
                    case "Schneller" -> 800d;
                    case "Schnell" -> 200d;
                    default -> 1400d;
                };
            }
        });

        mainController.getSimulationSpeedSlider().valueProperty().addListener((obs, o, n) -> speed = n.intValue());
        simulationThread = null;
    }

    /**
     * Startet einen neuen Simulationsthread und passt die Sichtbarkeit der Buttons in der View an
     */
    @FXML
    private void startSimulation() {
        mainController.getStartSimulationButton().setDisable(true);
        mainController.getStopSimulationButton().setDisable(false);
        mainController.getSingleStepSimulationButton().setDisable(true);
        mainController.getStartSimulationMenuItem().setDisable(true);
        mainController.getStopSimulationMenuItem().setDisable(false);
        mainController.getSingleStepSimulationMenuItem().setDisable(true);
        if (simulationThread == null) {
            simulationThread = new SimulationThread();
            simulationThread.setDaemon(true);
            simulationThread.start();
        }
    }

    /**
     * Stoppt den aktuellen Simulationsthread und passt die Sichtbarkeit der Buttons in der View an
     */
    @FXML
    private void stopSimulation() {
        if (simulationThread != null) {
            simulationThread.interrupt();
        }
        try {
            simulationThread.join();
        } catch (InterruptedException e) {
            SimulationThread.currentThread().interrupt();
        }
        mainController.getStartSimulationButton().setDisable(false);
        mainController.getStopSimulationButton().setDisable(true);
        mainController.getSingleStepSimulationButton().setDisable(false);
        mainController.getStartSimulationMenuItem().setDisable(false);
        mainController.getStopSimulationMenuItem().setDisable(true);
        mainController.getSingleStepSimulationMenuItem().setDisable(false);
        simulationThread = null;
    }

    /**
     * Thread-Klasse, die neue Generationen für die Population des Automaten erzeugt
     * <p>
     * Zwischen dem Erzeugen der Generationen wird der Thread schlafen gelegt.
     * Die Dauer hängt von der Einstellung des SimulationSpeedSliders ab.
     */
    class SimulationThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    referenceHandler.getAutomaton().nextGeneration();
                    Thread.sleep((long) (speed*0.75));
                } catch (InterruptedException e) {
                    interrupt();
                }
                catch (Throwable e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Laufzeitfehler in der Transform-Methode: " + e,
                            ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    }
}
