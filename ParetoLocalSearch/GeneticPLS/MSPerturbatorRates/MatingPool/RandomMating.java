/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneticPLS.MSPerturbatorRates.MatingPool;

import general.Genetic.SelectMatingChildren;
import java.util.Random;
/**
 *
 * @author Madalina Drugan, Utrecht University
 *
 */
public class RandomMating implements SelectMatingChildren{
    
    private final Random r = new Random();
    
    @Override
    public int[] select(int sizePool, Object[] population){
          int[] indexP = new int[sizePool];
          int indexC = -1;
          for(int i = 0; i < population.length; i++){
              while(indexC < sizePool-1){
                int rand = ((int) Math.floor(r.nextDouble() * population.length));
                boolean flag = false;
                for(int j = 0; j < indexC+1; j++){
                      if(indexP[j] == rand){
                          flag = true;
                          break;
                      }
                  }
                if(!flag){
                    indexC++;
                    indexP[indexC] = rand;
                    break;
                }
              }
          }
          return indexP;
      }
}
