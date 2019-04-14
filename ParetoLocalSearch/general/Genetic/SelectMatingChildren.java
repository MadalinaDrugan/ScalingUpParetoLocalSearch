/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general.Genetic;

/**
 *
 * @author Madalina Drugan, Utrecht University
 */
public interface SelectMatingChildren {
    public int[] select(int sizePool, Object[] population);
}
