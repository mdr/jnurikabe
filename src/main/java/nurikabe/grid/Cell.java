package nurikabe.grid;

import static java.lang.Math.abs;
import static nurikabe.grid.CellType.LAND;
import static nurikabe.grid.CellType.UNDETERMINED;
import static nurikabe.grid.CellType.WATER;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Cell {

    private NurikabeGrid grid;

    private int row;

    private int column;

    private CellType type;

    public Cell(NurikabeGrid grid, int row, int column, CellType type) {
        this.grid = grid;
        this.row = row;
        this.column = column;
        this.type = type;
    }

    Set<Cell> getNeighboursOfType(Set<CellType> types) {
        Set<Cell> neighbours = getNeighbours();
        Iterator<Cell> iterator = neighbours.iterator();
        while (iterator.hasNext()) {
            Cell neighbour = iterator.next();
            if (!types.contains(neighbour.getType() ))
                iterator.remove(); // TODO: Fix this
        }
        return neighbours;
    }

    public Set<Cell> getNeighbours() {
        Set<Cell> neighbours = new HashSet<Cell>();
        if (row > 0)
            neighbours.add(grid.cellAt(row - 1, column));
        if (row < grid.numberOfRows() - 1)
            neighbours.add(grid.cellAt(row + 1, column));
        if (column > 0)
            neighbours.add(grid.cellAt(row, column - 1));
        if (column < grid.numberOfColumns() - 1)
            neighbours.add(grid.cellAt(row, column + 1));
        return neighbours;
    }

    public boolean isLand() {
        return type == LAND;
    }

    public boolean isWater() {
        return type == WATER;
    }

    public boolean isUndetermined() {
        return type == UNDETERMINED;
    }

    public boolean isAdjacentTo(Cell other) {
        return this.getCoordinate().isAdjacentTo(other.getCoordinate());
    }

    
    public boolean isIslandNumber() {
        return grid.getIslandNumbers().containsKey(this);
    }
    
    public Coordinate getCoordinate() {
        return Coordinate.makeCoordinate(row, column);
    }

    public Polyomino getPolyomino() {
        Set<CellType> types = EnumSet.of(type);
        return Polyomino.getPolyomino(this, types);
    }

    @Override
    public String toString() {
        return Coordinate.makeCoordinate(row, column).toString() + ": " + type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Cell other = (Cell) obj;
        if (column != other.column)
            return false;
        if (row != other.row)
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public CellType getType() {
        return type;
    }

    public int distanceTo(Cell other) {
        return abs(row - other.row) + abs(column - other.column);
    }

    public boolean isDiagonallyAdjacentTo(Cell other) {
        return abs(row - other.row) == 1 && abs(column - other.column) == 1;
    }

    public Set<Polyomino> possiblePolyominoesFromIslandNumberCell() {
        Set<Polyomino> polyominoes = new HashSet<Polyomino>();

        return polyominoes;
    }

    // TODO: Consider subclass for number cells
    // TODO: Refactor to tidy
    public Map<Cell, Integer> reachableCellsFromIslandNumberCell() {
        int islandNumber = grid.getIslandNumber(this);
        Map<Cell, Integer> distances = new HashMap<Cell, Integer>();
        Polyomino islandSoFar = getPolyomino();
        for (Cell cell : islandSoFar)
            distances.put(cell, 0);
        Set<Cell> boundary = islandSoFar.getCells();
        int initialSize = islandSoFar.size();
        int distance = 1;
        while (!boundary.isEmpty() && distance <= islandNumber - initialSize) {
            Set<Cell> newBoundary = new HashSet<Cell>();
            for (Cell cell : boundary) {
                for (Cell neighbour : cell.getNeighbours()) {
                    if (isPathPossibleToNeighbour(neighbour, distances, boundary)) {
                        distances.put(neighbour, distance);
                        newBoundary.add(neighbour);
                    }
                }
            }
            boundary = newBoundary;
            distance++;
        }
        return distances;
    }

    private boolean isPathPossibleToNeighbour(Cell neighbour, Map<Cell, Integer> distances,
            Set<Cell> boundary) {
        if (neighbour.isWater())
            return false;
        if (distances.containsKey(neighbour))
            return false;
        for (Cell neighbourNeighbour : neighbour.getNeighbours()) {
            if (boundary.contains(neighbourNeighbour))
                continue;
            if (neighbourNeighbour.isLandAndConnectedToAnIslandNumber())
                return false;
        }

        return true;
    }

    public boolean isLandAndConnectedToAnIslandNumber() {
        if (!isLand())
            return false;
        Set<Polyomino> allPartialIslandsConnectedToANumber = grid.getAllPartialIslandsConnectedToANumber();
        for (Polyomino polyomino : allPartialIslandsConnectedToANumber)
            if (polyomino.contains(this))
                return true;
        return false;
    }

    public Cell getNorthNeighbour() {
        if (row == 0)
            throw new IllegalArgumentException("Cell has no north neighbour");
        return grid.cellAt(row - 1, column);
    }
    public Cell getWestNeighbour() {
        if (column == 0)
            throw new IllegalArgumentException("Cell has no west neighbour");
        return grid.cellAt(row, column - 1);
    }
    public Cell getSouthNeighbour() {
        if (row == grid.numberOfRows() - 1)
            throw new IllegalArgumentException("Cell has no south neighbour");
        return grid.cellAt(row + 1, column);
    }
    public Cell getEastNeighbour() {
        if (column == grid.numberOfColumns() - 1)
            throw new IllegalArgumentException("Cell has no east neighbour");
        return grid.cellAt(row, column + 1);
    }
}
