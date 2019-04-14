/*
 * @author Madalina Drugan: 2010-2018
 */

package GeneticPLS.MSPerturbatorRates;

import general.Genetic.PerturbatorStrategies;

public class MultipleRandomMut implements PerturbatorStrategies{
    private PerturbatorStrategies[] mutation;

    public int valCurrent;
    private final static java.util.Random r = new java.util.Random();


    public MultipleRandomMut(PerturbatorStrategies[] mut){
        mutation = mut;
    }
    
    @Override
    public double getPerturbator(){
        return mutation[valCurrent].getPerturbator();
    }

    // chose with equal probability all the operators
    // randomize also the position
    @Override
    public void chosePerturbator(){
        r.setSeed(r.nextLong());
        double rTemp = r.nextDouble();

       int sizeT = mutation.length;
       valCurrent = (int)Math.round(rTemp*sizeT);
       if(valCurrent == sizeT) {
            valCurrent--;
        }

    }

    @Override
    public void setPerturbator(Object mut){
        int l = ((PerturbatorStrategies[])mut).length;
        for(int i = 0; i < l; i++){
            mutation[i] = ((PerturbatorStrategies[])mut)[i];
        }
    }

    @Override
    public void setPerturbatorType(int typeP){
        mutation[valCurrent].setPerturbatorType(typeP);
        //return;
    }

    @Override
    public int getPerturbatorType(){
        return mutation[valCurrent].getPerturbatorType();
    }

    @Override
    public boolean adaptation(){
        return false;
    }

    @Override
    public double getAdjustment(){
        return mutation[valCurrent].getAdjustment();
    }
}
