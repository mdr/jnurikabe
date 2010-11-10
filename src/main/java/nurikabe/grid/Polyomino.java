package nurikabe.grid;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.StringUtils;

public class Polyomino implements Iterable<Cell> {

    private Set<Cell> cells;

    private CellType type = null;

    public Polyomino(Cell... cells) {
        this(new HashSet<Cell>(Arrays.asList(cells)));
    }

    public Polyomino(Set<Cell> cells) {
        if (!isPolyomino(cells))
            throw new IllegalArgumentException("Cells do not form a polyomino: " + cells);

        this.cells = cells;
        if (cells.size() > 0)
            type = cells.iterator().next().getType();
    }

    /**
     * Can return null if the polyomino is 0-sized
     */
    public CellType getType() {
        return type;
    }

    public int size() {
        return cells.size();
    }

    public Iterator<Cell> iterator() {
        return cells.iterator();
    }

    public boolean contains(Cell cell) {
        return cells.contains(cell);
    }

    @Override
    public String toString() {
        return "Polyomino(" + StringUtils.join(cells, ", ") + ")";
    }

    @Override
    public int hashCode() {
        return cells.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Polyomino other = (Polyomino) obj;
        if (cells == null) {
            if (other.cells != null)
                return false;
        } else if (!cells.equals(other.cells))
            return false;
        return true;
    }

    public Set<Cell> getExternalBoundary() {
        Set<Cell> boundary = new HashSet<Cell>();
        for (Cell cell : cells) {
            for (Cell neighbour : cell.getNeighbours())
                if (!cells.contains(neighbour))
                    boundary.add(neighbour);
        }
        return boundary;
    }

    public boolean isEnclosedByWater() {
        return isEnclosedByType(CellType.WATER);
    }

    public boolean isEnclosedByLand() {
        return isEnclosedByType(CellType.LAND);
    }

    private boolean isEnclosedByType(CellType cellType) {
        boolean enclosed = true;
        for (Cell boundaryPoint : getExternalBoundary()) {
            if (boundaryPoint.getType() != cellType) {
                enclosed = false;
                break;
            }
        }
        return enclosed;
    }

    public static boolean isPolyomino(Set<Cell> cells) {
        if (cells.size() <= 1)
            return true;
        Set<Cell> unresolvedCells = new HashSet<Cell>(cells);

        Cell firstCell = unresolvedCells.iterator().next();
        // CellType type = firstCell.getType();
        Set<Cell> connectedBoundary = new HashSet<Cell>();
        connectedBoundary.add(firstCell);

        while (!unresolvedCells.isEmpty()) {
            Set<Cell> newlyResolved = new HashSet<Cell>();
            for (Cell unresolvedCell : unresolvedCells) {
                // if (unresolvedCell.getType() != type)
                // return false;
                for (Cell boundaryCell : connectedBoundary) {
                    if (boundaryCell.isAdjacentTo(unresolvedCell)) {
                        connectedBoundary.add(unresolvedCell);
                        newlyResolved.add(unresolvedCell);
                        break;
                    }
                }

            }
            if (newlyResolved.isEmpty())
                return false;
            unresolvedCells.removeAll(newlyResolved);
            connectedBoundary = newlyResolved;

        }

        return true;

    }

    public Set<Cell> getCells() {
        return cells;
    }

    // TODO: What to do about non-land polys?
    // TODO: Contract: non-water poly connected to number
    public Set<Polyomino> growLandPolyominoInAllPossibleWays() {
        Set<Polyomino> polyominoes = new HashSet<Polyomino>();
        Polyomino partialIsland = getPartialIsland();

        Set<Cell> neighbours = getExternalBoundary();
        for (Cell neighbour : neighbours) {
            if (canExpandToNeighbour(neighbour, partialIsland)) {
                polyominoes.add(this.grow(neighbour));
            }
        }
        return polyominoes;
    }

    private Polyomino getPartialIsland() {
        for (Cell cell : cells)
            if (cell.isIslandNumber())
                return cell.getPolyomino();
        throw new AssertionError("This should never be called if no partial island is present");
    }

    private boolean canExpandToNeighbour(Cell neighbour, Polyomino partialIsland) {
        if (neighbour.isWater() || this.contains(neighbour))
            return false;
        for (Cell neighbourNeighbour : neighbour.getNeighbours())
            if (!partialIsland.contains(neighbourNeighbour) && neighbourNeighbour.isLandAndConnectedToAnIslandNumber())
                return false;
        return true;
    }

    // TODO: test me further
    public Polyomino grow(Cell newCell) {
        Set<Cell> newCells = new HashSet<Cell>(cells);
        newCells.add(newCell);
        return new Polyomino(newCells);
    }

    public static Polyomino getPolyomino(Cell startCell, Set<CellType> types) {
        return getPolyomino(startCell, types, Collections.<Cell>emptySet());
    }

    public static Polyomino getPolyomino(Cell startCell, Set<CellType> types, Set<Cell> cellsToAvoid) {
        Set<Cell> polyominoCells = new HashSet<Cell>();
        polyominoCells.add(startCell);
        Set<Cell> boundaryCells = new HashSet<Cell>(polyominoCells);
        while (!boundaryCells.isEmpty()) {
            Set<Cell> newCells = new HashSet<Cell>();
            for (Cell cell : boundaryCells) {
                Set<Cell> neighbours = cell.getNeighboursOfType(types);
                for (Cell neighbour : neighbours) {
                    if (cellsToAvoid.contains(neighbour))
                        continue;
                    else if (!polyominoCells.contains(neighbour))
                        newCells.add(neighbour);
                }

            }
            polyominoCells.addAll(newCells);
            boundaryCells = newCells;
        }
        return new Polyomino(polyominoCells);
    }

}
