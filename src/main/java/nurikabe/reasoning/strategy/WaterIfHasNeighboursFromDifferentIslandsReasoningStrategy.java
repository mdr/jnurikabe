package nurikabe.reasoning.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.Cell;
import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.WaterIfHasNeighboursFromDifferentIslandsRationale;

public class WaterIfHasNeighboursFromDifferentIslandsReasoningStrategy implements ReasoningStrategy {

    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) {
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();
        Set<Polyomino> partialIslands = grid.getAllPartialIslandsConnectedToANumber();

        for (Cell cell : grid.getUndeterminedCells()) {
            Set<Polyomino> neighbouringPartialIslands = getNeighbouringPartialIslands(partialIslands, cell);
            if (neighbouringPartialIslands.size() > 1) {
                Conclusion conclusion = new Conclusion(cell.getCoordinate(), CellType.WATER);
                Rationale rationale = new WaterIfHasNeighboursFromDifferentIslandsRationale(neighbouringPartialIslands);
                Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);
            }

        }
        return conclusions;
    }

    private Set<Polyomino> getNeighbouringPartialIslands(Set<Polyomino> partialIslands, Cell cell) {
        Set<Polyomino> neighbourPartialIslands = new HashSet<Polyomino>();
        Set<Cell> neighbours = cell.getNeighbours();
        for (Cell neighbour : neighbours) {
            if (!neighbour.isLand())
                continue;
            Polyomino neighbourPolyomino = neighbour.getPolyomino();
            if (!partialIslands.contains(neighbourPolyomino))
                continue;
            neighbourPartialIslands.add(neighbourPolyomino);
        }
        return neighbourPartialIslands;
    }

}
