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
public class StopingCriteria_PLS implements Stopping{
    private Variator var;

    public StopingCriteria_PLS(){
    }

    public StopingCriteria_PLS(Variator var){
        this.var = var;
    }

    public void setVariator(Variator var){
        this.var = var;
    }

    public boolean stopCriteria(){
        if(var.getNumberOfPLS() < var.getMaximPLSToOutput()) {
            return true;
        }
        return false;

    }

    public long writeCriteria(){
        return var.getNumberOfPLS();
    }
}
