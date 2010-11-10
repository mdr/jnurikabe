package nurikabe.reasoning.strategy;

import java.util.Set;

import nurikabe.grid.NurikabeGrid;

public interface AllPossibilitiesGridForker {

    public Set<ForkedGrid> forkGridIntoPossibilities(NurikabeGrid grid) ;
    
}
