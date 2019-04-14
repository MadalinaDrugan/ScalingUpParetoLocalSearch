/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.Scalarization;

import general.*;
//import localoptimization.NonDominatedArchive;


/**
 *
 * @author madalina
 */
public interface ImplementScal_PLS{
     
    public ArchiveSolutions localSearch(ArchiveSolutions tempA, Variator var, Scalarizer scal);
    //public void setVariator(Neighbours neigh, IteratorSolutions iter, SetScalarizeres scal);
    
    //public void setScalarizer(Scalarizer s);
}
