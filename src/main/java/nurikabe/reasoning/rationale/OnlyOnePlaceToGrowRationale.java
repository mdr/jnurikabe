package nurikabe.reasoning.rationale;

import nurikabe.grid.Polyomino;

public class OnlyOnePlaceToGrowRationale extends AbstractSimpleRationale {

    private Polyomino polyomino;

    public OnlyOnePlaceToGrowRationale(Polyomino polyomino) {
        this.polyomino = polyomino;
    }

    @Override
    public String getDescription() {
        return "there is only one place for the " + polyomino + " to grow";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        final OnlyOnePlaceToGrowRationale other = (OnlyOnePlaceToGrowRationale) obj;
        if (polyomino == null) {
            if (other.polyomino != null)
                return false;
        } else if (!polyomino.equals(other.polyomino))
            return false;
        return true;
    }
    
    

}
