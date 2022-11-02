package de.dittel;

/**
 * Abstrakte Klasse zur Repräsentation eines zellulären Automaten
 */
public abstract class Automaton {

    private int rows;
    private int columns;
    private final int numberOfStates;
    private boolean isMooreNeighborHood;
    private boolean isTorus;

    private Cell[][] population;

    /**
     * Konstruktor
     *
     * @param rows Anzahl an Reihen
     * @param columns Anzahl an Spalten
     * @param numberOfStates Anzahl an Zuständen; die Zustände des Automaten sind dann die Werte 0 bis numberOfStates -1
     * @param isMooreNeighborHood true, falls der Automat die Moore-Nachbarschaft benutzt;
     *                            false, falls der Automat die von-Neumann-Nachbarschaft benutzt
     * @param isTorus true, falls die Zellen als Torus betrachtet werden
     */
    public Automaton(int rows, int columns, int numberOfStates,
                     boolean isMooreNeighborHood, boolean isTorus) {
        this.rows = rows;
        this.columns = columns;
        this.numberOfStates = numberOfStates;
        this.isMooreNeighborHood = isMooreNeighborHood;
        this.isTorus = isTorus;
        population = new Cell[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                population[r][c] = new Cell();
            }
        }
    }

    /**
     * Implementierung der Transformationsregel
     *
     * @param cell die betroffene Zelle (darf nicht verändert werden!!!)
     * @param neighbors die Nachbarn der betroffenen Zelle (dürfen nicht verändert werden!!!)
     * @return eine neu erzeugte Zelle, die gemäß der Transformationsregel aus der betroffenen Zelle hervorgeht
     * @throws Throwable moeglicherweise wirft die Methode eine Exception
     */
    protected abstract Cell transform(Cell cell, Cell[] neighbors)
            throws Throwable;

    /**
     * Liefert die Anzahl an Zuständen des Automaten; gültige Zustände sind int-Werte zwischen 0 und Anzahl -1
     *
     * @return die Anzahl an Zuständen des Automaten
     */
    public int getNumberOfStates() {
        return numberOfStates;
    }

    /**
     * Liefert die Anzahl an Reihen
     *
     * @return die Anzahl an Reihen
     */
    public int getNumberOfRows() {
        return rows;
    }

    /**
     * Liefert die Anzahl an Spalten
     *
     * @return die Anzahl an Spalten
     */
    public int getNumberOfColumns() {
        return columns;
    }

    /**
     * Ändert die Größe des Automaten; Achtung: aktuelle Belegungen nicht gelöschter Zellen sollen beibehalten werden;
     * neue Zellen sollen im Zustand 0 erzeugt werden
     *
     * @param rows die neue Anzahl an Reihen
     * @param columns die neue Anzahl an Spalten
     */
    public void changeSize(int rows, int columns) {
        Cell[][] newPopulation = new Cell[rows][columns];

        if (rows > this.rows || columns > this.columns) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++)
                    newPopulation[r][c] = population[r][c];
            }
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if (newPopulation[r][c] == null) {
                        newPopulation[r][c] = new Cell();
                    }
                }
            }
        } else {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++)
                    newPopulation[r][c] = population[r][c];
            }
        }
        population = newPopulation;
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * Liefert Informationen, ob der Automat als Torus betrachtet wird
     *
     * @return true, falls der Automat als Torus betrachtet wird; false sonst
     */
    public boolean isTorus() {
        return isTorus;
    }

    /**
     * Ändert die Torus-Eigenschaft des Automaten
     *
     * @param isTorus true, falls der Automat als Torus betrachtet wird; false sonst
     */
    public void setTorus(boolean isTorus) {
        this.isTorus = isTorus;
    }

    /**
     * Liefert Informationen über die Nachbarschaft-Eigenschaft des Automaten
     * (Hinweis: Die Nachbarschaftseigenschaft kann nicht verändert werden)
     *
     * @return true, falls der Automat die Moore-Nachbarschaft berücksicht;
     * false, falls er die von-Neumann-Nachbarschaft berücksichtigt
     */
    public boolean isMooreNeighborHood() {
        return isMooreNeighborHood;
    }

    /**
     * setzt alle Zellen in den Zustand 0
     */
    public void clearPopulation() {}

    /**
     * setzt für jede Zelle einen zufällig erzeugten Zustand
     */
    public void randomPopulation() {}

    /**
     * Liefert eine Zelle des Automaten
     *
     * @param row Reihe der Zelle
     * @param column Spalte der Zelle
     * @return Cell-Objekt an Position row/column
     */
    public Cell getCell(int row, int column) {
        return new Cell();
    }

    /**
     * Aendert den Zustand einer Zelle
     *
     * @param row Reihe der Zelle
     * @param column Spalte der Zelle
     * @param state neuer Zustand der Zelle
     */
    public void setState(int row, int column, int state) {}

    /**
     * Aendert den Zustand eines ganzen Bereichs von Zellen
     *
     * @param fromRow Reihe der obersten Zelle
     * @param fromColumn Spalte der obersten Zelle
     * @param toRow Reihe der untersten Zelle
     * @param toColumn Spalte der untersten Zelle
     * @param state neuer Zustand der Zellen
     */
    public void setState(int fromRow, int fromColumn, int toRow,
                         int toColumn, int state) {}

    /**
     * überführt den Automaten in die nächste Generation;
     * ruft dabei die abstrakte Methode "transform" für alle Zellen auf;
     * Hinweis: zu berücksichtigen sind die Nachbarschaftseigenschaft und die Torus-Eigenschaft des Automaten
     *
     * @throws Throwable Exceptions der transform-Methode werden weitergeleitet
     */
    public void nextGeneration() throws Throwable {}
}

