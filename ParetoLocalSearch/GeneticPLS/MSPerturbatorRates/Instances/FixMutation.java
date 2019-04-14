/*
 * @author Madalina Drugan: 2010-2018
 */

package GeneticPLS.MSPerturbatorRates.Instances;

import general.Genetic.PerturbatorStrategies;
public class FixMutation implements PerturbatorStrategies{
    private Double mutation;

    private int typeOperator;

    public FixMutation(double mut){
        mutation = mut;
    }

    @Override
    public double getPerturbator(){
        return mutation+3;
    }

    @Override
    public void setPerturbator(Object mut){
        mutation = (Double)mut;
    }

    @Override
    public void setPerturbatorType(int typeP){
        typeOperator = typeP;
    }

    @Override
    public int getPerturbatorType(){
        return typeOperator;
    }

    @Override
    public boolean adaptation(){
        return false;
    }

    @Override
    public double getAdjustment(){
        return 3;
    }

    @Override
    public void chosePerturbator(){
    }

}
