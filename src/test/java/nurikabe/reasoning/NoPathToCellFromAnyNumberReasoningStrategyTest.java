package nurikabe.reasoning;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.NoPathToCellFromAnyNumberRationale;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.strategy.NoPathToCellFromAnyNumberReasoningStrategy;

import org.junit.Before;
import org.junit.Test;

public class NoPathToCellFromAnyNumberReasoningStrategyTest extends AbstractReasoningStrategyTest {

    @Before
    public void setUp() throws Exception {
        strategy = new NoPathToCellFromAnyNumberReasoningStrategy();
    }

    @Test
    public void testBasicDistance() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("5     5:"); // 0
        builder.append("       :"); // 1
        builder.append("       :"); // 2
        builder.append("       :"); // 3
        builder.append("       :"); // 4
        builder.append("       :"); // 5
        builder.append("5     5:"); // 6

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Conclusion conclusion1 = new Conclusion(2, 3, CellType.WATER);
        Conclusion conclusion2 = new Conclusion(3, 2, CellType.WATER);
        Conclusion conclusion3 = new Conclusion(3, 3, CellType.WATER);
        Conclusion conclusion4 = new Conclusion(3, 4, CellType.WATER);
        Conclusion conclusion5 = new Conclusion(4, 3, CellType.WATER);
        Rationale rationale = new NoPathToCellFromAnyNumberRationale();
        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion1, rationale);
        expectedConclusions.put(conclusion2, rationale);
        expectedConclusions.put(conclusion3, rationale);
        expectedConclusions.put(conclusion4, rationale);
        expectedConclusions.put(conclusion5, rationale);

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
        builder.append("2      :"); // 0
        builder.append("       :"); // 1
        builder.append("       :"); // 2
        builder.append("       :"); // 3
        builder.append("       :"); // 4
        builder.append("      1:"); // 5
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("2 #####:"); // 0
        builder.append(" ######:"); // 1
        builder.append("#######:"); // 2
        builder.append("#######:"); // 3
        builder.append("#######:"); // 4
        builder.append("######1:"); // 5
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);
    }

    @Test
    public void testNoRouteComplex() throws Exception {
        StringBuilder builder;
        NurikabeGrid startGrid;
        NurikabeGrid expectedGrid;

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("5    .3:"); // 0
        builder.append(" .     :"); // 1
        builder.append("       :"); // 2
        builder.append("    ###:"); // 3
        builder.append("    #  :"); // 4
        builder.append("    # 4:"); // 5
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("5    .3:"); // 0
        builder.append(" .  #  :"); // 1
        builder.append("   ####:"); // 2
        builder.append("  #####:"); // 3
        builder.append(" ####  :"); // 4
        builder.append("##### 4:"); // 5
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);
    }


}
