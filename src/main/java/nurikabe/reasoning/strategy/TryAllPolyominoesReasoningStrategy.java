package nurikabe.reasoning.strategy;

import java.util.Collections;
import java.util.EnumSet;
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
import nurikabe.reasoning.rationale.TryAllPolyominoesRationale;
import utils.CollectionsUtils;

public class TryAllPolyominoesReasoningStrategy implements ReasoningStrategy {

    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) {
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();
        for (Cell islandNumberCell : grid.getIslandNumberCells()) {
            Set<Polyomino> allPossiblePolyominoes = findAllPossiblePolyominoes(grid, islandNumberCell);
            Set<Cell> intersection = intersectAll(allPossiblePolyominoes);
            for (Cell cell : intersection) {
                if (!cell.isUndetermined())
                    continue;
                Conclusion conclusion = new Conclusion(cell.getCoordinate(), CellType.LAND);
                Rationale rationale = new TryAllPolyominoesRationale(islandNumberCell);
                Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);
            }
        }
        return conclusions;
    }

    // TODO: Use CollectionsUtils.intersectAll
    private Set<Cell> intersectAll(Set<Polyomino> polyominoes) {
        if (polyominoes.size() == 0)
            return Collections.emptySet();
        Set<Cell> intersection = null;
        for (Polyomino polyomino : polyominoes) {
            if (intersection == null)
                intersection = new HashSet<Cell>(polyomino.getCells());
            else
                intersection.retainAll(polyomino.getCells());
        }
        return intersection;
    }

    public Set<Polyomino> findAllPossiblePolyominoes(NurikabeGrid grid, Cell islandNumberCell) {
        Map<Cell, Set<Cell>> cellsToPossibleIslandsMap = NoPathToCellFromAnyNumberReasoningStrategy
                .calculateCellsToPossibleIslandsMap(grid);

        Set<Polyomino> workingPolyominoes = findCandidatePolyominoes(grid, islandNumberCell);

        filterPolyominoesThatDoNotContainMandatoryCells(workingPolyominoes, grid, islandNumberCell,
                cellsToPossibleIslandsMap);

        Map<Cell, Set<Cell>> obligatoryBlocksMap = calculateObligatoryBlocks(grid, cellsToPossibleIslandsMap);
        Set<Cell> obligatoryBlocks = obligatoryBlocksMap.get(islandNumberCell);
        if (obligatoryBlocks != null)
            filterPolyominoesThatDoNotSatisfyObligatoryBlocks(workingPolyominoes, obligatoryBlocks);

        filterPolyominoesThatSplitWater(workingPolyominoes, grid);

        return workingPolyominoes;
    }

    private void filterPolyominoesThatSplitWater(Set<Polyomino> polyominoes, NurikabeGrid grid) {
        Set<Polyomino> polyominoesToRemove = new HashSet<Polyomino>();
        polyomino: for (Polyomino polyomino : polyominoes) {
            Set<Cell> polyominoNeighbourCells = polyomino.getExternalBoundary();
            if (polyominoIsEntireGrid(polyominoNeighbourCells))
                return;
            Polyomino surroundingWaterAndUnknownPolyomino = getSurroundingWaterAndUnknownPolyomino(polyomino, polyominoNeighbourCells);
            for (Cell polyominoNeighbourCell : polyominoNeighbourCells) {
                if (!surroundingWaterAndUnknownPolyomino.contains(polyominoNeighbourCell)) {
                    polyominoesToRemove.add(polyomino);
                    continue polyomino;
                }
            }
        }
        polyominoes.removeAll(polyominoesToRemove);

    }

    private Polyomino getSurroundingWaterAndUnknownPolyomino(Polyomino polyomino, Set<Cell> polyominoNeighbourCells) {
        Cell aNeighbourCell = CollectionsUtils.getFirstElementOf(polyominoNeighbourCells);
        Set<CellType> types = EnumSet.of(CellType.UNDETERMINED, CellType.WATER);
        return Polyomino.getPolyomino(aNeighbourCell, types, polyomino.getCells());
    }

    private boolean polyominoIsEntireGrid(Set<Cell> externalBoundary) {
        return externalBoundary.size() == 0;
    }

    private Set<Polyomino> findCandidatePolyominoes(NurikabeGrid grid, Cell islandNumberCell) {
        int islandNumber = grid.getIslandNumber(islandNumberCell);
        Polyomino startPolyomino = islandNumberCell.getPolyomino();
        int size = startPolyomino.size();
        Set<Polyomino> workingPolyominoes = new HashSet<Polyomino>();
        workingPolyominoes.add(startPolyomino);
        while (size < islandNumber) {
            Set<Polyomino> newPolyominoes = new HashSet<Polyomino>();
            for (Polyomino workingPolyomino : workingPolyominoes) {
                Set<Polyomino> grownPolyominoes = workingPolyomino.growLandPolyominoInAllPossibleWays();
                newPolyominoes.addAll(grownPolyominoes);
            }
            size++;
            workingPolyominoes = newPolyominoes;
        }
        filterPolyominoesThatCouldExpandFurther(workingPolyominoes);
        return workingPolyominoes;
    }

    private void filterPolyominoesThatDoNotSatisfyObligatoryBlocks(Set<Polyomino> workingPolyominoes,
            Set<Cell> obligatoryBlocks) {
        Set<Polyomino> polyominoesToRemove = new HashSet<Polyomino>();
        for (Polyomino polyomino : workingPolyominoes) {
            boolean containsAtLeastOne = false;
            for (Cell cell : obligatoryBlocks) {
                if (polyomino.contains(cell)) {
                    containsAtLeastOne = true;
                    break;
                }
            }
            if (!containsAtLeastOne) {
                polyominoesToRemove.add(polyomino);

            }

        }
        workingPolyominoes.removeAll(polyominoesToRemove);
    }

    private Set<Cell> findMandatoryCells(NurikabeGrid grid, Cell islandNumberCell,
            Map<Cell, Set<Cell>> cellsToPossibleIslandsMap) {
        Set<Cell> mandatoryCells = new HashSet<Cell>();

        for (Cell landCell : grid.getLandCells()) {
            Set<Cell> possibleIslands = cellsToPossibleIslandsMap.get(landCell);
            if (possibleIslands != null && possibleIslands.size() == 1 && possibleIslands.contains(islandNumberCell))
                mandatoryCells.add(landCell);
        }
        return mandatoryCells;
    }

    private Map<Cell, Set<Cell>> calculateObligatoryBlocks(NurikabeGrid grid,
            Map<Cell, Set<Cell>> cellsToPossibleIslandsMap) {
        Map<Cell, Set<Cell>> islandsToOneOfSet = new HashMap<Cell, Set<Cell>>();
        int numberOfRows = grid.numberOfRows();
        int numberOfColumns = grid.numberOfColumns();
        if (numberOfRows < 2 || numberOfColumns < 2)
            return islandsToOneOfSet;
        for (int row = 0; row < numberOfRows - 1; row++) {
            blockSearch: for (int column = 0; column < numberOfColumns - 1; column++) {
                Cell topLeft = grid.cellAt(row, column);
                Cell bottomLeft = grid.cellAt(row + 1, column);
                Cell topRight = grid.cellAt(row, column + 1);
                Cell bottomRight = grid.cellAt(row + 1, column + 1);
                Set<Cell> block = CollectionsUtils.makeSet(topLeft, bottomLeft, topRight, bottomRight);
                Set<Cell> waterCells = new HashSet<Cell>();
                Set<Cell> undeterminedCells = new HashSet<Cell>();
                for (Cell cell : block) {
                    switch (cell.getType()) {
                        case LAND:
                            continue blockSearch;
                        case WATER:
                            waterCells.add(cell);
                            break;
                        case UNDETERMINED:
                            undeterminedCells.add(cell);
                            break;
                    }
                }
                if (waterCells.size() == 4)
                    continue blockSearch;
                Set<Cell> islandsThatCanReachThisBlock = new HashSet<Cell>();
                for (Cell undeterminedCell : undeterminedCells) {
                    Set<Cell> possibleIslands = cellsToPossibleIslandsMap.get(undeterminedCell);
                    if (possibleIslands == null || possibleIslands.size() != 1)
                        continue blockSearch;
                    islandsThatCanReachThisBlock.add(CollectionsUtils.getSingleElementOf(possibleIslands));

                }
                if (islandsThatCanReachThisBlock.size() != 1)
                    continue blockSearch;

                Cell onlyIslandThatCanReachThisBlock = CollectionsUtils
                        .getSingleElementOf(islandsThatCanReachThisBlock);
                islandsToOneOfSet.put(onlyIslandThatCanReachThisBlock, undeterminedCells);
            }
        }
        return islandsToOneOfSet;
    }

    private void filterPolyominoesThatDoNotContainMandatoryCells(Set<Polyomino> polyominoes, NurikabeGrid grid,
            Cell islandNumberCell, Map<Cell, Set<Cell>> cellsToPossibleIslandsMap) {
        Set<Cell> mandatoryCells = findMandatoryCells(grid, islandNumberCell, cellsToPossibleIslandsMap);

        Iterator<Polyomino> iterator = polyominoes.iterator();

        polyominoLoop: while (iterator.hasNext()) {
            Polyomino polyomino = iterator.next();
            for (Cell cell : mandatoryCells) {
                if (!polyomino.contains(cell)) {
                    iterator.remove();
                    continue polyominoLoop;
                }
            }
        }
    }

    /**
     * Filter polyominoes that are adjacent to a land square
     */
    private void filterPolyominoesThatCouldExpandFurther(Set<Polyomino> polyominoes) {
        Iterator<Polyomino> iterator = polyominoes.iterator();
        while (iterator.hasNext()) {
            Polyomino polyomino = iterator.next();
            Set<Cell> neighbours = polyomino.getExternalBoundary();
            boolean couldExpandFurther = false;
            for (Cell neighbour : neighbours) {
                if (neighbour.isLand()) {
                    couldExpandFurther = true;
                    break;
                }
            }
            if (couldExpandFurther)
                iterator.remove();
        }
    }

}
