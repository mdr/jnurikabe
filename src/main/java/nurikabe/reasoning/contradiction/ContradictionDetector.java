package nurikabe.reasoning.contradiction;

import java.util.Set;

import nurikabe.grid.NurikabeGrid;

public interface ContradictionDetector {

    public Set<Contradiction> findContradictions(NurikabeGrid grid);
    
}
