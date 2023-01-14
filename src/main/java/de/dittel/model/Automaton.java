package de.dittel.model;

import de.dittel.util.Observable;

import java.util.*;

/**
 * Abstrakte Klasse zur Repräsentation eines zellulären Automaten
 */
public abstract class Automaton extends Observable {

    private int rows;
    private int columns;
    private final int numberOfStates;
    private final boolean isMooreNeighborHood;
    private boolean isTorus;

    private transient Cell[][] population;
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
     * Liefert die Anzahl an Zuständen des Automaten; gültige Zustände sind int-Werte zwischen 0 und Anzahl -1
     *
     * @return die Anzahl an Zuständen des Automaten
     */
    public synchronized int getNumberOfStates() {
        return numberOfStates;
    }

    /**
     * Liefert die Anzahl an Reihen
     *
     * @return die Anzahl an Reihen
     */
    public synchronized int getNumberOfRows() {
        return rows;
    }

    /**
     * Liefert die Anzahl an Spalten
     *
     * @return die Anzahl an Spalten
     */
    public synchronized int getNumberOfColumns() {
        return columns;
    }

    /**
     * Getter für population
     */
    public Cell[][] getPopulation() {
        return population;
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
     * Ändert die Größe des Automaten; Achtung: aktuelle Belegungen nicht gelöschter Zellen sollen beibehalten werden;
     * neue Zellen sollen im Zustand 0 erzeugt werden
     *
     * @param rows    die neue Anzahl an Reihen
     * @param columns die neue Anzahl an Spalten
     */
    public void changeSize(int rows, int columns) {
        synchronized (this) {
            Cell[][] newPopulation = new Cell[rows][columns];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if (r < this.rows && c < this.columns) {
                        newPopulation[r][c] = population[r][c];
                    } else {
                        newPopulation[r][c] = new Cell();
                    }
                }
            }

            population = newPopulation;
            this.rows = rows;
            this.columns = columns;
        }
        notifyObserver();
    }

    /**
     * Liefert Informationen, ob der Automat als Torus betrachtet wird
     *
     * @return true, falls der Automat als Torus betrachtet wird; false sonst
     */
    public synchronized boolean isTorus() {
        return isTorus;
    }

    /**
     * Ändert die Torus-Eigenschaft des Automaten
     *
     * @param isTorus true, falls der Automat als Torus betrachtet wird; false sonst
     */
    public synchronized void setTorus(boolean isTorus) {
        this.isTorus = isTorus;
    }

    /**
     * Liefert Informationen über die Nachbarschaft-Eigenschaft des Automaten
     * (Hinweis: Die Nachbarschaftseigenschaft kann nicht verändert werden)
     *
     * @return true, falls der Automat die Moore-Nachbarschaft berücksicht;
     * false, falls er die von-Neumann-Nachbarschaft berücksichtigt
     */
    @SuppressWarnings("unused")
    public synchronized boolean isMooreNeighborHood() {
        return isMooreNeighborHood;
    }

    /**
     * Setzt alle Zellen in den Zustand 0
     */
    public void clearPopulation() {
        synchronized (this) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    population[r][c].setState(0);
                }
            }
        }
        notifyObserver();
    }

    /**
     * Setzt für jede Zelle einen zufällig erzeugten Zustand
     */
    public void randomPopulation() {
        synchronized (this) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    population[r][c].setState(random.nextInt(numberOfStates));
                }
            }
        }
        notifyObserver();
    }

    /**
     * Liefert eine Zelle des Automaten
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @return Cell-Objekt an Position row/column
     */
    public synchronized Cell getCell(int row, int column) {
        return population[row][column];
    }

    /**
     * Ändert den Zustand einer Zelle
     *
     * @param row    Reihe der Zelle
     * @param column Spalte der Zelle
     * @param state  neuer Zustand der Zelle
     */
    public void setState(int row, int column, int state) {
        synchronized (this) {
            population[row][column].setState(state);
        }
        notifyObserver();
    }

    /**
     * Ändert den Zustand eines ganzen Bereichs von Zellen
     *
     * @param fromRow    Reihe der obersten Zelle
     * @param fromColumn Spalte der obersten Zelle
     * @param toRow      Reihe der untersten Zelle
     * @param toColumn   Spalte der untersten Zelle
     * @param state      neuer Zustand der Zellen
     */
    public void setState(int fromRow, int fromColumn, int toRow,
                         int toColumn, int state) {
        synchronized (this) {
            for (; fromRow <= toRow; fromRow++) {
                for (int i = fromColumn; i <= toColumn; i++) {
                    population[fromRow][i].setState(state);
                }
            }
        }
        notifyObserver();
    }

    /**
     * überführt den Automaten in die nächste Generation;
     * ruft dabei die abstrakte Methode "transform" für alle Zellen auf;
     * Hinweis: zu berücksichtigen sind die Nachbarschaftseigenschaft und die Torus-Eigenschaft des Automaten
     *
     * @throws Throwable Exceptions der transform-Methode werden weitergeleitet
     */
    public void nextGeneration() throws Throwable {
        synchronized (this) {
            Cell[][] nextGeneration = new Cell[rows][columns];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    nextGeneration[r][c] = transform(population[r][c], getNeighbors(r, c));
                }
            }
            population = nextGeneration;
        }
        notifyObserver();
    }

    /**
     * Erstellt eine Liste von Nachbarn für eine Zelle
     *
     * @param row Reihe/x-Koordinate der Zelle
     * @param column Spalte/y-Koordinate der Zelle
     * @return Liste von allen Nachbarn entsprechend der ausgewählten Einstellungen
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
     * @return Liste der ausgewählten Nachbarn
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
     * @return Liste der ausgewählten Nachbarn
     */
    private List<Cell> torusOffNeighbors(int[][] neighborCoords) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        for (int[] neighborCoord : neighborCoords) {
            if (neighborCoord[0] >= 0 && neighborCoord[1] >= 0 && neighborCoord[0] < rows && neighborCoord[1] < columns)
                neighbors.add(getCell(neighborCoord[0], neighborCoord[1]));
        }
        return neighbors;
    }

    public synchronized boolean changeCells(Cell[][] population) {
        if (population == null || population.length != rows || population[0].length != columns) {
            return false;
        }
        this.population = population;
        notifyObserver();
        return true;
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Automaton automaton = (Automaton) o;
        return rows == automaton.rows && columns == automaton.columns && numberOfStates == automaton.numberOfStates
                && isMooreNeighborHood == automaton.isMooreNeighborHood && isTorus == automaton.isTorus
                && Arrays.deepEquals(population, automaton.population) && Objects.equals(random, automaton.random);
    }

    @Override
    public synchronized int hashCode() {
        int result = Objects.hash(super.hashCode(), rows, columns, numberOfStates, isMooreNeighborHood, isTorus, random);
        result = 31 * result + Arrays.deepHashCode(population);
        return result;
    }
}