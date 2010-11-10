package nurikabe.reasoning.rationale;

import java.util.Map;

import nurikabe.grid.Cell;
import nurikabe.grid.Polyomino;

public class AllPolyominoesForkRationale extends AbstractSimpleRationale {

    private final Map<Polyomino, Rationale> reasonForEachPolyomino;

    private final Cell islandNumberCell;

    public AllPolyominoesForkRationale(Cell islandNumberCell, Map<Polyomino, Rationale> reasonForEachPolyomino) {

        this.islandNumberCell = islandNumberCell;
        this.reasonForEachPolyomino = reasonForEachPolyomino;
    }

    @Override
    public String getDescription() {
        StringBuilder builder = new StringBuilder("given that there are a number of possibilities for the island at "
                + islandNumberCell.getCoordinate() + ", ");
        for (Polyomino polyomino : reasonForEachPolyomino.keySet()) {
            Rationale rationale = reasonForEachPolyomino.get(polyomino);
            builder.append("if the island is " + polyomino + ", then it is because " + rationale.getDescription()
                    + ", ");
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((islandNumberCell == null) ? 0 : islandNumberCell.hashCode());
        result = prime * result + ((reasonForEachPolyomino == null) ? 0 : reasonForEachPolyomino.hashCode());
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
        final AllPolyominoesForkRationale other = (AllPolyominoesForkRationale) obj;
        if (islandNumberCell == null) {
            if (other.islandNumberCell != null)
                return false;
        } else if (!islandNumberCell.equals(other.islandNumberCell))
            return false;
        if (reasonForEachPolyomino == null) {
            if (other.reasonForEachPolyomino != null)
                return false;
        } else if (!reasonForEachPolyomino.equals(other.reasonForEachPolyomino))
            return false;
        return true;
    }

}
