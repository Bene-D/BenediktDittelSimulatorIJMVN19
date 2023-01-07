package de.dittel.view2;

import de.dittel.model.Automaton;
import de.dittel.util.Observer;

/**
 * View-Klasse eines GameOfLife-Automaten für die Konsole
 */
@SuppressWarnings("unused")
public class GameOfLiveConsole implements Observer {

    private final Automaton automaton;

    /**
     * Konstruktor
     *
     * @param automaton Automat, der abgebildet werden soll
     */
    public GameOfLiveConsole(Automaton automaton) {
        this.automaton = automaton;
        automaton.add(this);
        print();
    }

    /**
     * Zeichnet die Population des Automaten in der Konsole
     */
    public void print() {
        for (int r = 0; r < automaton.getNumberOfRows(); r++) {
            for (int c = 0; c < automaton.getNumberOfColumns(); c++) {
                if (automaton.getCell(r, c).getState() == 0) {
                    System.out.print(".");
                } else {
                    System.out.print("*");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Zeichnet nach Benachrichtigung des Observable die Population des Models in die Konsole
     */
    @Override
    public void update() {
        print();
    }
}
