package de.dittel.model;

import java.util.List;

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
    public KruemelmonsterAutomaton(int rows, int columns, int numberOfStates, boolean isTorus) {
        super(rows, columns, numberOfStates, false, isTorus);
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
        this(100, 100, 10, true);
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
        int stateToCheck = (cell.getState() + 1) % getNumberOfStates();

        for (Cell neighbor : neighbors)
            if (neighbor.getState() == stateToCheck)
                return new Cell(stateToCheck);

        return cell;
    }
}
