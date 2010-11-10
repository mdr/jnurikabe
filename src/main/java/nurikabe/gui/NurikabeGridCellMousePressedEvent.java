package nurikabe.gui;

import java.awt.event.MouseEvent;
import java.util.EventObject;

import nurikabe.grid.NurikabeGrid;

public class NurikabeGridCellMousePressedEvent extends EventObject {

    private int row;

    private int column;

    private MouseEvent mouseEvent;

    private NurikabeGrid grid;

    public NurikabeGridCellMousePressedEvent(NurikabeGridView source, NurikabeGrid grid, int row, int column, MouseEvent mouseEvent) {
        super(source);
        this.row = row;
        this.column = column;
        this.mouseEvent = mouseEvent;
        this.grid = grid;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }

    public NurikabeGrid getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + row + ", " + column + ", " + mouseEvent.toString();
    }
    
}
