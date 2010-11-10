package nurikabe.reasoning.strategy;

import java.util.HashMap;
import java.util.Map;

import nurikabe.grid.Cell;
import nurikabe.grid.CellType;
import nurikabe.grid.Coordinate;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.UnknownPolyominoEnclosedByLandOrWaterRationale;

public class UnknownPolyominoEnclosedByLandOrWaterReasoningStrategy implements ReasoningStrategy {

    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) {

        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();

        boolean someWaterInGrid = grid.getWaterCells().size() > 0;
        for (Polyomino undeterminedPolyomino : grid.getPolyominoesOfType(CellType.UNDETERMINED)) {
            if (undeterminedPolyomino.isEnclosedByWater())
                for (Cell cell : undeterminedPolyomino)
                    makeConclusion(conclusions, cell.getCoordinate(), CellType.WATER);
            if (someWaterInGrid) {
                if (undeterminedPolyomino.isEnclosedByLand())
                    for (Cell cell : undeterminedPolyomino)
                        makeConclusion(conclusions, cell.getCoordinate(), CellType.LAND);
            }
        }

        return conclusions;
    }

    private void makeConclusion(Map<Conclusion, Rationale> conclusions, Coordinate coordinate, CellType cellType) {
        Conclusion conclusion = new Conclusion(coordinate, cellType);
        Rationale rationale = new UnknownPolyominoEnclosedByLandOrWaterRationale(cellType);
        Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);
    }

}
