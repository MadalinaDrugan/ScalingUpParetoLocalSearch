/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PLS.LocalSearch.Neighbourhood;

import PLS.LocalSearch.Counter;
import general.*;
import general.Genetic.PerturbatorStrategies;
import general.Reduction.Reduction;
import general.Reduction.SetReductions;
import general.Scalarization.*;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author madalina
 */
public class Neigh_first implements Neighbours{
   
   private Variator var;
   private ProblemInstance problem;
   private PerturbatorStrategies epsilon;
   private SetReductions redSet;

   IteratorSolutions iterator;

   long expandSol =0;

   public Neigh_first(){
   }
   
   private final static long serialVersionUID = 11239585071342354L;//10795; //
   private final static java.util.Random r = new java.util.Random();

   public Stack<Solution> ndaRemove = new Stack<>();

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

   // @Override
   // public void setVariator(ProblemInstance p, IteratorSolutions[] iter, PerturbatorStrategies epsilon2) {
    //   problem = p;
    //   
    //   iterator = iter;
    //   
    //   epsilon = epsilon2;
    //}

    @Override public ArchiveSolutions neighbours(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter, Scalarizer scal){

        iter.iteratorInit(ps, var, problem);
        expandSol = 0;
        ndaRemove.clear();
        while(iter.hasNext()){
            if(var.getNumberOfSwaps() > var.getMaxNumberOfSwaps()){
                break;
            }
            Solution s1 = iter.next();
            if(nda.contains(s1) || nda.dominates(s1,scal)) {
                continue;
            }

            if(s1.dominates(nda,scal)){
                Stack<Solution> tempSol = nda.getDominated(s1);
                while(!tempSol.isEmpty()){
                    ndaRemove.push(tempSol.pop());
                }
            }          

            //best first
            // WRONG
            expandSol++;
            if(s1.dominates(ps,scal)){
                //nda.reset();
                nda.add(s1,scal);
                ps.setVisited();
                return nda;
            } else if(s1.incomparable(nda,epsilon)) {
                nda.add(s1,scal);
            }
        }
        ps.setVisited();
        return nda;
    }

    double[][] tempWeights;
    @Override public ArchiveSolutions neighboursReduction(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter, int index){

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
            
            if(nda.containsSolInRed(s1,index) || nda.dominatesReduction(s1,index)) {
                continue;
            }
            
            if(s1.dominates(nda)){
                Stack<Solution> tempSol = nda.getDominated(s1);
                while(!tempSol.isEmpty()){
                    ndaRemove.push(tempSol.pop());
                }
            }          

            //best first
            // WRONG
            expandSol++;
            if(s1.dominatesReduction(ps,tempWeights)){
                //nda.reset();
                nda.addSolInRed(s1,index);
                ps.setVisited();
                return nda;
            } else if(s1.incomparableReduction(nda,tempWeights)) {
                nda.addSolInRed(s1,index);
            }
        }
        ps.setVisited();
        return nda;
    }

   @Override public ArchiveSolutions neighbours(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter){

        iter.iteratorInit(ps, var, problem);
        expandSol = 0;
        ndaRemove.clear();
        while(iter.hasNext()){
            if(var.getNumberOfSwaps() > var.getMaxNumberOfSwaps()){
                break;
            }
            Solution s1 = iter.next();
            if(nda.contains(s1) || nda.dominates(s1)) {
                continue;
            }
            
            if(s1.dominates(nda)){
                Stack<Solution> tempSol = nda.getDominated(s1);
                while(!tempSol.isEmpty()){
                    ndaRemove.push(tempSol.pop());
                }
            }          

            //best first
            // WRONG
            expandSol++;
            if(s1.dominates(ps)){
                //nda.reset();
                nda.add(s1);
                ps.setVisited();
                return nda;
            } else if(s1.incomparable(nda,epsilon))
                nda.add(s1);
        }
        ps.setVisited();
        return nda;
    }

   @Override public ArchiveSolutions neighbours(Solution ps, ArchiveSolutions nda, Variator var, IteratorSolutions iter, boolean usedMemory, HashMap<Long,Counter> tableSolVisited){

        if(!usedMemory) {
           return neighbours(ps, nda, var, iter);
        }
        expandSol = 0;
        ndaRemove.clear();
        iter.iteratorInit(ps, var, problem);
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
            
            if(nda.contains(s1) || nda.dominates(s1)) {
               continue;
            }
            
            if(s1.dominates(nda)){
                Stack<Solution> tempSol = nda.getDominated(s1);
                while(!tempSol.isEmpty()){
                    ndaRemove.push(tempSol.pop());
                }
            }          

            //best first
            if(s1.dominates(ps)){
                //nda.reset();
                nda.add(s1);
                ps.setVisited();
                return nda;
            } else if(s1.incomparable(nda,epsilon)) {
               nda.add(s1);
           }
        }
        ps.setVisited();
        return nda;
    }

   @Override public ArchiveSolutions neighbours(Solution ps, ArchiveSolutions nda, IteratorSolutions iter, boolean usedMemory, HashMap<Long,Counter> tableSolVisited){

       expandSol = 0;
       iter.iteratorInit(ps, problem);
       ndaRemove.clear();
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

            //best first
            if(s1.dominates(ps)){
                //nda.reset();
                nda.add(s1);
                ps.setVisited();
                return nda;
            } else if(s1.incomparable(nda, epsilon)) {
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
