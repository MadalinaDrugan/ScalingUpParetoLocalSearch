/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Reduction;

import Reduction.ReduceScalarizer;
import general.Solution;
import java.util.Comparator;
import java.util.Stack;

/**
 *
 * @author madalina
 */
public interface Reduction extends Comparable<Reduction>, java.io.Serializable, Comparator<Reduction> {
    
    Double[] reduction(Double[] objectives);
    Double[] reduction(Solution s);

    public void addReduction(Reduction scal);
    public Stack<ReduceScalarizer> getReductions();
    public double[][] getListWeightVectors();
    
    public void setReductions();
    public void setReductions(Stack<ReduceScalarizer> tempR);
    
    public void addScalarizer(ReduceScalarizer scal);
    public ReduceScalarizer getScalarizer(int indef);
    public void setScalarizer(ReduceScalarizer scal, int indef);
    public int size();
 
    public Reduction clone();
}
