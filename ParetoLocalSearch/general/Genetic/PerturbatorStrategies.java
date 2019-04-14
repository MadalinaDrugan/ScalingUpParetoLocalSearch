/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general.Genetic;

/**
 *
 * @author madalina
 */
public interface PerturbatorStrategies {

    public double getPerturbator();
    public void setPerturbator(Object mut);

    public void setPerturbatorType(int typeP);
    public int getPerturbatorType();

    public abstract boolean adaptation();
    public abstract void chosePerturbator();

    public double getAdjustment();
}
