/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Scalarization;

import general.*;
/**
 *
 * @author Madalina Drugan, Utrecht University
 */
public interface SolutionScalarizer{

    public Solution[] selectSolutions(Solution[] sol);
    public Solution[] selectSolutions(ArchiveSolutions sol);
    public int[] getIndexSolutions();
    
    public Scalarizer[] makeScalarizer(Solution[] sol);
    
    public abstract int getNrSolutions();

    //public abstract void setPerturbatorType(int operatorType);
    //public abstract PerturbatorStrategies getPerturbator();

    public abstract boolean adaptation();
    public abstract void restart();

    public void adaptation(ArchiveSolutions parents, ArchiveSolutions children, ArchiveSolutions currentNDA, PerformanceInterface perf);
}
