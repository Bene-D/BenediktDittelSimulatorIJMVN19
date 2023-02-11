package de.dittel.controller;

import de.dittel.util.ReferenceHandler;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;

/**
 * Controller-Klasse für die Simulation eines Automaten
 */
public class SimulationController {

    public static final int DEF_SPEED = 1800;
    public static final int MIN_SPEED = 200;
    public static final int MAX_SPEED = 3400;
    private static final int FASTER_SPEED = 1000;
    private static final int SLOWER_SPEED = 2600;
    private volatile double speed;
    private SimulationThread simulationThread;
    private final ReferenceHandler referenceHandler;
    private final MainController mainController;
    private final ResourcesController resourcesController = ResourcesController.getResourcesController();

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen (Model)
     */
    public SimulationController(ReferenceHandler referenceHandler) {
        this.referenceHandler = referenceHandler;
        this.mainController = referenceHandler.getMainController();

        mainController.getStartSimulationButton().setOnAction(e -> startSimulation());
        mainController.getStopSimulationButton().setOnAction(e -> stopSimulation());
        mainController.getStartSimulationMenuItem().setOnAction(e -> startSimulation());
        mainController.getStopSimulationMenuItem().setOnAction(e -> stopSimulation());

        int userSpeed = calculateSliderSpeed();
        speed = userSpeed;
        mainController.getSimulationSpeedSlider().setValue(userSpeed);

        simulationThread = null;
    }

    /**
     * Startet einen neuen Simulationsthread und passt die Sichtbarkeit der Buttons in der View an
     */
    private void startSimulation() {
        mainController.getStartSimulationButton().setDisable(true);
        mainController.getStopSimulationButton().setDisable(false);
        mainController.getSimulationStepButton().setDisable(true);
        mainController.getStartSimulationMenuItem().setDisable(true);
        mainController.getStopSimulationMenuItem().setDisable(false);
        mainController.getSimulationStepMenuItem().setDisable(true);

        if (simulationThread == null) {
            simulationThread = new SimulationThread();
            simulationThread.setDaemon(true);
            simulationThread.start();
        }
    }

    /**
     * Stoppt den aktuellen Simulationsthread und passt die Sichtbarkeit der Buttons in der View an
     */
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
        mainController.getSimulationStepButton().setDisable(false);
        mainController.getStartSimulationMenuItem().setDisable(false);
        mainController.getStopSimulationMenuItem().setDisable(true);
        mainController.getSimulationStepMenuItem().setDisable(false);
        simulationThread = null;
    }

    /**
     * Methode zum Erzeugen eines Sliders für die Simulation
     * <p>
     * Wird zur Internationalisierung benötigt, da der Slider bei Anpassung der Sprache
     * jedes Mal neu erzeugt werden muss.
     */
    public void createSlider(MainController mainController) {
        // Problem: Label werden einmal gezeichnet!

        mainController.getToolBar().getItems().remove(16);
        Slider slider = new Slider();
        slider.setMin(MIN_SPEED);
        slider.setMax(MAX_SPEED);
        slider.setValue(speed);
        slider.setMinorTickCount(0);
        slider.setMajorTickUnit(800);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        slider.labelFormatterProperty().bind(Bindings.createObjectBinding(() -> new StringConverter<>() {
            @Override
            public String toString(Double sliderSpeed) {
                if (sliderSpeed < 600) {
                    return resourcesController.getI18nValue("sliderLabelVeryFast");
                } else if (sliderSpeed < 1400) {
                    return resourcesController.getI18nValue("sliderLabelFast");
                } else if (sliderSpeed < 2200) {
                    return resourcesController.getI18nValue("sliderLabelStandard");
                } else if (sliderSpeed < 3000) {
                    return resourcesController.getI18nValue("sliderLabelSlow");
                } else {
                    return resourcesController.getI18nValue("sliderLabelVerySlow");
                }
            }

            @Override
            public Double fromString(String s) {
                throw new UnsupportedOperationException(); // Methode wird nicht benutzt
            }
        }));

        slider.setPrefHeight(38);
        slider.setPrefWidth(245);
        slider.setPadding(new Insets(0, 0, 0, 15));
        slider.setTooltip(new Tooltip(resourcesController.getI18nValue("sliderTooltip")));
        slider.valueProperty().addListener((obs, o, n) -> speed = n.intValue());
        mainController.getToolBar().getItems().add(slider);
    }

    /**
     * Helfermethode, um userSpeed einer validen Slideroption zuzuordnen
     */
    private int calculateSliderSpeed() {
        int userSpeed = PropertiesController.getPropertiesController().getSpeed();
        if (userSpeed <= 600) {
            userSpeed = MIN_SPEED;
        } else if (userSpeed <= 1400) {
            userSpeed = FASTER_SPEED;
        } else if (userSpeed <= 2200) {
            userSpeed = DEF_SPEED;
        } else if (userSpeed <= 3000) {
            userSpeed = SLOWER_SPEED;
        } else {
            userSpeed = MAX_SPEED;
        }

        return userSpeed;
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
                    Thread.sleep((long) (speed*0.40));
                } catch (InterruptedException e) {
                    interrupt();
                }
                catch (Throwable e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            resourcesController.getI18nValue("threadError") + "\n" + e,
                            ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    }
}