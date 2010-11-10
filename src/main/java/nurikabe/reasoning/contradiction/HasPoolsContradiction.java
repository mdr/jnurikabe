package nurikabe.reasoning.contradiction;

import nurikabe.grid.Polyomino;

public class HasPoolsContradiction extends AbstractContradiction {

    private Polyomino pool;

    public HasPoolsContradiction(Polyomino pool) {
        this.pool = pool;
    }

    @Override
    public String getDescription() {
        return "it has a 2x2 pool of water at " + pool;
    }

    public Polyomino getPool() {
        return pool;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pool == null) ? 0 : pool.hashCode());
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
        final HasPoolsContradiction other = (HasPoolsContradiction) obj;
        if (pool == null) {
            if (other.pool != null)
                return false;
        } else if (!pool.equals(other.pool))
            return false;
        return true;
    }

}
