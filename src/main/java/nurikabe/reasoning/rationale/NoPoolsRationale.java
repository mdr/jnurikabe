package nurikabe.reasoning.rationale;

import nurikabe.grid.Coordinate;

public class NoPoolsRationale extends AbstractSimpleRationale {

    public enum Corner {
        NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST;
    }

    private int row;

    private int column;

    private Corner corner;

    public NoPoolsRationale(int row, int column, Corner corner) {
        this.row = row;
        this.column = column;
        this.corner = corner;
    }

    public NoPoolsRationale(Coordinate locationConcludedToBeLand, Corner corner) {
        this(locationConcludedToBeLand.getRow(), locationConcludedToBeLand.getColumn(), corner);
    }

    @Override
    public String getDescription() {
        return "this is the " + corner + " corner of a 2x2 block where the other cells are all water;"
                + " a complete 2x2 block of water is not allowed.";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + ((corner == null) ? 0 : corner.hashCode());
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof NoPoolsRationale))
            return false;
        final NoPoolsRationale other = (NoPoolsRationale) obj;
        if (column != other.column)
            return false;
        if (corner == null) {
            if (other.corner != null)
                return false;
        } else if (!corner.equals(other.corner))
            return false;
        if (row != other.row)
            return false;
        return true;
    }

}
