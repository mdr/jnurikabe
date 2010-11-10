package nurikabe.grid;

public interface Grid<T> extends Iterable<T> {
    public int numberOfRows();
    public int numberOfColumns();
}
