package nurikabe.reasoning.rationale;

import nurikabe.grid.Cell;

public class TryAllPolyominoesRationale extends AbstractSimpleRationale {

    final Cell islandNumberCell;

    public TryAllPolyominoesRationale(Cell islandNumberCell) {
        this.islandNumberCell = islandNumberCell;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((islandNumberCell == null) ? 0 : islandNumberCell.hashCode());
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
        final TryAllPolyominoesRationale other = (TryAllPolyominoesRationale) obj;
        if (islandNumberCell == null) {
            if (other.islandNumberCell != null)
                return false;
        } else if (!islandNumberCell.equals(other.islandNumberCell))
            return false;
        return true;
    }

    @Override
    public String getDescription() {
        return "all possible polyominoes for the island " + islandNumberCell + " contain this cell";
    }

}
