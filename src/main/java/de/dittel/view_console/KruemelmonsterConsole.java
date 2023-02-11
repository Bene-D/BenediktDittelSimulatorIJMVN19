package de.dittel.view_console;

import de.dittel.model.Automaton;
import de.dittel.util.Observer;

/**
 * View-Klasse eines Kruemelmonster-Automaten für die Konsole
 */
@SuppressWarnings("unused")
public class KruemelmonsterConsole implements Observer {

    private final Automaton automaton;

    /**
     * Konstruktor
     *
     * @param automaton Automat, der abgebildet werden soll
     */
    public KruemelmonsterConsole(Automaton automaton) {
        this.automaton = automaton;
        automaton.add(this);
        print();
    }

    /**
     * Zeichnet die Population des Automaten in der Konsole
     */
    private void print() {
        for (int r = 0; r < automaton.getNumberOfRows(); r++) {
            for (int c = 0; c < automaton.getNumberOfColumns(); c++) {
                switch (automaton.getCell(r, c).getState()) {
                    case 0 -> System.out.print("0");
                    case 1 -> System.out.print("1");
                    case 2 -> System.out.print("2");
                    case 3 -> System.out.print("3");
                    case 4 -> System.out.print("4");
                    case 5 -> System.out.print("5");
                    case 6 -> System.out.print("6");
                    case 7 -> System.out.print("7");
                    case 8 -> System.out.print("8");
                    case 9 -> System.out.print("9");
                    default -> System.out.print("*");
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