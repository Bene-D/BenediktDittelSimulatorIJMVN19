package de.dittel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstrakte Klasse zur Repräsentation eines zellulären Automaten
 */
public abstract class Automaton {

    protected int rows;
    protected int columns;
    protected final int numberOfStates;
    protected final boolean isMooreNeighborHood;
    protected boolean isTorus;

    protected Cell[][] population;
    Random random = new Random();

    /**
     * Konstruktor
     *
     * @param rows                Anzahl an Reihen
     * @param columns             Anzahl an Spalten
     * @param numberOfStates      Anzahl an Zuständen; die Zustände des Automaten sind dann die Werte 0 bis numberOfStates -1
     * @param isMooreNeighborHood true, falls der Automat die Moore-Nachbarschaft benutzt;
     *                            false, falls der Automat die von-Neumann-Nachbarschaft benutzt
     * @param isTorus             true, falls die Zellen als Torus betrachtet werden
     */
    protected Automaton(int rows, int columns, int numberOfStates,
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
     * @param cell      die betroffene Zelle (darf nicht verändert werden!!!)
     * @param neighbors die Nachbarn der betroffenen Zelle (dürfen nicht verändert werden!!!)
     * @return eine neu erzeugte Zelle, die gemäß der Transformationsregel aus der betroffenen Zelle hervorgeht
     * @throws Throwable moeglicherweise wirft die Methode eine Exception
     */
    protected abstract Cell transform(Cell cell, List<Cell> neighbors)
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
     * @param rows    die neue Anzahl an Reihen
     * @param columns die neue Anzahl an Spalten
     */
    public void changeSize(int rows, int columns) {
        Cell[][] newPopulation = new Cell[rows][columns];

        for (int r = 0; r < rows; r++) {
            System.arraycopy(population[r], 0, newPopulation[r], 0, columns);
        }

        if (rows > this.rows || columns > this.columns) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if (newPopulation[r][c] == null) {
                        newPopulation[r][c] = new Cell();
                    }
                }
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
     * Setzt alle Zellen in den Zustand 0
     */
    public void clearPopulation() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                population[r][c].setState(0);
            }
        }
    }

    /**
     * Setzt für jede Zelle einen zufällig erzeugten Zustand
     */
    public void randomPopulation() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                population[r][c].setState(random.nextInt(numberOfStates));
            }
        }
    }

    /**
     * Liefert eine Zelle des Automaten
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @return Cell-Objekt an Position row/column
     */
    public Cell getCell(int row, int column) {
        return population[row][column];
    }

    /**
     * Aendert den Zustand einer Zelle
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @param state  neuer Zustand der Zelle
     */
    public void setState(int row, int column, int state) {
        population[row][column].setState(state);
    }

    /**
     * Aendert den Zustand eines ganzen Bereichs von Zellen
     *
     * @param fromRow    Reihe der obersten Zelle
     * @param fromColumn Spalte der obersten Zelle
     * @param toRow      Reihe der untersten Zelle
     * @param toColumn   Spalte der untersten Zelle
     * @param state      neuer Zustand der Zellen
     */
    public void setState(int fromRow, int fromColumn, int toRow,
                         int toColumn, int state) {
        for (; fromRow <= toRow; fromRow++) {
            for (int i = fromColumn; i <= toColumn; i++) {
                population[fromRow][i].setState(state);
            }
        }
    }

    /**
     * überführt den Automaten in die nächste Generation;
     * ruft dabei die abstrakte Methode "transform" für alle Zellen auf;
     * Hinweis: zu berücksichtigen sind die Nachbarschaftseigenschaft und die Torus-Eigenschaft des Automaten
     *
     * @throws Throwable Exceptions der transform-Methode werden weitergeleitet
     */
    public void nextGeneration() throws Throwable {
        Cell[][] nextGeneration = new Cell[rows][columns];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                nextGeneration[r][c] = transform(population[r][c], getNeighbors(r, c));
            }
        }
        population = nextGeneration;
    }

    /**
     * Erstellt eine Liste von Nachbarn für eine Zelle
     *
     * @param row Reihe/x-Koordinate der Zelle
     * @param column Spalte/y-Koordinate der Zelle
     * @return Liste von allen Nachbarn entsprechend der ausgewöhlten Einstellungen
     */
    private List<Cell> getNeighbors(int row, int column) {
        int[][] vonNeumannNeighbors =                  {{row-1, column},
                                         {row, column-1},               {row, column+1},
                                                        {row+1, column}};

        int[][] mooreNeighbors = {  {row-1, column-1}, {row-1, column}, {row-1, column+1},
                                    {row, column-1},                    {row, column+1},
                                    {row+1, column-1}, {row+1, column}, {row+1, column+1}};

        if (isTorus) {
            if (isMooreNeighborHood)
                return torusOnNeighbors(mooreNeighbors);
            else return torusOnNeighbors(vonNeumannNeighbors);
        } else {
            if (isMooreNeighborHood)
                return torusOffNeighbors(mooreNeighbors);
            else return torusOffNeighbors(vonNeumannNeighbors);
        }
    }

    /**
     * Gibt die Nachbarn einer Zelle zurück, wenn isTorus true ist
     *
     * @param neighborCoords Array mit den Koordinaten der Nachbarschaft
     * @return Liste der Nachbarn die betroffen sind
     */
    private List<Cell> torusOnNeighbors(int[][] neighborCoords) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        for (int[] neighborCoord : neighborCoords) {
            if (neighborCoord[0] < 0 && neighborCoord[1] < 0)
                neighbors.add(getCell(rows-1, columns-1));

            else if (neighborCoord[0] < 0 && neighborCoord[1] >= columns)
                neighbors.add(getCell(rows-1, 0));

            else if (neighborCoord[0] < 0)
                neighbors.add(getCell(rows-1, neighborCoord[1]));

            else if (neighborCoord[1] < 0 && neighborCoord[0] >= rows)
                neighbors.add(getCell(0, columns-1));

            else if (neighborCoord[1] < 0)
                neighbors.add(getCell(neighborCoord[0], columns-1));

            else if (neighborCoord[0] >= rows && neighborCoord[1] >= columns)
                neighbors.add(getCell(0, 0));

            else if (neighborCoord[0] >= rows)
                neighbors.add(getCell(0, neighborCoord[1]));

            else if (neighborCoord[1] >= columns)
                neighbors.add(getCell(neighborCoord[0], 0));

            else neighbors.add(getCell(neighborCoord[0], neighborCoord[1]));
        }

        return neighbors;
    }

    /**
     * Gibt die Nachbarn einer Zelle zurück, wenn isTorus false ist
     *
     * @param neighborCoords Array mit den Koordinaten der Nachbarschaft
     * @return Liste der Nachbarn die betroffen sind
     */
    private List<Cell> torusOffNeighbors(int[][] neighborCoords) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        for (int[] neighborCoord : neighborCoords) {
            if (neighborCoord[0] >= 0 && neighborCoord[1] >= 0 && neighborCoord[0] < rows && neighborCoord[1] < columns)
                neighbors.add(getCell(neighborCoord[0], neighborCoord[1]));
        }

        return neighbors;
    }
}