/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Scalarization;

/**
 *
 * @author madalina
 */
public interface ScalarizerIterator {
       
    public Scalarizer iterator();
    public void restartIterator();
    public boolean endIterator();

}
