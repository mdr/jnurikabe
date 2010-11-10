package nurikabe.reasoning.rationale;

import nurikabe.grid.Cell;

public class WaterOrLandLookaheadRationale extends AbstractSimpleRationale {

    private final Cell cellExamined;

    private final Rationale waterRationale;

    private final Rationale landRationale;

    public WaterOrLandLookaheadRationale(Cell cellExamined, Rationale waterRationale, Rationale landRationale) {
        this.cellExamined = cellExamined;
        this.waterRationale = waterRationale;
        this.landRationale = landRationale;
    }

    @Override
    public String getDescription() {
        return "if the cell at " + cellExamined.getCoordinate() + " is water then " + waterRationale.getDescription()
                + ", and if it is land, then it is because " + landRationale.getDescription();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cellExamined == null) ? 0 : cellExamined.hashCode());
        result = prime * result + ((landRationale == null) ? 0 : landRationale.hashCode());
        result = prime * result + ((waterRationale == null) ? 0 : waterRationale.hashCode());
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
        final WaterOrLandLookaheadRationale other = (WaterOrLandLookaheadRationale) obj;
        if (cellExamined == null) {
            if (other.cellExamined != null)
                return false;
        } else if (!cellExamined.equals(other.cellExamined))
            return false;
        if (landRationale == null) {
            if (other.landRationale != null)
                return false;
        } else if (!landRationale.equals(other.landRationale))
            return false;
        if (waterRationale == null) {
            if (other.waterRationale != null)
                return false;
        } else if (!waterRationale.equals(other.waterRationale))
            return false;
        return true;
    }

}
