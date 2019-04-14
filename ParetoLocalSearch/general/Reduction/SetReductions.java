/*
 *
 */

package general.Reduction;

//import java.util.Vector;

/**
 *
 * @author madalina
 */
public interface SetReductions{

    //public abstract boolean adaptation(Solution[] s, long[] refPoint);
    public void setReduction(Reduction scal);
    public void setReduction(int indentif);

    public Reduction getReduction();
    public Reduction getReduction(int indentif);
    //public int getReductionIdentif();

    public int indexOf(Reduction sc);
    public boolean contains(Reduction sc);
    public int getCurrentIndex();

    // add and remove scalarizers
    public boolean add(Reduction scal);
    public Reduction remove(int indentif);
    public void set(int indentif, Reduction scal);

    public boolean adaptation();
    public boolean genetic();
    public boolean solutionGen();
    
    public void restart();
    public int chooseReduction();
    
    public int size();
    
    public Reduction[] toArray();
}
