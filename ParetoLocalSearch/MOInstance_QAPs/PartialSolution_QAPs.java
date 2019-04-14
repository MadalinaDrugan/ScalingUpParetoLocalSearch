/*
 * @author Madalina Drugan: 2010-2018
 *PartialSolution with the constructor from QAPs
*/

package MOInstance_QAPs;

import java.util.*;
import org.apache.commons.lang3.ArrayUtils;

public class PartialSolution_QAPs implements Comparable<PartialSolution_QAPs>, java.io.Serializable, Comparator<PartialSolution_QAPs>{

    final static long serialVersionUID = 10917850938401090L;
    static final int ITEMS_SHOWN=80;

    public Integer[] items;
    public Long[] objectives;
	//public int spaceUsed;
    public static double epsilon;
    public boolean flagVisited = false;
    //private QAPs problem;
    
    public PartialSolution_QAPs(){}
	
    public PartialSolution_QAPs(long[] obj, int[] it){
	items = ArrayUtils.toObject(Arrays.copyOf(it, it.length));
        objectives = ArrayUtils.toObject(Arrays.copyOf(obj, obj.length));
    }
    
    public PartialSolution_QAPs(int[] it){
	items = ArrayUtils.toObject(Arrays.copyOf(it, it.length));
    }

    public PartialSolution_QAPs(PartialSolution_QAPs ks){
        items = Arrays.copyOf(ks.items, ks.items.length);
        objectives = Arrays.copyOf(ks.objectives, ks.objectives.length);
    }

    public PartialSolution_QAPs(Solution_QAPs ks){
        items = Arrays.copyOf((Integer[])ks.items, ks.items.length);
        objectives = Arrays.copyOf((Long[])ks.objectives, ks.objectives.length);
        //problem = ks.getProblem();
    }

    @Override public int compareTo(PartialSolution_QAPs o){
		if (this==o) return 0;
		//PartialSolution_QAPs ps = o.clone();
		for (int i=0;i<objectives.length;i++)
		{
			if (objectives[i] > o.objectives[i] + epsilon) return -1;
			else if (objectives[i] < o.objectives[i] - epsilon) return 1;
		}
		return 0;
	}

	public boolean dominates(PartialSolution_QAPs ps)
	{
		boolean oneBigger=false;
		for(int i=0;i<objectives.length;i++)
		{
			if (objectives[i] < ps.objectives[i] -epsilon) oneBigger=true;
			else if (objectives[i] > ps.objectives[i] + epsilon) return false;
		}
		return oneBigger;
	}

    @Override public int compare(PartialSolution_QAPs o, PartialSolution_QAPs o1){
        if(o == o1) return 0;
     //   if(problem != null)
      //      return (int)problem.getDistance(o.items, o1.items);
        else
            System.out.println("Problem not initialized");
        return 0;
    }

    public boolean equals(PartialSolution_QAPs o){
        if (this==o) return true;
        for(int i=0;i<objectives.length;i++){
             if (Math.abs(objectives[i] - o.objectives[i]) > epsilon)
                            return false;
        }
        return true;
    }

    @Override public PartialSolution_QAPs clone(){
        PartialSolution_QAPs newS = new PartialSolution_QAPs();
        //newS.epsilon = epsilon;
        newS.items = Arrays.copyOf(items, this.items.length);
        newS.objectives = Arrays.copyOf(objectives, this.objectives.length);

        //flag is not copied
        newS.flagVisited = false;
        return newS;
    }

    public boolean setVisited(){
        flagVisited = true;
        return flagVisited;
    }

}

