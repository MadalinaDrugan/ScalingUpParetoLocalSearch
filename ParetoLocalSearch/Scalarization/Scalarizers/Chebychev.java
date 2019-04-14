package Scalarization.Scalarizers;

import MOInstance_NK.Persistence_Solution_NK;
import MOInstance_QAPs.Persistence_Solution_QAPs;
import general.Scalarization.Scalarizer;
import java.util.*;


public final class Chebychev implements Scalarizer {

    private Object[] idealPoint;
    private Object[] referencePoint;

    private double[] weights;

    private Object[] offset;

    private int nrObjectives;
    final static java.util.Random r = new java.util.Random();
    final long identificationNumber = r.nextLong();

    public Chebychev(double[] w, Object[] ideal, Object[] off){
	weights = w;
	idealPoint = new Object[ideal.length];
        idealPoint = Arrays.copyOf(ideal, ideal.length);
        nrObjectives = weights.length;
        offset = new Object[off.length];
        offset = Arrays.copyOf(off, off.length);
        //referencePoint = Persistence_Solution_QAPs.resetIdealPoint(nrObjectives);
    }

    public Chebychev(double[] w, Object[] off){
	weights = w;
        nrObjectives = weights.length;
	//idealPoint = Persistence_Solution_QAPs.resetIdealPoint(nrObjectives);
        referencePoint = new Object[off.length];//Persistence_Solution_QAPs.resetIdealPoint(nrObjectives);
        offset = new Object[off.length];
        offset = Arrays.copyOf(off, off.length);
   }

    private double temp;
    @Override public double scalarize(Long[] objectives) {
	double highest = 0;
        if(idealPoint == null){
            Persistence_Solution_QAPs.setIdealPoint(referencePoint,objectives,offset);
        }
	for(int i=0;i<objectives.length;i++){
            if(idealPoint == null){
		temp = (objectives[i]-(Long)referencePoint[i])*weights[i];
            } else {
                temp = (objectives[i]-(Long)idealPoint[i])*weights[i];
            }
            if(temp>highest) {
                highest=temp;
            }
	}
	return highest;
    }

    @Override public double scalarize(Integer[] objectives) {
	double highest = 0;
        if(idealPoint == null){
            Persistence_Solution_QAPs.setIdealPoint(referencePoint,objectives,offset);
        }
	for(int i=0;i<objectives.length;i++){
            if(idealPoint == null){
		temp = (objectives[i]-(Long)referencePoint[i])*weights[i];
            } else {
                temp = (objectives[i]-(Long)idealPoint[i])*weights[i];
            }
            if(temp>highest) {
                highest=temp;
            }
	}
	return highest;
    }

    @Override public double scalarize(Double[] objectives) {
	double highest = 0;
        if(idealPoint == null){
            Persistence_Solution_NK.setIdealPoint(referencePoint,objectives,offset);
        }
	for(int i=0;i<objectives.length;i++){
            if(idealPoint == null){
		temp = (objectives[i]-(Long)referencePoint[i])*weights[i];
            } else {
                temp = (objectives[i]-(Long)idealPoint[i])*weights[i];
            }
            if(temp>highest) {
                highest=temp;
            }
	}
	return highest;
    }

    @Override public void setWeights(double[] w){
            this.nrObjectives = w.length;
            this.weights = Arrays.copyOf(w, nrObjectives);
            //referencePoint = Persistence_Solution_QAPs.resetIdealPoint(nrObjectives);
    }

    @Override public double[] getWeights(){
            double[] tempT = Arrays.copyOf(weights, nrObjectives);
            return tempT;
    }

    @Override public void setWeights(){
         weights = new double[this.nrObjectives];
         temp = 0;
         for(int i = 0; i < nrObjectives; i++){
             weights[i] = r.nextGaussian();
             temp += weights[i];
         }
         for(int i = 0; i < nrObjectives; i++) {
                weights[i] /= temp;
        }
    }

    //public void choseScalarization(){}
    //
    @Override public String toString(){
	StringBuilder sb = new StringBuilder();
	sb.append("Ideal Point:");
        if(idealPoint != null){
            for(int i=0;i<idealPoint.length;i++) {
                sb.append(idealPoint[i]+" ");
            }
        } else {
            for(int i=0;i<referencePoint.length;i++) {
                sb.append(referencePoint[i]+" ");
            }
        }
        sb.append("\n");
        sb.append("Weights:");
        for(int i=0;i<weights.length;i++) {
            sb.append(weights[i]+" ");
        }
	sb.append("\n");
	return sb.toString();
    }

    ///////////////////////////
    //////////////////////////////
    // scalarizer
    @Override public int compareTo(Scalarizer o){
        if(o == this) {
            return 0;
        }
        double[] scal = o.getWeights();
        for(int i = 0; i < scal.length; i++) {
            if(weights[i] != scal[i]) {
                return 1;
            }
        }
        return 0;
    }

    @Override public int compare(Scalarizer o, Scalarizer o1){
        if(o == o1) {
            return 0;
        }
        double[] scal = o.getWeights();
        double[] scal1 = o1.getWeights();
        for(int i = 0; i < scal.length; i++) {
            if(scal[i] != scal1[i]) {
                return 1;
            }
        }
        return 0;
    }

    @Override public int hashCode(){
        return (int) this.identificationNumber;
    }

    @Override public Scalarizer clone(){
        Chebychev newS;
        if(idealPoint == null) {
            newS = new Chebychev(weights, offset);
        }
        else {
            newS = new Chebychev(weights, idealPoint,offset);
        }
        //newS.nrObjectives = nrObjectives;
        return newS;
    }

    @Override public boolean equals(Object o){
        if (this==o) {
            return true;
        }
        for(int i=0;i<weights.length;i++){
            if (weights[i] != ((Chebychev)o).weights[i]) {
                return false;
            }
          }

          return true;
	}

}
