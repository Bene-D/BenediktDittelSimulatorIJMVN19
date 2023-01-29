package de.dittel.util;

import de.dittel.controller.MainController;
import de.dittel.model.Automaton;
import de.dittel.view.PopulationPanel;
import de.dittel.view.StatePanel;
import javafx.stage.Stage;

/**
 * Klasse die alle wichtigen Ressourcen im Projekt verwaltet
 */
public class ReferenceHandler implements Observer {

    private Automaton automaton = null;
    private StatePanel statePanel = null;
    private PopulationPanel populationPanel = null;
    private MainController mainController = null;
    private Stage mainStage = null;
    private boolean automatonHasChanged;

    /**
     * Getter für automaton
     */
    public Automaton getAutomaton() {
        return automaton;
    }

    /**
     * Setter für den automaton
     * <p>
     * Informiert alle Observer bei Änderungen
     */
    public void setAutomaton(Automaton automaton) {
        if (this.automaton != null) {
            automatonHasChanged = true;
            automaton.clear();
        }
        this.automaton = automaton;
        this.automaton.add(this);
        this.automaton.notifyObserver();
    }

    /**
     * Setter für das statePanel
     */
    public void setStatePanel(StatePanel statePanel) {
        this.statePanel = statePanel;
    }

    /**
     * Getter für das populationPanel
     */
    public PopulationPanel getPopulationPanel() {
        return populationPanel;
    }

    /**
     * Setter für das populationPanel
     */
    public void setPopulationPanel(PopulationPanel populationPanel) {
        this.populationPanel = populationPanel;
    }

    /**
     * Getter für den mainController
     */
    public MainController getMainController() {
        return mainController;
    }

    /**
     * Setter für den mainController
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Getter für die mainStage
     */
    public Stage getMainStage() {
        return mainStage;
    }

    /**
     * Setter für die mainStage
     */
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    /**
     * Update-Methode, die durch das Observable ausgelöst wird
     */
    @Override
    public void update() {
        if (populationPanel != null) {
            populationPanel.update();
        }
        if (automatonHasChanged && statePanel != null) {
            statePanel.update();
            mainController.setTorus();
            automatonHasChanged = false;
        }
    }
}
