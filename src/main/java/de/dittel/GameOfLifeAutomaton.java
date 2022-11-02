package de.dittel;

public class GameOfLifeAutomaton extends Automaton {
    public GameOfLifeAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, 2, true, isTorus);
    }

    public GameOfLifeAutomaton() {
        this(50, 50, true);
    }

    @Override
    protected Cell transform(Cell cell, Cell[] neighbors) {
        return null;
    }
}
