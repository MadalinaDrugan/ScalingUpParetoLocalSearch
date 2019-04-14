package PLS.LocalSearch;

/**
 *
 * simple class for adding values
 */
public class Counter{
    public int index;
    public Counter(){
        index = 0;
    }

    public void addOne(){
        index++;
    }

    public int getIndex(){
        return index;
    }
}