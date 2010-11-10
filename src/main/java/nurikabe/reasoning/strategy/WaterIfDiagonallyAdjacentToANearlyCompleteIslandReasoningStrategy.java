package nurikabe.reasoning.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.Cell;
import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.WaterIfDiagonallyAdjacentToANearlyCompleteIslandRationale;

public class WaterIfDiagonallyAdjacentToANearlyCompleteIslandReasoningStrategy implements ReasoningStrategy {

    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) { // TODO: Cleanup this!
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();

        Set<Cell> islandNumberCells = grid.getIslandNumberCells();
        for (Cell islandNumberCell : islandNumberCells) {
            int number = grid.getIslandNumber(islandNumberCell);
            Polyomino islandNumberPolyomino = islandNumberCell.getPolyomino();
            if (islandNumberPolyomino.size() != number - 1)
                continue;
            Set<Cell> undeterminedCells = getUndeterminedCellsInBoundary(islandNumberPolyomino);
            if (undeterminedCells.size() != 2)
                continue;
            Iterator<Cell> iterator = undeterminedCells.iterator();
            Cell cellOne = iterator.next();
            Cell cellTwo = iterator.next();
            if (!cellOne.isDiagonallyAdjacentTo(cellTwo))
                continue;
            Cell candidateCell = getCandidateCell(grid, islandNumberPolyomino, cellOne, cellTwo);
            if (!candidateCell.isUndetermined())
                continue;
            Conclusion conclusion = new Conclusion(candidateCell.getCoordinate(), CellType.WATER);
            Rationale rationale = new WaterIfDiagonallyAdjacentToANearlyCompleteIslandRationale(islandNumberPolyomino,
                    cellOne.getCoordinate(), cellTwo.getCoordinate());
            Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);

        }

        return conclusions;
    }

    // TODO: Document and/or refactor
    private Cell getCandidateCell(NurikabeGrid grid, Polyomino islandNumberPolyomino, Cell cellOne, Cell cellTwo) {
        int row1 = cellOne.getRow();
        int row2 = cellTwo.getRow();
        int column1 = cellOne.getColumn();
        int column2 = cellTwo.getColumn();
        Cell otherCellOne = grid.cellAt(row1, column2);
        Cell otherCellTwo = grid.cellAt(row2, column1);
        if (islandNumberPolyomino.contains(otherCellOne))
            return otherCellTwo;
        else if (islandNumberPolyomino.contains(otherCellTwo))
            return otherCellOne;
        else
            throw new AssertionError("This should not occur");
    }

    private Set<Cell> getUndeterminedCellsInBoundary(Polyomino islandNumberPolyomino) {
        Set<Cell> externalBoundary = islandNumberPolyomino.getExternalBoundary();
        Set<Cell> undeterminedCells = new HashSet<Cell>();
        for (Cell boundaryCell : externalBoundary)
            if (boundaryCell.isUndetermined())
                undeterminedCells.add(boundaryCell);
        return undeterminedCells;
    }

}
