package nurikabe.reasoning.strategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.ForkExplainer;

public class ForkedGrid {
    private final ForkExplainer forkExplainer;

    private final Set<NurikabeGrid> possibleGrids;

    public ForkedGrid(ForkExplainer forkExplainer, Set<NurikabeGrid> possibleGrids) {
        this.forkExplainer = forkExplainer;
        this.possibleGrids = Collections.unmodifiableSet(new HashSet<NurikabeGrid>(possibleGrids));
    }

    public ForkExplainer getForkExplainer() {
        return forkExplainer;
    }

    public Set<NurikabeGrid> getPossibleGrids() {
        return possibleGrids;
    }

}
