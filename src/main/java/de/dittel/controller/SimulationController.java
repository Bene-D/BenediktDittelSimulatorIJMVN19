package de.dittel.controller;

import de.dittel.model.Automaton;

public class SimulationController {

    static final int DEF_SPEED = 750;
    static final int MIN_SPEED = 100;
    static final int MAX_SPEED = 2000;
    private volatile int speed;

    private SimulationThread simulationThread;
    private final Automaton automaton;
    private final MainController mainController;

    public SimulationController(Automaton automaton, MainController mainController) {
        this.automaton = automaton;
        this.mainController = mainController;
        this.speed = DEF_SPEED;

        mainController.getStartSimulationButton().setOnAction(e -> startSimulation());
        mainController.getStopSimulationButton().setOnAction(e -> stopSimulation());
        mainController.getStartSimulationMenuItem().setOnAction(e -> startSimulation());
        mainController.getStopSimulationMenuItem().setOnAction(e -> stopSimulation());

        mainController.getSimulationSpeedSlider().setMin(MIN_SPEED);
        mainController.getSimulationSpeedSlider().setMax(MAX_SPEED);
        mainController.getSimulationSpeedSlider().setValue(DEF_SPEED);

        mainController.getSimulationSpeedSlider().valueProperty().addListener((obs, o, n) -> speed = n.intValue());
        simulationThread = null;
    }

    private void startSimulation() {
        mainController.getStartSimulationButton().setDisable(true);
        mainController.getStopSimulationButton().setDisable(false);
        mainController.getSingleStepSimulationButton().setDisable(true);
        mainController.getStartSimulationMenuItem().setDisable(true);
        mainController.getStopSimulationMenuItem().setDisable(false);
        mainController.getSingleStepSimulationMenuItem().setDisable(true);
        if (simulationThread == null) {
            simulationThread = new SimulationThread();
            simulationThread.start();
        }
    }

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

    class SimulationThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    automaton.nextGeneration();
                    Thread.sleep(Math.abs(speed - MAX_SPEED - MIN_SPEED));
                } catch (InterruptedException e) {
                    interrupt();
                }
                catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
