/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reduction.Reductions;

import Reduction.ReduceScalarizer;
import general.Solution;
import general.Reduction.Reduction;
import general.Scalarization.Scalarizer;
import java.util.Stack;
/**
 *
 * @author madalina
 */
public class CollectScalarizers implements Reduction{
    
    private Stack<ReduceScalarizer> scal;
    
    public CollectScalarizers(){
        scal = new Stack<>();
    }
    
    @Override
    public Double[] reduction(Double[] objectives){
        Double[] temp = new Double[scal.size()];
        for(int i = 0; i < scal.size(); i++){
            Scalarizer s1 = scal.get(i);
            temp[i] = s1.scalarize(objectives); 
        }
        return temp;
    }

   @Override
    public Double[] reduction(Solution s){
        Double[] temp = new Double[scal.size()];
        
        for(int i = 0; i < scal.size(); i++){
            Scalarizer s1 = scal.get(i);
            temp[i] = s1.scalarize((Double[])s.objectives); 
        }
        return temp;
    }

    @Override
    public void addReduction(Reduction sc){
        for(int i = 0; i < sc.size(); i++){
            scal.add(sc.getScalarizer(i));
        }
    }
    
    @Override
    public void addScalarizer(ReduceScalarizer sc){
          scal.add(sc);
    }
    
    
    @Override
    public Stack<ReduceScalarizer> getReductions(){
        return scal;
    }
    
    @Override
    public ReduceScalarizer getScalarizer(int indef){
        return scal.get(indef);
    }
    
   @Override
    public void setScalarizer(ReduceScalarizer sc, int indef){
        scal.set(indef, sc);
    }
    
    @Override
    public int size(){
        return scal.size();
    }

    @Override
    public void setReductions(){
        
    }

    @Override
    public Reduction clone(){        
       CollectScalarizers newS;
       newS = new CollectScalarizers();
       return newS;
    }
    
    @Override public int compareTo(Reduction o){
        if(o == this) {
            return 0;
        }
        Stack<ReduceScalarizer> scal1 = o.getReductions();
        if(scal1.size() != scal.size()){
            return 1;
        }
        for(int i = 0; i < scal1.size(); i++){
            if(!scal.elementAt(i).equals(scal1.elementAt(i))) {
                return 1;
            }
        }
        return 0;
    }

    @Override public int compare(Reduction o, Reduction o1){
        if(o == o1) {
            return 0;
        }
        Stack<ReduceScalarizer> scal2 = o.getReductions();
        Stack<ReduceScalarizer> scal1 = o1.getReductions();
        if(scal1.size() != scal2.size()){
            return 1;
        }
        for(int i = 0; i < scal2.size(); i++) {
            if(!scal2.elementAt(i).equals(scal1.elementAt(i))) {
                return 1;
            }
        }
        return 0;
    }

    @Override public boolean equals(Object o){
        if (this==o) {
            return true;
        }
        Stack<ReduceScalarizer> scal1 = ((CollectScalarizers) o).getReductions();
        if(scal1.size() != scal.size()){
            return false;
        }
        for(int i=0;i<scal.size();i++){
            if (!scal.elementAt(i).equals(scal1.elementAt(i))) {
                return false;
            }
          }

          return true;
	}

    @Override
    public void setReductions(Stack<ReduceScalarizer> tempR) {
        scal = tempR;
    }

    double[][] tempWeights;
    @Override
    public double[][] getListWeightVectors() {
        if(scal.empty()){
            return null;
        }
        tempWeights = new double[scal.size()][scal.get(0).getWeights().length];
        for(int i = 0; i < scal.size(); i++){
            tempWeights[i] = scal.get(i).getWeights();
        }
        return tempWeights;
    }
}
