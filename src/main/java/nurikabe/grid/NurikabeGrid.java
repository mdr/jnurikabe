package nurikabe.grid;

import static nurikabe.grid.CellType.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NurikabeGrid implements Grid<Cell> {

    private final Map<Cell, Integer> islandNumbers;

    private final List<List<Cell>> cellGrid;

    private final int numberOfRows;

    private final int numberOfColumns;

    // TODO: Keep or delete?
    // private List<List<CellType>> safeCopy(List<List<CellType>> aCellGrid) {
    // List<List<CellType>> newCellGrid = new ArrayList<List<CellType>>();
    // for (List<CellType> row : aCellGrid) {
    // List<CellType> safeCopy = Collections.unmodifiableList(new ArrayList<CellType>(row));
    // newCellGrid.add(safeCopy);
    // }
    // return Collections.unmodifiableList(newCellGrid);
    // }
    //
    // private Map<Cell, Integer> safeCopy(Map<Cell, Integer> someIslandNumbers) {
    // Map<Cell, Integer> newIslandNumbers = new HashMap<Cell, Integer>(someIslandNumbers);
    // return Collections.unmodifiableMap(newIslandNumbers);
    // }

    private NurikabeGrid(List<List<CellType>> cellTypeGrid, Map<Coordinate, Integer> islandNumbersMap) {
        numberOfRows = cellTypeGrid.size();
        numberOfColumns = cellTypeGrid.get(0).size();

        this.cellGrid = new ArrayList<List<Cell>>(numberOfRows);
        for (int row = 0; row < numberOfRows; row++) {
            List<Cell> rowList = new ArrayList<Cell>(numberOfColumns);
            for (int column = 0; column < numberOfColumns; column++) {
                CellType cellType = cellTypeGrid.get(row).get(column);
                Cell cell = new Cell(this, row, column, cellType);
                rowList.add(cell);
            }
            this.cellGrid.add(rowList);
        }

        this.islandNumbers = new HashMap<Cell, Integer>();
        for (Coordinate coordinate : islandNumbersMap.keySet()) {
            Cell cell = cellAt(coordinate);
            Integer number = islandNumbersMap.get(coordinate);
            this.islandNumbers.put(cell, number);
        }
    }

    public int getIslandNumber(int row, int column) {
        return islandNumbers.get(cellAt(row, column));
    }

    public int getIslandNumber(Cell cell) {
        return islandNumbers.get(cell);
    }
    
    public boolean hasNumberFor(int row, int column) {
        return islandNumbers.containsKey(cellAt(row, column));
    }

    public Map<Cell, Integer> getIslandNumbers() {
        return Collections.unmodifiableMap(islandNumbers);
    }

    public Cell cellAt(int row, int column) {
        return cellGrid.get(row).get(column);
    }

    public Cell cellAt(Coordinate coordinate) {
        return cellAt(coordinate.getRow(), coordinate.getColumn());
    }

    public int numberOfColumns() {
        return numberOfColumns;
    }

    public int numberOfRows() {
        return numberOfRows;
    }

    public boolean isComplete() {
        for (int row = 0; row < numberOfRows; row++)
            for (int column = 0; column < numberOfColumns; column++)
                if (cellAt(row, column).isUndetermined())
                    return false;
        return true;
    }

    public boolean hasPools() {
        if (numberOfRows < 2 || numberOfColumns < 2)
            return false;
        for (int row = 0; row < numberOfRows - 1; row++)
            for (int column = 0; column < numberOfColumns - 1; column++)
                if (isPool(row, column))
                    return true;
        return false;
    }

    private boolean isPool(int row, int column) {
        return (cellAt(row, column).isWater() && cellAt(row + 1, column).isWater() && cellAt(row, column + 1).isWater() && cellAt(
                row + 1, column + 1).isWater());
    }

    public boolean isWaterConnected() {
        return Polyomino.isPolyomino(getWaterCells());
    }

    public Set<Cell> getAllCells() {
        Set<Cell> cells = new HashSet<Cell>();
        for (int row = 0; row < numberOfRows; row++) 
            for (int column = 0; column < numberOfColumns; column++) 
                    cells.add(cellAt(row, column));
        return cells;
    }
    
    private Set<Cell> getCellsOfType(CellType type) {
        Set<Cell> cells = new HashSet<Cell>();
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                Cell cell = cellAt(row, column);
                if (cell.getType() == type)
                    cells.add(cell);
            }
        }
        return cells;

    }

    public Set<Cell> getWaterCells() {
        return getCellsOfType(WATER);
    }

    public Set<Cell> getLandCells() {
        return getCellsOfType(LAND);
    }

    public Set<Cell> getUndeterminedCells() {
        return getCellsOfType(UNDETERMINED);
    }

    public boolean isEachNumberCellPartOfAnIslandOfThatSize() {
        Set<Cell> numberCells = islandNumbers.keySet();
        for (Cell numberCell : numberCells) {
            Polyomino island = numberCell.getPolyomino();
            Integer numberValue = islandNumbers.get(numberCell);
            if (!numberValue.equals(island.size()))
                return false;
        }
        return true;
    }

    public boolean isCompleteAndCorrect() {

        if (!isComplete())
            return false;

        // No 2x2 water blocks
        if (hasPools())
            return false;

        // All water must be connected
        if (!isWaterConnected())
            return false;

        // Each numbered cell must be part of a polyomino of that size
        if (!isEachNumberCellPartOfAnIslandOfThatSize())
            return false;

        // Islands must have exactly one numbered cell
        if (doesSomeIslandContainMultipleNumbers())
            return false;

        // All land cells must be part of an island
        if (!isEveryLandCellPartOfAnIsland())
            return false;

        return true;
    }

    public boolean isEveryLandCellPartOfAnIsland() {
        Set<Cell> landCells = getLandCells();

        Set<Cell> numberCells = islandNumbers.keySet();
        Set<Polyomino> islands = new HashSet<Polyomino>();
        for (Cell numberCell : numberCells)
            islands.add(numberCell.getPolyomino());

        for (Cell cell : landCells) {
            boolean found = false;
            for (Polyomino island : islands) {
                if (island.contains(cell)) {
                    found = true;
                    break;
                }
            }
            if (!found)
                return false;
        }

        return true;
    }

    public boolean doesSomeIslandContainMultipleNumbers() {
        Set<Cell> numberCells = islandNumbers.keySet();
        Set<Polyomino> islands = new HashSet<Polyomino>();
        for (Cell numberCell : numberCells) {
            Polyomino island = numberCell.getPolyomino();
            if (islands.contains(island))
                return true;
            islands.add(island);
        }
        return false;
    }

    private List<List<CellType>> copyGrid() {
        List<List<CellType>> newCellGrid = new ArrayList<List<CellType>>(numberOfRows);
        for (List<Cell> row : cellGrid) {
            List<CellType> newRow = new ArrayList<CellType>(numberOfColumns);
            for (Cell cell : row)
                newRow.add(cell.getType());
            newCellGrid.add(newRow);
        }
        return newCellGrid;
    }

    private Map<Coordinate, Integer> copyIslandNumbers() {
        Map<Coordinate, Integer> newIslandNumbers = new HashMap<Coordinate, Integer>();
        Set<Cell> cells = this.islandNumbers.keySet();
        for (Cell cell : cells)
            newIslandNumbers.put(cell.getCoordinate(), islandNumbers.get(cell));
        return newIslandNumbers;

    }

    private NurikabeGrid makeGridWithChangedCell(int row, int column, CellType type) {
        if (islandNumbers.containsKey(cellAt(row, column)))
            throw new IllegalArgumentException("Cannot alter number cell: " + row + ", " + column);

        List<List<CellType>> newGrid = copyGrid();
        newGrid.get(row).set(column, type);
        Map<Coordinate, Integer> newIslandNumbers = copyIslandNumbers();
        return new NurikabeGrid(newGrid, newIslandNumbers);

    }

    public NurikabeGrid concludeCellIsWater(int row, int column) {
        return makeGridWithChangedCell(row, column, WATER);
    }

    public NurikabeGrid concludeCellIsLand(int row, int column) {
        return makeGridWithChangedCell(row, column, LAND);
    }

    public NurikabeGrid removeConclusionFromCell(int row, int column) {
        return makeGridWithChangedCell(row, column, UNDETERMINED);
    }

    /**
     * Convenience method, primarily for testing, for generating a NurikabeGrid from a string. Limitations are that the
     * numbers cannot be larger than 9.
     */
    public static NurikabeGrid createFromAsciiGrid(String string) {
        String gridString;
        if (string.endsWith(":"))
            gridString = string.substring(0, string.length() - 1);
        else
            gridString = string;
        List<List<CellType>> cellGrid = new ArrayList<List<CellType>>();
        Map<Coordinate, Integer> islandNumbers = new HashMap<Coordinate, Integer>();
        String[] rowStrings = gridString.split(":", -1);
        int rowLength = -1;
        int rowNumber = 0;
        for (String rowString : rowStrings) {
            if (rowNumber == 0)
                rowLength = rowString.length();
            else if (rowString.length() != rowLength)
                throw new IllegalArgumentException("Grid rows are of unequal length");
            List<CellType> row = new ArrayList<CellType>();
            for (int columnNumber = 0; columnNumber < rowLength; columnNumber++) {
                char cellChar = rowString.charAt(columnNumber);
                processCellCharacter(cellChar, row, islandNumbers, rowNumber, columnNumber);

            }
            cellGrid.add(row);
            rowNumber++;
        }

        NurikabeGrid grid = new NurikabeGrid(cellGrid, islandNumbers);
        return grid;
    }

    private static void processCellCharacter(char cellChar, List<CellType> row, Map<Coordinate, Integer> islandNumbers,
            int rowNumber, int columnNumber) {
        switch (cellChar) {
            case ' ':
                row.add(UNDETERMINED);
                break;
            case '.':
                row.add(LAND);
                break;
            case '#':
                row.add(WATER);
                break;
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                row.add(LAND);
                int digit = Character.digit(cellChar, 10);
                islandNumbers.put(Coordinate.makeCoordinate(rowNumber, columnNumber), digit);
                break;
            default:
                throw new IllegalArgumentException("Unknown character: " + cellChar);
        }
    }

    @Override
    public String toString() {
        return renderAsString();
    }

    public void prettyPrintGrid() {
        System.out.println(renderAsString());
    }

    private String renderAsString() {
        final String LINE_SEPARATOR = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        for (int columnNumber = 0; columnNumber < numberOfRows; columnNumber++)
            builder.append(columnNumber);
        builder.append(LINE_SEPARATOR);
        int rowNumber = 0;
        prettyLine(builder, LINE_SEPARATOR);
        for (List<Cell> row : cellGrid) {
            builder.append("|");
            int columnNumber = 0;
            for (Cell cell : row) {
                switch (cell.getType()) {
                    case WATER:
                        builder.append("#");
                        break;
                    case UNDETERMINED:
                        builder.append(" ");
                        break;
                    case LAND:
                        if (hasNumberFor(rowNumber, columnNumber)) {
                            int islandNumber = getIslandNumber(rowNumber, columnNumber);
                            if (islandNumber <= 9)
                                builder.append(Integer.toString(islandNumber));
                            else
                                builder.append(">");
                        } else {
                            builder.append(".");
                        }
                        break;
                }
                columnNumber++;
            }
            builder.append("| " + rowNumber +  LINE_SEPARATOR);
            rowNumber++;
        }
        prettyLine(builder, LINE_SEPARATOR);
        // builder.setLength(builder.length() - 1);

        return builder.toString();
    }

    private void prettyLine(StringBuilder builder, String lineSeparator) {
        builder.append("+");
        for (int i = 0; i < numberOfColumns(); i++)
            builder.append("-");
        builder.append("+ " + lineSeparator);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cellGrid == null) ? 0 : cellGrid.hashCode());
        result = prime * result + ((islandNumbers == null) ? 0 : islandNumbers.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof NurikabeGrid))
            return false;
        final NurikabeGrid other = (NurikabeGrid) obj;
        if (cellGrid == null) {
            if (other.cellGrid != null)
                return false;
        } else if (!cellGrid.equals(other.cellGrid))
            return false;
        if (islandNumbers == null) {
            if (other.islandNumbers != null)
                return false;
        } else if (!islandNumbers.equals(other.islandNumbers))
            return false;
        return true;
    }

    // private List<List<CellType>> safeCopy(List<List<CellType>> aCellGrid) {
    // List<List<CellType>> newCellGrid = new ArrayList<List<CellType>>();
    // for (List<CellType> row : aCellGrid) {
    // List<CellType> safeCopy = Collections.unmodifiableList(new ArrayList<CellType>(row));
    // newCellGrid.add(safeCopy);
    // }
    // return Collections.unmodifiableList(newCellGrid);
    // }
    //
    // private Map<Cell, Integer> safeCopy(Map<Cell, Integer> someIslandNumbers) {
    // Map<Cell, Integer> newIslandNumbers = new HashMap<Cell, Integer>(someIslandNumbers);
    // return Collections.unmodifiableMap(newIslandNumbers);
    // }

    public Set<Cell> getIslandNumberCells() {
        return islandNumbers.keySet();
    }
    
    

    public Set<Polyomino> getCompletedIslands() {
        Set<Polyomino> completedIslands = new HashSet<Polyomino>();
        for (Cell cell : getIslandNumberCells()) {
            Integer numberValue = islandNumbers.get(cell);
            Polyomino islandPolyomino = cell.getPolyomino();
            if (islandPolyomino.size() == numberValue)
                completedIslands.add(islandPolyomino);
        }
        return completedIslands;
    }

    public CellType getCellType(int row, int column) {
        return cellAt(row, column).getType();
    }

    public Set<Polyomino> getPolyominoesOfType(CellType type) {
        Set<Cell> cells = getCellsOfType(type);
        Set<Polyomino> unknownPolyominoes = new HashSet<Polyomino>();
        for (Cell cell : cells)
            unknownPolyominoes.add(cell.getPolyomino());
        return unknownPolyominoes;
    }

    public boolean isCompleteIsland(Polyomino polyomino) {
        Set<Cell> islandNumberCells = getIslandNumberCells();
        for (Cell cell : islandNumberCells) {
            if (polyomino.contains(cell)) {
                if (polyomino.size() == getIslandNumbers().get(cell))
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    public Iterator<Cell> iterator() {
        List<Cell> cells = new ArrayList<Cell>();
        for (List<Cell> row : cellGrid) 
            cells.addAll(row);
        return cells.iterator();
    }

    public Set<Polyomino> getAllPartialIslandsConnectedToANumber() {
        Set<Polyomino> partialIslands = new HashSet<Polyomino>();
        for (Cell numberCell : getIslandNumberCells())
            partialIslands.add(numberCell.getPolyomino());
        return partialIslands;
    }


}
