/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Performance;

import general.*;

/**
 *
 * @author madalina
 */
public class ImprovNDA  implements PerformanceInterface{

    @Override public boolean performanceImprovement(ArchiveSolutions p, ArchiveSolutions children, ArchiveSolutions nda){
     if(children.contains(p))
         return false;
     if(children.dominatesAtLeastOne(p))
         return true;
     return false;
    }
    
    @Override
    public boolean performanceImprovement(Solution p, Solution children){
     if(children.dominates(p))
         return true;
     return false;
    }
}
