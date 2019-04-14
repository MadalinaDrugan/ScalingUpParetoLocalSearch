/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reduction;

import general.Scalarization.Scalarizer;
import java.util.Hashtable;
import java.util.Stack;

/**
 *
 * @author madalina
 */
public class ReduceScalarizer implements Scalarizer{
    
    private final Scalarizer scal;
    
    private final boolean[] mask;
    private final double[] maskedWeights;
    
    //private final double[] weights;
    private final Stack<Integer> st = new Stack<>();
    private final Hashtable<Integer,Integer> revSt = new Hashtable<>();

     final static java.util.Random r = new java.util.Random();
   
    public ReduceScalarizer(Scalarizer s){
        scal = s;
        double[] weights = scal.getWeights();
        mask = new boolean[weights.length];
        int countNZ = 0;
        for(int i = 0; i < mask.length; i++){
            if(weights[i] > 0.0001){
                mask[i] = true;
                countNZ++;
                st.add(i);
            } else {
                mask[i] = false;
            }
        }
        
        maskedWeights = new double[countNZ];
        for(int i =0; i < st.size(); i++){
            maskedWeights[i] = weights[st.get(i)];
            revSt.put(i,st.get(i));
        }
    }
    
    @Override
    public double scalarize(Long[] objectives){
        return scal.scalarize(objectives);
    }

    @Override
    public double scalarize(Double[] objectives){
        return scal.scalarize(objectives);
    }

    @Override
    public double scalarize(Integer[] objectives){
        return scal.scalarize(objectives);
    }

    @Override
    public void setWeights(double[] w){
        for(int i = 0; i < w.length; i++){
            maskedWeights[i] = w[revSt.get(i)];
            //weights[revSt.get(i)] = w[i];
        }
        scal.setWeights(w);
    }
 
    
    @Override
    public double[] getWeights(){
        return scal.getWeights();
   }

    @Override
    public void setWeights(){
        double[] weights = new double[maskedWeights.length];
        for(int i = 0; i < st.size(); i++){
            maskedWeights[i] = r.nextDouble();
            weights[revSt.get(i)] = maskedWeights[i];
        }
        scal.setWeights(weights);
    }

    @Override
    public Scalarizer clone(){
        ReduceScalarizer temp = new ReduceScalarizer(scal);
        return temp;
    }

    @Override
    public int compareTo(Scalarizer o) {
       if(o == this) {
            return 0;
        }
        double[] scal1 = o.getWeights();
        double[] weights = scal.getWeights();
        for(int i = 0; i < scal1.length; i++) {
            if(weights[i] != scal1[i]) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int compare(Scalarizer o, Scalarizer o1) {
         if(o == o1) {
            return 0;
        }
        double[] scal2 = o.getWeights();
        double[] scal1 = o1.getWeights();
        for(int i = 0; i < scal2.length; i++) {
            if(scal2[i] != scal1[i]) {
                return 1;
            }
        }
        return 0;
   }
}
