package nurikabe.reasoning.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.Cell;
import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.CompleteIslandIsSurroundedByWaterRationale;
import nurikabe.reasoning.rationale.Rationale;


public class CompleteIslandIsSurroundedByWaterReasoningStrategy implements ReasoningStrategy {

    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) {
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();

        Set<Polyomino> completedIslands = grid.getCompletedIslands();
        for (Polyomino island : completedIslands)
            probeNeighboursOfIsland(island, grid, conclusions);

        return conclusions;
    }

    private void probeNeighboursOfIsland(Polyomino island, NurikabeGrid grid, Map<Conclusion, Rationale> conclusions) {
        for (Cell islandCell : island)
            for (Cell neighbour : islandCell.getNeighbours())
                if (neighbour.isUndetermined())
                    addConclusion(conclusions, islandCell, neighbour);
    }

    private void addConclusion(Map<Conclusion, Rationale> conclusions, Cell islandCell, Cell neighbour) {
        Conclusion conclusion = new Conclusion(neighbour.getCoordinate(), CellType.WATER);
        Rationale rationale = new CompleteIslandIsSurroundedByWaterRationale(islandCell.getCoordinate());
        Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);
    }

}
