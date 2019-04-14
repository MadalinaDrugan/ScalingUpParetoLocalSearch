/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Reduction;

import Reduction.ReduceScalarizer;
import general.Genetic.PerturbatorStrategies;
import general.Solution;
import java.util.Stack;

/**
 *
 * @author madalina
 */
public interface ReductionOperators {
    public Stack<ReduceScalarizer> makeScalarizations(Solution[] selectSol, Reduction[] parents, PerturbatorStrategies strat);
    public int nrParents();
    public int nrSolutions();
}
