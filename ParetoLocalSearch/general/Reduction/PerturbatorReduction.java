/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Reduction;

import general.Genetic.PerturbatorStrategies;
import general.Solution;
/**
 *
 * @author Madalina Drugan, Utrecht University
 */
public interface PerturbatorReduction{
    public Reduction makeReduction(Solution[] selectSol, Reduction[] parents, PerturbatorStrategies strat);
 
    public int[] chooseReduction(Reduction[] parents, int sizeScal);
    public int[] chooseSolutions(Solution[] parents, int sizeScal);
    
    public int getNrParents();
    public int getNrSolutions();

    public int getParent();

   public Reduction makeRandomReduction(Reduction[] parents);  
   public void choosePerturbator();
}
