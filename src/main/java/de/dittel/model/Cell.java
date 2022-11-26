package de.dittel.model;

/**
 * Klasse zur Repräsentation einer Zelle eines zellulären Automaten
 */
public class Cell {
    private int state;

    /**
     * Initialisiert die Zelle im Zustand 0
     */
    public Cell() {
        state = 0;
    }

    /**
     * Initialisiert die Zelle im übergebenen Zustand
     *
     * @param state, Zustand der neuen Zelle
     */
    public Cell(int state) {
        this.state = state;
    }

    /**
     * Copy-Konstruktor; initialisiert die Zelle mit dem Zustand der übergebenen Zelle
     *
     * @param cell, Zelle die verändert wird
     */
    public Cell(Cell cell) {
        state = cell.getState();
    }

    /**
     * Liefert den Zustand der Zelle
     *
     * @return Zustand der Zelle
     */
    public int getState() {
        return state;
    }

    /**
     * Ändert den Zustand der Zelle
     *
     * @param state der neue Zustand der Zelle
     */
    void setState(int state) {
        this.state = state;
    }
}
