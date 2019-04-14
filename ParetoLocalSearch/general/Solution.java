/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

//import localoptimization.Solution;
import general.Genetic.PerturbatorStrategies;
import general.Scalarization.Scalarizer;
import general.Reduction.SetReductions;
import java.util.*;

/**PartialSolution with the constructor from QAPs
 *
 * @author madalina
 */
public abstract class Solution implements Comparable<Solution>, java.io.Serializable, Comparator<Solution>{
    public boolean flagVisited = false;
    public Object[] items;
    public Object[] objectives;

    //relationships
    public abstract boolean dominates(ArchiveSolutions ps);
    public abstract boolean isDominated(ArchiveSolutions ps);

    public abstract boolean dominates(Solution ps);
    public abstract boolean isDominated(Solution ps);
    
    //public abstract boolean dominates(Solution ps, PerturbatorStrategies epsilon2);
    public abstract boolean dominates(Object[] obj);
    public abstract Object[] dominatingObj(Object[] obj);
    public abstract boolean isDominated(Object[] obj);
    
    public abstract boolean incomparable(Object[] obj, PerturbatorStrategies epsilon2);
    
    public abstract boolean incomparable(Solution ps, PerturbatorStrategies epsilon2);
    public abstract boolean incomparable(ArchiveSolutions ps, PerturbatorStrategies epsilon2);
    
    public abstract long getIdentifNumber();

    //minimum number of interchanges between two solutions
    public abstract int getDistance(Solution s);
    public abstract int getDistance(Object[] temp);
    public abstract int getDistance(Object[] temp, Object[] temp2);

    public abstract Solution getASolution();

    public abstract boolean setVisited();

    public abstract void setTypeProblem(boolean type);

    /////////////////////
    // set aditional 
    public abstract void setFlagOperation(int f);
    public abstract int getFlagOperation();

    public abstract boolean dominates(Solution ps, Scalarizer scal);
    public abstract boolean dominates(ArchiveSolutions ps, Scalarizer scal);
    public abstract boolean isDominated(Solution ps, Scalarizer scal);
    public abstract boolean isDominated(ArchiveSolutions ps, Scalarizer scal);
    public abstract boolean incomparable(Solution ps, Scalarizer scal);
    public abstract boolean incomparable(ArchiveSolutions ps, Scalarizer scal);
    public abstract void setScalarizer(Scalarizer scal);

    //public abstract boolean dominates(ArchiveSolutions ps, PerturbatorStrategies epsilon2);
    public abstract boolean dominatesReduction(Solution ps, double[][] scalWeights);
    public abstract boolean dominatesReduction(ArchiveSolutions ps, double[][] scalWeights);
    public abstract boolean isDominatedReduction(Solution ps, double[][] scalWeights);
    public abstract boolean isDominatedReduction(ArchiveSolutions ps, double[][] scalWeights);
    public abstract boolean incomparableReduction(Solution ps, double[][] scalWeights);
    public abstract boolean incomparableReduction(ArchiveSolutions ps, double[][] scalWeights);
    public abstract void setSetReduction(SetReductions scal);
    
    public abstract Solution clone();
}
