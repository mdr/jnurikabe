package nurikabe.reasoning;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.CompleteIslandIsSurroundedByWaterRationale;
import nurikabe.reasoning.rationale.CompositeRationale;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.strategy.CompleteIslandIsSurroundedByWaterReasoningStrategy;

import org.junit.Before;
import org.junit.Test;

public class CompleteIslandIsSurroundedByWaterReasoningStrategyTest extends AbstractReasoningStrategyTest {

    @Before
    public void setUp() throws Exception {
        strategy = new CompleteIslandIsSurroundedByWaterReasoningStrategy();
    }

    @Test
    public void testSingleton() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("    :"); // 0
        builder.append(" 1  :"); // 1
        builder.append("    :"); // 2
        builder.append("    :"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Conclusion conclusion1 = new Conclusion(0, 1, CellType.WATER);
        Rationale rationale1 = new CompleteIslandIsSurroundedByWaterRationale(1, 1);
        Conclusion conclusion2 = new Conclusion(1, 2, CellType.WATER);
        Rationale rationale2 = new CompleteIslandIsSurroundedByWaterRationale(1, 1);
        Conclusion conclusion3 = new Conclusion(1, 0, CellType.WATER);
        Rationale rationale3 = new CompleteIslandIsSurroundedByWaterRationale(1, 1);
        Conclusion conclusion4 = new Conclusion(2, 1, CellType.WATER);
        Rationale rationale4 = new CompleteIslandIsSurroundedByWaterRationale(1, 1);

        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion1, rationale1);
        expectedConclusions.put(conclusion2, rationale2);
        expectedConclusions.put(conclusion3, rationale3);
        expectedConclusions.put(conclusion4, rationale4);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testSoleNumberNotOne() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("    :"); // 0
        builder.append(" 3  :"); // 1
        builder.append(" .. :"); // 2
        builder.append("    :"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Conclusion conclusion1 = new Conclusion(0, 1, CellType.WATER);
        Rationale rationale1 = new CompleteIslandIsSurroundedByWaterRationale(1, 1);
        Conclusion conclusion2 = new Conclusion(1, 2, CellType.WATER);
        Rationale rationale2 = new CompleteIslandIsSurroundedByWaterRationale(1, 1);
        Rationale rationale2_another = new CompleteIslandIsSurroundedByWaterRationale(2, 2);
        Conclusion conclusion3 = new Conclusion(1, 0, CellType.WATER);
        Rationale rationale3 = new CompleteIslandIsSurroundedByWaterRationale(1, 1);
        Conclusion conclusion4 = new Conclusion(2, 0, CellType.WATER);
        Rationale rationale4 = new CompleteIslandIsSurroundedByWaterRationale(2, 1);
        Conclusion conclusion5 = new Conclusion(3, 1, CellType.WATER);
        Rationale rationale5 = new CompleteIslandIsSurroundedByWaterRationale(2, 1);
        Conclusion conclusion6 = new Conclusion(3, 2, CellType.WATER);
        Rationale rationale6 = new CompleteIslandIsSurroundedByWaterRationale(2, 2);
        Conclusion conclusion7 = new Conclusion(2, 3, CellType.WATER);
        Rationale rationale7 = new CompleteIslandIsSurroundedByWaterRationale(2, 2);

        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion1, rationale1);
        expectedConclusions.put(conclusion2, new CompositeRationale(rationale2, rationale2_another));
        expectedConclusions.put(conclusion3, rationale3);
        expectedConclusions.put(conclusion4, rationale4);
        expectedConclusions.put(conclusion5, rationale5);
        expectedConclusions.put(conclusion6, rationale6);
        expectedConclusions.put(conclusion7, rationale7);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);
    }

    @Test
    public void testTwoOverlappingNumbers() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;
        builder = new StringBuilder();
        builder.append("1 1:");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Conclusion conclusion = new Conclusion(0, 1, CellType.WATER);
        Rationale rationale1 = new CompleteIslandIsSurroundedByWaterRationale(0, 0);
        Rationale rationale2 = new CompleteIslandIsSurroundedByWaterRationale(0, 2);
        Map<Conclusion, Rationale> expectedConclusions = new HashMap<Conclusion, Rationale>();
        expectedConclusions.put(conclusion, new CompositeRationale(rationale1, rationale2));
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
        builder.append("1  2:"); // 0
        builder.append(" 3 .:"); // 1
        builder.append(" .. :"); // 2
        builder.append("1  1:"); // 3
        builder.append("    :"); // 4
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        // ............ 0123
        builder.append("1##2:"); // 0
        builder.append("#3#.:"); // 1
        builder.append("#..#:"); // 2
        builder.append("1##1:"); // 3
        builder.append("#  #:"); // 3
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);
    }

}
