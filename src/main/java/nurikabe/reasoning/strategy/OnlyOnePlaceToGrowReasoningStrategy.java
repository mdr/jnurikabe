package nurikabe.reasoning.strategy;

import static nurikabe.grid.CellType.LAND;
import static nurikabe.grid.CellType.UNDETERMINED;
import static nurikabe.grid.CellType.WATER;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.Cell;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.OnlyOnePlaceToGrowRationale;
import nurikabe.reasoning.rationale.Rationale;

public class OnlyOnePlaceToGrowReasoningStrategy implements ReasoningStrategy {

    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) {
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();

        Set<Polyomino> landPolyominoes = grid.getPolyominoesOfType(LAND);
        lookForUniquePlacesToGrow(landPolyominoes, grid, conclusions);
        Set<Polyomino> waterPolyominoes = grid.getPolyominoesOfType(WATER);
        if (waterPolyominoes.size() > 1)
            lookForUniquePlacesToGrow(waterPolyominoes, grid, conclusions);

        return conclusions;
    }

    private void lookForUniquePlacesToGrow(Set<Polyomino> landPolyominoes, NurikabeGrid grid,
            Map<Conclusion, Rationale> conclusions) {
        for (Polyomino polyomino : landPolyominoes) {
            if (polyomino.getType() == LAND && grid.isCompleteIsland(polyomino))
                continue;// TODO: Should perhaps pass this in as a strategy

            Set<Cell> externalBoundary = polyomino.getExternalBoundary();
            Cell undeterminedCell = null;
            int undeterminedCount = 0;
            for (Cell cell : externalBoundary) {
                if (cell.getType() == UNDETERMINED) {
                    undeterminedCell = cell;
                    if (++undeterminedCount > 1)
                        break;
                }
            }

            if (undeterminedCell != null && undeterminedCount == 1)
                makeConclusion(conclusions, undeterminedCell, polyomino);

        }
    }

    private void makeConclusion(Map<Conclusion, Rationale> conclusions, Cell undeterminedCell, Polyomino polyomino) {
        Conclusion conclusion = new Conclusion(undeterminedCell.getCoordinate(), polyomino.getType());
        Rationale rationale = new OnlyOnePlaceToGrowRationale(polyomino);
        Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);
    }

}
