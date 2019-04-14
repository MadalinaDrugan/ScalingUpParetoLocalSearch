/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PLS.LocalSearch.Neighbourhood;

import PLS.LocalSearch.Counter;
import general.Genetic.PerturbatorStrategies;
import general.*;
import java.util.HashMap;

import general.Scalarization.Scalarizer;
import general.Reduction.Reduction;
import general.Reduction.SetReductions;
import java.util.Stack;

/**
 *
 * @author madalina
 */
public class Neigh_best implements Neighbours{
   
   //private Variator var;
   private ProblemInstance problem;
   private PerturbatorStrategies epsilon;

   private SetReductions redSet;
   
   IteratorSolutions iterator;

   long expandSol =0;

   public Stack<Solution> ndaRemove = new Stack<>();

   public Neigh_best(){}
   
   private final static long serialVersionUID = 11239585071342354L;//10795; //
   //private final static java.util.Random r = new java.util.Random();


   @Override public void setVariator(ProblemInstance p, IteratorSolutions iter, PerturbatorStrategies epsilon2){
       problem = p;
       iterator = iter;
       epsilon = epsilon2;
   }

   private Stack<double[][]> weightV = new Stack<>();
   @Override public void setVariator(ProblemInstance p, IteratorSolutions iter, PerturbatorStrategies epsilon2, SetReductions redS){
       problem = p;
       
       iterator = iter;
       
       epsilon = epsilon2;
       
       redSet = redS;
       Reduction[] tempRed = redSet.toArray();
       //Stack<ReduceScalarizer> tempScal;
       for (Reduction tempRed1 : tempRed) {
            weightV.add(tempRed1.getListWeightVectors());            
       }

   }

   ///////////////////
   //best improvement
   //////////////////
   boolean improvement;
   @Override public ArchiveSolutions neighbours(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter){

        improvement = false;
        iter.iteratorInit(ps, var, problem);
        expandSol = 0;
        ndaRemove.clear();
        while(iter.hasNext()){
            if(var.getNumberOfSwaps() > var.getMaxNumberOfSwaps()){
                break;
            }
            
            Solution s1 = iter.next();
            expandSol++;
            if(nda.contains(s1) || nda.dominates(s1)) {
                continue;
            }
            
            if(s1.dominates(nda)){
                Stack<Solution> tempSol = nda.getDominated(s1);
                while(!tempSol.isEmpty()){
                    ndaRemove.push(tempSol.pop());
                }
            }          

            if(s1.dominates(nda) || s1.incomparable(nda, epsilon)) {
                improvement = true;
                nda.add(s1);
            }
        }
        
        ps.setVisited();
        return nda;
    }

   @Override public ArchiveSolutions neighbours(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter, Scalarizer scal){

        improvement = false;
        iter.iteratorInit(ps, var, problem);
        expandSol = 0;
        ndaRemove.clear();
        while(iter.hasNext()){
            if(var.getNumberOfSwaps() > var.getMaxNumberOfSwaps()){
                break;
            }

            Solution s1 = iter.next();
            expandSol++;
            if(nda.contains(s1) || nda.dominates(s1,scal)) {
                continue;
            }
            
            if(s1.dominates(nda,scal)){
                Stack<Solution> tempSol = nda.getDominated(s1);
                while(!tempSol.isEmpty()){
                    ndaRemove.push(tempSol.pop());
                }
            }          

            if(s1.dominates(nda,scal) || s1.incomparable(nda, epsilon)) {
                improvement = true;
                nda.add(s1,scal);
                
            }
        }
        
        ps.setVisited();
        return nda;
    }

   double[][] tempWeights;
   @Override public ArchiveSolutions neighboursReduction(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter, int index){

        improvement = false;
        iter.iteratorInit(ps, var, problem);
        expandSol = 0;
        ndaRemove.clear();
        tempWeights = weightV.get(index);
        while(iter.hasNext()){
            if(var.getNumberOfSwaps() > var.getMaxNumberOfSwaps()){
                break;
            }

            Solution s1 = iter.next();
            
            //s1.setSetReduction(redSet);
            expandSol++;
            if(nda.containsSolInRed(s1,index) || nda.dominatesReduction(s1,index)) {
                continue;
            }
            
            if(s1.dominatesReduction(nda,tempWeights)){
                Stack<Solution> tempSol = nda.getDominated(s1);
                while(!tempSol.isEmpty()){
                    ndaRemove.push(tempSol.pop());
                }
            }
            
            if(s1.dominatesReduction(nda,tempWeights) || s1.incomparableReduction(nda, tempWeights)) {
                improvement = true;
                nda.addSolInRed(s1,index);
            }
        }
        
        ps.setVisited();
        return nda;
    }

   @Override public ArchiveSolutions neighbours(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter, boolean usedMemory, HashMap<Long,Counter> tableSolVisited){

        if(!usedMemory) {
            return neighbours(ps, nda, var, iter);
        }

        ndaRemove.clear();
        iter.iteratorInit(ps, var, problem);
        expandSol = 0;
        while(iter.hasNext()){
            if(var.getNumberOfSwaps() > var.getMaxNumberOfSwaps()){
                break;
            }
            
            Solution s1 = iter.next();
            Long temp = s1.getIdentifNumber();
            
            if(tableSolVisited.containsKey(temp)){
                 tableSolVisited.get(temp).index++;
            }else{
                 Counter temps = new Counter();
                 temps.index=1;
                 tableSolVisited.put(temp, temps);
            }
            expandSol++;

            if(nda.contains(s1)  || nda.dominates(s1)) {
                continue;
            }

            if(s1.dominates(nda)){
                Stack<Solution> tempSol = nda.getDominated(s1);
                while(!tempSol.isEmpty()){
                    ndaRemove.push(tempSol.pop());
                }
            }
            
            if(s1.dominates(nda) || s1.incomparable(nda, epsilon)) {
                nda.add(s1);
            }
        }
        ps.setVisited();
        return nda;
    }

   @Override public ArchiveSolutions neighbours(Solution ps, ArchiveSolutions nda, IteratorSolutions iter, boolean usedMemory, HashMap<Long,Counter> tableSolVisited){

        iter.iteratorInit(ps, problem);
        ndaRemove.clear();
        expandSol = 0;
        while(iter.hasNext()){
            Solution s1 = iter.next();

            if(usedMemory){
                Long temp = s1.getIdentifNumber();

                if(tableSolVisited.containsKey(temp)){
                     tableSolVisited.get(temp).index++;
                }else {
                 Counter temps = new Counter();
                 temps.index=1;
                 tableSolVisited.put(temp, temps);
                }
            }
            expandSol++;
            if(nda.contains(s1) || nda.dominates(s1)) {
                continue;
            }

            if(s1.dominates(nda)){
                Stack<Solution> tempSol = nda.getDominated(s1);
                while(!tempSol.isEmpty()){
                    ndaRemove.push(tempSol.pop());
                }
            }
            
            if(s1.dominates(nda) || s1.incomparable(nda, epsilon)) {
                nda.add(s1);
            }
        }
        ps.setVisited();
        return nda;
    }

    @Override
    public long getExpandSol(){
        return expandSol;
    }

   @Override
    public Stack<Solution> getNDARemoved(){
        return ndaRemove;
    }
}
