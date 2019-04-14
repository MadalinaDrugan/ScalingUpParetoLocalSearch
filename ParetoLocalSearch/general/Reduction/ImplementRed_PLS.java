/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.Reduction;

import general.Scalarization.*;
import general.*;
//import localoptimization.NonDominatedArchive;


/**
 *
 * @author madalina
 */
public interface ImplementRed_PLS{
     
    public ArchiveSolutions localSearchReduction(ArchiveSolutions tempA, Variator var, int index);
    //public void setVariator(Neighbours neigh, IteratorSolutions iter, SetScalarizeres scal);
    
    //public void setReduction(Reduction s);
}
