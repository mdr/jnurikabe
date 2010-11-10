package nurikabe.reasoning.contradiction;

import java.util.HashSet;
import java.util.Set;

import nurikabe.grid.Cell;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;

//TODO: Remove equivalent functionality from NurikabeGrid & Test
public class HasPoolsContradictionDetector implements ContradictionDetector {

    public Set<Contradiction> findContradictions(NurikabeGrid grid) {
        Set<Contradiction> contradictions = new HashSet<Contradiction>();
        int numberOfRows = grid.numberOfRows();
        int numberOfColumns = grid.numberOfColumns();
        if (numberOfRows < 2 || numberOfColumns < 2)
            return contradictions;
        for (int row = 0; row < numberOfRows - 1; row++)
            for (int column = 0; column < numberOfColumns - 1; column++)
                checkForPoolAtPoint(grid, contradictions, row, column);
        return contradictions;
    }

    private void checkForPoolAtPoint(NurikabeGrid grid, Set<Contradiction> contradictions, int row, int column) {
        Set<Cell> cells = new HashSet<Cell>();
        cells.add(grid.cellAt(row, column));
        cells.add(grid.cellAt(row + 1, column));
        cells.add(grid.cellAt(row, column + 1));
        cells.add(grid.cellAt(row + 1, column + 1));
        for (Cell cell : cells)
            if (!cell.isWater())
                return;
        HasPoolsContradiction contradiction = new HasPoolsContradiction(new Polyomino(cells));
        contradictions.add(contradiction);
    }

}
