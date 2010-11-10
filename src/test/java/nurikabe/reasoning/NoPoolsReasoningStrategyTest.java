package nurikabe.reasoning;

import static nurikabe.reasoning.rationale.NoPoolsRationale.Corner.NORTH_EAST;
import static nurikabe.reasoning.rationale.NoPoolsRationale.Corner.NORTH_WEST;
import static nurikabe.reasoning.rationale.NoPoolsRationale.Corner.SOUTH_EAST;
import static nurikabe.reasoning.rationale.NoPoolsRationale.Corner.SOUTH_WEST;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nurikabe.grid.CellType;
import nurikabe.grid.Coordinate;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.CompositeRationale;
import nurikabe.reasoning.rationale.NoPoolsRationale;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.NoPoolsRationale.Corner;
import nurikabe.reasoning.strategy.NoPoolsReasoningStrategy;

import org.junit.Before;
import org.junit.Test;


public class NoPoolsReasoningStrategyTest extends AbstractReasoningStrategyTest {


    @Before
    public void setUp() throws Exception {
        strategy = new NoPoolsReasoningStrategy();
    }

    @Test
    public void testSimpleSingleReasoning() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        builder.append("    :");
        builder.append("  # :");
        builder.append(" ## :");
        builder.append("    :");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        checkSimpleCase(grid, Coordinate.makeCoordinate(1, 1), Corner.NORTH_WEST);

        builder = new StringBuilder();
        builder.append("    :");
        builder.append(" #  :");
        builder.append(" ## :");
        builder.append("    :");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        checkSimpleCase(grid, Coordinate.makeCoordinate(1, 2), Corner.NORTH_EAST);

        builder = new StringBuilder();
        builder.append("    :");
        builder.append(" ## :");
        builder.append(" #  :");
        builder.append("    :");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        checkSimpleCase(grid, Coordinate.makeCoordinate(2, 2), Corner.SOUTH_EAST);

        builder = new StringBuilder();
        builder.append("    :");
        builder.append(" ## :");
        builder.append("  # :");
        builder.append("    :");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        checkSimpleCase(grid, Coordinate.makeCoordinate(2, 1), Corner.SOUTH_WEST);
    }

    private void checkSimpleCase(NurikabeGrid grid, Coordinate conclusionLocation, Corner corner) {
        Conclusion expectedConclusion = new Conclusion(conclusionLocation, CellType.LAND);
        Rationale expectedRationale = new NoPoolsRationale(conclusionLocation, corner);
        Map<Conclusion, Rationale> expectedConclusions = Collections
                .singletonMap(expectedConclusion, expectedRationale);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);
    }

    @Test
    public void testMultipleConclusions() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("2 . :"); // 0
        builder.append("  ##:"); // 1
        builder.append(" ## :"); // 2
        builder.append(" # .:"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Conclusion conclusion1 = new Conclusion(2, 3, CellType.LAND);
        Rationale rationale1 = new NoPoolsRationale(2, 3, SOUTH_EAST);
        Conclusion conclusion2 = new Conclusion(3, 2, CellType.LAND);
        Rationale rationale2 = new NoPoolsRationale(3, 2, SOUTH_EAST);
        Conclusion conclusion3 = new Conclusion(1, 1, CellType.LAND);
        Rationale rationale3 = new NoPoolsRationale(1, 1, NORTH_WEST);
        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion1, rationale1);
        expectedConclusions.put(conclusion2, rationale2);
        expectedConclusions.put(conclusion3, rationale3);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void shouldNotConcludeIfCellIsFull() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("2 . :"); // 0
        builder.append(" ## :"); // 1
        builder.append(" ## :"); // 2
        builder.append("   .:"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertEquals(Collections.emptyMap(), strategy.makeConclusions(grid));

        builder = new StringBuilder();
        // ............ 0123
        builder.append("2 . :"); // 0
        builder.append(" ## :"); // 1
        builder.append(" .# :"); // 2
        builder.append("   .:"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertEquals(Collections.emptyMap(), strategy.makeConclusions(grid));

        builder = new StringBuilder();
        // ............ 0123
        builder.append("2 . :"); // 0
        builder.append(" ## :"); // 1
        builder.append(" 5# :"); // 2
        builder.append("   .:"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertEquals(Collections.emptyMap(), strategy.makeConclusions(grid));

    }

    @Test
    public void testMultipleRationalesForSameConclusion() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("    :"); // 0
        builder.append(" ## :"); // 1
        builder.append("# # :"); // 2
        builder.append("## .:"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Conclusion conclusion = new Conclusion(2, 1, CellType.LAND);
        Rationale rationale1 = new NoPoolsRationale(2, 1, NORTH_EAST);
        Rationale rationale2 = new NoPoolsRationale(2, 1, SOUTH_WEST);
        Rationale compositeRationale = new CompositeRationale(rationale1, rationale2);

        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion, compositeRationale );

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testConclusionApplied() throws Exception {

        StringBuilder builder;
        NurikabeGrid startGrid;
        NurikabeGrid expectedGrid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("####:"); // 0
        builder.append("#  #:"); // 1
        builder.append("####:"); // 2
        builder.append(" ## :"); // 3
        builder.append("##  :"); // 4
        builder.append("####:"); // 4
        builder.append("#2.#:"); // 4
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        // ............ 0123
        builder.append("####:"); // 0
        builder.append("#..#:"); // 1
        builder.append("####:"); // 2
        builder.append(".##.:"); // 3
        builder.append("##. :"); // 4
        builder.append("####:"); // 4
        builder.append("#2.#:"); // 4
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        
        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);
        
    }
    
}
