package general.Scalarization;
import java.util.*;

public interface Scalarizer extends Comparable<Scalarizer>, java.io.Serializable, Comparator<Scalarizer> {

    double scalarize(Long[] objectives);
    double scalarize(Double[] objectives);
    double scalarize(Integer[] objectives);
    
    public void setWeights(double[] weights);
    public void setWeights();

    public double[] getWeights();

    public Scalarizer clone();
}
