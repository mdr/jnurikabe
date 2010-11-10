package nurikabe.reasoning;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.WaterIfHasNeighboursFromDifferentIslandsRationale;
import nurikabe.reasoning.strategy.WaterIfHasNeighboursFromDifferentIslandsReasoningStrategy;

import org.junit.Before;
import org.junit.Test;

import utils.CollectionsUtils;

public class WaterIfHasNeighboursFromDifferentIslandsReasoningStrategyTest extends AbstractReasoningStrategyTest {

    @Before
    public void setUp() throws Exception {
        strategy = new WaterIfHasNeighboursFromDifferentIslandsReasoningStrategy();
    }

    @Test
    public void testSimpleCaseWorks() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("..  :"); // 0
        builder.append(".5  :"); // 1
        builder.append("  4.:"); // 2
        builder.append("  . :"); // 3
        builder.append("    :"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Conclusion conclusion1 = new Conclusion(1, 2, CellType.WATER);
        Conclusion conclusion2 = new Conclusion(2, 1, CellType.WATER);
        Set<Polyomino> polyominoSet = CollectionsUtils.makeSet(grid.cellAt(1, 1).getPolyomino(), grid.cellAt(2, 2)
                .getPolyomino());
        Rationale rationale = new WaterIfHasNeighboursFromDifferentIslandsRationale(polyominoSet);

        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion1, rationale);
        expectedConclusions.put(conclusion2, rationale);
        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

        builder = new StringBuilder();
        // ............ 0123
        builder.append("..  :"); // 0
        builder.append(".4  :"); // 1
        builder.append("  3.:"); // 2
        builder.append("  . :"); // 3
        builder.append("    :"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void shouldReasonOnlyOnNumberPolyominoes() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append(". . :"); // 0
        builder.append("  5 :"); // 1
        builder.append(".. .:"); // 2
        builder.append(". . :"); // 3
        builder.append(" . .:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Map<Conclusion, Rationale> expectedConclusions = Collections.emptyMap();
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
        builder.append("1 2 :"); // 0
        builder.append("   4:"); // 1
        builder.append("1   :"); // 2
        builder.append("   2:"); // 3
        builder.append("1 2 :"); // 4
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        // ............ 0123
        builder.append("1#2#:"); // 0
        builder.append("# #4:"); // 1
        builder.append("1  #:"); // 2
        builder.append("# #2:"); // 3
        builder.append("1#2#:"); // 4
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);

    }

}
