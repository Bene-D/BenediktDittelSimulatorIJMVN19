package de.dittel.model;

import java.util.List;
import java.util.Scanner;

/**
 * Klasse die einen Game-of-Life-Automaten mit seinen Eigenschaften implementiert
 */
public class GameOfLifeAutomaton extends Automaton {

    /**
     * Konstruktor
     *
     * @param rows                Anzahl an Reihen
     * @param columns             Anzahl an Spalten
     * @param isTorus             true, falls die Zellen als Torus betrachtet werden
     */
    public GameOfLifeAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, 2, true, isTorus);
//        setState(0, 0, 0, 2, 1);
        setState(1, 3, 1);
        setState(3, 2, 1);
        setState(3, 3, 1);
        setState(3, 4, 1);
        setState(2, 4, 1);
    }

    /**
     * Default-Konstruktor
     */
    public GameOfLifeAutomaton() {
        this(50, 50, true);
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
        int neighborsAlive = 0;

        for (Cell neighbor : neighbors)
            if (neighbor.getState() == 1)
                neighborsAlive += 1;

        if (cell.getState() == 0) {
            if (neighborsAlive == 3)
                return new Cell(1);
        } else
            if (neighborsAlive < 2 || neighborsAlive > 3)
                return new Cell(0);

        return cell;
    }

    /**
     * Methode zum Testen des Automaten in der Konsole
     */
    public static void main(String[] args) {
        int i = 100;
        try (Scanner scanner = new Scanner(System.in)) {
            GameOfLifeAutomaton gameOfLifeAutomaton = new GameOfLifeAutomaton(5, 5, true);
            gameOfLifeAutomaton.print();
            while (i>0) {
                System.out.println("Drücke eine Taste für die nächste Generation!");
                scanner.next();
                gameOfLifeAutomaton.nextGeneration();
                gameOfLifeAutomaton.print();
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
                if (getCell(r, c).getState() == 0) {
                    System.out.print(".");
                } else {
                    System.out.print("*");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
