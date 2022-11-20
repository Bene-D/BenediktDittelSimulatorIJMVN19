package de.dittel;

import de.dittel.automaton.Automaton;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Klasse zur Darstellung eines Automaten in der View
 */
public class PopulationPanel extends Region {

    private static final double BORDER_WIDTH = 2;
    private static final double BORDER_HEIGHT = 2;
    private static final double MAX_POPULATION_WIDTH = 40;
    private static final double MAX_POPULATION_HEIGHT = 40;
    private static final double MIN_POPULATION_WIDTH = 8;
    private static final double MIN_POPULATION_HEIGHT = 8;
    private double populationWidth = 15;
    private double populationHeight = 15;

    private final Canvas canvas;

    private final Automaton automaton;
    private final List<ColorPicker> colorPickers;
    /**
     * Konstruktor
     *
     * @param automaton Automat der abgebildet werden soll
     */
    public PopulationPanel(Automaton automaton, List<ColorPicker> colorPickers) {
        this.automaton = automaton;
        this.colorPickers = colorPickers;
        this.canvas = new Canvas(calcCanvasWidth(), calcCanvasHeight());
        this.getChildren().add(canvas);
        paintCanvas();
    }

    /**
     * Getter für das Canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Helfermethode zur Berechnung der Canvas-Breite
     *
     * @return Breite, die der Automat benötigt (inkl. Kanten)
     */
    private double calcCanvasWidth() {
        return 2 * BORDER_WIDTH + populationWidth * automaton.getNumberOfColumns();
    }

    /**
     * Helfermethode zur Berechnung der Canvas-Höhe
     *
     * @return Höhe, die der Automat benötigt (inkl. Kanten)
     */
    private double calcCanvasHeight() {
        return 2 * BORDER_HEIGHT + populationHeight * automaton.getNumberOfRows();
    }

    /**
     * Zeichnet den aktuellen Zustand des Automaten in das Canvas (inkl. Kanten)
     * <p>
     * Je nach Zustand der einzelnen Zellen werden diese unterschiedlich gefärbt
     */
    public void paintCanvas() {
        canvas.setWidth(calcCanvasWidth());
        canvas.setHeight(calcCanvasHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setLineWidth(1);
        gc.setStroke(Color.GRAY);

        for (int r = 0; r < automaton.getNumberOfRows(); r++) {
            for (int c = 0; c < automaton.getNumberOfColumns(); c++) {
                gc.setFill(colorPickers.get(automaton.getCell(r, c).getState()).getValue());
                gc.fillRect(BORDER_WIDTH + c * populationWidth, BORDER_HEIGHT + r * populationHeight,
                        populationWidth, populationHeight);
                gc.strokeRect(BORDER_WIDTH + c * populationWidth, BORDER_HEIGHT + r * populationHeight,
                        populationWidth, populationHeight);
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

    /**
     * Vergrößert die Breite und Höhe der Population
     *
     * @return boolean, ob die maximalen Werte erreicht wurden.
     */
    public boolean zoomIn() {
        if (populationWidth < MAX_POPULATION_WIDTH && populationHeight < MAX_POPULATION_HEIGHT) {
            populationWidth += 2;
            populationHeight += 2;
        }
        return populationHeight >= MAX_POPULATION_HEIGHT && populationWidth >= MAX_POPULATION_WIDTH;
    }

    /**
     * Verkleinert die Breite und Höhe der Population
     *
     * @return boolean, ob die minimalen Werte erreicht wurden.
     */
    public boolean zoomOut() {
        if (populationWidth > MIN_POPULATION_WIDTH && populationHeight > MIN_POPULATION_HEIGHT) {
            populationWidth -= 2;
            populationHeight -= 2;
        }
        return populationHeight <= MIN_POPULATION_HEIGHT && populationWidth <= MIN_POPULATION_WIDTH;
    }

    /**
     * Ändert den Zustand/Farbe einer Zelle der Population
     *
     * @param x X-Koordinate der ausgewählten Zelle
     * @param y Y-Koordinate der ausgewählten Zelle
     * @param state Zustand des ausgewählten RadioButtons
     */
    public void canvasPaintSingleCell(double x, double y, int state) {
        if (x < BORDER_WIDTH || y < BORDER_HEIGHT || x > BORDER_WIDTH + automaton.getNumberOfColumns() * populationWidth
                || y > BORDER_HEIGHT + automaton.getNumberOfRows() * populationHeight) {
            return;
        }
        int row = (int) ((y - BORDER_HEIGHT) / populationHeight);
        int col = (int) ((x - BORDER_WIDTH) / populationWidth);
        automaton.setState(row, col, state);
        paintCanvas();
    }

    /**
     * Ändert den Zustand/Farbe eines Zellenbereichs der Population
     *
     * @param xFrom X-Koordinate der Startzelle des Bereichs
     * @param yFrom Y-Koordinate der Startzelle des Bereichs
     * @param xTo X-Koordinate der Endzelle des Bereichs
     * @param yTo Y-Koordinate der Endzelle des Bereichs
     * @param state Zustand des ausgewählten RadioButtons
     */
    public void canvasPaintDragAndRelease(double xFrom, double yFrom, double xTo, double yTo, int state) {
        int fromRow = (int) ((yFrom - BORDER_HEIGHT) / populationHeight);
        int fromColumn = (int) ((xFrom - BORDER_WIDTH) / populationWidth);
        int toRow = (int) ((yTo - BORDER_WIDTH) / populationWidth);
        int toColumn = (int) ((xTo - BORDER_WIDTH) / populationWidth);
        if (toRow < 0) {
            toRow = 0;
        } else if (toRow >= automaton.getNumberOfRows()) {
            toRow = automaton.getNumberOfRows()-1;
        }
        if (toColumn < 0) {
            toColumn = 0;
        } else if (toColumn >= automaton.getNumberOfColumns()) {
            toColumn = automaton.getNumberOfColumns()-1;
        }
        int lowRowIndex = Math.min(fromRow, toRow);
        int highRowIndex = Math.max(fromRow, toRow);
        int lowColumnIndex = Math.min(fromColumn, toColumn);
        int highColumnIndex = Math.max(fromColumn, toColumn);

        automaton.setState(lowRowIndex, lowColumnIndex, highRowIndex, highColumnIndex, state);
        paintCanvas();
    }
}
