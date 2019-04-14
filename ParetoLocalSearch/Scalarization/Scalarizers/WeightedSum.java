/*
 * Scalarization class: summed adaptation
 * contain functions that depend of the analysed problem
 *
 */
package Scalarization.Scalarizers;

import general.Scalarization.*;
import java.util.*;

public class WeightedSum implements Scalarizer{

    double[] weights;

    //Object[] referencePoint;
    //long[] NadirPoint;

    int nrObjectives;
    final static java.util.Random r = new java.util.Random();
    final long identificationNumber = r.nextLong();
    
    public WeightedSum(double[] w){
	weights = w;
        nrObjectives = weights.length;
        //if(typeProb == 1)
        //    referencePoint = Persistence_Solution_QAPs.resetIdealPoint(nrObjectives);
        //else if(typeProb == 2)
        //    referencePoint = Persistence_Solution_NK.resetIdealPoint(nrObjectives);
    }
	
    public WeightedSum(){
    }

    @Override
    public double scalarize(Long[] objectives) {
	double retVal=0;
	for(int i=0;i<objectives.length;i++) {
            retVal+=weights[i]*objectives[i];
        }
		return retVal;
    }

    @Override
    public double scalarize(Double[] objectives) {
	double retVal=0;
	for(int i=0;i<objectives.length;i++) {
            retVal+=weights[i]*objectives[i];
        }
		return retVal;
    }

    @Override
    public double scalarize(Integer[] objectives) {
	double retVal=0;
	for(int i=0;i<objectives.length;i++) {
            retVal+=weights[i]*objectives[i];
        }
		return retVal;
    }

    @Override
    public void setWeights(double[] w){
            this.nrObjectives = w.length;
            this.weights = Arrays.copyOf(w, nrObjectives);

            //referencePoint = Persistence_Solution_QAPs.resetIdealPoint(weights);
    }

    public double[] getWeights(){
            double[] temp = Arrays.copyOf(weights, nrObjectives);
            return temp;
    }

    public void setWeights(){
        double temp = 0;
        for(int i = 0; i < this.weights.length; i++){
                weights[i] = r.nextGaussian();
                temp += weights[i];
        }
        for(int i = 0; i < weights.length; i++) {
                weights[i] /= temp;
        }
    }

    //public void choseScalarization(){}
    //
    ///////////////////////////
    //////////////////////////////
    // scalarizer
    @Override public int compareTo(Scalarizer o){
        if(o == this) {
            return 0;
        }
        double[] scal = o.getWeights();
        for(int i = 0; i < scal.length; i++){
            if(weights[i] != scal[i]){
                return 1;
            }
        }
        return 0;
    }

    @Override public int compare(Scalarizer o, Scalarizer o1){
        if(o == o1) return 0;
        double[] scal = o.getWeights();
        double[] scal1 = o1.getWeights();
        for(int i = 0; i < scal.length; i++)
            if(scal[i] != scal1[i])
                return 1;
        return 0;
    }

    @Override public int hashCode(){
        return (int) this.identificationNumber;
    }

    @Override public Scalarizer clone(){
        WeightedSum newS = new WeightedSum();
        newS.weights = Arrays.copyOf(weights, this.weights.length);
        //newS.referencePoint = Arrays.copyOf(referencePoint, this.referencePoint.length);
        newS.nrObjectives = nrObjectives;
        return newS;
    }

    @Override public boolean equals(Object o){
          if (this==o) return true;
          for(int i=0;i<weights.length;i++){
              if (weights[i] != ((WeightedSum)o).weights[i])
                  return false;
          }

          return true;
	}

}
