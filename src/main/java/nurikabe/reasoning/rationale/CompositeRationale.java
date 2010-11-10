package nurikabe.reasoning.rationale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.StringUtils;

public class CompositeRationale implements Rationale {

    Set<Rationale> rationales;

    public CompositeRationale(Rationale... rationales) {
        this.rationales = new HashSet<Rationale>();
        for (Rationale rationale : rationales)
            this.rationales.addAll(rationale.getChildRationales());
    }

    public CompositeRationale(Set<Rationale> rationales) {
        this.rationales = new HashSet<Rationale>(rationales);
    }

    public String getDescription() {
        List<String> descriptions = new ArrayList<String>();
        for (Rationale rationale : rationales)
            descriptions.add(rationale.getDescription());
        return StringUtils.join(descriptions, "; alternatively, ");
    }

    public CompositeRationale addRationale(Rationale rationale) {
        Set<Rationale> newRationales = new HashSet<Rationale>(rationales);
        newRationales.add(rationale);
        return new CompositeRationale(newRationales);
    }

    public Set<Rationale> getChildRationales() {
        return Collections.unmodifiableSet(rationales);
    }

    @Override
    public String toString() {
        return "CompositeRationale(" + getDescription() + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rationales == null) ? 0 : rationales.hashCode());
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
        final CompositeRationale other = (CompositeRationale) obj;
        if (rationales == null) {
            if (other.rationales != null)
                return false;
        } else if (!rationales.equals(other.rationales))
            return false;
        return true;
    }

}
