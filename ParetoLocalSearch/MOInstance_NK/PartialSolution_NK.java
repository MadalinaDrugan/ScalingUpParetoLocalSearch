/*
 * @author Madalina Drugan: 2010-2018
 *PartialSolution with the constructor from NK
*/

package MOInstance_NK;

import java.util.*;

public class PartialSolution_NK implements Comparable<PartialSolution_NK>, java.io.Serializable, Comparator<PartialSolution_NK>{

    final static long serialVersionUID = 10917850938401090L;
    static final int ITEMS_SHOWN=80;

    public Boolean[] items;
    public Double[] objectives;
    
    public boolean flagVisited = false;
    
    public PartialSolution_NK(){}
	
    public PartialSolution_NK(Double[] obj, Boolean[] it){
	items = Arrays.copyOf(it, it.length);
        objectives = Arrays.copyOf(obj, obj.length);
    }
    
    public PartialSolution_NK(Boolean[] it){
	items = Arrays.copyOf(it, it.length);
    }

    public PartialSolution_NK(PartialSolution_NK ks){
        items = Arrays.copyOf(ks.items, ks.items.length);
        objectives = Arrays.copyOf(ks.objectives, ks.objectives.length);
    }

    public PartialSolution_NK(Solution_NK ks){
        items = Arrays.copyOf((Boolean[])ks.items, ks.items.length);
        objectives = Arrays.copyOf((Double[])ks.objectives, ks.objectives.length);
    }

    @Override public int compareTo(PartialSolution_NK o){
	if (this==o) return 0;
	for (int i=0;i<objectives.length;i++){
            if (objectives[i] < o.objectives[i]) return -1;
            else if (objectives[i] > o.objectives[i]) return 1;
	}
	return 0;
    }

    public boolean dominates(PartialSolution_NK ps){
	boolean oneBigger=false;
	for(int i=0;i<objectives.length;i++){
            if (objectives[i] < ps.objectives[i]) oneBigger=true;
            else if (objectives[i] > ps.objectives[i]) return false;
	}
	return oneBigger;
    }

    @Override public int compare(PartialSolution_NK o, PartialSolution_NK o1){
        if(o == o1) return 0;
     //   if(problem != null)
      //      return (int)problem.getDistance(o.items, o1.items);
        else
            System.out.println("Problem not initialized");
        return 0;
    }

    public boolean equals(PartialSolution_NK o){
        if (this==o) return true;
        for(int i=0;i<objectives.length;i++){
            if (Math.abs(objectives[i] - o.objectives[i]) > 0)
               return false;
        }
        return true;
    }

    @Override public PartialSolution_NK clone(){
        PartialSolution_NK newS = new PartialSolution_NK();
        newS.items = Arrays.copyOf(items, items.length);
        newS.objectives = Arrays.copyOf(objectives, objectives.length);

        //flag is not copied
        newS.flagVisited = false;
        return newS;
    }

    public boolean setVisited(){
        flagVisited = true;
        return flagVisited;
    }

}

