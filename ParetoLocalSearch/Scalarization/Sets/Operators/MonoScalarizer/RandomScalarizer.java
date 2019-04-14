/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scalarization.Sets.Operators.MonoScalarizer;

import general.Scalarization.*;
import general.Solution;
import general.Genetic.*;
        
/**
 *
 * @author Madalina Drugan, Utrecht University
 *
 */
public class RandomScalarizer  implements ScalOperators{
    
    private final java.util.Random r = new java.util.Random();
 
    @Override
    public double[] makeWeights(Solution[] selectSol, Scalarizer[] children, PerturbatorStrategies strat){
        double[] weights = children[0].getWeights();
        double sumW = 0;
        for(int j = 0; j < weights.length; j++){
               weights[j] = r.nextDouble();
               sumW += weights[j];
        }        
        for(int j = 0; j < weights.length; j++){
                weights[j] /= sumW;
        }

        return weights;
    }

    @Override
    public int nrParents() {
        return 1;
    }

   @Override
    public int nrSolutions() {
       return 0;
    }

    @Override
    public double[] makeWeights(Solution[] selectSol, Scalarizer[] parents, int[] objectives, PerturbatorStrategies strat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
