/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Scalarization;

import general.*;
import general.Genetic.PerturbatorStrategies;
import java.util.Stack;
/**
 *
 * @author Madalina Drugan, Utrecht University
 */
public interface PerturbatorScalarization {
    public Scalarizer makeScalarizer(Solution[] selectSol, Scalarizer[] parents, PerturbatorStrategies strat);

    public int[] chooseScalarizers(Scalarizer[] parents, int sizeScal);
    public int[] chooseSolutions(Solution[] parents, int sizeScal);
    
    public int getParent();

    public int getNrParents();
    public int getNrSolutions();

   public Scalarizer makeRandomScalarizer(Scalarizer[] parents);  
   public void choosePerturbator();
}
