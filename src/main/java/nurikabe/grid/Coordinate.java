/**
 * 
 */
package nurikabe.grid;

public class Coordinate {
    private int row;

    private int column;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    private Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static Coordinate makeCoordinate(int row, int column) {
        return new Coordinate(row, column);
    }

    @Override
    public int hashCode() {
        int result = 31 + column;
        result = 31 * result + row;
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
        final Coordinate other = (Coordinate) obj;
        if (column != other.column)
            return false;
        if (row != other.row)
            return false;
        return true;
    }

    public boolean isAdjacentTo(Coordinate other) {
        int rowDistance = Math.abs(this.row - other.row);
        int columnDistance = Math.abs(this.column - other.column);
        return (rowDistance == 1 && columnDistance == 0) || (rowDistance == 0 && columnDistance == 1);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

}