/*
 *
 */

package general.Neighbourhoods;

//import java.util.Vector;

import Neighbourhoods.Iterator_Solution.Iterator_Solutions_QAPs;
import general.IteratorSolutions;


/**
 *
 * @author madalina
 */
public interface SetNeighbourhoods extends IteratorSolutions{

    //public abstract boolean adaptation(Solution[] s, long[] refPoint);
    public void setNeighbours(Iterator_Solutions_QAPs scal);
    public void setNeighbours(int indentif);

    public Iterator_Solutions_QAPs getNeighbours();
    public Iterator_Solutions_QAPs getNeighbours(int indentif);

    public int indexOfNeigh(Iterator_Solutions_QAPs sc);
    public boolean containsNeigh(Iterator_Solutions_QAPs sc);
    public int getCurrentIndexNeigh();

    // add and remove scalarizers
    public boolean addNeigh(Iterator_Solutions_QAPs scal);
    public Iterator_Solutions_QAPs removeNeigh(int indentif);
    public void setNeigh(int indentif, Iterator_Solutions_QAPs scal);

    public boolean adaptationNeigh();
    //public boolean genetic();
    //public boolean solutionGen();
    
    public void restart();
    public int chooseNeighbours();
    
    public int sizeNeigh();
    
    public Iterator_Solutions_QAPs[] toArrayNeigh();
}
