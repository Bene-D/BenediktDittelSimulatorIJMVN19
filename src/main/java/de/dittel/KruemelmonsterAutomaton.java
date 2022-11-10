package de.dittel;

import java.util.List;
import java.util.Scanner;

public class KruemelmonsterAutomaton extends Automaton {

    public KruemelmonsterAutomaton(int rows, int columns, boolean isTorus) {
        super(rows, columns, 10, false, isTorus);
    }

    public KruemelmonsterAutomaton() {
        this(100, 100, true);
    }

    @Override
    protected Cell transform(Cell cell, List<Cell> neighbors) {
        int stateToCheck = cell.getState() + 1;
        if (stateToCheck == numberOfStates)
            stateToCheck = 0;

        for (Cell neighbor : neighbors)
            if (neighbor.getState() == stateToCheck)
                return new Cell(stateToCheck);

        return cell;
    }

    public static void main(String[] args) {
        int i = 10;
        try (Scanner scanner = new Scanner(System.in)) {
            KruemelmonsterAutomaton kruemelmonsterAutomaton = new KruemelmonsterAutomaton(9, 13, false);
            kruemelmonsterAutomaton.setState(7, 6, 7);
            kruemelmonsterAutomaton.setState(0, 6, 4);
            kruemelmonsterAutomaton.setState(8, 7, 5);
            kruemelmonsterAutomaton.setState(8, 5, 8);
            kruemelmonsterAutomaton.setState(8, 6, 3);
            kruemelmonsterAutomaton.setState(8, 12, 9);
            kruemelmonsterAutomaton.setState(1, 1, 1);
            kruemelmonsterAutomaton.print();
            while (i>0) {
                System.out.println("Drücke eine Taste für die nächste Generation!");
                scanner.next();
                kruemelmonsterAutomaton.nextGeneration();
                kruemelmonsterAutomaton.print();
                i--;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void print() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                switch (population[r][c].getState()) {
                    case 0:
                        System.out.print("0");
                        break;
                    case 1:
                        System.out.print("1");
                        break;
                    case 2:
                        System.out.print("2");
                        break;
                    case 3:
                        System.out.print("3");
                        break;
                    case 4:
                        System.out.print("4");
                        break;
                    case 5:
                        System.out.print("5");
                        break;
                    case 6:
                        System.out.print("6");
                        break;
                    case 7:
                        System.out.print("7");
                        break;
                    case 8:
                        System.out.print("8");
                        break;
                    case 9:
                        System.out.print("9");
                        break;
                    default:
                        System.out.print("*");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
