/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Reduction;

import general.*;
/**
 *
 * @author Madalina Drugan, Utrecht University
 */
public interface SolutionReduction{

    public Solution[] selectSolutions(Solution[] sol);
    public Solution[] selectSolutions(ArchiveSolutions sol);
    public int[] getIndexSolutions();
    
    public Reduction[] makeReduction(Solution[] sol);
    
    public abstract int getNrSolutions();

    //public abstract void setPerturbatorType(int operatorType);
    //public abstract PerturbatorStrategies getPerturbator();

    public abstract boolean adaptation();
    public abstract void restart();

    public void adaptation(ArchiveSolutions parents, ArchiveSolutions children, ArchiveSolutions currentNDA, PerformanceInterface perf);
}
