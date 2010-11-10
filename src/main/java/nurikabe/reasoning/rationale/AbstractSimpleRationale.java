package nurikabe.reasoning.rationale;

import java.util.Collections;
import java.util.Set;

abstract public class AbstractSimpleRationale implements Rationale {

    public Set<Rationale> getChildRationales() {
        return Collections.<Rationale> singleton(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(\"" + getDescription() + "\")";
    }

    abstract public String getDescription();

    
    @Override
    abstract public boolean equals(Object obj);
    
    @Override
    abstract public int hashCode() ;
}
