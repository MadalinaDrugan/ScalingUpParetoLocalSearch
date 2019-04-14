/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Neighbourhoods.Iterator_Solution;

import MOInstance_QAPs.QAPs;
import MOInstance_QAPs.Solution_QAPs;
import general.*;
import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author madalina
 */
public class Iterator_Solutions_QAPs implements IteratorSolutions{
   
   private Variator var;
   private QAPs problem;
   //private long epsilon;
   private int sizeN;
   private int nrItems;

   //int[][] listRandomPairs; fixed lenght
   int[][] listRandomPairs;
   //Vector<Integer> randomNumbers;
   Solution_QAPs currentSolution;

   //private final static long serialVersionUID = 11239585071342354L;//10795; //
   //private final static java.util.Random r = new java.util.Random();

   private int[] indexes;
   private int nrNeigh;
   public Iterator_Solutions_QAPs(int sizeNeigh, int nrI){
       sizeN = sizeNeigh;
       nrNeigh = (int)combinations(nrItems, sizeN);
       
       listRandomPairs = new int[nrNeigh][sizeN];
       
       int position = 0;
       indexes = new int[sizeN];
       for(int i = 0; i < sizeN; i++){
           indexes[i] = i;
       }
       int currentIndex = 0; 
       while(position < nrNeigh){
           
           currentIndex = sizeN-1;
           while(true){
               if(indexes[currentIndex] < nrItems - (sizeN - currentIndex)){
                   indexes[currentIndex]++;
                   if(currentIndex == sizeN-1){
                       break;
                   } else {
                       // iterate all with one
                       for(int j = currentIndex+1; j < sizeN; j++){
                           indexes[j] = indexes[currentIndex] + j - currentIndex;
                       }
                       break;
                   }
               } else {
                   // go back with one and repeat the iteration
                   if(currentIndex == 0){
                       break;
                   } else 
                       currentIndex--;
               }
           }
           System.arraycopy(indexes, 0, listRandomPairs[position], 0, sizeN);
           position++;
       }
       
   }
   
   public Iterator_Solutions_QAPs(int sizeNeigh){
       sizeN = sizeNeigh;
    }
   public int getNeigh(){
       return sizeN;
   }
   
   @Override public void iteratorInit(Solution ps, Variator var, ProblemInstance problem){
       this.var = var;
       this.iteratorInit(ps, problem);
       nrItems = ((Solution_QAPs) ps).items.length;
       
       if(listRandomPairs == null){
           listRandomPairs = new int[(int)combinations(nrItems, sizeN)][sizeN];

            int position = 0;
            indexes = new int[sizeN];
            for(int i = 0; i < sizeN; i++){
                indexes[i] = i;
            }
            int currentIndex = 0; 
            while(position < listRandomPairs.length){
           
                currentIndex = sizeN-1;
                while(true){
                    if(indexes[currentIndex] < nrItems - (sizeN - currentIndex)){
                        indexes[currentIndex]++;
                        if(currentIndex == sizeN-1){
                            break;
                        } else {
                            // iterate all with one
                            for(int j = currentIndex+1; j < sizeN; j++){
                                indexes[j] = indexes[currentIndex] + j - currentIndex;
                            }
                            break;
                        }
                    } else {
                        // go back with one and repeat the iteration
                        if(currentIndex == 0){
                            break;
                        } else 
                            currentIndex--;
                    }
                }
                System.arraycopy(indexes, 0, listRandomPairs[position], 0, sizeN);
                position++;
            }
       }
   }

   //////////////////
   // too slow; update on the flight the neighbourhood
   /////////////////
   @Override public void iteratorInit(Solution ps, ProblemInstance problem){

       this.problem = (QAPs)problem;

       currentState = 0;
       currentSolution = (Solution_QAPs)ps;
       itemT = new Integer[ps.items.length];
       nrItems = itemT.length;
        
       if(listRandomPairs == null){
            listRandomPairs = new int[(int)combinations(nrItems, sizeN)][sizeN];

            int position = 0;
            indexes = new int[sizeN];
            for(int i = 0; i < sizeN; i++){
                indexes[i] = i;
            }
            System.arraycopy(indexes, 0, listRandomPairs[position], 0, sizeN);
            position++;
            
            int currentIndex = 0; 
            while(position < listRandomPairs.length){
           
                currentIndex = sizeN-1;
                while(true){
                    if(indexes[currentIndex] < nrItems - (sizeN - currentIndex)){
                        indexes[currentIndex]++;
                        if(currentIndex == sizeN-1){
                            break;
                        } else {
                            // iterate all with one
                            for(int j = currentIndex+1; j < sizeN; j++){
                                indexes[j] = indexes[currentIndex] + j - currentIndex;
                            }
                            break;
                        }
                    } else {
                        // go back with one and repeat the iteration
                        if(currentIndex == 0){
                            break;
                        } else 
                            currentIndex--;
                    }
                }
                System.arraycopy(indexes, 0, listRandomPairs[position], 0, sizeN);
                position++;
            }
       }

   }
   

   static long combinations(int n, int k) {
	long coeff = 1;
	for (int i = n - k + 1; i <= n; i++) {
			coeff *= i;
	}
	for (int i = 1; i <= k; i++) {
			coeff /= i;
	}
	return coeff;
   }
 
   // public static void main(String[] args){
   //     System.out.println(combinations(5, 3));
   // }
   int currentState;
   int leftSwap;
   int rightSwap;
   @Override
   public boolean hasNext(){
       ////////////////////
       if(currentState < listRandomPairs.length){
           return true;
       }
       return false;
   }

   Stack<Integer> tempStack = new Stack();
   Integer[] itemT;
   @Override
   public Solution next(){
       //////////////////
       // Version 2. 
       // adapted to the new version
       // compute a new permutation
       //////////////////
       Solution s1 = currentSolution.clone();
       /////////////
       // compute the tempStack
       tempStack.clear();
       for(int i = 0; i < listRandomPairs[0].length; i++){
           tempStack.add(listRandomPairs[currentState][i]);
       }
       
       //currentState = listRandomPairs[0].size()-1;
       itemT = Arrays.copyOf((Integer[])currentSolution.items, currentSolution.items.length);
       for(int i = 0; i < tempStack.size()-1; i++){
           leftSwap = tempStack.get(i);
           rightSwap = tempStack.get(i+1);
           
           int middleS = itemT[leftSwap];
           itemT[leftSwap] = itemT[rightSwap];
           itemT[rightSwap] = middleS;
       }
       
       ////////////////
       //leftSwap = listRandomPairs[currentState][0];
       //rightSwap = listRandomPairs[currentState][1];

       Long[] tempObj = new Long[currentSolution.objectives.length];
       tempObj = (Long[]) problem.computeSolution(itemT);
       s1.objectives = Arrays.copyOf(tempObj, tempObj.length);
       s1.items = Arrays.copyOf(itemT, itemT.length);

       if(var != null) {
           var.updateNrSwaps(tempStack.size());
       }

       currentState++;

       return s1;
   }

    @Override
    public boolean adaptation() {
        return false;
    }

    @Override
    public void restart() {
          currentState = 0;   
    }
}
