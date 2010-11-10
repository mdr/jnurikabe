package nurikabe.reasoning;

import java.util.Map;
import java.util.Set;

import nurikabe.grid.CellType;
import nurikabe.grid.Coordinate;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.CompositeRationale;
import nurikabe.reasoning.rationale.Rationale;

public class Conclusion {

    private final int row;

    private final int column;

    private final CellType cellType;

    public Conclusion(int row, int column, CellType cellType) {
        this.row = row;
        this.column = column;
        this.cellType = cellType;
    }

    public Conclusion(Coordinate coordinate, CellType cellType) {
        this(coordinate.getRow(), coordinate.getColumn(), cellType);
    }

    public NurikabeGrid applyTo(NurikabeGrid grid) {
        switch (cellType) {
            case LAND:
                return grid.concludeCellIsLand(row, column);
            case WATER:
                return grid.concludeCellIsWater(row, column);
            case UNDETERMINED:
                return grid.removeConclusionFromCell(row, column);
        }
        throw new AssertionError("Unknown cell type: " + cellType);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public CellType getCellType() {
        return cellType;
    }

    public static NurikabeGrid applyAll(Set<Conclusion> conclusions, NurikabeGrid startGrid) {
        NurikabeGrid resultGrid = startGrid;
        for (Conclusion conclusion : conclusions) 
            resultGrid = conclusion.applyTo(resultGrid);
        return resultGrid;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cellType == null) ? 0 : cellType.hashCode());
        result = prime * result + column;
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Conclusion))
            return false;
        final Conclusion other = (Conclusion) obj;
        if (cellType == null) {
            if (other.cellType != null)
                return false;
        } else if (!cellType.equals(other.cellType))
            return false;
        if (column != other.column)
            return false;
        if (row != other.row)
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Conclusion(" + Coordinate.makeCoordinate(row, column) + " is " + cellType +")";
    }
    
    public static void putAdditionalConclusion(Map<Conclusion,Rationale> conclusions, Conclusion conclusion, Rationale rationale) {
        Rationale updatedRationale;
        if (conclusions.containsKey(conclusion)) {
            Rationale existingRationale = conclusions.get(conclusion);
            updatedRationale = new CompositeRationale(existingRationale, rationale);
        } else {
            updatedRationale = rationale; 
        }
        conclusions.put(conclusion, updatedRationale);

    }

}
