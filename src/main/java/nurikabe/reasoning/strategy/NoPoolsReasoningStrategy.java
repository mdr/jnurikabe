package nurikabe.reasoning.strategy;

import static nurikabe.grid.CellType.LAND;
import static nurikabe.grid.CellType.UNDETERMINED;
import static nurikabe.grid.CellType.WATER;

import java.util.HashMap;
import java.util.Map;

import nurikabe.grid.CellType;
import nurikabe.grid.Coordinate;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.NoPoolsRationale;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.rationale.NoPoolsRationale.Corner;

public class NoPoolsReasoningStrategy implements ReasoningStrategy {

    public Map<Conclusion, Rationale> makeConclusions(NurikabeGrid grid) {
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();

        for (int row = 0; row < grid.numberOfRows() - 1; row++)
            for (int column = 0; column < grid.numberOfColumns() - 1; column++)
                analyse2x2Block(grid, row, column, conclusions);

        return conclusions;
    }

    private void analyse2x2Block(NurikabeGrid grid, int row, int column, Map<Conclusion, Rationale> conclusions) {
        CellType nwCell = grid.getCellType(row, column);
        CellType neCell = grid.getCellType(row, column + 1);
        CellType swCell = grid.getCellType(row + 1, column);
        CellType seCell = grid.getCellType(row + 1, column + 1);

        Corner corner = null;
        Coordinate locationConcludedToBeLand = null;
        if (nwCell == UNDETERMINED && neCell == WATER && swCell == WATER && seCell == WATER) {
            locationConcludedToBeLand = Coordinate.makeCoordinate(row, column);
            corner = Corner.NORTH_WEST;
        } else if (nwCell == WATER && neCell == UNDETERMINED && swCell == WATER && seCell == WATER) {
            locationConcludedToBeLand = Coordinate.makeCoordinate(row, column + 1);
            corner = Corner.NORTH_EAST;
        } else if (nwCell == WATER && neCell == WATER && swCell == UNDETERMINED && seCell == WATER) {
            locationConcludedToBeLand = Coordinate.makeCoordinate(row + 1, column);
            corner = Corner.SOUTH_WEST;
        } else if (nwCell == WATER && neCell == WATER && swCell == WATER && seCell == UNDETERMINED) {
            locationConcludedToBeLand = Coordinate.makeCoordinate(row + 1, column + 1);
            corner = Corner.SOUTH_EAST;
        }

        if (locationConcludedToBeLand != null) {
            Conclusion conclusion = new Conclusion(locationConcludedToBeLand, LAND);
            Rationale rationale = new NoPoolsRationale(locationConcludedToBeLand, corner);
            Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);
        }
    }

}
