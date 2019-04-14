/*
@author Madalina Drugan: 2010-2018 
* Path-guided mutation that respects the cycles
 * 
 */

package GeneticPLS.Exploration.Permutation;

import general.Genetic.*;
import java.util.*;
import general.*;

public class Iter_path_QAP extends Path_QAP{
   
   private ProblemInstance problem;
   private PerturbatorStrategies strat;
   
   private Solution s1;
   
   private GeneticOperators iter;
   private int typeOperator = 1; //recomb
   private Solution[] results = new Solution[2];

   int mut_rate;
   
   public Iter_path_QAP(Iter_QAP it){
       iter = it;
   }
   public Iter_path_QAP(){
   }
      
   private final static long serialVersionUID = 11239585071342354L;//10795; //
   private final static java.util.Random r = new java.util.Random();

   @Override public void setVariator(ProblemInstance p, PerturbatorStrategies stat){
       problem = p;
       cycles = new int[problem.numberOfFacilities];
       tempItem1 = new int[problem.numberOfFacilities];
       tempItem2 = new int[problem.numberOfFacilities];

       strat = stat;
   }


   ///////////////////////
   // construct the sequence of swaps
   //////////////////////
   public void sequenceSwaps(int[] tempItem1, int[] tempItem2, Vector<int[]> cyclePosition, int distTotal, Vector<int[]> sequence, int mut_rate){
        boolean b;
        int[] cycles2 = new int[cyclePosition.size()];
        int nrSwaps = 0;
        Arrays.fill(cycles2,-1);
        sequence.clear();
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
           
           //tempR.clear();
           for(int i = 0; i < dist-1; i++){
               indexI = tempItem1[indexSwap[rInt]];
               //find indexI in temp2
               indexII = -1;
               for(int k = 0; k < indexSwap.length; k++)
                    if(tempItem2[indexSwap[k]] == indexI){
                          indexII = indexSwap[k];
                          break;
                    }
               
              int[] tempInt = new int[2];
              tempInt[0] = indexII;
              tempInt[1] = indexSwap[rInt];

              sequence.add(tempInt);

              //swaps the two positions
              int temp = tempItem1[indexSwap[rInt]];
              tempItem1[indexSwap[rInt]]=tempItem1[indexII];
              tempItem1[indexII]=temp;

              nrSwaps++;
              if(nrSwaps >= mut_rate-1)
                  break;

           }
           if(nrSwaps >= mut_rate-1)
                  break;
        }
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


   //cycle recombination
   @Override public Solution[] perturbator(Solution[] ps1, ProblemInstance p){
        if(this.typeOperator == 0 || ps1.length == 1){
            typeOperator = 0;
            return iter.perturbator(ps1, p);
        }
       results = null;
        if(ps1[0].equals(ps1[1]))
            return null;

        //r.setSeed(r.nextLong());
        tempItem1 = new int[ps1[0].items.length];
        tempItem2 = new int[ps1[1].items.length];
        for(int i = 0; i < tempItem1.length; i++){
            tempItem1[i] = (Integer)ps1[0].items[i];
            tempItem2[i] = (Integer)ps1[1].items[i];
        }
        
        //System.arraycopy(ps1[0].items, 0, tempItem1, 0, ps1[0].items.length);
        //System.arraycopy(ps1[1].items, 0, tempItem2, 0, ps1[1].items.length);
        //test the difference between the two strings
        cyclePosition.clear();
        samePositions.clear();

        //swap in this cycles only; with a given probability
        Arrays.fill(cycles,-1);

        ///////////////////////////////
        //find the cycles in the two
        mut_rate = (int)strat.getPerturbator();
        distTotal = buildCycles( tempItem1, tempItem2, cycles, cyclePosition);
        //if(distTotal < 4)
        //    return null;

        ///////////////
        // return null in these cases: the distance is not big enough
        if(cyclePosition.size() < 1 || tempItem1.length - samePositions.size() <= mut_rate+1 || distTotal <= mut_rate+1)
            return null;

        //see the distance
        if(cyclePosition.size() ==1 && cyclePosition.get(0).length < mut_rate)
            return null;

        ////////////////////////
        // choose the positions and swap
        this.sequenceSwaps(tempItem1, tempItem2, cyclePosition, distTotal, sequence,mut_rate);

        // fizicaly make the swaps
        /*for(int i = 0; i < sequence.size(); i++){
            //find indexI in temp2
            int[] tempInt = sequence.get(i);

            int temp = tempItem1[tempInt[0]];
            tempItem1[tempInt[0]] = tempItem1[tempInt[1]];
            tempItem1[tempInt[1]] = temp;
        }*/
        
        //return at random one of them
        s1 = ps1[0].clone();
        s1.flagVisited = false;
        s1.items = new Integer[tempItem1.length];
        for(int i = 0; i < tempItem1.length; i++){
            s1.items[i] = tempItem1[i];
        }      
        //System.arraycopy(tempItem1, 0, s1.items, 0, tempItem1.length);
        //problem.computeSolution(s1);
        int dist = s1.getDistance(ps1[0]);
        
        //var.updateNrSwaps(dist);

        results = new Solution[1];
        results[0] = s1;

        return results;
    }

   @Override public Solution[] getParents(ArchiveSolutions nda, SelectMatingChildren set){
        if(nda == null || nda.size() == 0)
            return null;
        Solution[] temp;
        if(nda.size() == 1 && this.getNrParents() == 1){
            //temp = new Solution[1];
            //temp[0] = nda.getNthSolution(0);
            //this.setPerturbatorType(0);
            //strat.setPerturbatorType(0);
            //return temp;
            return null;
        }

        //((MaximumDistanceMating)set).setStrat(strat);
        //((MaximumDistanceMating)set).setNDA(nda);
        //set.select(1, null);
        mut_rate = (int)strat.getPerturbator();
        Vector<Solution[]> tempNDA = nda.getDistanceSol(mut_rate);
        if(tempNDA == null || tempNDA.isEmpty()){
            //temp = new Solution[1];
            //temp[0] = nda.getNthSolution(0);
            //setPerturbatorType(0);
            //strat.setPerturbatorType(0);
            //return temp;
            temp = new Solution[1];
            this.setPerturbatorType(0);
            strat.setPerturbatorType(0);
            int i = r.nextInt(nda.size());
            temp[0] = nda.getNthSolution(i);
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
