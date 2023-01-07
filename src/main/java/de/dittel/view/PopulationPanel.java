package de.dittel.view;

import de.dittel.controller.MainController;
import de.dittel.util.Pair;
import de.dittel.util.ReferenceHandler;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.Optional;

/**
 * Klasse zur Darstellung der Population eines Automaten in der View
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

    private final ReferenceHandler referenceHandler;
    private final MainController mainController;

    /**
     * Konstruktor
     *
     * @param referenceHandler verwaltet die verwendeten Referenzen
     * @param mainController Hauptcontroller der View, zum Verwalten der FXML-Elemente
     */
    public PopulationPanel(ReferenceHandler referenceHandler, MainController mainController) {
        this.referenceHandler = referenceHandler;
        this.mainController = mainController;
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
        return 2 * BORDER_WIDTH + populationWidth * referenceHandler.getAutomaton().getNumberOfColumns();
    }

    /**
     * Helfermethode zur Berechnung der Canvas-Höhe
     *
     * @return Höhe, die der Automat benötigt (inkl. Kanten)
     */
    private double calcCanvasHeight() {
        return 2 * BORDER_HEIGHT + populationHeight * referenceHandler.getAutomaton().getNumberOfRows();
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

        for (int r = 0; r < referenceHandler.getAutomaton().getNumberOfRows(); r++) {
            for (int c = 0; c < referenceHandler.getAutomaton().getNumberOfColumns(); c++) {
                gc.setFill(mainController.getColorPickerList().get(referenceHandler.getAutomaton().
                        getCell(r, c).getState()).getValue());
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
     * @return boolean, ob die maximalen Werte erreicht wurden
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
     * @return boolean, ob die minimalen Werte erreicht wurden
     */
    public boolean zoomOut() {
        if (populationWidth > MIN_POPULATION_WIDTH && populationHeight > MIN_POPULATION_HEIGHT) {
            populationWidth -= 2;
            populationHeight -= 2;
        }
        return populationHeight <= MIN_POPULATION_HEIGHT && populationWidth <= MIN_POPULATION_WIDTH;
    }

    /**
     * Liefert ein Pair, welches die Reihe und Spalte des Events enthält
     *
     * @param x Reihe der Population
     * @param y Spalte der Population
     * @return Pair, mit Reihe und Spalte des Events
     */
    public Optional<Pair<Integer>> getRowAndCol(double x, double y) {
        if (x < BORDER_WIDTH || y < BORDER_HEIGHT
                || x > BORDER_WIDTH + referenceHandler.getAutomaton().getNumberOfColumns() * populationWidth
                || y > BORDER_HEIGHT + referenceHandler.getAutomaton().getNumberOfRows() * populationHeight) {
            return Optional.empty();
        }
        int row = (int) ((y - BORDER_HEIGHT) / populationHeight);
        int col = (int) ((x - BORDER_WIDTH) / populationWidth);
        return Optional.of(new Pair<>(row, col));
    }

    /**
     * Zeichnet das Canvas neu und zentriert es
     */
    public void update() {
        if(Platform.isFxApplicationThread()) {
            paintCanvas();
            center(mainController.getPopulationScrollPane().getViewportBounds());
        } else {
            Platform.runLater(() -> {
                paintCanvas();
                center(mainController.getPopulationScrollPane().getViewportBounds());
            });
        }
    }
}
