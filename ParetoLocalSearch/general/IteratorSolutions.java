package general;
//import QAPs_optimal.PartialSolution_;
//import localoptimization.NonDominatedArchive;

/**
 *
 * @author madalina
 */
public interface IteratorSolutions {

    public boolean hasNext();
    public void iteratorInit(Solution ps, Variator var,  ProblemInstance problem);
    public void iteratorInit(Solution ps, ProblemInstance problem);
    public Solution next();
    
    public boolean adaptation();
    
    public void restart();
}
