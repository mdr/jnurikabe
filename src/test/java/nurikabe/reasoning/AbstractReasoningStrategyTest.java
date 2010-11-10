package nurikabe.reasoning;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.strategy.ReasoningStrategy;

public abstract class AbstractReasoningStrategyTest {

    protected ReasoningStrategy strategy;

    protected void checkResultsOfStrategyAppliedToGrid(NurikabeGrid startGrid, NurikabeGrid expectedGrid) {
        Map<Conclusion, Rationale> conclusionsAndRationales = strategy.makeConclusions(startGrid);
        Set<Conclusion> conclusions = conclusionsAndRationales.keySet();
        NurikabeGrid actualGrid = Conclusion.applyAll(conclusions, startGrid);
        assertEquals(expectedGrid, actualGrid);
    }

    protected void checkNoConclusionsForThisGrid(NurikabeGrid grid) {
        Map<Conclusion, Rationale> expectedConclusions = Collections.emptyMap();
        Map<Conclusion, Rationale> actualConclusions = strategy.makeConclusions(grid);
        assertEquals(expectedConclusions, actualConclusions);
    }
    
}
