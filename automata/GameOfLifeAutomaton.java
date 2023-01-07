import de.dittel.model.Automaton;
import de.dittel.model.Cell;
import de.dittel.model.Callable;

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
    }

    /**
     * Default-Konstruktor
     */
    public GameOfLifeAutomaton() {
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

    @Callable
    public void setGlider(int row, int column) throws IllegalAccessException {
        if (row + 2 < getNumberOfRows() && column - 1 >= 0 && column + 1 < getNumberOfColumns()) {
            setState(row, column, 1);
            setState(row + 1, column + 1, 1);
            setState(row + 2, column - 1, row + 2, column + 1, 1);
        } else {
            throw new IllegalAccessException();
        }
    }

    @Callable
    public void setOscillator(int row, int column) throws IllegalAccessException {
        if (column + 2 < getNumberOfColumns()) {
            setState(row, column, row, column + 2, 1);
        } else {
            throw new IllegalAccessException();
        }
    }
}
