package nurikabe.reasoning.rationale;

import java.util.Set;

public interface Rationale {

    public String getDescription();
    
    public Set<Rationale> getChildRationales();
    
}
