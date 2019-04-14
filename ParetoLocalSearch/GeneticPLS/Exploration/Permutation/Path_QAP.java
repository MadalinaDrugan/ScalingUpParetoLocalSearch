/*
 *@author Madalina Drugan: 2010-2018
 Path relinking*/

package GeneticPLS.Exploration.Permutation;
import general.Genetic.PerturbatorStrategies;
import general.Genetic.GeneticOperators;
import java.util.*;
import general.*;

public class Path_QAP extends Recomb_QAP{
   
   private ProblemInstance problem;
   
   private Solution s1;

   Stack<Integer> samePositions = new Stack<>();

   private final static long serialVersionUID = 11239585071342354L;//10795; //
   private final static java.util.Random r = new java.util.Random();


   //always include some mutation
   private GeneticOperators iter;
   //PerturbatorStrategies statIter;
   private int typeOperator = 1; //recomb
   private Solution[] results = new Solution[2];

   // sequences of positions to swap
   Stack<int[]> sequence = new Stack<>();
   int distTotal;

   private int stepPath = 1;
   public Path_QAP(Iter_QAP it, int step){ //, PerturbatorStrategies statI){
       iter = it;
       stepPath = step;
   }

   public Path_QAP(){   }
      
   private PerturbatorStrategies strat;

   @Override public void setVariator(ProblemInstance p, PerturbatorStrategies stat){
       problem = p;
       cycles = new int[problem.numberOfFacilities];
       tempItem1 = new int[problem.numberOfFacilities];
       tempItem2 = new int[problem.numberOfFacilities];

       //s1.items = new int[problem.numberOfFacilities];
       //s1.objectives = new long[problem.numberOfObjectives];

       strat = stat;
   }

   ///////////////////////
   // construct the sequence of swaps
   //////////////////////
   public void sequenceSwaps(int[] tempItem1, int[] tempItem2, Vector<int[]> cyclePosition, int distTotal, Vector<int[]> sequence){
        // choose positions to swap
        boolean b;
        int nrSwaps = 0;
        //if(side == 0){
        int[] cycles2 = new int[cyclePosition.size()];
        Arrays.fill(cycles2,-1);
        while(true){
           int j = r.nextInt(cyclePosition.size());

           b = false;
           for(int i = 0; i < cycles2.length; i++)
               if(cycles2[i] == -1){
                   b = true;
                   break;
                }
           if(!b){
                break;
           }
           while(cycles2[j] != -1)
                    j = r.nextInt(cyclePosition.size());
           cycles2[j] = 1;

           int[] indexSwap = cyclePosition.elementAt(j).clone();
           int dist = indexSwap.length; //+1;//ps1.getDistance(tempDist1,tempDist2);

           //
           int rInt = r.nextInt(indexSwap.length);
           int indexI, indexII;
           indexI = tempItem1[indexSwap[rInt]];
           //tempR.clear();
           for(int i = 0; i < dist; i++){
               //find indexI in temp2
               indexII = -1;
               for(int k = 0; k < indexSwap.length; k++)
                    if(tempItem2[indexSwap[k]] == indexI){
                          indexII = indexSwap[k];
                          break;
                    }
              //
              if(indexII == -1){
                            System.err.println("Error in chosing an individual \n");
              }

              int[] tempInt = new int[2];
              tempInt[0] = indexII;
              tempInt[1] = indexSwap[rInt];
              sequence.add(tempInt);

              nrSwaps++;
              if(nrSwaps < 2 || nrSwaps % stepPath == 0)
                  continue;
              if(nrSwaps >  - 2)
                  break;

           }
        }
   }

   //private boolean side;

    //cycle recombination
   @Override public Solution[] perturbator(Solution[] ps1, Variator var){
        results = this.perturbator(ps1, var.getProblem());
        if(results != null){
            int dist = results[results.length-1].getDistance(ps1[0]);
            var.updateNrSwaps(dist);
        }
        return results;
    }

   Stack<Solution> tempR = new Stack<>();
   //cycle recombination
   @Override public Solution[] perturbator(Solution[] ps1, ProblemInstance p){
        if(this.typeOperator == 0 || ps1.length < 2){
            typeOperator = 1;
            return iter.perturbator(ps1, p);
        }
        results = null;
        if(ps1[0].equals(ps1[1]))
            return null;

        r.setSeed(r.nextLong());
        System.arraycopy(ps1[0].items, 0, tempItem1, 0, ps1[0].items.length);
        System.arraycopy(ps1[0].items, 0, tempItem2, 0, ps1[0].items.length);
        //test the difference between the two strings
        cyclePosition.clear();
        //samePositions.clear();

        //swap in this cycles only; with a given probability
        distTotal = ps1[0].getDistance(ps1[1]);
        if(distTotal < 4)
            return null;
        
        Arrays.fill(cycles,-1);

        //find the cycles in the two
        buildCycles(tempItem1, tempItem2, cycles, cyclePosition);

        if(cyclePosition.size() < 1)
            return null;

        // choose positions to swap
        this.sequenceSwaps(tempItem1, tempItem2, cyclePosition, distTotal,sequence);
        
        // build the
        tempR.clear();
        for(int i = 0; i < sequence.size(); i++){
            //find indexI in temp2
            int[] tempInt = sequence.get(i);

            int temp = tempItem1[tempInt[0]];
            tempItem1[tempInt[0]] = tempItem1[tempInt[1]];
            tempItem1[tempInt[1]] = temp;

              //generate a new individual
              s1 = ps1[0].clone();
              s1.flagVisited = true;
              System.arraycopy(tempItem1, 0, s1.items, 0, tempItem1.length);
              p.computeSolution(s1);
              //var.updateNrSwaps(1);

              tempR.add(s1);
           }

        results = new Solution[tempR.size()];
        for(int i = 0; i < results.length; i++)
              results[i] = tempR.get(i);

        typeOperator = 1;
        return results;
    }
      
    @Override public PerturbatorStrategies getPerturbator(){
       return strat;
   }

}
