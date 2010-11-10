package nurikabe.grid;

import static org.junit.Assert.*;

import org.junit.Test;


public class CellTest {

    @Test
    public void testIsLandAndConnectedToAnIslandNumber() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123456
        builder.append("1 2 2 .:"); // 0
        builder.append("    . .:"); // 1
        builder.append("3. #  .:"); // 2
        builder.append("  .   .:"); // 3
        builder.append("..  ...:"); // 4
        builder.append("     .9:"); // 5
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        
        assertTrue(grid.cellAt(0, 0).isLandAndConnectedToAnIslandNumber());
        assertTrue(grid.cellAt(0, 2).isLandAndConnectedToAnIslandNumber());
        assertTrue(grid.cellAt(0, 4).isLandAndConnectedToAnIslandNumber());
        assertTrue(grid.cellAt(0, 6).isLandAndConnectedToAnIslandNumber());
        assertTrue(grid.cellAt(1, 4).isLandAndConnectedToAnIslandNumber());
        assertTrue(grid.cellAt(2, 0).isLandAndConnectedToAnIslandNumber());
        assertTrue(grid.cellAt(2, 1).isLandAndConnectedToAnIslandNumber());

        assertFalse(grid.cellAt(0, 1).isLandAndConnectedToAnIslandNumber());
        assertFalse(grid.cellAt(2, 3).isLandAndConnectedToAnIslandNumber());
        assertFalse(grid.cellAt(4, 0).isLandAndConnectedToAnIslandNumber());
        assertFalse(grid.cellAt(4, 1).isLandAndConnectedToAnIslandNumber());

    }
    
}
