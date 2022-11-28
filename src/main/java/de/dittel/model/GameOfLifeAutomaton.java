package de.dittel.model;

import java.util.List;

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
    protected synchronized Cell transform(Cell cell, List<Cell> neighbors) {
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
}
