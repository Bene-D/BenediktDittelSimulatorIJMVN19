package de.dittel;

public class KruemelmonsterAutomaton extends Automaton {

    public KruemelmonsterAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, 10, false, isTorus);
    }

    public KruemelmonsterAutomaton() {
        this(100, 100, true);
    }

    @Override
    protected Cell transform(Cell cell, Cell[] neighbors) {
        return null;
    }
}
