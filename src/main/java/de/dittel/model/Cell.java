package de.dittel.model;

import java.io.Serializable;

/**
 * Klasse zur Repräsentation einer Zelle eines zellulären Automaten
 */
public class Cell implements Serializable {
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
     * @param state Zustand der neuen Zelle
     */
    @SuppressWarnings("unused")
    public Cell(int state) {
        this.state = state;
    }

    /**
     * Copy-Konstruktor; initialisiert die Zelle mit dem Zustand der übergebenen Zelle
     *
     * @param cell Zelle die verändert wird
     */
    @SuppressWarnings("unused")
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
