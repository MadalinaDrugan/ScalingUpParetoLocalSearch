package Scalarization.Scalarizers;

import MOInstance_NK.Persistence_Solution_NK;
import general.Scalarization.Scalarizer;
import java.util.*;
import MOInstance_QAPs.Persistence_Solution_QAPs;


public final class Weighted_Lp implements Scalarizer{

    private Object[] idealPoint;
    private Object[] referencePoint;

    private double[] weights;

    private Object[] offset;
    private int power;
    
    private static int typeOpt = 0;

    private int nrObjectives;
    private final static java.util.Random r = new java.util.Random();
    private final long identificationNumber = r.nextLong();

    public Weighted_Lp(double[] w, Long[] ideal, Long[] off, int p){
	weights = w;
	idealPoint = new Long[ideal.length];
        idealPoint = Arrays.copyOf(ideal, ideal.length);
        nrObjectives = weights.length;
        offset = new Long[off.length];
        offset = Arrays.copyOf(off, off.length);
        power = p;

        typeOpt = 0;
    }

    public Weighted_Lp(double[] w, Double[] ideal, Double[] off, int p){
	weights = w;
	idealPoint = new Double[ideal.length];
        idealPoint = Arrays.copyOf(ideal, ideal.length);
        nrObjectives = weights.length;
        offset = new Double[off.length];
        offset = Arrays.copyOf(off, off.length);
        power = p;
        
        typeOpt = 1;
    }

    public Weighted_Lp(double[] w, Long[] off, int p){
	weights = w;
        nrObjectives = weights.length;
        offset = new Long[off.length];
        offset = Arrays.copyOf(off, off.length);
        power = p;
        
        typeOpt = 0;
   }

    public Weighted_Lp(double[] w, Double[] off, int p){
	weights = w;
        nrObjectives = weights.length;
        offset = new Double[off.length];
        offset = Arrays.copyOf(off, off.length);
        power = p;
        
        typeOpt = 1;
   }

    private double temp;
    @Override public double scalarize(Long[] objectives) {
        if(idealPoint == null){
            Persistence_Solution_QAPs.setIdealPoint(referencePoint,objectives, offset);
        }
        temp = 0;
	for(int i=0;i<objectives.length;i++){
            if(idealPoint == null){
		temp += Math.pow(objectives[i]-(Long)referencePoint[i], getPower())*getWeights()[i];
            } else {
                temp += Math.pow(objectives[i]-(Long)idealPoint[i], getPower())*getWeights()[i];
            }
	}
        temp = Math.pow(temp, 1.0/getPower());
	return temp;
    }

    @Override public double scalarize(Integer[] objectives) {
        if(idealPoint == null){
            Persistence_Solution_QAPs.setIdealPoint(referencePoint,objectives, offset);
        }
        temp = 0;
	for(int i=0;i<objectives.length;i++){
            if(idealPoint == null){
		temp += Math.pow(objectives[i]-(Long)referencePoint[i], getPower())*getWeights()[i];
            } else {
                temp += Math.pow(objectives[i]-(Long)idealPoint[i], getPower())*getWeights()[i];
            }
	}
        temp = Math.pow(temp, 1.0/getPower());
	return temp;
    }
    
    @Override public double scalarize(Double[] objectives) {
        if(idealPoint == null){
            Persistence_Solution_NK.setIdealPoint(referencePoint,objectives, offset);
        }
        temp = 0;
	for(int i=0;i<objectives.length;i++){
            if(idealPoint == null){
		temp += Math.pow(objectives[i]-(Double)referencePoint[i], getPower())*getWeights()[i];
            } else {
                temp += Math.pow(objectives[i]-(Double)idealPoint[i], getPower())*getWeights()[i];
            }
	}
        temp = Math.pow(temp, 1.0/getPower());
	return temp;
    }

    @Override public void setWeights(double[] w){
            this.setNrObjectives(w.length);
            weights = Arrays.copyOf(w, getNrObjectives());
            //referencePoint = Persistence_Solution_QAPs.resetIdealPoint(nrObjectives);
    }

    @Override public double[] getWeights(){
            double[] tempT = Arrays.copyOf(weights, getNrObjectives());
            return tempT;
    }

    @Override public void setWeights(){
            setWeights(new double[this.getNrObjectives()]);
            temp = 0;
            for(int i = 0; i < getNrObjectives(); i++){
                getWeights()[i] = r.nextGaussian();
                temp += getWeights()[i];
            }
            for(int i = 0; i < getNrObjectives(); i++){
                getWeights()[i] /= temp;
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
            for(int i=0;i<getReferencePoint().length;i++) {
                sb.append(getReferencePoint()[i]+" ");
            }
        }
	sb.append("\n");
	sb.append("Weights:");
	for(int i=0;i<getWeights().length;i++) {
            sb.append(getWeights()[i]+" ");
        }
	sb.append("\n");
	return sb.toString();
    }

    ///////////////////////////
    //////////////////////////////
    // scalarizer
    @Override public int compareTo(Scalarizer o){
        if(o == this) return 0;
        double[] scal = o.getWeights();
        for(int i = 0; i < scal.length; i++) {
            if(getWeights()[i] != scal[i]) {
                return 1;
            }
        }
        return 0;
    }

    @Override public int compare(Scalarizer o, Scalarizer o1){
        if(o == o1) return 0;
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
        Weighted_Lp newS;
        if(idealPoint == null){
            if(typeOpt == 0){
                newS = new Weighted_Lp(weights, (Long[])offset, getPower());
            } else { //if(typeOpt == 1){
                newS = new Weighted_Lp(weights, (Double[])offset, getPower());
            }
        } else {
            if(typeOpt == 0){
                newS = new Weighted_Lp(weights,(Long[])idealPoint, (Long[])offset, getPower());
            } else { //if(typeOpt == 1){
                newS = new Weighted_Lp(weights,(Double[])idealPoint, (Double[])offset, getPower());
            }
        }
        //newS.nrObjectives = nrObjectives;
        return newS;
    }

    @Override
    public boolean equals(Object o){
          if(this==o){
              return true;
          }
          for(int i=0;i<getWeights().length;i++){
              if (getWeights()[i] != ((Weighted_Lp) o).getWeights()[i]){
                  return false;
              }
          }

          return true;
	}

    /**
     * @return the referencePoint
     */
    public Object[] getReferencePoint() {
        return referencePoint;
    }

    /**
     * @param ref
     * @param referencePoint the referencePoint to set
     */
    public void setReferencePoint(Object[] ref) {
        referencePoint = ref;
    }

    /**
     * @param weights the weights to set
     */
    //public void setWeights(double[] w) {
    //    weights = w;
    //}

    /**
     * @return the offset
     */
    public Object[] getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(Object[] off) {
        offset = off;
    }

    /**
     * @return the power
     */
    public int getPower() {
        return power;
    }

    /**
     * @param power the power to set
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * @return the nrObjectives
     */
    public int getNrObjectives() {
        return nrObjectives;
    }

    /**
     * @param nrObjectives the nrObjectives to set
     */
    public void setNrObjectives(int nrObjectives) {
        this.nrObjectives = nrObjectives;
    }

}
