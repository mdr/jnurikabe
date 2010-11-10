package nurikabe.reasoning;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;

import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;
import nurikabe.reasoning.rationale.CompleteIslandIsSurroundedByWaterRationale;
import nurikabe.reasoning.rationale.CompositeRationale;
import nurikabe.reasoning.rationale.NoPoolsRationale;
import nurikabe.reasoning.rationale.OnlyOnePlaceToGrowRationale;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.UnknownPolyominoEnclosedByLandOrWaterRationale;
import nurikabe.reasoning.rationale.WaterOrLandLookaheadRationale;
import nurikabe.reasoning.strategy.CompleteIslandIsSurroundedByWaterReasoningStrategy;
import nurikabe.reasoning.strategy.NoPoolsReasoningStrategy;
import nurikabe.reasoning.strategy.OnlyOnePlaceToGrowReasoningStrategy;
import nurikabe.reasoning.strategy.UnknownPolyominoEnclosedByLandOrWaterReasoningStrategy;
import nurikabe.reasoning.strategy.WaterOrLandLookaheadReasoningStrategy;

import org.junit.Test;

public class WaterOrLandLookaheadReasoningStrategyTest extends AbstractReasoningStrategyTest {

    @Test
    public void testSimpleCaseWorks() throws Exception {
        strategy = new WaterOrLandLookaheadReasoningStrategy(new NoPoolsReasoningStrategy(),
                new OnlyOnePlaceToGrowReasoningStrategy());

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("  ##:"); // 0
        builder.append("    :"); // 1
        builder.append("  ##:"); // 2
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Conclusion conclusion = new Conclusion(1, 2, CellType.LAND);

        Rationale waterRationale = new CompositeRationale(
                new NoPoolsRationale(1, 2, NoPoolsRationale.Corner.SOUTH_WEST), new NoPoolsRationale(1, 2,
                        NoPoolsRationale.Corner.NORTH_WEST));

        NurikabeGrid ifLandGrid = new Conclusion(1, 3, CellType.LAND).applyTo(grid);
        Polyomino landPolyomino = ifLandGrid.cellAt(1, 3).getPolyomino();
        Rationale landRationale = new OnlyOnePlaceToGrowRationale(landPolyomino);

        Rationale rationale = new WaterOrLandLookaheadRationale(grid.cellAt(1, 3), waterRationale, landRationale);

        Map<Conclusion, Rationale> expectedConclusions = Collections.singletonMap(conclusion, rationale);
        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);
    }

    @Test
    public void testSomeOtherCase() throws Exception {
        strategy = new WaterOrLandLookaheadReasoningStrategy(new UnknownPolyominoEnclosedByLandOrWaterReasoningStrategy(),
                new CompleteIslandIsSurroundedByWaterReasoningStrategy());

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 01234
        builder.append("     :"); // 0
        builder.append(" #   :"); // 1
        builder.append("# #  :"); // 2
        builder.append("  2  :"); // 3
        builder.append("     :"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Conclusion conclusion = new Conclusion(2, 1, CellType.WATER);

        Rationale waterRationale = new UnknownPolyominoEnclosedByLandOrWaterRationale(CellType.WATER);

        Rationale landRationale = new CompleteIslandIsSurroundedByWaterRationale(3, 1);

        Rationale rationale = new WaterOrLandLookaheadRationale(grid.cellAt(3, 1), waterRationale, landRationale);

        Map<Conclusion, Rationale> expectedConclusions = Collections.singletonMap(conclusion, rationale);
        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);
    }

    
    // @Test
    // public void testCasesWhereRuleShouldNotApply() throws Exception {
    // StringBuilder builder;
    // NurikabeGrid grid;
    //
    // builder = new StringBuilder();
    // // ............ 0123
    // builder.append("# ##:"); // 0
    // builder.append("#..#:"); // 2
    // builder.append("#.5 :"); // 3
    // builder.append("## :"); // 4
    // grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
    //
    // checkNoConclusionsForThisGrid(grid);
    //
    // builder = new StringBuilder();
    // // ............ 0123
    // builder.append("####:"); // 0
    // builder.append("#..#:"); // 2
    // builder.append("#.6 :"); // 3
    // builder.append("## :"); // 4
    // grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
    //
    // checkNoConclusionsForThisGrid(grid);
    //
    // builder = new StringBuilder();
    // // ............ 0123
    // builder.append("####:"); // 0
    // builder.append("#..#:"); // 2
    // builder.append("#.5 :"); // 3
    // builder.append("## .:"); // 4
    // grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
    //
    // checkNoConclusionsForThisGrid(grid);
    //
    // builder = new StringBuilder();
    // // ............ 0123
    // builder.append("####:"); // 0
    // builder.append("#..#:"); // 2
    // builder.append("#.. :"); // 3
    // builder.append("## :"); // 4
    // grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
    //
    // checkNoConclusionsForThisGrid(grid);
    //
    // }
    //
    // @Test
    // public void testConclusionApplied() throws Exception {
    //
    // StringBuilder builder;
    // NurikabeGrid startGrid;
    // NurikabeGrid expectedGrid;
    //
    // builder = new StringBuilder();
    // // ............ 012345
    // builder.append("2 2:"); // 0
    // builder.append(" :"); // 1
    // builder.append(" 2# :"); // 2
    // builder.append(" # 4:"); // 3
    // builder.append("3 #..:"); // 4
    // startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());
    //
    // builder = new StringBuilder();
    // // ............ 012345
    // builder.append("2 2:"); // 0
    // builder.append(" # # :"); // 1
    // builder.append(" 2## :"); // 2
    // builder.append(" # 4:"); // 3
    // builder.append("3 #..:"); // 4
    // expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());
    //
    // checkResultsOfStrategyAppliedToGrid(startGrid, expectedGrid);
    //
    // }
}