package nurikabe.reasoning.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.Cell;
import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;
import nurikabe.reasoning.rationale.AllPolyominoesForkExplainer;
import nurikabe.reasoning.rationale.ForkExplainer;

public class AllPolyominoesGridForker implements AllPossibilitiesGridForker {

    public Set<ForkedGrid> forkGridIntoPossibilities(NurikabeGrid grid) {
        Set<ForkedGrid> forkedGrids = new HashSet<ForkedGrid>();
        for (Cell islandNumberCell : grid.getIslandNumberCells()) {
            // TODO: Consider factoring this code out into shared worker code somewhere
            Set<Polyomino> allPossiblePolyominoes = new TryAllPolyominoesReasoningStrategy()
                    .findAllPossiblePolyominoes(grid, islandNumberCell);
            Map<NurikabeGrid, Polyomino> possibleGrids = new HashMap<NurikabeGrid, Polyomino>();
            for (Polyomino polyomino : allPossiblePolyominoes) {
                NurikabeGrid possibleGrid = applyPolyomino(polyomino, grid);
                possibleGrids.put(possibleGrid, polyomino);
            }
            ForkExplainer forkExplainer = new AllPolyominoesForkExplainer(islandNumberCell, possibleGrids);
            forkedGrids.add(new ForkedGrid(forkExplainer, possibleGrids.keySet()));
        }
        return forkedGrids;
    }

    // TODO: This is a woefully inefficient way of doing this
    private NurikabeGrid applyPolyomino(Polyomino polyomino, NurikabeGrid grid) {
        Set<Cell> cells = polyomino.getCells();
        NurikabeGrid newGrid = grid;
        for (Cell cell : cells)
            if (cell.getType() == CellType.UNDETERMINED)
                newGrid = grid.concludeCellIsLand(cell.getRow(), cell.getColumn());
        return newGrid;
    }

}
