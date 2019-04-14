/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

import PLS.LocalSearch.Counter;
import general.Reduction.ImplementRed_PLS;
import general.Scalarization.ImplementScal_PLS;
import java.util.HashMap;
//import localoptimization.NonDominatedArchive;


/**
 *
 * @author madalina
 */
public interface ImplementPLS  extends ImplementScal_PLS, ImplementRed_PLS{
     
    public ArchiveSolutions localSearch(ArchiveSolutions tempA, Variator var);
    public ArchiveSolutions localSearch(ArchiveSolutions tempA);
    // public ArchiveSolutions localSearch(ArchiveSolutions tempA, Variator var, Reduction scal);
    //public void setVariator(Neighbours neigh, IteratorSolutions iter, SetScalarizeres scal);
    
    //public void setReduction(Reduction s);
    
    //public ArchiveScalSolutions localSearch(ArchiveScalSolutions tempA, Variator var, Scalarizer scal);

    public void setVariator(Neighbours neigh, IteratorSolutions iter);
    //public void setVariator(Neighbours neigh, IteratorSolutions[] iter);
    
    public void reset();
    public HashMap<Long,Counter> getVisited();
    public HashMap<Long,Counter> getExplored();
    public HashMap<Long,Object[]> getSolutions();
    public int getLevel();

    public int sizeNDA();
    public double getSizeNDA();
    public double getVarSizeNDA();
}
