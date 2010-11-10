package nurikabe.reasoning.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.Cell;
import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.NoPathToCellFromAnyNumberRationale;
import nurikabe.reasoning.rationale.Rationale;

public class NoPathToCellFromAnyNumberReasoningStrategy implements ReasoningStrategy {

    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) {

        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();
        Map<Cell, Set<Cell>> cellsToPossibleIslandsMap = calculateCellsToPossibleIslandsMap(grid);
        Set<Cell> reachableCells = cellsToPossibleIslandsMap.keySet();
        for (Cell undeterminedCell : grid.getUndeterminedCells())
            if (!reachableCells.contains(undeterminedCell))
                addNewConclusion(conclusions, undeterminedCell);
        
        return conclusions;
    }

    private void addNewConclusion(Map<Conclusion, Rationale> conclusions, Cell cell) {
        Conclusion conclusion = new Conclusion(cell.getCoordinate(), CellType.WATER);
        Rationale rationale = new NoPathToCellFromAnyNumberRationale();
        Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);
    }

    // TODO: obsolete; remove
    public static Set<Cell> findAllIslandsWhichCanReachCell(NurikabeGrid grid, Cell targetCell) {
        Set<Cell> islandsWhichCanReach = new HashSet<Cell>();
        for (Cell numberCell : grid.getIslandNumberCells()) {
            int number = grid.getIslandNumber(numberCell);
            int distance = targetCell.distanceTo(numberCell);
            if (distance >= number)
                continue;
            if (numberCell.reachableCellsFromIslandNumberCell().keySet().contains(targetCell))
                islandsWhichCanReach.add(numberCell);
        }
        return islandsWhichCanReach;
    }

    /**
     * Map from cells in grid to all islands which are reachable 
     */
    public static Map<Cell, Set<Cell>> calculateCellsToPossibleIslandsMap(NurikabeGrid grid) {
        Map<Cell, Set<Cell>> reachableMap = new HashMap<Cell, Set<Cell>>();
        for (Cell islandNumberCell : grid.getIslandNumberCells()) {
            Set<Cell> cellsReachableFromThisIsland = islandNumberCell.reachableCellsFromIslandNumberCell().keySet();
            for (Cell reachableCell : cellsReachableFromThisIsland) 
                addIslandNumberCell(reachableMap, reachableCell, islandNumberCell);
        }
        return reachableMap;
    }

    private static void addIslandNumberCell(Map<Cell, Set<Cell>> reachableMap, Cell reachableCell, Cell islandNumberCell) {
        Set<Cell> islandsWhichCanReachThisCell = reachableMap.get(reachableCell);
        if (islandsWhichCanReachThisCell == null) {
            islandsWhichCanReachThisCell = new HashSet<Cell>();
            reachableMap.put(reachableCell, islandsWhichCanReachThisCell);
        }
        islandsWhichCanReachThisCell.add(islandNumberCell);
    }

}
