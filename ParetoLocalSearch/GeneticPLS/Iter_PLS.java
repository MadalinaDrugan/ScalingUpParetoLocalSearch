/*
  @author Madalina Drugan: 2010-2018 
* IPLS
 * stoping criteria; perturbators; etc
 *
 */

package GeneticPLS;

import PLS.*;
import general.*;

public class Iter_PLS extends MultiRest_PLS{

    private final static long serialVersionUID = 11239585071342354L;
    
    private Solution[] s2;
    private Solution p;
    private Solution p1;
    
    
    public Iter_PLS(){
    }

    ////////////////////////////////
    // Iterated local search --> main function
    @Override public ArchiveSolutions getNext(ArchiveSolutions nda){
         this.initStopCriteria();
         
         while(stopCriteria()){

            s2 = restartingSolutions(nda);
            if(s2 == null){
                continue;
            }

            p1 = getParents()[0];
            
            for(int i = 0; i < s2.length; i++){
                p = s2[i];
                
                nda = PLS_procedure(p,nda);
            }

            statistics(p1,nda);

           }
         
         statisticsFinal(nda);
         return nda;
    }
    
}
