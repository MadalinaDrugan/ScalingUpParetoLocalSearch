/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Reduction;

import general.Scalarization.*;

/**
 *
 * @author madalina
 */
public interface ReductionIterator {
       
    public Reduction iterator();
    public void restartIterator();
    public boolean endIterator();

}
