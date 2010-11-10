package nurikabe.reasoning.strategy;

import java.util.Map;

import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.Rationale;

public interface ReasoningStrategy {
    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid);
}
