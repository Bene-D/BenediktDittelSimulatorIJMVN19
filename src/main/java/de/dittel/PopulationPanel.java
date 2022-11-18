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
        paintCanvas();
        this.getChildren().add(canvas);
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
     *
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
}
