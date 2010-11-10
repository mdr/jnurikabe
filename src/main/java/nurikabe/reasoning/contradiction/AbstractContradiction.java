package nurikabe.reasoning.contradiction;

abstract public class AbstractContradiction implements Contradiction {

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(\"" + getDescription() + "\")";
    }

    abstract public String getDescription();

    @Override
    abstract public boolean equals(Object obj);

    @Override
    abstract public int hashCode();
}
