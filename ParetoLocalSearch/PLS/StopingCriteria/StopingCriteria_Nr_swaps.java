/*
 * Stoping criteria: number of PLSs
 * input: variator
 */

package PLS.StopingCriteria;

import general.*;
/**
 *
 * @author madalina
 */
public class StopingCriteria_Nr_swaps implements Stopping{
    private Variator var;

    public StopingCriteria_Nr_swaps(){
    }

    public StopingCriteria_Nr_swaps(Variator var){
        this.var = var;
    }

    public void setVariator(Variator var){
        this.var = var;
    }

    public boolean stopCriteria(){
        if(var.getNumberOfSwaps() <= var.getMaxNumberOfSwaps())
            return true;
        return false;

    }

    public long writeCriteria(){
        return var.getNumberOfSwaps();
    }

}
