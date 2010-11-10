package nurikabe.reasoning.rationale;

import nurikabe.grid.CellType;

public class UnknownPolyominoEnclosedByLandOrWaterRationale extends AbstractSimpleRationale {

    private CellType cellType;

    public UnknownPolyominoEnclosedByLandOrWaterRationale(CellType cellType) {
        this.cellType = cellType;
        if (cellType != CellType.LAND && cellType != CellType.WATER)
            throw new IllegalArgumentException("Cell type must be land or water");
    }

    @Override
    public String getDescription() {
        if (cellType == CellType.WATER)
            return "it is surrounded by water; all land must be part of a numbered island";
        else
            return "it is surrounded by land; all water must be connected";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cellType == null) ? 0 : cellType.hashCode());
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
        final UnknownPolyominoEnclosedByLandOrWaterRationale other = (UnknownPolyominoEnclosedByLandOrWaterRationale) obj;
        if (cellType == null) {
            if (other.cellType != null)
                return false;
        } else if (!cellType.equals(other.cellType))
            return false;
        return true;
    }

}
