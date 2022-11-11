package de.dittel;

import java.util.List;
import java.util.Scanner;

public class GameOfLifeAutomaton extends Automaton {
    public GameOfLifeAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, 2, true, isTorus);
    }

    public GameOfLifeAutomaton() {
        this(50, 50, true);
    }

    @Override
    protected Cell transform(Cell cell, List<Cell> neighbors) {
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

    public static void main(String[] args) {
        int i = 100;
        try (Scanner scanner = new Scanner(System.in)) {
            GameOfLifeAutomaton gameOfLifeAutomaton = new GameOfLifeAutomaton(5, 5, true);
//            gameOfLifeAutomaton.setState(1, 2, 1);
//            gameOfLifeAutomaton.setState(3, 1, 1);
//            gameOfLifeAutomaton.setState(3, 2, 1);
//            gameOfLifeAutomaton.setState(3, 3, 1);
//            gameOfLifeAutomaton.setState(2, 3, 1);
            gameOfLifeAutomaton.setState(0, 1, 1);
            gameOfLifeAutomaton.setState(0, 2, 1);
            gameOfLifeAutomaton.setState(0, 3, 1);
            gameOfLifeAutomaton.print();
            while (i>0) {
                System.out.println("Drücke eine Taste für die nächste Generation!");
                scanner.next();
                gameOfLifeAutomaton.nextGeneration();
                gameOfLifeAutomaton.print();
                i--;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void print() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (population[r][c].getState() == 0) {
                    System.out.print(".");
                } else {
                    System.out.print("*");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
