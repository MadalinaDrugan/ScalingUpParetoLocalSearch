/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scalarization.Sets.Operators.MonoScalarizer;

import general.Genetic.*;
import general.Scalarization.*;
import general.Solution;
import java.util.Random;
        
/**
 *
 * @author Madalina Drugan, Utrecht University
 *
 */
public class MutationScalarizer   implements ScalOperators{
    
    static private Random r = new Random();
    private int sizeScal;
    
    @Override
    public int nrParents(){
        return 1;
    }

    @Override
    public int nrSolutions() {
       return 0;
    }

    @Override
    public double[] makeWeights(Solution[] selectSol, Scalarizer[] children, PerturbatorStrategies strat){
        double[] weights = children[0].getWeights();
        double sumW = 0;
        double[] deviation = new double[2]; 
        int j = r.nextInt(weights.length);
        while(weights[j] < 0.001){
            j = r.nextInt(weights.length);
        }
        
        //for(int j = 0; j < weights.length; j++){
        deviation[0] = weights[j];
        deviation[1] = strat.getAdjustment();
        strat.setPerturbator(deviation);
        weights[j] = strat.getPerturbator();

        for(j = 0; j < weights.length; j++){
            sumW += weights[j];
        }
        //}        
        for(j = 0; j < weights.length; j++){
                weights[j] /= sumW;
        }

        return weights;
    }

    @Override
    public double[] makeWeights(Solution[] selectSol, Scalarizer[] parents, int[] objectives, PerturbatorStrategies strat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
