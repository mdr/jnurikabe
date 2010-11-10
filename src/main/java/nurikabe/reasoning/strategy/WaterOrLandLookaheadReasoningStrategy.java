package nurikabe.reasoning.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.AutoSolver;
import nurikabe.grid.Cell;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.WaterOrLandLookaheadRationale;
import utils.CollectionsUtils;

public class WaterOrLandLookaheadReasoningStrategy implements ReasoningStrategy {

    private List<ReasoningStrategy> strategies;

    public WaterOrLandLookaheadReasoningStrategy(ReasoningStrategy... strategies) {
        this(Arrays.asList(strategies));
    }

    public WaterOrLandLookaheadReasoningStrategy(List<ReasoningStrategy> strategies) {
        this.strategies = new ArrayList<ReasoningStrategy>(strategies);
    }

    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) {
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();
        AutoSolver autoSolver = new AutoSolver(strategies);

        for (Cell undeterminedCell : grid.getUndeterminedCells()) {
            int row = undeterminedCell.getRow();
            int column = undeterminedCell.getColumn();
            NurikabeGrid ifWaterGrid = grid.concludeCellIsWater(row, column);
            Map<Conclusion, Rationale> waterConclusionsMap = autoSolver.getConclusions(ifWaterGrid);
            NurikabeGrid ifLandGrid = grid.concludeCellIsLand(row, column);
            Map<Conclusion, Rationale> landConclusionsMap = autoSolver.getConclusions(ifLandGrid);
            Set<Conclusion> intersection = CollectionsUtils.intersection(waterConclusionsMap.keySet(),
                    landConclusionsMap.keySet());
            for (Conclusion conclusion : intersection) {
                Rationale waterRationale = waterConclusionsMap.get(conclusion);
                Rationale landRationale = landConclusionsMap.get(conclusion);
                Rationale rationale = new WaterOrLandLookaheadRationale(undeterminedCell, waterRationale, landRationale);
                Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);
            }
        }
        return conclusions;
    }

}
