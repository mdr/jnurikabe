package nurikabe.grid;

import static org.junit.Assert.*;
import static utils.CollectionsUtils.makeSet;

import java.util.Set;

import nurikabe.grid.Cell;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;

import org.junit.Test;

public class PolyominoTest {

    @Test
    public void testGrow() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("3.  :"); // 0
        builder.append("   #:"); // 1
        builder.append("..##:"); // 2
        builder.append(".   :"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Polyomino polyomino = new Polyomino(makeSet(grid.cellAt(3, 0), grid.cellAt(2, 0)));
        Polyomino actualPolyomino = polyomino.grow(grid.cellAt(2, 1));
        Polyomino expectedPolyomino = new Polyomino(makeSet(grid.cellAt(3, 0), grid.cellAt(2, 0), grid.cellAt(2, 1)));
        assertEquals(expectedPolyomino, actualPolyomino);
    }

    @Test
    public void testGetExternalBoundary() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("##2.:"); // 0
        builder.append(" 1##:"); // 1
        builder.append(". #5:"); // 2
        builder.append("3. .:"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Set<Cell> cells = makeSet(grid.cellAt(0, 2), grid.cellAt(0, 3));
        assertTrue(Polyomino.isPolyomino(cells));
        Polyomino polyomino = new Polyomino(cells);
        Set<Cell> expectedBoundary = makeSet(grid.cellAt(0, 1), grid.cellAt(1, 2), grid.cellAt(1, 3));
        Set<Cell> actualBoundary = polyomino.getExternalBoundary();
        assertEquals(expectedBoundary, actualBoundary);

    }

    @Test
    public void testIsEnclosedByWater() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 01234
        builder.append(" ##  :"); // 0
        builder.append("# ## :"); // 1
        builder.append("# ###:"); // 2
        builder.append(" ## 2:"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Polyomino polyomino1 = new Polyomino(makeSet(grid.cellAt(1, 1), grid.cellAt(2, 1)));
        assertTrue(polyomino1.isEnclosedByWater());
        Polyomino polyomino2 = new Polyomino(makeSet(grid.cellAt(1, 1)));
        assertFalse(polyomino2.isEnclosedByWater());
        Polyomino polyomino3 = new Polyomino(makeSet(grid.cellAt(0, 4), grid.cellAt(0, 3), grid.cellAt(1, 4)));
        assertTrue(polyomino3.isEnclosedByWater());

    }

    @Test
    public void testIsPolyomino() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;
        Set<Cell> cells;
        builder = new StringBuilder();
        // ............ 01234
        builder.append(" ##  :"); // 0
        builder.append("# ## :"); // 1
        builder.append("# ###:"); // 2
        builder.append(" ## 2:"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        cells = makeSet(grid.cellAt(0, 0), grid.cellAt(0, 3));
        assertFalse(Polyomino.isPolyomino(cells));
        try {
            new Polyomino(cells);
            fail();
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        cells = makeSet(grid.cellAt(0, 0), grid.cellAt(0, 1));
//        assertFalse(Polyomino.isPolyomino(cells));
        assertTrue(Polyomino.isPolyomino(cells));
//        try {
//            new Polyomino(cells);
//            fail();
//        } catch (IllegalArgumentException e) {
//            // Exception expected
//        }

    }

    @Test
    public void testGrowLandPolyominoInAllPossibleWays() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 01234
        builder.append("2 #  :"); // 0
        builder.append(". 5. :"); // 1
        builder.append("  .  :"); // 2
        builder.append("   . :"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Polyomino polyomino = new Polyomino(makeSet(grid.cellAt(1, 2), grid.cellAt(1, 3)));
        Set<Polyomino> actualPolyominoes = polyomino.growLandPolyominoInAllPossibleWays();

        Polyomino expectedPolyomino1 = new Polyomino(makeSet(grid.cellAt(1, 2), grid.cellAt(1, 3), grid.cellAt(0, 3)));
        Polyomino expectedPolyomino2 = new Polyomino(makeSet(grid.cellAt(1, 2), grid.cellAt(1, 3), grid.cellAt(1, 4)));
        Polyomino expectedPolyomino3 = new Polyomino(makeSet(grid.cellAt(1, 2), grid.cellAt(1, 3), grid.cellAt(2, 3)));
        Polyomino expectedPolyomino4 = new Polyomino(makeSet(grid.cellAt(1, 2), grid.cellAt(1, 3), grid.cellAt(2, 2)));
        Set<Polyomino> expectedPolyominoes = makeSet(expectedPolyomino1, expectedPolyomino2, expectedPolyomino3,
                expectedPolyomino4);
        assertEquals(expectedPolyominoes, actualPolyominoes);
    }
}
