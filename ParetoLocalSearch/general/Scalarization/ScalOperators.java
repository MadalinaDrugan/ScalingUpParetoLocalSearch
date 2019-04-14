/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Scalarization;

import general.Genetic.PerturbatorStrategies;
import general.Solution;

/**
 *
 * @author madalina
 */
public interface ScalOperators {
    public double[] makeWeights(Solution[] selectSol, Scalarizer[] parents, PerturbatorStrategies strat);
    public double[] makeWeights(Solution[] selectSol, Scalarizer[] parents, int[] objectives, PerturbatorStrategies strat);
    public int nrParents();
    public int nrSolutions();
}
