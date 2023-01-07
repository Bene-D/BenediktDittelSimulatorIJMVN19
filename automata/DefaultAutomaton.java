import de.dittel.model.Automaton;
import de.dittel.model.Cell;

import java.util.List;

public class DefaultAutomaton extends Automaton {
    private static final int INIT_NUMBER_OF_ROWS = 50;
    private static final int INIT_NUMBER_OF_COLUMNS = 50;
    private static final int NUMBER_OF_STATES = 2;
    private static final boolean MOORE_NEIGHBORHOOD = true;
    private static final boolean INIT_TORUS = true;

    public DefaultAutomaton() {
        super(INIT_NUMBER_OF_ROWS, INIT_NUMBER_OF_COLUMNS,
                NUMBER_OF_STATES, MOORE_NEIGHBORHOOD, INIT_TORUS);
    }

    protected Cell transform(Cell cell, List<Cell> neighbors) {
        return cell;
    }
}