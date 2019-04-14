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
public class StopingCriteria_Nr_PLS implements Stopping{
    private Variator var;

    public StopingCriteria_Nr_PLS(){
    }

    public StopingCriteria_Nr_PLS(Variator var){
        this.var = var;
    }

    public void setVariator(Variator var){
        this.var = var;
    }

    public boolean stopCriteria(){
        if(var.getNumberOfPLS() <= var.getInitPLS())
            return true;
        return false;

    }

    public long writeCriteria(){
        return var.getNumberOfPLS();
    }

}
