package nurikabe.reasoning;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;

import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.CompositeRationale;
import nurikabe.reasoning.rationale.OnlyOnePlaceToGrowRationale;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.strategy.OnlyOnePlaceToGrowReasoningStrategy;

import org.junit.Before;
import org.junit.Test;

public class OnlyOnePlaceToGrowReasoningStrategyTest extends AbstractReasoningStrategyTest {


    @Before
    public void setUp() throws Exception {
        strategy = new OnlyOnePlaceToGrowReasoningStrategy();
    }

    @Test
    public void testNumberedCell() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append(" #  :"); // 0
        builder.append("#2  :"); // 1
        builder.append(" #  :"); // 2
        builder.append("    :"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Conclusion conclusion = new Conclusion(1, 2, CellType.LAND);
        Rationale rationale = new OnlyOnePlaceToGrowRationale(grid.cellAt(1, 1).getPolyomino());
        Map<Conclusion, Rationale> expectedConclusions = Collections.singletonMap(conclusion, rationale);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testUnnumberedCell() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append(" #  :"); // 0
        builder.append("#.  :"); // 1
        builder.append(" #  :"); // 2
        builder.append("    :"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Conclusion conclusion = new Conclusion(1, 2, CellType.LAND);
        Rationale rationale = new OnlyOnePlaceToGrowRationale(grid.cellAt(1, 1).getPolyomino());
        Map<Conclusion, Rationale> expectedConclusions = Collections.singletonMap(conclusion, rationale);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testLongerLandPolyomino() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("####:"); // 0
        builder.append("#..#:"); // 1
        builder.append("##.#:"); // 2
        builder.append(" ..7:"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Conclusion conclusion = new Conclusion(3, 0, CellType.LAND);
        Rationale rationale = new OnlyOnePlaceToGrowRationale(grid.cellAt(3, 3).getPolyomino());
        Map<Conclusion, Rationale> expectedConclusions = Collections.singletonMap(conclusion, rationale);

        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testDoubleExtendPolyomino() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 01234
        builder.append("#####:"); // 0
        builder.append("#3 3#:"); // 1
        builder.append("#####:"); // 2

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Conclusion conclusion = new Conclusion(1, 2, CellType.LAND);
        Rationale rationale1 = new OnlyOnePlaceToGrowRationale(grid.cellAt(1, 1).getPolyomino());
        Rationale rationale2 = new OnlyOnePlaceToGrowRationale(grid.cellAt(1, 3).getPolyomino());
        Rationale compositeRationale = new CompositeRationale(rationale1, rationale2);
        Map<Conclusion, Rationale> expectedConclusions = Collections.singletonMap(conclusion, compositeRationale);
        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);

    }

    @Test
    public void testNoFalseExtensions() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("### :"); // 0
        builder.append("#2# :"); // 1
        builder.append("#.# :"); // 2
        builder.append("# # :"); // 3

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
        builder.append("1   :"); // 0
        builder.append("# ##:"); // 1
        builder.append("2 2 :"); // 2
        builder.append("####:"); // 3
        builder.append("2   :"); // 4
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        // ............ 0123
        builder.append("1   :"); // 0
        builder.append("####:"); // 1
        builder.append("2.2 :"); // 2
        builder.append("####:"); // 3
        builder.append("2.  :"); // 4
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);
    }

     
}
