package nurikabe.grid;
import static nurikabe.grid.Coordinate.makeCoordinate;

import static org.junit.Assert.*;

import nurikabe.grid.Coordinate;

import org.junit.Test;

public class CoordinateTest {

    @Test
    public void testIsAdjacent() throws Exception {
        Coordinate tl = makeCoordinate(0, 0);
        Coordinate tc = makeCoordinate(0, 1);
        Coordinate tr = makeCoordinate(0, 2);
        Coordinate ml = makeCoordinate(1, 0);
        Coordinate mc = makeCoordinate(1, 1);
        Coordinate mr = makeCoordinate(1, 2);
        Coordinate bl = makeCoordinate(2, 0);
        Coordinate bc = makeCoordinate(2, 1);
        Coordinate br = makeCoordinate(2, 2);

        assertTrue(tl.isAdjacentTo(tc));
        assertTrue(tr.isAdjacentTo(tc));
        assertTrue(ml.isAdjacentTo(tl));
        assertTrue(bl.isAdjacentTo(ml));
        
        assertFalse(tl.isAdjacentTo(mc));
        assertFalse(tl.isAdjacentTo(mr));
        assertFalse(tc.isAdjacentTo(bc));
        assertFalse(br.isAdjacentTo(mc));
        
        assertFalse(tc.isAdjacentTo(tc));

    }
}
