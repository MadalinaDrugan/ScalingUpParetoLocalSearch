/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scalarization.Sets.Operators;

import Scalarization.Sets.Operators.MonoScalarizer.RandomScalarizer;
import general.Genetic.PerturbatorStrategies;
import general.Scalarization.PerturbatorScalarization;
import general.Scalarization.ScalOperators;
import general.Scalarization.Scalarizer;
import general.Solution;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author madalina
 */
public class ScalarizersGenerator implements PerturbatorScalarization{

  static private Random r = new Random();
  private ScalOperators scalO;
  
  private int[] posPar;
  private int nrParents;
  
  public ScalarizersGenerator(ScalOperators s){
      scalO = s;
      nrParents = s.nrParents();
  }
  
   @Override
   public int getNrParents(){
        return nrParents;
    }
    
    @Override
    public int getNrSolutions(){
        return 1;
    }
    
    
    @Override
    public Scalarizer makeScalarizer(Solution[] selectSol, Scalarizer[] parents, PerturbatorStrategies strat){    
      if(parents == null){
          return null;
      }
      if(selectSol != null){
          posPar = new int[1]; //chooseSolutions(selectSol,nrParents);
          posPar[0] = 0;
          double[] weights = scalO.makeWeights(selectSol,null, strat);
          parents[0].setWeights(weights);  
          return parents[posPar[0]];
      } //else {
       
      posPar = chooseScalarizers(parents,nrParents);
      Scalarizer[] tempP = new Scalarizer[posPar.length];
      //if(selectSol != null){
      //    tempP[0] = parents[0];
      //} else {
      for(int i = 0; i < posPar.length; i++){
          tempP[i] = parents[posPar[i]];
      }
      //}
      double[] weights = scalO.makeWeights(selectSol,tempP, strat);
      parents[posPar[0]].setWeights(weights);  
       return parents[posPar[0]];
   }
        
    @Override
    public int getParent(){
       return posPar[0];
   }
    
    Stack<Integer> index = new Stack<Integer>(); 
    @Override
    public int[] chooseScalarizers(Scalarizer[] parents, int sizeScal){
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

    RandomScalarizer rand = new RandomScalarizer();
    @Override
    public Scalarizer makeRandomScalarizer(Scalarizer[] parents) {
       Scalarizer temp = parents[0].clone();
       temp.setWeights(rand.makeWeights(null, parents, null));
       return temp;
    }

    @Override
    public void choosePerturbator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
