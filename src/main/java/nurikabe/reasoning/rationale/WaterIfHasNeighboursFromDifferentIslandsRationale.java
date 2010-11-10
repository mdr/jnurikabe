package nurikabe.reasoning.rationale;

import java.util.Set;

import nurikabe.grid.Polyomino;

public class WaterIfHasNeighboursFromDifferentIslandsRationale extends AbstractSimpleRationale  {

    private Set<Polyomino> neighbourPartialIslands;

    public WaterIfHasNeighboursFromDifferentIslandsRationale(Set<Polyomino> neighbourPartialIslands) {
        this.neighbourPartialIslands = neighbourPartialIslands;
    }

    @Override
    public String getDescription() {
        return "it is bordered by " + neighbourPartialIslands.size() +" distinct islands, and therefore cannot be land, because multiple islands would then share the same cell (" + neighbourPartialIslands + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((neighbourPartialIslands == null) ? 0 : neighbourPartialIslands.hashCode());
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
        final WaterIfHasNeighboursFromDifferentIslandsRationale other = (WaterIfHasNeighboursFromDifferentIslandsRationale) obj;
        if (neighbourPartialIslands == null) {
            if (other.neighbourPartialIslands != null)
                return false;
        } else if (!neighbourPartialIslands.equals(other.neighbourPartialIslands))
            return false;
        return true;
    }

}
