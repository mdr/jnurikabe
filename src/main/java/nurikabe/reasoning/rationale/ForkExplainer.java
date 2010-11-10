package nurikabe.reasoning.rationale;

import java.util.Map;

import nurikabe.grid.NurikabeGrid;

public interface ForkExplainer {

    public Rationale makeRationale(Map<NurikabeGrid, Rationale> explanationsPerPossibleGrid);

}
