package nurikabe.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static utils.CollectionsUtils.makeSet;

import java.util.HashSet;
import java.util.Set;

import nurikabe.grid.Cell;
import nurikabe.grid.CellType;
import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;

import org.junit.Before;
import org.junit.Test;

public class NurikabeGridTest {

    private NurikabeGrid samplePuzzle;

    private NurikabeGrid sampleSolution;

    @Before
    public void setup() {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append("2        2:");
        builder.append(" #    2 # :");
        builder.append(" 2  7     :");
        builder.append("          :");
        builder.append("      3#3 :");
        builder.append("  2#  #3# :");
        builder.append("2 #4   .  :");
        builder.append(" #        :");
        builder.append("#1#   2#4 :");
        samplePuzzle = NurikabeGrid.createFromAsciiGrid(builder.toString());

        builder = new StringBuilder();
        builder.append("2.#...##.2:");
        builder.append("###..#2###:");
        builder.append("#2#.7#.#.#:");
        builder.append("#.######.#:");
        builder.append("##.#..3#3#:");
        builder.append(".#2####3##:");
        builder.append("2##4.#..#.:");
        builder.append("##..#####.:");
        builder.append("#1###.2#4.");
        sampleSolution = NurikabeGrid.createFromAsciiGrid(builder.toString());

    }

    @Test
    public void testCreateFromAsciiGrid() throws Exception {
        assertTrue(samplePuzzle.cellAt(0, 0).isLand());
        assertTrue(samplePuzzle.cellAt(2, 4).isLand());
        assertTrue(samplePuzzle.cellAt(6, 7).isLand());
        assertTrue(samplePuzzle.cellAt(0, 3).isUndetermined());
        assertTrue(samplePuzzle.cellAt(2, 2).isUndetermined());
        assertTrue(samplePuzzle.cellAt(1, 1).isWater());
        assertTrue(samplePuzzle.cellAt(5, 3).isWater());
        assertEquals(10, samplePuzzle.numberOfColumns());
        assertEquals(9, samplePuzzle.numberOfRows());
        assertEquals(14, samplePuzzle.getIslandNumbers().size());
        assertEquals(7, samplePuzzle.getIslandNumber(2, 4));
        assertEquals(2, samplePuzzle.getIslandNumber(0, 0));
        assertEquals(1, samplePuzzle.getIslandNumber(8, 1));
    }

    @Test
    public void isCompleteShouldReturnTrueWhenAppropriate() throws Exception {
        assertTrue(sampleSolution.isComplete());
    }

    @Test
    public void isCompleteShouldReturnFalseWhenAppropriate() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("2.#...##.2:");
        builder.append("###..#2###:");
        builder.append("#2#.7#.#.#:");
        builder.append("#.# ####.#:");
        builder.append("##.#..3#3#:");
        builder.append(".#2####3##:");
        builder.append("2##4.#..#.:");
        builder.append("##..#####.:");
        builder.append("#1###.2#4.");
        NurikabeGrid grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.isComplete());
    }

    @Test
    public void testHasPools() throws Exception {
        assertFalse(samplePuzzle.hasPools());
        assertFalse(sampleSolution.hasPools());

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        builder.append("##");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.hasPools());

        builder = new StringBuilder();
        builder.append("2##...##.2:");
        builder.append("###..#2###:");
        builder.append("#2#.7#.#.#:");
        builder.append("#.######.#:");
        builder.append("##.#..3#3#:");
        builder.append(".#2####3##:");
        builder.append("2##4.#..#.:");
        builder.append("##..#####.:");
        builder.append("#1###.2#4.");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.hasPools());

        builder = new StringBuilder();
        builder.append("###...##.2:");
        builder.append("##...#2###:");
        builder.append("#2#.7#.#.#:");
        builder.append("#.######.#:");
        builder.append("##.#..3#3#:");
        builder.append(".#2####3##:");
        builder.append("2##4.#..#.:");
        builder.append("##..#####.:");
        builder.append("#1###.2#4.");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.hasPools());

        builder = new StringBuilder();
        builder.append("##:");
        builder.append("##");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.hasPools());
        
        builder = new StringBuilder();
        // ............ 012345
        builder.append("      :"); // 0
        builder.append(" #    :"); // 1
        builder.append("#.#2  :"); // 2
        builder.append(".5    :"); // 3
        builder.append("     #:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.hasPools());
        
        builder = new StringBuilder();
        // ............ 012345
        builder.append(" ##   :"); // 0
        builder.append(" ##   :"); // 1
        builder.append("#.#2  :"); // 2
        builder.append(".5    :"); // 3
        builder.append("     #:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.hasPools());
    }

    @Test
    public void isWaterConnected() throws Exception {
        assertTrue(sampleSolution.isWaterConnected());

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        builder.append("2.#...##.2:");
        builder.append("###..#2###:");
        builder.append("#2#.7#.#.#:");
        builder.append("#.#.####.#:");
        builder.append("##.#..3#3#:");
        builder.append(".#2####3##:");
        builder.append("2##4.#..#.:");
        builder.append("##..#####.:");
        builder.append("#1###.2#4.");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.isWaterConnected());

        builder = new StringBuilder();
        builder.append("  # :");
        builder.append(" ## :");
        builder.append(" #  :");
        builder.append(" #.#");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.isWaterConnected());

        builder = new StringBuilder();
        builder.append("# :");
        builder.append(" #");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.isWaterConnected());

        builder = new StringBuilder();
        builder.append("##:");
        builder.append(" #");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isWaterConnected());

        builder = new StringBuilder();
        builder.append("#                         #:");
        builder.append("###########################");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isWaterConnected());

        builder = new StringBuilder();
        builder.append("#   #                     #:");
        builder.append("###########################");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isWaterConnected());

        builder = new StringBuilder();
        builder.append("..:");
        builder.append("..");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isWaterConnected());

        builder = new StringBuilder();
        builder.append("..:");
        builder.append(".#");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isWaterConnected());

        builder = new StringBuilder();
        builder.append("2. ...  .#:");
        builder.append(" ####### #:");
        builder.append(" #  7 .#.#:");
        builder.append(" # ### #.#:");
        builder.append(" #.#.#3#3#:");
        builder.append(".#2#   # #:");
        builder.append(" # ##### #:");
        builder.append(" #..     #:");
        builder.append(" #########");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isWaterConnected());

    }

    @Test
    public void testGetCellTypeCoordinates() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;
        builder = new StringBuilder();
        builder.append("2. #.:");
        builder.append(".# #.:");
        builder.append("#.3#.");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Set<Cell> expectedCells = makeSet(grid.cellAt(0, 3), grid.cellAt(1, 1), grid.cellAt(1, 3), grid.cellAt(2, 0),
                grid.cellAt(2, 3));
        assertEquals(expectedCells, grid.getWaterCells());

        builder = new StringBuilder();
        builder.append("2.  .:");
        builder.append(".   .:");
        builder.append(" .3..");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertEquals(0, grid.getWaterCells().size());

        builder = new StringBuilder();
        builder.append("2 1#.:");
        builder.append(".# # :");
        builder.append("#.3#.");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        expectedCells = makeSet(grid.cellAt(0, 0), grid.cellAt(0, 2), grid.cellAt(0, 4), grid.cellAt(1, 0), grid
                .cellAt(2, 1), grid.cellAt(2, 2), grid.cellAt(2, 4));
        assertEquals(expectedCells, grid.getLandCells());

    }

    @Test
    public void testGetNeighbours() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;
        builder = new StringBuilder();
        builder.append("   :");
        builder.append("   :");
        builder.append("   ");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Set<Cell> actualCells;
        Set<Cell> expectedCells;

        actualCells = grid.cellAt(1, 1).getNeighbours();
        expectedCells = makeSet(grid.cellAt(0, 1), grid.cellAt(1, 0), grid.cellAt(1, 2), grid.cellAt(2, 1));
        assertEquals(expectedCells, actualCells);

        actualCells = grid.cellAt(0, 0).getNeighbours();
        expectedCells = makeSet(grid.cellAt(1, 0), grid.cellAt(0, 1));
        assertEquals(expectedCells, actualCells);

        actualCells = grid.cellAt(2, 2).getNeighbours();
        expectedCells = makeSet(grid.cellAt(1, 2), grid.cellAt(2, 1));
        assertEquals(expectedCells, actualCells);

        actualCells = grid.cellAt(0, 1).getNeighbours();
        expectedCells = makeSet(grid.cellAt(0, 0), grid.cellAt(0, 2), grid.cellAt(1, 1));
        assertEquals(expectedCells, actualCells);

    }

    @Test
    public void testGetPolynomioAt() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;
        builder = new StringBuilder();
        builder.append("2. #.:");
        builder.append(" ###.:");
        builder.append("#.3#.");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Polyomino actualPolyomino;
        Polyomino expectedPolyomino;

        actualPolyomino = grid.cellAt(0, 0).getPolyomino();
        expectedPolyomino = new Polyomino(makeSet(grid.cellAt(0, 0), grid.cellAt(0, 1)));
        assertEquals(expectedPolyomino, actualPolyomino);

        actualPolyomino = grid.cellAt(1, 3).getPolyomino();
        expectedPolyomino = new Polyomino(makeSet(grid.cellAt(0, 3), grid.cellAt(1, 3), grid.cellAt(2, 3), grid.cellAt(
                1, 2), grid.cellAt(1, 1)));
        assertEquals(expectedPolyomino, actualPolyomino);

        actualPolyomino = grid.cellAt(1, 0).getPolyomino();
        expectedPolyomino = new Polyomino(makeSet(grid.cellAt(1, 0)));
        assertEquals(expectedPolyomino, actualPolyomino);

    }

    @Test
    public void testIsEachNumberCellPartOfAnIslandOfThatSize() throws Exception {
        assertFalse(samplePuzzle.isEachNumberCellPartOfAnIslandOfThatSize());
        assertTrue(sampleSolution.isEachNumberCellPartOfAnIslandOfThatSize());
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        builder.append("2.#...##.2:");
        builder.append("###..#2###:");
        builder.append("#2#.7#.#.#:");
        builder.append("#.######.#:");
        builder.append("##.#..3#4#:");
        builder.append(".#2####3##:");
        builder.append("2##4.#..#.:");
        builder.append("##..#####.:");
        builder.append("#1###.2#4.");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.isEachNumberCellPartOfAnIslandOfThatSize());

        builder = new StringBuilder();
        builder.append("    :");
        builder.append("    :");
        builder.append("    ");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isEachNumberCellPartOfAnIslandOfThatSize());

        builder = new StringBuilder();
        builder.append(" 2. :");
        builder.append(" #  :");
        builder.append("    ");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isEachNumberCellPartOfAnIslandOfThatSize());

        builder = new StringBuilder();
        builder.append("   :");
        builder.append(" 3 :");
        builder.append("   ");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.isEachNumberCellPartOfAnIslandOfThatSize());

        builder = new StringBuilder();
        builder.append("22  :");
        builder.append("  3 :");
        builder.append(" 3. ");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isEachNumberCellPartOfAnIslandOfThatSize());

    }

    @Test
    public void testDoesSomeIslandContainMultipleNumbers() throws Exception {
        assertFalse(samplePuzzle.doesSomeIslandContainMultipleNumbers());
        assertFalse(sampleSolution.doesSomeIslandContainMultipleNumbers());

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        builder.append("2   :");
        builder.append("  3 :");
        builder.append(" 3. ");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.doesSomeIslandContainMultipleNumbers());

    }

    @Test
    public void testIsEveryLandCellPartOfAnIsland() throws Exception {
        assertTrue(samplePuzzle.isEveryLandCellPartOfAnIsland());
        assertTrue(sampleSolution.isEveryLandCellPartOfAnIsland());

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        builder.append("2.# :");
        builder.append("  3 :");
        builder.append(".#. ");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.isEveryLandCellPartOfAnIsland());

    }

    @Test
    public void testIsASolution() throws Exception {
        assertFalse(samplePuzzle.isCompleteAndCorrect());
        assertTrue(sampleSolution.isCompleteAndCorrect());
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        builder.append("2.#:");
        builder.append("###:");
        builder.append("1#1");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertTrue(grid.isCompleteAndCorrect());

        builder = new StringBuilder();
        builder.append("2.#:");
        builder.append("# #:");
        builder.append("1#1");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        assertFalse(grid.isCompleteAndCorrect());

    }

    @Test
    public void testSomeSolutions() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        builder.append(".5##3.#..:");
        builder.append(".#8#.##5.:");
        builder.append(".#.###2#.:");
        builder.append(".#...#.##:");
        builder.append("####.###.:");
        builder.append("#..#.#.#.:");
        builder.append("##3#.#.#.:");
        builder.append("#3####3#.:");
        builder.append("#..#1##6.");
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        checkSolution(grid);
    }

    private void checkSolution(NurikabeGrid solution) {
        assertTrue(solution.isCompleteAndCorrect());
        assertTrue(solution.isComplete());
        assertFalse(solution.hasPools());
        assertTrue(solution.isWaterConnected());
        assertTrue(solution.isEachNumberCellPartOfAnIslandOfThatSize());
        assertFalse(solution.doesSomeIslandContainMultipleNumbers());
        assertTrue(solution.isEveryLandCellPartOfAnIsland());
    }

    @Test
    public void testConcludeCell() throws Exception {
        StringBuilder builder;

        builder = new StringBuilder();
        builder.append("2.# :");
        builder.append("    :");
        builder.append("1 1 ");
        NurikabeGrid startGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        NurikabeGrid actualGrid = startGrid.concludeCellIsWater(2, 1);

        builder = new StringBuilder();
        builder.append("2.# :");
        builder.append("    :");
        builder.append("1#1 ");
        NurikabeGrid expectedGrid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        assertEquals(expectedGrid, actualGrid);

        try {
            startGrid.concludeCellIsWater(0, 0);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        try {
            startGrid.concludeCellIsWater(5, 5);
            fail("Exception expected");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }

        assertEquals(startGrid, startGrid.concludeCellIsWater(0, 2));

        assertEquals(startGrid, startGrid.concludeCellIsWater(1, 1).removeConclusionFromCell(1, 1));

    }

    @Test
    public void testGetNumberCells() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("  2 :"); // 0
        builder.append(" 1  :"); // 1
        builder.append("   5:"); // 2
        builder.append("3   :"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Set<Cell> expectedCells = makeSet(grid.cellAt(0, 2), grid.cellAt(1, 1), grid.cellAt(2, 3), grid.cellAt(3, 0));
        Set<Cell> actualCells = grid.getIslandNumberCells();
        assertEquals(expectedCells, actualCells);

    }

    @Test
    public void testCompletedIslands() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append(" #2.:"); // 0
        builder.append(" 1##:"); // 1
        builder.append(".  5:"); // 2
        builder.append("3. .:"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Polyomino polyomino1 = new Polyomino(makeSet(grid.cellAt(1, 1)));
        Polyomino polyomino2 = new Polyomino(makeSet(grid.cellAt(0, 2), grid.cellAt(0, 3)));
        Polyomino polyomino3 = new Polyomino(makeSet(grid.cellAt(3, 0), grid.cellAt(3, 1), grid.cellAt(2, 0)));
        Set<Polyomino> expectedIslands = makeSet(polyomino1, polyomino2, polyomino3);
        Set<Polyomino> actualIslands = grid.getCompletedIslands();
        assertEquals(expectedIslands, actualIslands);

    }

    @Test
    public void testGetPolyominoesOfType() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("##2.:"); // 0
        builder.append(" 1##:"); // 1
        builder.append(". #5:"); // 2
        builder.append("3. .:"); // 3

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        Polyomino polyomino1 = new Polyomino(makeSet(grid.cellAt(1, 1)));
        Polyomino polyomino2 = new Polyomino(makeSet(grid.cellAt(0, 2), grid.cellAt(0, 3)));
        Polyomino polyomino3 = new Polyomino(makeSet(grid.cellAt(3, 0), grid.cellAt(3, 1), grid.cellAt(2, 0)));
        Polyomino polyomino5 = new Polyomino(makeSet(grid.cellAt(2, 3), grid.cellAt(3, 3)));
        Set<Polyomino> expectedIslands = makeSet(polyomino1, polyomino2, polyomino3, polyomino5);
        Set<Polyomino> actualIslands = grid.getPolyominoesOfType(CellType.LAND);
        assertEquals(expectedIslands, actualIslands);

    }

    @Test
    public void testIsCompleteIsland() throws Exception {

        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append(".3##:"); // 0
        builder.append(" .  :"); // 1
        builder.append("   2:"); // 2
        builder.append("..  :"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Polyomino polyomino1 = new Polyomino(makeSet(grid.cellAt(0, 0), grid.cellAt(0, 1), grid.cellAt(1, 1)));
        assertTrue(grid.isCompleteIsland(polyomino1));
        Polyomino polyomino2 = new Polyomino(makeSet(grid.cellAt(3, 0), grid.cellAt(3, 1)));
        assertFalse(grid.isCompleteIsland(polyomino2));
        Polyomino polyomino3 = new Polyomino(makeSet(grid.cellAt(2, 3)));
        assertFalse(grid.isCompleteIsland(polyomino3));
        Polyomino polyomino4 = new Polyomino(makeSet(grid.cellAt(0, 2)));
        assertFalse(grid.isCompleteIsland(polyomino4));

    }

    @Test
    public void testGetAllPartialIslandsConnectedToANumber() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 012345
        builder.append("   .. :"); // 0
        builder.append(".#    :"); // 1
        builder.append("#.#2 .:"); // 2
        builder.append(" 5  . :"); // 3
        builder.append(". . .#:"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        
        Set<Polyomino> actualIslands = grid.getAllPartialIslandsConnectedToANumber();
        
        Set<Polyomino> expectedIslands = new HashSet<Polyomino>();
        expectedIslands.add(new Polyomino(makeSet(grid.cellAt(3, 1), grid.cellAt(2, 1))));
        expectedIslands.add(new Polyomino(makeSet(grid.cellAt(2, 3))));
        assertEquals(expectedIslands, actualIslands);
    }
    
//    @Test
//    public void test() throws Exception {
//    
//    }
//    
}

