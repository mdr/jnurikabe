package nurikabe.reasoning;

import java.util.Collections;
import java.util.Map;

import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.UnknownPolyominoEnclosedByLandOrWaterRationale;
import nurikabe.reasoning.strategy.UnknownPolyominoEnclosedByLandOrWaterReasoningStrategy;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LandIfSurroundedByLandTest extends AbstractReasoningStrategyTest {

    @Before
    public void setUp() throws Exception {
        strategy = new UnknownPolyominoEnclosedByLandOrWaterReasoningStrategy();
    }

    @Test
    public void testSimpleCaseWorks() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("   #:"); // 0
        builder.append(" .  :"); // 1
        builder.append(". . :"); // 2
        builder.append(" .  :"); // 3
        builder.append("    :"); // 4

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Conclusion conclusion = new Conclusion(2, 1, CellType.LAND);
        Rationale rationale = new UnknownPolyominoEnclosedByLandOrWaterRationale(CellType.LAND);
        Map<Conclusion, Rationale> expectedConclusions = Collections.singletonMap(conclusion, rationale);
        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testMiddleIsNotAllWater() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("    :"); // 0
        builder.append(" .  :"); // 1
        builder.append(". . :"); // 2
        builder.append(" .  :"); // 3
        builder.append("    :"); // 4

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
        builder.append(".   :"); // 0
        builder.append(" . .:"); // 1
        builder.append(" .. :"); // 2
        builder.append(" ..#:"); // 3
        builder.append(". . :"); // 4
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        // ............ 0123
        builder.append("....:"); // 0
        builder.append("....:"); // 1
        builder.append("... :"); // 2
        builder.append("...#:"); // 3
        builder.append("... :"); // 4
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);

    }

}
