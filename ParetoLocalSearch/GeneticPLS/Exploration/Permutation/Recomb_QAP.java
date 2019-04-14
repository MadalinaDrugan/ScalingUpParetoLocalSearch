/*
 * @author Madalina Drugan: 2010-2018
 */

package GeneticPLS.Exploration.Permutation;
import general.Genetic.*;
import java.util.Stack;
import java.util.Iterator;
import java.util.Arrays;
import general.*;

public class Recomb_QAP implements GeneticOperators{
   
   private Variator var;
   private ProblemInstance problem;
   
   //vectors for the components of the recombination
   int[] tempItem1; // = new Vector<Integer>();
   int[] tempItem2; // = new Vector<Integer>();

   int[] cycles;
   Stack<int[]> cyclePosition = new Stack<>();
   Stack<Integer> tempArray = new Stack<>();

   private Solution[] results = new Solution[2];
   private PerturbatorStrategies strat;

   private Solution s1;

   //always include some mutation
   private GeneticOperators iter;
   //PerturbatorStrategies statIter;
   private int typeOperator = 1; //recomb

   private boolean side = false;

   public Recomb_QAP(){
   }

   public Recomb_QAP(Iter_QAP it){ //, PerturbatorStrategies statI){
       iter = it;
       //statIter = statI;
   }
      
   private final static long serialVersionUID = 11239585071342354L;//10795; //
   private final static java.util.Random r = new java.util.Random();

   @Override public void setVariator(ProblemInstance p, PerturbatorStrategies stat){
       problem = p;
       cycles = new int[problem.numberOfFacilities];
       tempItem1 = new int[problem.numberOfFacilities];
       tempItem2 = new int[problem.numberOfFacilities];

       s1.items = new Integer[problem.numberOfFacilities];
       s1.objectives = new Long[problem.numberOfObjectives];

       strat = stat;
   }

   @Override
   public boolean adaptation(){
        return strat.adaptation();
    }

   @Override
    public void restart(){
        
    }

   @Override
    public int getNrParents(){
       return 2;
   }

   @Override
   public PerturbatorStrategies getPerturbator(){
       return strat;
   }

   @Override
   public Solution[] getParents(ArchiveSolutions nda, SelectMatingChildren set){
        if(nda == null || nda.size() == 0) {
            return null;
        }
        Solution[] temp;
        if(nda.size() == 1){
            temp = new Solution[1];
            temp[0] = nda.getNthSolution(0);
            setPerturbatorType(0);
            return temp;
        }
        temp = new Solution[2];

        Solution[] population = nda.ndaToArray();
        int[] indexP = set.select(2, population); 
        temp[0] = nda.getNthSolution(indexP[0]);
        temp[1] = nda.getNthSolution(indexP[1]);

        return temp;
    }

   //////////////////////////
   // build the cycles
   /////////////////////
   public int buildCycles(int[] tempItem1, int[] tempItem2, int[] cycles, Stack<int[]> cyclePosition){
        int dist = 0;
        for(int j = 0; j < tempItem1.length;j++)
           if(cycles[j] == -1){
                    int tempJ = j;
                    cycles[j] = tempJ;
                    if(tempItem1[tempJ] == tempItem2[tempJ]){
                        //samePositions.add(tempJ);
                        continue;
                    }
                    tempArray.clear();
                    tempArray.add(tempJ);
                    while(true){
                        boolean flagContinue = false;
                        for(int k = j+1; k< tempItem1.length; k++)
                            if(tempJ != k & tempItem1[k] == tempItem2[tempJ]){
                                cycles[k] = k;
                                tempJ = k;
                                tempArray.add(tempJ);
                                flagContinue = true;
                                break;
                            }
                        if(!flagContinue | tempItem1[j] == tempItem2[tempJ])
                            break;
                    }
                    //find
                    int[] tempArray1 = new int[tempArray.size()];
                    int i = 0;
                    Iterator<Integer> thisIter = tempArray.iterator();
                    while(thisIter.hasNext()){
                        tempArray1[i++] = thisIter.next();
                    }
                    Arrays.sort(tempArray1);
                    cyclePosition.add(tempArray1);
                    dist += tempArray1.length - 1;
            }
        return dist;
    }

   
    //cycle recombination
    @Override
    public Solution[] perturbator(Solution[] ps1, Variator var){
        results = this.perturbator(ps1, var.getProblem());
        if(results != null){
            if(side == true){
                int dist = results[0].getDistance(ps1[0]);
                var.updateNrSwaps(dist);
            }else{
                int dist = results[0].getDistance(ps1[1]);
                var.updateNrSwaps(dist);
            }
        }
        return results;
    }

    //cycle recombination
   @Override
    public Solution[] perturbator(Solution[] ps1, ProblemInstance p){
        if(this.typeOperator == 0){
            typeOperator = 1;
            return iter.perturbator(ps1, var);
        }
        results = null;
        if(ps1[0].equals(ps1[1])){
            this.typeOperator = 1;
            return null;
        }

        System.arraycopy(ps1[0].items, 0, tempItem1, 0, ps1[0].items.length);
        System.arraycopy(ps1[1].items, 0, tempItem2, 0, ps1[1].items.length);
        //test the difference between the two strings
        cyclePosition.clear();

        Arrays.fill(cycles,-1);

        //make the cycles
        buildCycles( tempItem1, tempItem2, cycles, cyclePosition);

        if(cyclePosition.size() < 1)
                return null;
           //else break;
        //}

        double recombP = strat.getPerturbator();
        //swap in this cycles only; with a given probability
        for(int j = 0; j < cyclePosition.size(); j++){
           //find all the elements from a cycle
           if(r.nextDouble() > recombP)
                 continue;
           //exchange
           int[] indexSwap = cyclePosition.elementAt(j).clone();
           for(int k =0; k < indexSwap.length; k++){
              //make a swap in the current cycle

              int indexK = indexSwap[k];
              //int indexK1 = ((Integer)indexSwap.elementAt(k+1)).intValue();
              int swapItem = tempItem1[indexK];
              tempItem1[indexK] = tempItem2[indexK];
              tempItem2[indexK] = swapItem;

             // System.out.println(tempItem2.elementAt(numberFacilities-1).toString()+ ") ");
            }
            //var.updateNrSwaps(indexSwap.length);

        }

        results = new Solution[1];
        side = r.nextBoolean();
        //return at random one of them
        if(side == true){
            s1 = (Solution)ps1[0].clone();
            s1.flagVisited = true;
            System.arraycopy(tempItem1, 0, s1.items, 0, tempItem1.length);
            //for(int j1 = 0; j1 < var.getNumberFacilities(); j1++)
            //    ((Solution)s1).items[j1] = tempItem1.elementAt(j1).intValue();
            //for(int j1 = 0; j1 < var.getNumberObjectives(); j1++)
            //    ((Solution)s1).objectives[j1] = ((Solution) ps1).objectives[j1];
            problem.computeSolution(s1);
            //int dist = s1.getDistance(ps1[0]);
            //var.updateNrSwaps(dist);
            results[0] = s1;
        }else{
            s1 = ps1[1].clone();
            s1.flagVisited = true;
            System.arraycopy(tempItem2, 0, s1.items, 0, tempItem2.length);
            //for(int j1 = 0; j1 < var.getNumberFacilities(); j1++)
            //    ((Solution)s1).items[j1] = ((Integer)tempItem2.elementAt(j1)).intValue();
            s1 = var.getProblem().computeSolution(s1);
            //int dist = s1.getDistance(ps1[1]);
            //var.updateNrSwaps(dist);
            results[0] = s1;
        }
        typeOperator = 1;
        return results;
    }

   @Override
    public void setPerturbatorType(int operatorType){
        this.typeOperator = operatorType;
    }
}
