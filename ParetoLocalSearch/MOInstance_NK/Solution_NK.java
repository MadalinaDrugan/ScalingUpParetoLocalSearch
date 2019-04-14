/*
 * @author Madalina Drugan: 2010-2018
 */

package MOInstance_NK;

import general.Genetic.PerturbatorStrategies;
import general.Scalarization.Scalarizer;
import java.util.*;
import general.*;
import general.Reduction.SetReductions;
import org.apache.commons.lang3.ArrayUtils;

public class Solution_NK extends Solution{
    
    public TreeSet<PartialSolution_NK> set;

    static final int ITEMS_SHOWN=0;
    static Random r = new Random();
    protected long identificationNumber = r.nextLong();

    private int flagOperation = 0;

    public Scalarizer scal;
    public SetReductions red;

    public Solution_NK(){
        //epsilon = 0;
    }
    
    public Solution_NK(double[] obj, boolean[] rep){//, PerturbatorStrategies e){
        items = ArrayUtils.toObject(Arrays.copyOf(rep, rep.length));
        objectives = ArrayUtils.toObject(Arrays.copyOf(obj, obj.length));
    }
    
    public Solution_NK(Double[] obj, Boolean[] rep){//, PerturbatorStrategies e){
        items = Arrays.copyOf(rep, rep.length);
        objectives = Arrays.copyOf(obj, obj.length);
    }
    
    public Solution_NK(Double[] obj, Stack<Boolean> rep){//, PerturbatorStrategies e){
        items = new Boolean[rep.size()];
        for(int i = 0; i < rep.size(); i++) {
            items[i] = rep.get(i);
        }
        objectives = new Double[obj.length];
        objectives = Arrays.copyOf(obj, obj.length);

    }

    public Solution_NK(Solution_NK s){
        items = Arrays.copyOf(s.items, s.items.length);
        objectives = Arrays.copyOf(s.objectives, s.objectives.length);        
    }

    @Override
    public String toString(){
	StringBuilder sb = new StringBuilder();
        sb.append("Permutation: \t");
        for (Object item : items) {
            sb.append(item).append("\t");
        }
        sb.append('\n');

        sb.append("value of solution");
        for (Object objective : objectives) {
            sb.append(objective).append("\t");
        }
        sb.append('\n');
	if (set != null && !set.isEmpty() ){
            sb.append("Near values ");

            Iterator<PartialSolution_NK> iter = set.iterator();
            while(iter.hasNext()){
                PartialSolution_NK iterSol = iter.next();

                for(int j=0;j<iterSol.items.length;j++){
                    sb.append(iterSol.items[j]).append(",\t");
                }
		sb.append('\n');
            }
	}
 	return sb.toString();
    }

    @Override public void setTypeProblem(boolean type){
        //this.minimization = type;
    }
    
    @Override public void setScalarizer(Scalarizer scal){
        this.scal = scal;
    }

    @Override public void setSetReduction(SetReductions sc){
        red = sc;
    }

    //////////////////////////
    // dominating relationships for one solution
    ///////////////////////////
    @Override
    public boolean incomparable(Solution ps, PerturbatorStrategies epsilon2){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean equalB = true;
        boolean tooClose = true;
        for(int i=0;i<objectives.length;i++){
            if(Double.compare((Double)objectives[i], (Double)ps.objectives[i]) < 0){
                oneBiggerB=true;
                equalB = false;
            }
            if(Double.compare((Double)objectives[i], (Double)ps.objectives[i]) > 0){
                oneSmaller = true;
                equalB = false;
            }

            if(Math.abs((Double)objectives[i] - (Double)ps.objectives[i]) > epsilon2.getPerturbator()) {
                tooClose = false;
            }
        }
        if(oneBiggerB && oneSmaller && !tooClose) {
            return true;
        }
        if(equalB && !tooClose) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDominated(Solution ps){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<objectives.length;i++){
            if (Double.compare((Double)objectives[i], (Double)ps.objectives[i]) < 0){
                oneBiggerB=true;
            }
            if (Double.compare((Double)objectives[i], (Double)ps.objectives[i]) > 0){
                oneSmaller = true;
            }
	}
        if(oneBiggerB && !oneSmaller) {
            return false;
        }
        return true;
    }

    @Override
    public boolean incomparable(Solution ps, Scalarizer scal){
        if(scal == null) {
            return isDominated(ps);
        }
        double scal0_ = scal.scalarize((Double[]) objectives);
        double scal1_ = scal.scalarize((Double[])ps.objectives);
        if(scal1_ == scal0_) {
            return true;
        }
        return false;
    }

    double[] scal0, scal1;
    @Override
    public boolean incomparableReduction(Solution ps, double[][] redWeights){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean equalB = true;
        
        scal0 = new double[redWeights.length];
        scal1 = new double[redWeights.length];
        for(int i = 0; i < scal0.length; i++){
            scal0[i] = 0;
            scal1[i] = 0;
            for(int j = 0; j < ps.objectives.length; j++){
                scal0[i] += redWeights[i][j] * (Double)objectives[j];
                scal1[i] += redWeights[i][j] * (Double)ps.objectives[j];
            }
        }
        
        for(int i = 0; i < scal1.length; i++){
            if (scal0[i] < scal1[i]){
                oneBiggerB=true;
                equalB = false;
            }
            if (scal0[i] > scal1[i]){
                oneSmaller = true;
                equalB = false;
            }            
        }
        if(oneBiggerB && oneSmaller) {
            return true;
        }
        if(equalB ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean incomparable(Object[] obj, PerturbatorStrategies epsilon2){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean oneEqual_1 = true;
        boolean tooClose = true;
        for(int i=0;i<obj.length;i++){
            if ((Double)objectives[i] < (Double)obj[i]){
                oneBiggerB=true;
            }
            if ((Double)objectives[i] > (Double)obj[i]){
                oneSmaller = true;
            }
            if(!Objects.equals(objectives[i], (Double)obj[i])) {
                oneEqual_1 = false;
            }
            if(Math.abs((Double)objectives[i] - (Double)obj[i]) > epsilon2.getPerturbator()) {
                tooClose = false;
            }
        }
        if(((oneBiggerB && oneSmaller) || oneEqual_1) && !tooClose) {
            return true;
        }
        return false;
    }


    @Override
    public boolean isDominatedReduction(ArchiveSolutions ps, double[][] redWeights){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean equalB = true;
        //
        //boolean incomp = true;
        boolean domin = false;
        boolean isDomin = false;
        //
        
        scal0 = new double[redWeights.length];
        scal1 = new double[redWeights.length];
        for(int i = 0; i < scal0.length; i++){
            scal0[i] = 0;
            for(int j = 0; j < objectives.length; j++){
                scal0[i] += redWeights[i][j] * (Double)objectives[j];
             }
        }

        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_NK s1 = (Solution_NK)iter.next();

            for(int i = 0; i < scal0.length; i++){
                scal1[i] = 0;
                for(int j = 0; j < s1.objectives.length; j++){
                    scal1[i] += redWeights[i][j] * (Double)s1.objectives[j];
                }
            }

            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            for(int i=0;i<scal0.length;i++) {
                if (scal0[i] > scal1[i]){
                    oneSmaller = true;
                    equalB = false;
                }
                else if(scal0[i] < scal1[i]){
                    oneBiggerB=true;
                    equalB = false;
                }
            }
            //if((oneBiggerB && oneSmaller) || equalB) {
            //    incomp = true;
            //}
            //else 
            if(oneBiggerB) {
                domin = true;
            }
            else if(oneSmaller) {
                isDomin = true;
            }
        }
        if(domin && !isDomin) {
            return true;
        }
        return false;
    }

    @Override
    public boolean incomparable(ArchiveSolutions ps, Scalarizer scal){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean oneEqual_1 = true;
        //
        double scal0_ = scal.scalarize((Double[])objectives);
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_NK s1 = (Solution_NK)iter.next();
            double scal1_ = scal.scalarize((Double[])s1.objectives);

            if(scal0_ > scal1_) {
                oneBiggerB = true;
            }
            if(scal0_ < scal1_) {
                oneSmaller = true;
            }
            if(scal0_ != scal1_) {
                oneEqual_1 = false;
            }
        }
        if((oneBiggerB && oneSmaller) || oneEqual_1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean incomparableReduction(ArchiveSolutions ps, double[][] redWeights){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean equalB = true;
        //
        boolean incomp = true;
        boolean domin = false;
        boolean isDomin = false;

        scal0 = new double[redWeights.length];
        scal1 = new double[redWeights.length];
        for(int i = 0; i < scal0.length; i++){
            scal0[i] = 0;
            for(int j = 0; j < objectives.length; j++){
                scal0[i] += redWeights[i][j] * (Double)objectives[j];
            }
        }

        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_NK s1 = (Solution_NK)iter.next();

            for(int i = 0; i < scal0.length; i++){
                scal1[i] = 0;
                for(int j = 0; j < objectives.length; j++){
                    scal1[i] += redWeights[i][j] * (Double)s1.objectives[j];
                }
            }
            
//this dominates one of the parents
            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            for(int i=0;i<scal0.length;i++){
                 if (scal0[i] > scal1[i]){
                     oneSmaller = true;
                     equalB = false;
                 }
                 else if(scal0[i] < scal1[i]){
                     oneBiggerB=true;
                     equalB = false;
                 }
            }
            if((oneBiggerB && oneSmaller) || equalB) {
                incomp = true;
            }
            else if(oneBiggerB) {
                domin = true;
            }
            else if(oneSmaller) {
                isDomin = true;
            }
        }
        if((domin && isDomin) || (incomp && !domin && !isDomin)) {
            return true;
        }
        return false;
    }

    ////////////////////////////
    // dominating relationship of one solution against a front
    ////////////////////////////
    @Override
    public boolean incomparable(ArchiveSolutions ps, PerturbatorStrategies epsilon2){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean equalB = true;
        //
        boolean incomp = true;
        boolean domin = false;
        boolean isDomin = false;

        boolean tooClose = true;
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_NK s1 = (Solution_NK)iter.next();
 
            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            tooClose = true;
            for(int i=0;i<objectives.length;i++){
                 if ((Double)objectives[i] > (Double)s1.objectives[i]){
                     oneSmaller = true;
                     equalB = false;
                 }
                 else if((Double)objectives[i] < (Double)s1.objectives[i]){
                     oneBiggerB=true;
                     equalB = false;
                 }
                 if(Math.abs((Double)objectives[i] - (Double)s1.objectives[i]) >  epsilon2.getPerturbator()) {
                    tooClose = false;
                }
            }
            if(((oneBiggerB && oneSmaller) || equalB) && !tooClose) {
                incomp = true;
            }
            else if(oneBiggerB) {
                domin = true;
            }
            else if(oneSmaller) {
                isDomin = true;
            }
        }
        if((domin && isDomin) || (incomp && !domin && !isDomin)) {
            return true;
        }
        return false;
    }

    ///////////////////////////
    //////////////////////////
    //aditionale; specifice
    @Override
    public Object[] dominatingObj(Object[] obj){
        for(int j=0;j<obj.length;j++){
            if((Double)objectives[j] < (Double)obj[j])
                obj[j] = objectives[j];
	}
        return obj;
    }

    @Override
    public boolean isDominated(Solution ps, Scalarizer scal){
        if(scal == null) {
            return isDominated(ps);
        }
        double scal0_ = scal.scalarize((Double[])objectives);
        double scal1_ = scal.scalarize((Double[])ps.objectives);

        if(scal0_ < scal1_) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDominatedReduction(Solution ps, double[][] redWeights){
        if(red == null) {
            return isDominated(ps);
        }
        boolean oneBiggerB=false;
        boolean oneSmaller = false;

        scal0 = new double[redWeights.length];
        scal1 = new double[redWeights.length];
        for(int i = 0; i < scal0.length; i++){
            scal0[i] = 0;
            scal1[i] = 0;
            for(int j = 0; j < ps.objectives.length; j++){
                scal0[i] += redWeights[i][j] * (Double)objectives[j];
                scal1[i] += redWeights[i][j] * (Double)ps.objectives[j];
            }
        }
        for(int i=0;i<objectives.length;i++){
            if (scal0[i] < scal1[i]){
                oneBiggerB=true;
            }
            if (scal0[i] > scal1[i]){
                oneSmaller = true;
            }
	}
        if(oneBiggerB && !oneSmaller) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dominates(Solution ps){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<objectives.length;i++){
            if ((Double)objectives[i] < (Double)ps.objectives[i]){
                oneBiggerB=true;
            }
            if ((Double)objectives[i] > (Double)ps.objectives[i]){
                oneSmaller = true;
            }
        }
        if(!oneBiggerB && oneSmaller) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dominates(Object[] obj){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<obj.length;i++){
            if ((Double)objectives[i] < (Double)obj[i]){
                oneBiggerB=true;
            }
            if ((Double)objectives[i] > (Double)obj[i]){
                oneSmaller = true;
            }
        }
        if(!oneBiggerB && oneSmaller) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dominates(Solution ps, Scalarizer scal){
        if(scal == null) {
            return isDominated(ps);
        }
        double scal0_ = scal.scalarize((Double[])objectives);
        double scal1_ = scal.scalarize((Double[])ps.objectives);
        
        if(scal0_ > scal1_) {
            return true;
        }
        return false;
    }

    //long[] scal0, scal1;
    @Override
    public boolean dominatesReduction(Solution ps, double[][] redWeights){
        scal0 = new double[redWeights.length];
        scal1 = new double[redWeights.length];
        for(int i = 0; i < scal0.length; i++){
            scal0[i] = 0;
            scal1[i] = 0;
            for(int j = 0; j < ps.objectives.length; j++){
                scal0[i] += redWeights[i][j] * (Double)objectives[j];
                scal1[i] += redWeights[i][j] * (Double)ps.objectives[j];
            }
        }

        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<scal0.length;i++){
            if (scal0[i] < scal1[i]){
                oneBiggerB=true;
            }
            if (scal0[i] > scal1[i]){
                oneSmaller = true;
            }
        }
        
        if(!oneBiggerB && oneSmaller) {
            return true;
        }
        return false;
    }

    ////////////////////////////
    // dominating relationship of one solution against a front
    ////////////////////////////
    @Override
    public boolean dominates(ArchiveSolutions ps){
        boolean oneBiggerB;
        boolean oneSmaller;
        boolean equalB;
        //
        boolean incomp = true;
        boolean domin = false;
        boolean isDomin = false;
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_NK s1 = (Solution_NK)iter.next();
            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            for(int i=0;i<objectives.length;i++) {
                if ((Double)objectives[i] > (Double)s1.objectives[i]) {
                    oneSmaller = true;
                    equalB = false;
                } else if ((Double)objectives[i] < (Double)s1.objectives[i]) {
                    oneBiggerB = true;
                    equalB = false;
                }
            }
            if((oneBiggerB && oneSmaller) || equalB) {
                incomp = true;
            }
            else if(oneBiggerB) {
                domin = true;
            }
            else if(oneSmaller) {
                isDomin = true;
            }
        }
        if(!domin && isDomin) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dominates(ArchiveSolutions ps, Scalarizer scal){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        //
        double scal0_ = scal.scalarize((Double[])objectives);
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_NK s1 = (Solution_NK)iter.next();
            double scal1_ = scal.scalarize((Double[])s1.objectives);

            if(scal0_ > scal1_) {
                oneBiggerB = true;
            }
            if(scal0_ < scal1_) {
                oneSmaller = true;
            }
        }
        if(oneBiggerB && !oneSmaller) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dominatesReduction(ArchiveSolutions ps, double[][] redWeights){
        boolean oneBiggerB;
        boolean oneSmaller;
        boolean equalB = true;
        //
        //boolean incomp = true;
        boolean domin = false;
        boolean isDomin = false;
        //
        scal0 = new double[redWeights.length];
        scal1 = new double[redWeights.length];
        for(int i = 0; i < scal0.length; i++){
            scal0[i] = 0;
            //scal1[i] = 0;
            for(int j = 0; j < objectives.length; j++){
                scal0[i] += redWeights[i][j] * (Double)objectives[j];
                //scal1[i] += redWeights[i][j] * ps.objectives[j];
            }
        }
        
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_NK s1 = (Solution_NK)iter.next();

            for(int i = 0; i < scal0.length; i++){
                scal1[i] = 0;
                for(int j = 0; j < objectives.length; j++){
                    scal1[i] += redWeights[i][j] * (Double)s1.objectives[j];
                }
            }

            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            for(int i=0;i<scal0.length;i++) {
                if (scal0[i] > scal1[i]) {
                    oneSmaller = true;
                    equalB = false;
                } else if (scal0[i] < scal1[i]) {
                    oneBiggerB = true;
                    equalB = false;
                }
            }

            if(oneBiggerB) {
                domin = true;
            }
            else if(oneSmaller) {
                isDomin = true;
            }
        }
        
        if(!domin && isDomin) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDominated(Object[] obj){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<obj.length;i++){
            if((Double)objectives[i] < (Double)obj[i]){
                oneBiggerB=true;
            }
            if ((Double)objectives[i] > (Double)obj[i]){
                oneSmaller = true;
            }
        }
        if(oneBiggerB && !oneSmaller) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDominated(ArchiveSolutions ps, Scalarizer scal){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        //
        double scal0_ = scal.scalarize((Double[])objectives);
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_NK s1 = (Solution_NK)iter.next();
            double scal1_ = scal.scalarize((Double[])s1.objectives);

            if(scal0_ > scal1_) {
                oneBiggerB = true;
            }
            if(scal0_ < scal1_) {
                oneSmaller = true;
            }
        }
        if(!oneBiggerB && oneSmaller) {
            return true;
        }
        return false;
    }

    ////////////////////////////
    // dominating relationship of one solution against a front
    ////////////////////////////
    @Override
    public boolean isDominated(ArchiveSolutions ps){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean equalB = true;
        //
        boolean incomp = true;
        boolean domin = false;
        boolean isDomin = false;
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_NK s1 = (Solution_NK)iter.next();
            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            for(int i=0;i<objectives.length;i++) {
                if ((Double)objectives[i] > (Double)s1.objectives[i]){
                    oneSmaller = true;
                    equalB = false;
                }
                else if((Double)objectives[i] < (Double)s1.objectives[i]){
                    oneBiggerB=true;
                    equalB = false;
                }
            }
            if((oneBiggerB && oneSmaller) || equalB) {
                incomp = true;
            }
            else if(oneBiggerB) {
                domin = true;
            }
            else if(oneSmaller) {
                isDomin = true;
            }
        }
        if(domin && !isDomin)
            return true;
        return false;
    }

    ///////////////////////////
    //////////////////////////////
    // check also the compare to solution
    int oneBigger = 0;
    int oneEqual = 0;
    @Override public int compareTo(Solution o){
        oneBigger = 0;
        oneEqual = 0;
        if(o == this) {
            return 0;
        }
        //we assume that is not dominant 
        if(flagOperation == 2){
          for(int i=0;i<objectives.length;i++){
              if (Math.abs((Double)objectives[i] - (Double)o.objectives[i]) == 0) {
                    return -1;
                }
          }
        }
      
        for(int i=0;i<this.objectives.length;i++){
                if((Double)o.objectives[i] < (Double)objectives[i]) {
                oneBigger++;
            }
                else if (Math.abs((Double)o.objectives[i]-(Double)objectives[i]) == 0) {
                oneEqual++;
            }
        }

        if(flagOperation == 2 & oneBigger > 0 & oneBigger+oneEqual < objectives.length){
             /*   if(set == null)
                    set = new TreeSet<PartialSolution_QAPs>();
                if(set.contains((Solution_QAPs) o))
                    set.add(new PartialSolution_QAPs((Solution_QAPs)o));*/
        } else if(flagOperation == 3 | flagOperation == 4){

            if(oneBigger == 0 & oneEqual != objectives.length) {
                return 1;
            }
            if(oneBigger > 0 & oneBigger+oneEqual == objectives.length) {
                return -1;
            }
            //if(flagOperation == 3 & oneBigger > 0 & oneBigger+oneEqual < problem.numberOfObjectives)
             //   return -1;
            //else return 1;
        } else if(flagOperation == 1){
            if(oneBigger > 0 & oneBigger+oneEqual < objectives.length) {
                return 1;
            }
        }
        return 0;
    }

    /*public boolean dominates(PartialSolution_QAPs ps){
	boolean oneD=false;
	for(int i=0;i<objectives.length;i++){
		if (objectives[i] < ps.objectives[i]) oneD=true;
		else if (objectives[i] > ps.objectives[i]) return false;
	}
	return oneD;
   }*/

    //a kind of dominates only in real number
    // 1 - o dominates o1
    // - 1 - o is dominated by o1
    // 0 - o and o1 are non-dominated
    
    @Override public int compare(Solution o, Solution o1){
        if(o == this) {
            return 0;
        }
        //we assume that is not dominant 
        if(flagOperation == 1 | flagOperation == 2){
          for(int i=0;i<objectives.length;i++){
              if (Math.abs((Double)objectives[i] - (Double)((Solution_NK)o).objectives[i]) ==0) {
                    return 1;
                }
          }            
        } else if(flagOperation == 3 | flagOperation == 4){
            int lengthO = o.objectives.length;
            oneBigger=0;
            oneEqual = 0;
            for(int i=0;i<lengthO;i++){
                if((Double)o.objectives[i] < (Double)objectives[i]) {
                    oneBigger++;
                }
                else if (Math.abs((Double)o.objectives[i] - (Double)objectives[i]) == 0) {
                    oneEqual++;
                }
            }

            if(oneBigger == 0 & oneEqual != lengthO) {
                return -1;
            }
            if(oneBigger > 0 & oneBigger+oneEqual == lengthO) {
                return 1;
            }
        }
        return 0;
    }
    

    @Override public int hashCode(){
        return (int) this.identificationNumber;
    }

    @Override
    public long getIdentifNumber(){
        long temp = 0;
        for(int i = 0; i < objectives.length; i++){
            temp += (long)(Math.pow(10, 8) * (Double)objectives[i]) * (long) Math.pow(10, 8 * (i-1));
        }
        return temp;
    }

    //minimum number of interchanges between two solutions
    @Override
    public int getDistance(Solution s){
       //test the difference between the two strings
        int dist = 0;

        for(int j = 0; j < items.length;j++){
            if(!Objects.equals(items[j], (Boolean)s.items[j])){
                dist++;
            }
        }
        return dist; //items.length - cyclePosition.size();
    }

    //minimum number of interchanges between two solutions
    @Override
    public int getDistance(Object[] temp){
        //test the difference between the two strings
        int dist = 0;
       
        for(int j = 0; j < items.length;j++){
            if(!Objects.equals(items[j], (Boolean)temp[j])){
                dist++;
            }
        }
        return dist; //items.length - cyclePosition.size();
    }

    //minimum number of interchanges between two solutions
    @Override
    public int getDistance(Object[] temp, Object[] temp2){
       //test the difference between the two strings
        int dist = 0;
       
        for(int j = 0; j < temp.length;j++){
            if(!Objects.equals((Boolean)temp[j], (Boolean)temp2[j])){
                dist++;
            }
        }
        return dist; //items.length - cyclePosition.size();
    }

    @Override
    public Solution_NK getASolution(){
       if(set == null)
           return this.clone();
       
       int N = r.nextInt(set.size()+1);
       if(N == 0)
           return clone();

       int i =0;
       Iterator<PartialSolution_NK> iter = set.iterator();
       while(iter.hasNext() & i++ < N-1)
           iter.next();

       return clone(iter.next());
    }

    @Override public Solution_NK clone(){
        Solution_NK newS = new Solution_NK(); //epsilon);
        newS.items = Arrays.copyOf(items, this.items.length);
        newS.objectives = Arrays.copyOf(objectives, this.objectives.length);
        newS.flagVisited = flagVisited;
        return newS;
    }

    public Solution_NK clone(PartialSolution_NK original){
        Solution_NK sol = new Solution_NK(); //epsilon);
        sol.items = Arrays.copyOf(original.items, original.items.length);
        sol.objectives = Arrays.copyOf(original.objectives, original.objectives.length);
        sol.flagVisited = flagVisited;
        //sol.problem = problem;
        return sol;
   }

    @Override
    public boolean setVisited(){
        flagVisited = true;
        return flagVisited;
    }

    @Override
    public void setFlagOperation(int f){
        this.flagOperation = f;
    }
    @Override
    public int getFlagOperation(){
        return flagOperation;
    }


}
