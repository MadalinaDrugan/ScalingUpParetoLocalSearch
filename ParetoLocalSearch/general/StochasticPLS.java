/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

import general.Genetic.*;
import general.Reduction.*;
import general.Scalarization.*;

/**
 *
 * @author madalina
 */
public interface StochasticPLS {
    public void setVariator(Variator v, ProblemInstance problem, GeneticOperators g, ImplementPLS d, Stopping st, SelectMatingChildren m, boolean deac); //, PerturbatorStrategies e);
    public void setVariator(Variator v, ProblemInstance p, GeneticOperators g, ImplementPLS d, Stopping st, SelectMatingChildren m, SetScalarizer s);
    public void setVariator(Variator v, ProblemInstance p, GeneticOperators g, ImplementPLS d, Stopping st, SelectMatingChildren m, SetReductions s);
    //public void setVariator(Variator v, ProblemInstance p, GeneticOperators g, ImplementPLS d, Stopping st, SelectMatingChildren m, Iterator_R s, GeneticReduction gen);    
    
    public ArchiveSolutions getNext(ArchiveSolutions nda);
    
    public boolean stopCriteria();
    public void initStopCriteria();
    public void setStatistics(Statistics stat);
    public ArchiveSolutions PLS_procedure(Solution p, ArchiveSolutions nda);
    public void statistics(Solution p, ArchiveSolutions nda);
    public void statisticsFinal(ArchiveSolutions nda);
    
    public Solution[] restartingSolutions(ArchiveSolutions nda);
    public Solution[] getParents();
    public Solution randomSolution();
    
    //public void setPerformance(PerformanceInterface perf);
    public void activateStatistics(boolean statA);
    public void setMemory(boolean mem);
}
