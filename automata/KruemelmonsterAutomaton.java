import de.dittel.model.Automaton;
import de.dittel.model.Cell;
import de.dittel.model.Callable;

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

    @Callable
    public void kruemelmonsterTest(int row, int column)  throws IllegalAccessException{
        if (row + 9 < getNumberOfRows() && column + 9 < getNumberOfColumns()) {
            setState(row, column, 6);
            setState(row, column + 6, 3);
            setState(row + 1, column + 1, 1);
            setState(row + 7, column + 6, 7);
            setState(row + 7, column + 1, 4);
            setState(row + 8, column + 7, 5);
            setState(row + 8, column + 5, 2);
            setState(row + 9, column + 9, 8);
        } else {
            throw new IllegalAccessException();
        }
    }
}
