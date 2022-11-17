package de.dittel.automaton;

import java.util.List;
import java.util.Scanner;

/**
 * Klasse die einen Kruemelmonster-Automaten mit seinen Eigenschaften implementiert
 */
public class KruemelmonsterAutomaton extends Automaton {

    /**
     * Konstruktor
     *
     * @param rows                Anzahl an Reihen
     * @param columns             Anzahl an Spalten
     * @param isTorus             true, falls die Zellen als Torus betrachtet werden
     */
    public KruemelmonsterAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, 10, false, isTorus);
        setState(7, 6, 7);
        setState(0, 6, 3);
        setState(8, 7, 5);
        setState(8, 5, 2);
        setState(7, 1, 4);
        setState(9, 9, 8);
        setState(1, 1, 1);
        setState(0, 0, 6);

    }

    /**
     * Default-Konstruktor
     */
    public KruemelmonsterAutomaton() {
        this(100, 100, true);
    }

    /**
     * Implementierung der Transformationsregel
     *
     * @param cell      die betroffene Zelle (darf nicht verändert werden!!!)
     * @param neighbors die Nachbarn der betroffenen Zelle (dürfen nicht verändert werden!!!)
     * @return eine neu erzeugte Zelle, die gemäß der Transformationsregel aus der betroffenen Zelle hervorgeht
     */
    @Override
    protected Cell transform(Cell cell, List<Cell> neighbors) {
        int stateToCheck = cell.getState() + 1;
        if (stateToCheck == getNumberOfStates())
            stateToCheck = 0;

        for (Cell neighbor : neighbors)
            if (neighbor.getState() == stateToCheck)
                return new Cell(stateToCheck);

        return cell;
    }

    /**
     * Methode zum Testen des Automaten in der Konsole
     */
    public static void main(String[] args) {
        int i = 10;
        try (Scanner scanner = new Scanner(System.in)) {
            KruemelmonsterAutomaton kruemelmonsterAutomaton = new KruemelmonsterAutomaton(9, 13, false);
            kruemelmonsterAutomaton.print();
            while (i>0) {
                System.out.println("Drücke eine Taste für die nächste Generation!");
                scanner.next();
                kruemelmonsterAutomaton.nextGeneration();
                kruemelmonsterAutomaton.print();
                i--;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Methode zum Testen des Automaten in der Konsole
     */
    public void print() {
        for (int r = 0; r < getNumberOfRows(); r++) {
            for (int c = 0; c < getNumberOfColumns(); c++) {
                switch (getCell(r, c).getState()) {
                    case 0:
                        System.out.print("0");
                        break;
                    case 1:
                        System.out.print("1");
                        break;
                    case 2:
                        System.out.print("2");
                        break;
                    case 3:
                        System.out.print("3");
                        break;
                    case 4:
                        System.out.print("4");
                        break;
                    case 5:
                        System.out.print("5");
                        break;
                    case 6:
                        System.out.print("6");
                        break;
                    case 7:
                        System.out.print("7");
                        break;
                    case 8:
                        System.out.print("8");
                        break;
                    case 9:
                        System.out.print("9");
                        break;
                    default:
                        System.out.print("*");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
