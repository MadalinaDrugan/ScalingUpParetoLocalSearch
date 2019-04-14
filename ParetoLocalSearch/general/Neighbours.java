package general;
//import QAPs_optimal.PartialSolution_;
//import localoptimization.NonDominatedArchive;

import general.Genetic.PerturbatorStrategies;
import java.util.HashMap;
import PLS.LocalSearch.Counter;

//import general.Scalarization.ArchiveScalSolutions;

import general.Scalarization.Scalarizer;
import general.Reduction.SetReductions;
import java.util.Stack;
/**
 *
 * @author madalina
 */
public interface Neighbours {

    public ArchiveSolutions  neighbours(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter);
    
    public ArchiveSolutions  neighbours(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter, boolean usedM, HashMap<Long,Counter> tableSolVisited);
    public ArchiveSolutions  neighbours(Solution ps, ArchiveSolutions nda, IteratorSolutions iter, boolean usedM, HashMap<Long,Counter> tableSolVisited);

    public ArchiveSolutions  neighbours(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter, Scalarizer scal);
    public ArchiveSolutions  neighboursReduction(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter, int index);
    
    public Stack<Solution> getNDARemoved();

    public void setVariator(ProblemInstance p, IteratorSolutions iter, PerturbatorStrategies epsilon2);
    public void setVariator(ProblemInstance p, IteratorSolutions iter, PerturbatorStrategies epsilon2, SetReductions setRed);
    //public void setVariator(ProblemInstance p, IteratorSolutions[] iter, PerturbatorStrategies epsilon2);
    public long getExpandSol();

}
