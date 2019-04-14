/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reduction.Sets.Operators;

import Reduction.ReduceScalarizer;
import Reduction.Sets.Operators.MonoReduction.RandomReduction;
import general.Genetic.PerturbatorStrategies;
import general.Reduction.PerturbatorReduction;
import general.Reduction.Reduction;
import general.Scalarization.ScalOperators;
import general.Solution;
import java.util.Random;
import java.util.Stack;
        
/**
 *
 * @author Madalina Drugan, Utrecht University
 *
 */
public class ReductionGenerator implements PerturbatorReduction{
    
    static private Random r = new Random();
    
    private int sizeRed; // how many scalarizers will be modified

    private ScalOperators[] scalO; // the scalarizer
    //private int[] posScal;
     //private int nrParents;
    
    //int[][] indexes; //
    public ReductionGenerator(){}
    
    public ReductionGenerator(ScalOperators[] s, int sizeR){
        scalO = s;
        //indexes = ind.indexes(nrObj); 
        //if(sizeR <= indexes.length ){
            sizeRed = sizeR;
        //} else sizeRed = indexes.length;
    }
    
    @Override
    public int getNrParents(){
        return sizeRed;
    }
    
    @Override
    public int getNrSolutions(){
        return 1;
    }
    
    Stack<ReduceScalarizer> scalInRed = new Stack<>();
    @Override
    public Reduction makeReduction(Solution[] selectSol, Reduction[] parents, PerturbatorStrategies strat){
       if(parents == null){
          return null;
      }
      //if(selectSol != null){
      //    posScal = chooseSolutions(selectSol,sizeRed);
      //} else {
      //    posScal = chooseReduction(parents,sizeRed);
      //}
      scalInRed = parents[0].getReductions();
      ReduceScalarizer[] tempP = new ReduceScalarizer[scalInRed.size()];
      for(int i = 0; i < tempP.length; i++){
        
          tempP[i] = scalInRed.get(i);
          
          double[] w = scalO[i].makeWeights(selectSol,tempP, strat);  
          tempP[i].setWeights(w);
          
          parents[0].setScalarizer(tempP[i], i);
      }

      return parents[0];
    }

        
    
    Stack<Integer> index = new Stack<>(); 
    @Override
    public int[] chooseReduction(Reduction[] parents, int sizeScal){
        if(parents.length < sizeScal){
            int[] tempT = new int[parents.length];
            for(int i = 0; i < parents.length; i++){
                tempT[i] = i;
            }
            return tempT;
        }

        //int sizeScal  = parents.length;
        int mainP = r.nextInt(parents.length);
        index.clear();
        index.add(mainP);
        while(index.size() < sizeScal){
            mainP = r.nextInt(parents.length);
            while(index.contains(mainP)){
                mainP = r.nextInt(parents.length);
            }
            index.add(mainP);
        }
        
        int[] tempT = new int[sizeScal];
        for(int i = 0; i < sizeScal; i++){
            tempT[i] = index.get(i);
        }
        return tempT;
    }

    @Override
    public int[] chooseSolutions(Solution[] parents, int sizeScal){
        if(parents.length < sizeScal){
            int[] tempT = new int[parents.length];
            for(int i = 0; i < parents.length; i++){
                tempT[i] = i;
            }
            return tempT;
        }

        //int sizeScal  = parents.length;
        int mainP = r.nextInt(parents.length);
        index.clear();
        index.add(mainP);
        while(index.size() < sizeScal){
            mainP = r.nextInt(parents.length);
            while(index.contains(mainP)){
                mainP = r.nextInt(parents.length);
            }
            index.add(mainP);
        }
        
        int[] tempT = new int[sizeScal];
        for(int i = 0; i < sizeScal; i++){
            tempT[i] = index.get(i);
        }
        return tempT;
    }

    @Override
    public int getParent() {
        return 0; //posScal[0];
    }

    RandomReduction rand = new RandomReduction();
    @Override
    public Reduction makeRandomReduction(Reduction[] parents) {
       Reduction temp = parents[0].clone();
       temp.setReductions(rand.makeScalarizations(null, parents, null));
       return temp;
    }

    @Override
    public void choosePerturbator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
