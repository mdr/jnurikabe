package nurikabe.reasoning.strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nurikabe.grid.AutoSolver;
import nurikabe.grid.NurikabeGrid;
import nurikabe.reasoning.Conclusion;
import nurikabe.reasoning.rationale.ForkExplainer;
import nurikabe.reasoning.rationale.Rationale;

public class AllPossibilitiesReasoningStrategy implements ReasoningStrategy {

    private final AutoSolver autoSolver;

    private final AllPossibilitiesGridForker gridForker;

    public AllPossibilitiesReasoningStrategy( AllPossibilitiesGridForker gridSplitter, ReasoningStrategy... strategies ) {
        this( gridSplitter, Arrays.asList( strategies ) );
    }

    public AllPossibilitiesReasoningStrategy( AllPossibilitiesGridForker gridForker, List<ReasoningStrategy> strategies ) {
        this.autoSolver = new AutoSolver( strategies );
        this.gridForker = gridForker;
    }

    public Map<Conclusion, Rationale> makeConclusions( NurikabeGrid grid ) {
        Map<Conclusion, Rationale> conclusions = new HashMap<Conclusion, Rationale>();
        Set<ForkedGrid> gridForks = gridForker.forkGridIntoPossibilities( grid );
        for ( ForkedGrid gridFork : gridForks ) {
            Set<NurikabeGrid> possibleGrids = gridFork.getPossibleGrids();
            if ( possibleGrids.size() == 0 )
                continue; // TODO: Log exception?
            Map<NurikabeGrid, Map<Conclusion, Rationale>> conclusionsPerGrid = new HashMap<NurikabeGrid, Map<Conclusion, Rationale>>();
            Set<Conclusion> sharedConclusionsAcrossFork = getSharedConclusionsAcrossFork( possibleGrids, conclusionsPerGrid );

            ForkExplainer forkExplainer = gridFork.getForkExplainer();

            for ( Conclusion conclusion : sharedConclusionsAcrossFork ) {
                Map<NurikabeGrid, Rationale> explanationsPerPossibleGrid = new HashMap<NurikabeGrid, Rationale>();
                for ( NurikabeGrid possibleGrid : possibleGrids ) {
                    Rationale rationaleForPossibleGrid = conclusionsPerGrid.get( possibleGrid ).get( conclusion );
                    explanationsPerPossibleGrid.put( possibleGrid, rationaleForPossibleGrid );
                }
                Rationale rationale = forkExplainer.makeRationale( explanationsPerPossibleGrid );
                Conclusion.putAdditionalConclusion( conclusions, conclusion, rationale );
            }

        }
        return conclusions;
    }

    private Set<Conclusion> getSharedConclusionsAcrossFork( Set<NurikabeGrid> possibleGrids, Map<NurikabeGrid, Map<Conclusion, Rationale>> conclusionsPerGrid ) {
        Set<Conclusion> sharedConclusionsAcrossFork = null;
        for ( NurikabeGrid possibleGrid : possibleGrids ) {
            Map<Conclusion, Rationale> conclusionsForThisGrid = autoSolver.getConclusions( possibleGrid );
            conclusionsPerGrid.put( possibleGrid, conclusionsForThisGrid );
            if ( sharedConclusionsAcrossFork == null )
                sharedConclusionsAcrossFork = new HashSet<Conclusion>( conclusionsForThisGrid.keySet() );
            else
                sharedConclusionsAcrossFork.retainAll( conclusionsForThisGrid.keySet() );
        }
        return sharedConclusionsAcrossFork;
    }

}
