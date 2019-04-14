/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reduction.Sets.Operators.MonoReduction;

import Reduction.ReduceScalarizer;
import Scalarization.Scalarizers.WeightedSum;
import Scalarization.Sets.Operators.MonoScalarizer.*;
import general.Scalarization.*;
import general.Solution;
import general.Genetic.*;
import general.Reduction.Reduction;
import general.Reduction.ReductionOperators;
import java.util.Stack;
        
/**
 *
 * @author Madalina Drugan, Utrecht University
 *
 */
public class RandomReduction  implements ReductionOperators{
    
    private final java.util.Random r = new java.util.Random();
 
    Stack<ReduceScalarizer> tempScal = new Stack();
    RandomScalarizer rand = new RandomScalarizer();
    @Override
    public Stack<ReduceScalarizer> makeScalarizations(Solution[] selectSol, Reduction[] children, PerturbatorStrategies strat){
        tempScal.clear();// = children[0].getReductions();
        //double sumW = 0;
        for(int j = 0; j < children.length; j++){
            double[] weights = children[0].getScalarizer(0).getWeights();
            Scalarizer[] newScal = new WeightedSum[1];
            newScal[0] = new WeightedSum(weights);
            ReduceScalarizer tempS = new ReduceScalarizer(new WeightedSum(rand.makeWeights(selectSol, newScal, strat)));
            tempScal.add(j,tempS);
        }        

        return tempScal;
    }

    @Override
    public int nrParents() {
        return 1;
    }

   @Override
    public int nrSolutions() {
       return 0;
    }

}
