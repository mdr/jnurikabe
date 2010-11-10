package nurikabe.reasoning;

import static org.junit.Assert.assertEquals;
import static utils.CollectionsUtils.makeSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.TryAllPolyominoesRationale;
import nurikabe.reasoning.strategy.TryAllPolyominoesReasoningStrategy;

import org.junit.Before;
import org.junit.Test;

public class TryAllPolyominoesReasoningStrategyTest extends AbstractReasoningStrategyTest {

    @Before
    public void setUp() throws Exception {
        strategy = new TryAllPolyominoesReasoningStrategy();
    }

    @Test
    public void testBasic() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("5 ##1:"); // 0
        builder.append("    #:"); // 1
        builder.append("####1:"); // 2

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Conclusion conclusion1 = new Conclusion(1, 1, CellType.LAND);
        Conclusion conclusion2 = new Conclusion(1, 2, CellType.LAND);
        Rationale rationale = new TryAllPolyominoesRationale(grid.cellAt(0, 0));

        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion1, rationale);
        expectedConclusions.put(conclusion2, rationale);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testSlightlyComplex() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("7.#2#:"); // 0
        builder.append("   .#:"); // 1
        builder.append("#.  #:"); // 2
        builder.append("#  ##:"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Conclusion conclusion = new Conclusion(1, 1, CellType.LAND);
        Rationale rationale = new TryAllPolyominoesRationale(grid.cellAt(0, 0));

        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion, rationale);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testWithBareUnreachableLand() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("3. . :"); // 0
        builder.append(" #   :"); // 1
        builder.append("     :"); // 2
        builder.append("     :"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Conclusion conclusion = new Conclusion(1, 0, CellType.LAND);
        Rationale rationale = new TryAllPolyominoesRationale(grid.cellAt(0, 0));

        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion, rationale);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testConclusionApplied() throws Exception {

        StringBuilder builder;
        NurikabeGrid startGrid;
        NurikabeGrid expectedGrid;

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("5    #1:"); // 0
        builder.append("#####  :"); // 1
        builder.append("2.     :"); // 2
        builder.append(" ######:"); // 3
        builder.append("#5. .  :"); // 4
        builder.append(" ######:"); // 5
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("5....#1:"); // 0
        builder.append("#####  :"); // 1
        builder.append("2.     :"); // 2
        builder.append(" ######:"); // 3
        builder.append("#5.... :"); // 4
        builder.append(" ######:"); // 5
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);
    }

    @Test
    public void testFindPolyominoesWithBareUnreachableLand() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("3. . :"); // 0
        builder.append("     :"); // 1
        builder.append("     :"); // 2
        builder.append("     :"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        TryAllPolyominoesReasoningStrategy reasoningStrategy = new TryAllPolyominoesReasoningStrategy();
        Set<Polyomino> actualPolyominoes = reasoningStrategy.findAllPossiblePolyominoes(grid, grid.cellAt(0, 0));
        Polyomino polyomino1 = new Polyomino(makeSet(grid.cellAt(0, 0), grid.cellAt(0, 1), grid.cellAt(1, 0)));
        Polyomino polyomino2 = new Polyomino(makeSet(grid.cellAt(0, 0), grid.cellAt(0, 1), grid.cellAt(1, 1)));
        Set<Polyomino> expectedPolyominoes = makeSet(polyomino1, polyomino2);
        assertEquals(expectedPolyominoes, actualPolyominoes);

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("4. ..:"); // 0
        builder.append(" #   :"); // 1
        builder.append(" #  5:"); // 2
        builder.append("     :"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        actualPolyominoes = reasoningStrategy.findAllPossiblePolyominoes(grid, grid.cellAt(0, 0));
        Polyomino polyomino = new Polyomino(makeSet(grid.cellAt(0, 0), grid.cellAt(0, 1), grid.cellAt(1, 0), grid
                .cellAt(2, 0)));
        expectedPolyominoes = Collections.singleton(polyomino);
        assertEquals(expectedPolyominoes, actualPolyominoes);

    }
    
    @Test   
    public void testWithMandatoryCells() throws Exception {
        StringBuilder builder;
        NurikabeGrid startGrid;
        NurikabeGrid expectedGrid;

        builder = new StringBuilder();
        builder.append("  ##3. ##:"); 
        builder.append(" 6#4# #.#:");
        builder.append("   .   . :");
        builder.append("         :");
        builder.append("    5    :");
        builder.append("     #   :");
        builder.append("#. .#.#  :");
        builder.append("#####2#7 :");
        builder.append("#...4##  :");
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        

        builder = new StringBuilder();
        builder.append("  ##3. ##:"); 
        builder.append(" 6#4# #.#:");
        builder.append(" . .   . :");
        builder.append(" .     . :");
        builder.append(" .  5  . :");
        builder.append(" . . # . :");
        builder.append("#. .#.#. :");
        builder.append("#####2#7 :");
        builder.append("#...4##  :");
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);

    }
}
