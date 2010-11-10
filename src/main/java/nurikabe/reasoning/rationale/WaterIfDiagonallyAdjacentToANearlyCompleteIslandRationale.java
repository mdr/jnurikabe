package nurikabe.reasoning.rationale;

import java.util.Set;

import nurikabe.grid.Coordinate;
import nurikabe.grid.Polyomino;
import utils.CollectionsUtils;

public class WaterIfDiagonallyAdjacentToANearlyCompleteIslandRationale extends AbstractSimpleRationale {

    private final Polyomino polyomino;

    private final Coordinate coordinateOne;

    private final Coordinate coordinateTwo;

    private final Set<Coordinate> coordinates;

    public WaterIfDiagonallyAdjacentToANearlyCompleteIslandRationale(Polyomino polyomino, Coordinate coordinateOne,
            Coordinate coordinateTwo) {
        this.polyomino = polyomino;
        this.coordinateOne = coordinateOne;
        this.coordinateTwo = coordinateTwo;
        this.coordinates = CollectionsUtils.makeSet(coordinateOne, coordinateTwo);
    }

    @Override
    public String getDescription() {
        return "the final cell of the island " + polyomino + " can only be completed in two ways: either "
                + coordinateOne + " or " + coordinateTwo
                + ", but either way, this cell will be forced to be water, as it will be adjacent to a complete island";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        result = prime * result + ((polyomino == null) ? 0 : polyomino.hashCode());
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
        final WaterIfDiagonallyAdjacentToANearlyCompleteIslandRationale other = (WaterIfDiagonallyAdjacentToANearlyCompleteIslandRationale) obj;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        if (polyomino == null) {
            if (other.polyomino != null)
                return false;
        } else if (!polyomino.equals(other.polyomino))
            return false;
        return true;
    }

}
