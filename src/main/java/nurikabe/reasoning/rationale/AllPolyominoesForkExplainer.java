package nurikabe.reasoning.rationale;

import java.util.HashMap;
import java.util.Map;

import nurikabe.grid.Cell;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;

public class AllPolyominoesForkExplainer implements ForkExplainer {

    private final Cell islandNumberCell;

    private final Map<NurikabeGrid, Polyomino> possibleGrids;

    public AllPolyominoesForkExplainer(Cell islandNumberCell, Map<NurikabeGrid, Polyomino> possibleGrids) {
        this.islandNumberCell = islandNumberCell;
        this.possibleGrids = possibleGrids;
    }

    public Rationale makeRationale(Map<NurikabeGrid, Rationale> explanationsPerPossibleGrid) {
        Map<Polyomino, Rationale> reasonForEachPolyomino = new HashMap<Polyomino, Rationale>();
        for (NurikabeGrid grid : possibleGrids.keySet()) {
            Polyomino polyomino = possibleGrids.get(grid);
            Rationale rationale = explanationsPerPossibleGrid.get(grid);
            reasonForEachPolyomino.put(polyomino, rationale);
        }
        return new AllPolyominoesForkRationale(islandNumberCell, reasonForEachPolyomino);
    }

}
