/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

/**
 *
 * @author madalina
 */
public interface Stopping {

    public void setVariator(Variator var);
    
    public boolean stopCriteria();
    public long writeCriteria();
    //public long getNumber();
}
