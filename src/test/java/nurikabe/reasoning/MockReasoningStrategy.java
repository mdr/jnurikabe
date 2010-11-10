package nurikabe.reasoning;

import java.util.Map;

import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.strategy.ReasoningStrategy;

public class MockReasoningStrategy implements ReasoningStrategy {

    private Map<Conclusion, Rationale> conclusions;

    public MockReasoningStrategy(Map<Conclusion, Rationale> conclusions) {
        this.conclusions = conclusions;
    }
    
    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) {
        return conclusions;
    }

}
