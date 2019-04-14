/*
 * @author Madalina Drugan: 2010-2018
 */

package GeneticPLS.MSPerturbatorRates;

import general.Genetic.PerturbatorStrategies;

public class MultipleRandomMutRecomb implements PerturbatorStrategies{
    private PerturbatorStrategies[] mutation;
    private PerturbatorStrategies[] recomb;

    int typeOperator;
    public int valCurrent;
    private final static java.util.Random r = new java.util.Random();

    public MultipleRandomMutRecomb(PerturbatorStrategies[] mut, PerturbatorStrategies[] rec){
        recomb = rec;
        mutation = mut;
    }
    
    @Override
    public double getPerturbator(){
        if(this.typeOperator == 0) {
            return mutation[valCurrent].getPerturbator();
        }
        return recomb[valCurrent].getPerturbator();
    }

    @Override
    public void chosePerturbator(){
        r.setSeed(r.nextLong());
        double rTemp = r.nextDouble();
        if(rTemp < 0.5){
            typeOperator = 0;
        } else {
            typeOperator = 1;
        }
        int sizeT = mutation.length;
        rTemp = r.nextDouble();
        valCurrent = (int)Math.round(rTemp*sizeT);
        if(valCurrent == sizeT) {
            valCurrent--;
        }

    }

    @Override
    public void setPerturbator(Object mut){
        int l = ((PerturbatorStrategies[])mut).length;
        for(int i = 0; i < l; i++){
            if(i < l/2) {
                mutation[i] = ((PerturbatorStrategies[])mut)[i];
            }
            else {
                recomb[i] =  ((PerturbatorStrategies[])mut)[i-l/2];
            }
        }
    }

    @Override
    public void setPerturbatorType(int typeP){
        typeOperator = typeP;
        return;
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
        if(typeOperator == 0) {
            return mutation[valCurrent].getAdjustment();
        }
        return recomb[valCurrent].getAdjustment();
    }
}
