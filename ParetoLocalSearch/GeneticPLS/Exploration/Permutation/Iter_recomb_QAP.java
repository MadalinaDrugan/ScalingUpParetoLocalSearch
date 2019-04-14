/*
 * @author Madalina Drugan: 2010-2018
 */

package GeneticPLS.Exploration.Permutation;

import general.Genetic.*;
import java.util.*;
import general.*;

/**
 *
 * @author madalina
 */
public class Iter_recomb_QAP extends Recomb_QAP{
   
   private Variator var;
   private ProblemInstance problem;
   private PerturbatorStrategies strat;
   
   Vector<Integer> samePositions = new Vector<Integer>();

   private Solution s1;
   
   private GeneticOperators iter;
   private int typeOperator = 1; //recomb
   private Solution[] results = new Solution[2];
   public Iter_recomb_QAP(Iter_QAP it){
       iter = it;
   }

   public Iter_recomb_QAP(){
   }
      
   private final static long serialVersionUID = 11239585071342354L;//10795; //
   private final static java.util.Random r = new java.util.Random();

   @Override public void setVariator(ProblemInstance p, PerturbatorStrategies stat){
       problem = p;
       cycles = new int[problem.numberOfFacilities];
       tempItem1 = new int[problem.numberOfFacilities];
       tempItem2 = new int[problem.numberOfFacilities];

       //s1.items = new int[problem.numberOfFacilities];
       //s1.objectives = new long[problem.numberOfObjectives];

       strat = stat;
   }

    //cycle recombination
    @Override public Solution[] perturbator(Solution[] ps1, Variator var){
        results = this.perturbator(ps1, var.getProblem());
        if(results != null){
            int dist = results[0].getDistance(ps1[0]);
            var.updateNrSwaps(dist);
        }
        return results;
    }

   int mut_rate;
   //cycle recombination
   @Override public Solution[] perturbator(Solution[] ps1, ProblemInstance p){
        if(this.typeOperator == 0){
            typeOperator = 1;
            return iter.perturbator(ps1, var);
        }
       results = null;
       if(ps1[0].equals(ps1[1]))
            return null;

        r.setSeed(r.nextLong());
        System.arraycopy(ps1[0].items, 0, tempItem1, 0, ps1[0].items.length);
        System.arraycopy(ps1[1].items, 0, tempItem2, 0, ps1[1].items.length);
        //test the difference between the two strings
        cyclePosition.clear();
        samePositions.clear();
 
        //swap in this cycles only; with a given probability
        mut_rate = (int)strat.getPerturbator();
         int remainDist = mut_rate;
        int distTotal = ps1[0].getDistance(ps1[1]);
        if(distTotal < 4)
            return null;

        Arrays.fill(cycles,-1);

        buildCycles( tempItem1, tempItem2, cycles, cyclePosition);
          
        if(cyclePosition.size() < 1)
            return null;

        ///////////////////
        // pick at random different parts from a individual and mix them
        /////////////////////////////
        boolean b;
        Arrays.fill(cycles,-1);
        while(remainDist > 0){
            int j = r.nextInt(cyclePosition.size());

            int t = 0, q = 0;
            //randomly pick a cycle, it can be that are twice picked ?
            //No if the cycle is shrinked to the
            int[] indexSwap = cyclePosition.get(j).clone();
            if(indexSwap.length >= 2){
                    t = r.nextInt(indexSwap.length);

                    //make the swap until the distance is right
                    b = true;
                    for(int k = 0; k < indexSwap.length; k++)
                        if(tempItem2[indexSwap[k]] == tempItem1[indexSwap[t]]){
                            b = false;
                            q = k;
                            break;
                        }
                    if(b == true){
                        System.err.print("error, no cycle \n");
                    }
            } else System.err.print("error cycle size 1\n");

            // swap the two positions
            int temp = tempItem1[indexSwap[t]];
            tempItem1[indexSwap[t]]=tempItem1[indexSwap[q]];
            tempItem1[indexSwap[q]]=temp;

                    //int[] newIndexSwap = new int[indexSwap.length-1];
                    //for(int k = 0; k < indexSwap.length; k++)
                    //    if(k < t)
                    //        newIndexSwap[k] = indexSwap[k];
                    //    else if(k > t)
                    //     newIndexSwap[k] = indexSwap[k+1];

                    //shrink the cycle; make the cycle
                    //cyclePosition.add(newIndexSwap);

                remainDist--;
            }

        //return at random one of them
        s1 = ps1[0].clone();
        s1.flagVisited = false;
        System.arraycopy(tempItem1, 0, s1.items, 0, tempItem1.length);
        problem.computeSolution(s1);
        int dist = s1.getDistance(ps1[0]);
        int dist2 = s1.getDistance(ps1[1]);
        //if(dist == 1 || dist2 == 1)
        //    return null;
        //var.updateNrSwaps(dist);

        results = new Solution[1];
        results[0] = s1;

        return results;
    }

   @Override public Solution[] getParents(ArchiveSolutions nda, SelectMatingChildren set){
        if(nda == null || nda.size() == 0)
            return null;
        Solution[] temp;
        if(nda.size() == 1){
            temp = new Solution[1];
            temp[0] = nda.getNthSolution(0);
            setPerturbatorType(0);
            return temp;
        }

        mut_rate = (int)strat.getPerturbator();
        Vector<Solution[]> tempNDA = nda.getDistanceSol(mut_rate);
        if(tempNDA == null || tempNDA.size() == 0){
            temp = new Solution[1];
            temp[0] = nda.getNthSolution(0);
            setPerturbatorType(0);
            return temp;
        }

        int i = r.nextInt(tempNDA.size());
        temp = tempNDA.get(i);

        return temp;
    }
     
    @Override public PerturbatorStrategies getPerturbator(){
       return strat;
   }

}
