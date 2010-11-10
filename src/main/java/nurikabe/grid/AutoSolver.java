package nurikabe.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.Rationale;
import nurikabe.reasoning.strategy.AllPolyominoesGridForker;
import nurikabe.reasoning.strategy.AllPossibilitiesReasoningStrategy;
import nurikabe.reasoning.strategy.CompleteIslandIsSurroundedByWaterReasoningStrategy;
import nurikabe.reasoning.strategy.NoPathToCellFromAnyNumberReasoningStrategy;
import nurikabe.reasoning.strategy.NoPoolsReasoningStrategy;
import nurikabe.reasoning.strategy.OnlyOnePlaceToGrowReasoningStrategy;
import nurikabe.reasoning.strategy.ReasoningStrategy;
import nurikabe.reasoning.strategy.TryAllPolyominoesReasoningStrategy;
import nurikabe.reasoning.strategy.UnknownPolyominoEnclosedByLandOrWaterReasoningStrategy;
import nurikabe.reasoning.strategy.WaterIfDiagonallyAdjacentToANearlyCompleteIslandReasoningStrategy;
import nurikabe.reasoning.strategy.WaterIfHasNeighboursFromDifferentIslandsReasoningStrategy;
import nurikabe.reasoning.strategy.WaterOrLandLookaheadReasoningStrategy;

public class AutoSolver {

    private List<ReasoningStrategy> strategies;

    public AutoSolver(ReasoningStrategy... strategies) {
        this(Arrays.asList(strategies));
    }

    public AutoSolver(List<ReasoningStrategy> strategies) {
        this.strategies = new ArrayList<ReasoningStrategy>(strategies);
    }

    public static AutoSolver makeStandardSolver() {
        List<ReasoningStrategy> baseStrategies = getBaseStrategies();
        List<ReasoningStrategy> allStrategies = new ArrayList<ReasoningStrategy>(baseStrategies);
        allStrategies.add(new WaterOrLandLookaheadReasoningStrategy(baseStrategies));
        AllPossibilitiesReasoningStrategy allPolyominoesLookaheadStrategy = new AllPossibilitiesReasoningStrategy(new AllPolyominoesGridForker(), baseStrategies);
        allStrategies.add(allPolyominoesLookaheadStrategy);
        return new AutoSolver(allStrategies);
    }

    private static List<ReasoningStrategy> getBaseStrategies() {
        List<ReasoningStrategy> baseStrategies = new ArrayList<ReasoningStrategy>();
        baseStrategies.add(new NoPoolsReasoningStrategy());
        baseStrategies.add(new CompleteIslandIsSurroundedByWaterReasoningStrategy());
        baseStrategies.add(new WaterIfHasNeighboursFromDifferentIslandsReasoningStrategy());
        baseStrategies.add(new UnknownPolyominoEnclosedByLandOrWaterReasoningStrategy());
        baseStrategies.add(new OnlyOnePlaceToGrowReasoningStrategy());
        baseStrategies.add(new NoPathToCellFromAnyNumberReasoningStrategy());
        baseStrategies.add(new WaterIfDiagonallyAdjacentToANearlyCompleteIslandReasoningStrategy());
        baseStrategies.add(new TryAllPolyominoesReasoningStrategy());
        return baseStrategies;
    }

    public static void main(String[] args) {
        StringBuilder builder;
        NurikabeGrid grid;

        builder = new StringBuilder();
        // ............ 0123456
        builder.append(" 3   1 :"); // 0
        builder.append("       :"); // 1
        builder.append("2  1   :"); // 2
        builder.append("       :"); // 3
        builder.append(" 1  2  :"); // 4
        builder.append("  2    :"); // 5
        builder.append("1   1 6:"); // 6

        // builder = new StringBuilder();
        // // ............ 01234
        // builder.append("2 :"); // 0
        // builder.append(" 1:"); // 1
        // builder.append(" :"); // 2
        // builder.append("5 :"); // 3
        // builder.append(" 2:"); // 4
        //

        grid = NurikabeGrid.createFromAsciiGrid(builder.toString());

        AutoSolver solver = new AutoSolver();

        solver.run(grid);
    }

    public void run(NurikabeGrid startGrid) {
        NurikabeGrid grid = startGrid;
        grid.prettyPrintGrid();
        while (true) {
            boolean changed = false;
            for (ReasoningStrategy strategy : strategies) {
                Map<Conclusion, Rationale> conclusionsAndRationales = strategy.makeConclusions(grid);
                for (Conclusion conclusion : conclusionsAndRationales.keySet()) {
                    changed = true;
                    Rationale rationale = conclusionsAndRationales.get(conclusion);
                    System.out.println(conclusion + " because " + rationale.getDescription());
                    grid = conclusion.applyTo(grid);
                    grid.prettyPrintGrid();
                }
                if (changed)
                    break;
            }
            if (!changed)
                break;

        }
    }

    public Map<Conclusion, Rationale> getFirstConclusion(NurikabeGrid grid) { // TODO: Put this somewhere sensible
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();
        for (ReasoningStrategy strategy : strategies) {
            Map<Conclusion, Rationale> strategyConclusions = strategy.makeConclusions(grid);
            if (strategyConclusions.size() == 0)
                continue;
            for (Conclusion conclusion : strategyConclusions.keySet()) {
                Rationale rationale = strategyConclusions.get(conclusion);
                conclusions.put(conclusion, rationale);
                return conclusions;
            }
        }
        return conclusions;
    }

    public Map<Conclusion, Rationale> getConclusions(NurikabeGrid grid) { // TODO: Put this somewhere sensible
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();
        for (ReasoningStrategy strategy : strategies) {
            Map<Conclusion, Rationale> strategyConclusions = strategy.makeConclusions(grid);
            if (strategyConclusions.size() == 0)
                continue;
            for (Conclusion conclusion : strategyConclusions.keySet()) {
                Rationale rationale = strategyConclusions.get(conclusion);
                Conclusion.putAdditionalConclusion(conclusions, conclusion, rationale);
            }
        }
        return conclusions;
    }
}
