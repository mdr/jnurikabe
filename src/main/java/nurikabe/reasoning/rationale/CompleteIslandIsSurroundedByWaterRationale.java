package nurikabe.reasoning.rationale;

import nurikabe.grid.Coordinate;


public class CompleteIslandIsSurroundedByWaterRationale extends AbstractSimpleRationale  {

    private Coordinate islandNeighbour; // TODO: Add island poly too

    public CompleteIslandIsSurroundedByWaterRationale(Coordinate islandCell) {
        this.islandNeighbour = islandCell; 
    }
    public CompleteIslandIsSurroundedByWaterRationale(int row, int column) {
        this(Coordinate.makeCoordinate(row, column));
    }

    @Override
    public String getDescription() {
        return "it is adjacent to " + islandNeighbour + ", which is part of a complete island";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((islandNeighbour == null) ? 0 : islandNeighbour.hashCode());
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
        final CompleteIslandIsSurroundedByWaterRationale other = (CompleteIslandIsSurroundedByWaterRationale) obj;
        if (islandNeighbour == null) {
            if (other.islandNeighbour != null)
                return false;
        } else if (!islandNeighbour.equals(other.islandNeighbour))
            return false;
        return true;
    }
    
}
