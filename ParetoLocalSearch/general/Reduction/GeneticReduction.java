/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Reduction;

import general.*;
import java.util.Stack;
/**
 *
 * @author Madalina Drugan, Utrecht University
 */
public interface GeneticReduction{

    /*public Reduction[] selectParents(int sizePool, SetReductions s, SelectMatingChildren set); // select the parents
    public SetReductions replaceParents(int[] indexP, SetReductions par, Reduction[] children, SelectReplacingParents set);
    public int[] getIndexParents();

    public Solution[] setSolutions(int sizePool, Solution[] sol, SelectMatingChildren set);
    public Solution[] setSolutions(int sizePool, ArchiveSolutions sol, SelectMatingChildren set);
    public int[] getIndexSolutions();
    
    public SetReductions makeReduction(SetReductions par, Solution[] sol);
    
    public abstract int getNrParents();
    public abstract int getNrSolutions();

    public abstract void setPerturbatorType(int operatorType);
    public abstract PerturbatorStrategies getPerturbator();

    public abstract boolean adaptation();
    public abstract void restart();
    */

    public Stack<Reduction> selectParents(); // select the parents
    public Stack<Reduction> replaceParents(Stack<Reduction> children);
    public int[] getIndexParents();

    //public Solution[] selectSolutions(Solution[] sol);
    //public Solution[] selectSolutions(ArchiveSolutions sol);
    //public int[] getIndexSolutions();
    
    public Reduction[] makeReduction();
    
    public abstract int getNrParents();
    //public abstract int getNrSolutions();

    //public abstract void setPerturbatorType(int operatorType);
    //public abstract PerturbatorStrategies getPerturbator();

    public abstract boolean adaptation();
    public abstract void restart();

    public void adaptation(ArchiveSolutions parents, ArchiveSolutions children, ArchiveSolutions currentNDA, PerformanceInterface perf);
}
