package de.dittel;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * Klasse zur Darstellung eines Automaten in der View
 */
public class PopulationPanel extends Region {

    private static final double POPULATION_WIDTH = 20;
    private static final double POPULATION_HEIGHT = 20;
    private static final double BORDER_WIDTH = 2;
    private static final double BORDER_HEIGHT = 2;

    private final Canvas canvas;
    private final Automaton automaton;

    /**
     * Konstruktor
     *
     * @param automaton Automat der abgebildet werden soll
     */
    public PopulationPanel(Automaton automaton) {
        this.automaton = automaton;
        this.canvas = new Canvas(calcCanvasWidth(), calcCanvasHeight());
        paintCanvas();
        this.getChildren().add(canvas);
    }

    /**
     * Helfermethode zur Berechnung der Canvas-Breite
     *
     * @return Breite, die der Automat benötigt (inkl. Kanten)
     */
    private double calcCanvasWidth() {
        return 2 * BORDER_WIDTH + POPULATION_WIDTH * automaton.getNumberOfColumns();
    }

    /**
     * Helfermethode zur Berechnung der Canvas-Höhe
     *
     * @return Höhe, die der Automat benötigt (inkl. Kanten)
     */
    private double calcCanvasHeight() {
        return 2 * BORDER_HEIGHT + POPULATION_HEIGHT * automaton.getNumberOfRows();
    }

    /**
     * Zeichnet den aktuellen Zustand des Automaten in das Canvas (inkl. Kanten)
     *
     * Je nach Zustand der einzelnen Zellen werden diese unterschiedlich gefärbt
     */
    public void paintCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);

        for (int r = 0; r < automaton.getNumberOfRows(); r++) {
            for (int c = 0; c < automaton.getNumberOfColumns(); c++) {
                switch (automaton.getCell(r, c).getState()) {
                    case 1 -> gc.setFill(Color.BLACK);
                    case 2 -> gc.setFill(Color.valueOf("#0f7b4e"));
                    case 3 -> gc.setFill(Color.valueOf("#b6d4e1"));
                    case 4 -> gc.setFill(Color.valueOf("#f9ee3d"));
                    case 5 -> gc.setFill(Color.valueOf("#5f85c4"));
                    case 6 -> gc.setFill(Color.valueOf("eaa8fe"));
                    case 7 -> gc.setFill(Color.valueOf("2f8789"));
                    case 8 -> gc.setFill(Color.valueOf("213986"));
                    case 9 -> gc.setFill(Color.valueOf("535c79"));
                    default -> gc.setFill(Color.WHITE);
                }
                gc.fillRect(BORDER_WIDTH + c * POPULATION_WIDTH, BORDER_HEIGHT + r * POPULATION_HEIGHT,
                        POPULATION_WIDTH, POPULATION_HEIGHT);
                gc.strokeRect(BORDER_WIDTH + c * POPULATION_WIDTH, BORDER_HEIGHT + r * POPULATION_HEIGHT,
                        POPULATION_WIDTH, POPULATION_HEIGHT);
            }
        }
    }

    /**
     * Helfermethode zum Zentrieren des Canvas
     *
     * @param viewPortBounds Ansichtsfenster-Grenzen, welche als Rechengrundlage dienen
     */
    public void center(Bounds viewPortBounds) {
        double width = viewPortBounds.getWidth();
        double height = viewPortBounds.getHeight();
        if (width > this.calcCanvasWidth()) {
            this.canvas.setTranslateX((width - this.calcCanvasWidth()) / 2);
        } else {
            this.canvas.setTranslateX(0);
        }
        if (height > this.calcCanvasHeight()) {
            this.canvas.setTranslateY((height - this.calcCanvasHeight()) / 2);
        } else {
            this.canvas.setTranslateY(0);
        }
    }
}
