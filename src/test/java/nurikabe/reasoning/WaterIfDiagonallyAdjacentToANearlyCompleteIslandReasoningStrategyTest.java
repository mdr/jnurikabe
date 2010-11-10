package nurikabe.reasoning;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;

import nurikabe.grid.CellType;
import nurikabe.grid.Coordinate;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.WaterIfDiagonallyAdjacentToANearlyCompleteIslandRationale;
import nurikabe.reasoning.strategy.WaterIfDiagonallyAdjacentToANearlyCompleteIslandReasoningStrategy;

import org.junit.Before;
import org.junit.Test;

public class WaterIfDiagonallyAdjacentToANearlyCompleteIslandReasoningStrategyTest extends
        AbstractReasoningStrategyTest {

    @Before
    public void setUp() throws Exception {
        strategy = new WaterIfDiagonallyAdjacentToANearlyCompleteIslandReasoningStrategy();
    }

    @Test
    public void testSimpleCaseWorks() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("####:"); // 0
        builder.append("#..#:"); // 1
        builder.append("#.5 :"); // 2
        builder.append("##  :"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Conclusion conclusion = new Conclusion(3, 3, CellType.WATER);
        Rationale rationale = new WaterIfDiagonallyAdjacentToANearlyCompleteIslandRationale(grid.cellAt(2, 2)
                .getPolyomino(), Coordinate.makeCoordinate(3, 2), Coordinate.makeCoordinate(2, 3));

        Map<Conclusion, Rationale> expectedConclusions = Collections.singletonMap(conclusion, rationale);
        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);
    }
    
    @Test
    public void testCasesWhereRuleShouldNotApply() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("# ##:"); // 0
        builder.append("#..#:"); // 2
        builder.append("#.5 :"); // 3
        builder.append("##  :"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        
        checkNoConclusionsForThisGrid(grid);
    
        builder = new StringBuilder();
        // ............ 0123
        builder.append("####:"); // 0
        builder.append("#..#:"); // 2
        builder.append("#.6 :"); // 3
        builder.append("##  :"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        
        checkNoConclusionsForThisGrid(grid);

        builder = new StringBuilder();
        // ............ 0123
        builder.append("####:"); // 0
        builder.append("#..#:"); // 2
        builder.append("#.5 :"); // 3
        builder.append("## .:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        
        checkNoConclusionsForThisGrid(grid);

        builder = new StringBuilder();
        // ............ 0123
        builder.append("####:"); // 0
        builder.append("#..#:"); // 2
        builder.append("#.. :"); // 3
        builder.append("##  :"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        
        checkNoConclusionsForThisGrid(grid);

    }

    @Test
    public void testConclusionApplied() throws Exception {

        StringBuilder builder;
        NurikabeGrid startGrid;
        NurikabeGrid expectedGrid;

        builder = new StringBuilder();
        // ............ 012345
        builder.append("2    2:"); // 0
        builder.append("      :"); // 1
        builder.append("  2#  :"); // 2
        builder.append("  #  4:"); // 3
        builder.append("3  #..:"); // 4
        startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        // ............ 012345
        builder.append("2    2:"); // 0
        builder.append(" #  # :"); // 1
        builder.append("  2## :"); // 2
        builder.append("  #  4:"); // 3
        builder.append("3  #..:"); // 4
        expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);

    }
    
}
