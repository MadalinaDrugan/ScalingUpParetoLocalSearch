/*
 *
 */

package general.Scalarization;

//import java.util.Vector;

/**
 *
 * @author madalina
 */
public interface SetScalarizer{

    //public abstract boolean adaptation(Solution[] s, long[] refPoint);
    public void setScalarizer(Scalarizer scal);
    public void setScalarizer(int indentif);

    public Scalarizer getScalarizer();
    public Scalarizer getScalarizer(int indentif);

    public int indexOf(Scalarizer sc);
    public boolean contains(Scalarizer sc);
    public int getCurrentIndex();

    // add and remove scalarizers
    public boolean add(Scalarizer scal);
    public Scalarizer remove(int indentif);
    public void set(int indentif, Scalarizer scal);

    public boolean adaptation();
    public boolean genetic();
    public boolean solutionGen();
    
    public void restart();
    public int chooseScalarizer();
    
    public int size();
    
    public Scalarizer[] toArray();
}
