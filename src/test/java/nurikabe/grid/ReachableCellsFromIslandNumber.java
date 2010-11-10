package nurikabe.grid;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ReachableCellsFromIslandNumber {

    @Test
    public void testBareNumbers() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 012345
        builder.append("      :"); // 0
        builder.append(" #    :"); // 1
        builder.append("# #2  :"); // 2
        builder.append(" 5    :"); // 3
        builder.append("     #:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Map<Cell, Integer> actualReachableCells = grid.cellAt(3, 1).reachableCellsFromIslandNumberCell();

        Map<Cell, Integer> expectedReachableCells = new HashMap<Cell, Integer>();
        expectedReachableCells.put(grid.cellAt(3, 1), 0);
        expectedReachableCells.put(grid.cellAt(2, 1), 1);
        expectedReachableCells.put(grid.cellAt(3, 0), 1);
        expectedReachableCells.put(grid.cellAt(3, 2), 1);
        expectedReachableCells.put(grid.cellAt(4, 1), 1);
        expectedReachableCells.put(grid.cellAt(4, 0), 2);
        expectedReachableCells.put(grid.cellAt(4, 2), 2);
        expectedReachableCells.put(grid.cellAt(4, 3), 3);
        expectedReachableCells.put(grid.cellAt(4, 4), 4);

        assertEquals(expectedReachableCells, actualReachableCells);
    }

    @Test
    public void testIslandNumberIsNotBare() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 012345
        builder.append("      :"); // 0
        builder.append(" #    :"); // 1
        builder.append("#.#2  :"); // 2
        builder.append(".5    :"); // 3
        builder.append("     #:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Map<Cell, Integer> actualReachableCells = grid.cellAt(3, 1).reachableCellsFromIslandNumberCell();

        Map<Cell, Integer> expectedReachableCells = new HashMap<Cell, Integer>();
        expectedReachableCells.put(grid.cellAt(3, 1), 0);
        expectedReachableCells.put(grid.cellAt(2, 1), 0);
        expectedReachableCells.put(grid.cellAt(3, 0), 0);       
        expectedReachableCells.put(grid.cellAt(4, 0), 1);
        expectedReachableCells.put(grid.cellAt(4, 1), 1);
        expectedReachableCells.put(grid.cellAt(3, 2), 1);
        expectedReachableCells.put(grid.cellAt(4, 2), 2);       

        assertEquals(expectedReachableCells, actualReachableCells);
    }

    @Test
    public void testSingleton() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 012345
        builder.append("      :"); // 0
        builder.append(" #    :"); // 1
        builder.append("# #   :"); // 2
        builder.append("  1   :"); // 3
        builder.append("     #:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Map<Cell, Integer> actualReachableCells = grid.cellAt(3, 2).reachableCellsFromIslandNumberCell();

        Map<Cell, Integer> expectedReachableCells = new HashMap<Cell, Integer>();
        expectedReachableCells.put(grid.cellAt(3, 2), 0);

        assertEquals(expectedReachableCells, actualReachableCells);
    } 
    
    
    @Test
    public void testWithConnectedLand() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 012345
        builder.append("      :"); // 0
        builder.append(" #    :"); // 1
        builder.append("#.#2  :"); // 2
        builder.append(".5 .  :"); // 3
        builder.append("     #:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Map<Cell, Integer> actualReachableCells = grid.cellAt(3, 1).reachableCellsFromIslandNumberCell();

        Map<Cell, Integer> expectedReachableCells = new HashMap<Cell, Integer>();
        expectedReachableCells.put(grid.cellAt(3, 1), 0);
        expectedReachableCells.put(grid.cellAt(2, 1), 0);
        expectedReachableCells.put(grid.cellAt(3, 0), 0);       
        expectedReachableCells.put(grid.cellAt(4, 0), 1);
        expectedReachableCells.put(grid.cellAt(4, 1), 1);
        expectedReachableCells.put(grid.cellAt(4, 2), 2);       

        assertEquals(expectedReachableCells, actualReachableCells);
    }

    @Test
    public void testWithUnconnectedLand() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 012345
        builder.append("      :"); // 0
        builder.append(" #    :"); // 1
        builder.append("#.#.  :"); // 2
        builder.append(".5 .  :"); // 3
        builder.append("  .. #:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Map<Cell, Integer> actualReachableCells = grid.cellAt(3, 1).reachableCellsFromIslandNumberCell();

        Map<Cell, Integer> expectedReachableCells = new HashMap<Cell, Integer>();
        expectedReachableCells.put(grid.cellAt(3, 1), 0);
        expectedReachableCells.put(grid.cellAt(2, 1), 0);
        expectedReachableCells.put(grid.cellAt(3, 0), 0);       
        expectedReachableCells.put(grid.cellAt(4, 0), 1);
        expectedReachableCells.put(grid.cellAt(4, 1), 1);
        expectedReachableCells.put(grid.cellAt(3, 2), 1);
        expectedReachableCells.put(grid.cellAt(4, 2), 2);       
        expectedReachableCells.put(grid.cellAt(3, 3), 2);

        assertEquals(expectedReachableCells, actualReachableCells);
    }

}
