package nurikabe.reasoning.rationale;

import java.util.Collections;
import java.util.Set;

public class NoPathToCellFromAnyNumberRationale implements Rationale {

    
    public String getDescription() {
        return "it is too far away from any number";
    }

    public Set<Rationale> getChildRationales() {
        return Collections.<Rationale> singleton(this);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(\"" + getDescription() + "\")";
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

    
}
