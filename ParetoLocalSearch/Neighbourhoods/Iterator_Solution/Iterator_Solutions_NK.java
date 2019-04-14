/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Neighbourhoods.Iterator_Solution;

import MOInstance_NK.NK;
import MOInstance_NK.Solution_NK;
import general.*;
import java.util.Arrays;
import java.util.Stack;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author madalina
 */
public class Iterator_Solutions_NK implements IteratorSolutions{
   
   private Variator var;
   private NK problem;
   private int sizeN;
   private int nrItems;

   int[][] listRandomPairs;
   Solution_NK currentSolution;

   private int[] indexes;
   private int nrNeigh;

   public Iterator_Solutions_NK(int sizeNeigh, int nrI){
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
   
   public Iterator_Solutions_NK(int sizeNeigh){
       sizeN = sizeNeigh;
    }
   public int getNeigh(){
       return sizeN;
   }
   
   @Override public void iteratorInit(Solution ps, Variator var, ProblemInstance problem){
       this.var = var;
       this.iteratorInit(ps, problem);
       nrItems = ((Solution_NK) ps).items.length;
       
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
    Integer[] itemT;
    @Override public void iteratorInit(Solution ps, ProblemInstance prob){

       problem = (NK)prob;

       currentState = 0;
       currentSolution = (Solution_NK)ps;
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
 
   @Override
   public boolean hasNext(){
       ////////////////////
       if(currentState < listRandomPairs.length){
           return true;
       }
       return false;
   }

   Stack<Integer> tempStack = new Stack();
   Boolean[] itemB;
   int currentState;
   int leftSwap;
   @Override
   public Solution next(){
       //////////////////
       // Version 2. 
       // adapted to the new version
       // compute a new permutation
       //////////////////
       Solution_NK s1 = currentSolution.clone();
       /////////////
       // compute the tempStack
       tempStack.clear();
       for(int i = 0; i < listRandomPairs[0].length; i++){
           tempStack.add(listRandomPairs[currentState][i]);
       }
       
        //currentState = listRandomPairs[0].size()-1;
        itemB = Arrays.copyOf((Boolean[])currentSolution.items, currentSolution.items.length);
        for (Integer tempStack1 : tempStack) {
            leftSwap = tempStack1;
            itemB[leftSwap] = !itemB[leftSwap];
        }
       
        ////////////////
        //evaluate the new 
        Double[] tempObj = new Double[currentSolution.objectives.length];
        tempObj = (Double[])problem.computeSolution(itemB);
        s1.objectives = Arrays.copyOf(tempObj, tempObj.length);
        s1.items = Arrays.copyOf(itemB, itemB.length);

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
