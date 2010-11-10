package nurikabe.reasoning.contradiction;

import static org.junit.Assert.assertEquals;
import static utils.CollectionsUtils.makeSet;

import java.util.Collections;
import java.util.Set;

import nurikabe.grid.NurikabeGrid;
import nurikabe.grid.Polyomino;

import org.junit.Before;
import org.junit.Test;

public class HasPoolsContradictionDetectorTest {

    private ContradictionDetector contradictionDetector;

    @Before
    public void setup() {
        contradictionDetector = new HasPoolsContradictionDetector();
    }

    @Test
    public void testFindsNoContradiction() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;
        Set<Contradiction> contradictions;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("    :"); // 0
        builder.append("    :"); // 1
        builder.append("    :"); // 2
        builder.append("    :"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        contradictions = contradictionDetector.findContradictions(grid);
        assertEquals(Collections.emptySet(), contradictions);

        builder = new StringBuilder();
        // ............ 0123
        builder.append("####:"); // 0
        builder.append("#  #:"); // 1
        builder.append("#  #:"); // 2
        builder.append("####:"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        contradictions = contradictionDetector.findContradictions(grid);
        assertEquals(Collections.emptySet(), contradictions);

        builder = new StringBuilder();
        // ............ 0123
        builder.append("####:"); // 0
        builder.append("#2 #:"); // 1
        builder.append("#.1#:"); // 2
        builder.append("####:"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        contradictions = contradictionDetector.findContradictions(grid);
        assertEquals(Collections.emptySet(), contradictions);
    }

    @Test
    public void testFindsSingleContradiction() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("    :"); // 0
        builder.append(" ## :"); // 1
        builder.append(" ## :"); // 2
        builder.append("    :"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Polyomino pool = new Polyomino(makeSet(grid.cellAt(1, 1), grid.cellAt(1, 2), grid.cellAt(2, 1), grid.cellAt(2,
                2)));
        Contradiction expectedContradiction = new HasPoolsContradiction(pool);

        Set<Contradiction> actualContradictions = contradictionDetector.findContradictions(grid);

        assertEquals(Collections.singleton(expectedContradiction), actualContradictions);
    }

    @Test
    public void testFindsMultipleContradictions() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("### :"); // 0
        builder.append("###.:"); // 1
        builder.append("### :"); // 2
        builder.append("  .2:"); // 3
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Polyomino pool1 = new Polyomino(grid.cellAt(0, 0), grid.cellAt(0, 1), grid.cellAt(1, 0), grid.cellAt(1, 1));
        Polyomino pool2 = new Polyomino(grid.cellAt(0, 1), grid.cellAt(0, 2), grid.cellAt(1, 1), grid.cellAt(1, 2));
        Polyomino pool3 = new Polyomino(grid.cellAt(1, 0), grid.cellAt(1, 1), grid.cellAt(2, 0), grid.cellAt(2, 1));
        Polyomino pool4 = new Polyomino(grid.cellAt(1, 1), grid.cellAt(1, 2), grid.cellAt(2, 1), grid.cellAt(2, 2));
        Set<HasPoolsContradiction> expectedContradictions = makeSet(new HasPoolsContradiction(pool1),
                new HasPoolsContradiction(pool2), new HasPoolsContradiction(pool3), new HasPoolsContradiction(pool4));
        Set<Contradiction> actualContradictions = contradictionDetector.findContradictions(grid);
        assertEquals(expectedContradictions, actualContradictions);
    }

    @Test
    public void testSmallGrids() throws Exception {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123
        builder.append("## :"); // 0
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        Set<Contradiction> contradictions = contradictionDetector.findContradictions(grid);
        assertEquals(Collections.emptySet(), contradictions);

        builder = new StringBuilder();
        // ............ 0
        builder.append("#:"); // 0
        builder.append("#:"); // 1
        builder.append("2:"); // 2
        builder.append(".:"); // 3
        builder.append(" :"); // 4
        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());
        
        contradictions = contradictionDetector.findContradictions(grid);
        assertEquals(Collections.emptySet(), contradictions);
    }

}
