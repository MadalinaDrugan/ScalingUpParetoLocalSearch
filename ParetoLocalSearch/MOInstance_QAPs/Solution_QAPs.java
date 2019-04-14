/*
 * @author Madalina Drugan: 2010-2018
 */

package MOInstance_QAPs;

import general.Genetic.PerturbatorStrategies;
import general.Scalarization.Scalarizer;
import java.util.*;
import general.*;
import general.Reduction.SetReductions;
import org.apache.commons.lang3.ArrayUtils;

public class Solution_QAPs extends Solution{
    
    public TreeSet<PartialSolution_QAPs> set;

    public int[] cycles;
    public int[] identArray;
    public int[] combArray;

    static final int ITEMS_SHOWN=0;
    static Random r = new Random();
    protected long identificationNumber = r.nextLong();

    ///////////////////////
    // additional functionality
    //private PerturbatorStrategies epsilon;

    private Stack<int[]> cyclePosition = new Stack<>();
    private Stack<Integer> tempArray = new Stack<>();
    
    private int flagOperation = 0;

    //public boolean minimization = true;

    public Scalarizer scal;
    public SetReductions red;

    public Solution_QAPs(){
        //epsilon = 0;
    }
    
    public Solution_QAPs(long[] obj, int[] rep){//, PerturbatorStrategies e){
        items = ArrayUtils.toObject(ArrayUtils.clone(rep)); //.copyOf(rep, rep.length);
        objectives = ArrayUtils.toObject(ArrayUtils.clone(obj)); //Arrays.copyOf(obj, obj.length);
        cycles = new int[items.length];
        identArray = new int[items.length];
        combArray = new int[items.length];
    }
    
    public Solution_QAPs(Long[] obj, Integer[] rep){//, PerturbatorStrategies e){
        items = ArrayUtils.clone(rep); //.copyOf(rep, rep.length);
        objectives = ArrayUtils.clone(obj); //Arrays.copyOf(obj, obj.length);
        cycles = new int[items.length];
        identArray = new int[items.length];
        combArray = new int[items.length];
    }
    
    public Solution_QAPs(long[] obj, Stack<Integer> rep){//, PerturbatorStrategies e){
        items = new Integer[rep.size()];
        for(int i = 0; i < rep.size(); i++) {
            items[i] = rep.get(i);
        }
        
        objectives = new Long[obj.length];
        objectives = ArrayUtils.toObject(ArrayUtils.clone(obj));
        
        cycles = new int[items.length];
        identArray = new int[items.length];
        combArray = new int[items.length];
    }

    public Solution_QAPs(Solution_QAPs s){
        items = Arrays.copyOf(s.items, s.items.length);
        objectives = Arrays.copyOf(s.objectives, s.objectives.length);
        
        cycles = new int[items.length];
        identArray = new int[items.length];
        combArray = new int[items.length];
        //epsilon = s.epsilon;
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

            Iterator<PartialSolution_QAPs> iter = set.iterator();
            while(iter.hasNext()){
                PartialSolution_QAPs iterSol = iter.next();

                for(int j=0;j<iterSol.items.length;j++){
                    sb.append(iterSol.items[j]).append(",\t");
                }
		        sb.append('\n');
            }
		}
 		//sb.append(" space:"+spaceUsed);
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
            if((Long)objectives[i] < (Long)ps.objectives[i]){
                oneBiggerB=true;
                equalB = false;
            }
            if ((Long)objectives[i] > (Long)ps.objectives[i]){
                oneSmaller = true;
                equalB = false;
            }

            if(Math.abs((Long)objectives[i] - (Long)ps.objectives[i]) > epsilon2.getPerturbator()) {
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
            if ((Long)objectives[i] < (Long)ps.objectives[i]){
                oneBiggerB=true;
            }
            if ((Long)objectives[i] > (Long)ps.objectives[i]){
                oneSmaller = true;
            }
	}
        if(!oneBiggerB && oneSmaller) {
            return true;
        }
        return false;
    }

    @Override
    public boolean incomparable(Solution ps, Scalarizer scal){
        if(scal == null) {
            return isDominated(ps);
        }
        double scal0_ = scal.scalarize((Long[])objectives);
        double scal1_ = scal.scalarize((Long[])ps.objectives);
        
        if(scal0_ == scal1_) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDominated(Solution ps, Scalarizer scal){
        if(scal == null) {
            return isDominated(ps);
        }
        double scal0_ = scal.scalarize((Long[])objectives);
        double scal1_ = scal.scalarize((Long[])ps.objectives);
        
        if(scal0_ > scal1_) {
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
                scal0[i] += redWeights[i][j] * (Long)objectives[j];
                scal1[i] += redWeights[i][j] * (Long)ps.objectives[j];
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
                scal0[i] += redWeights[i][j] * (Long)objectives[j];
                scal1[i] += redWeights[i][j] * (Long)ps.objectives[j];
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
        if(!oneBiggerB && oneSmaller) {
            return true;
        }
        //if(oneBiggerB && !oneSmaller && !minimization) {
        //    return true;
        //}
        return false;
    }

    @Override
    public boolean dominates(Solution ps){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<objectives.length;i++){
            if ((Long)objectives[i] < (Long)ps.objectives[i]){
                oneBiggerB=true;
            }
            if ((Long)objectives[i] > (Long)ps.objectives[i]){
                oneSmaller = true;
            }
        }
        if(oneBiggerB && !oneSmaller){// && minimization) {
            return true;
        }
        //if(!oneBiggerB && oneSmaller && !minimization) {
        //    return true;
        //}
        return false;
    }

    @Override
    public boolean dominates(Object[] obj){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<obj.length;i++){
            if ((Long)objectives[i] < (Long)obj[i]){
                oneBiggerB=true;
            }
            if ((Long)objectives[i] > (Long)obj[i]){
                oneSmaller = true;
            }
        }
        if(oneBiggerB && !oneSmaller){ // && minimization) {
            return true;
        }
        //if(!oneBiggerB && oneSmaller && !minimization) {
        //    return true;
        //}
        return false;
    }

    @Override
    public boolean incomparable(Object[] obj, PerturbatorStrategies epsilon2){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        boolean oneEqual_1 = true;
        boolean tooClose = true;
        for(int i=0;i<obj.length;i++){
            if ((Long)objectives[i] < (Long)obj[i]){
                oneBiggerB=true;
            }
            if ((Long)objectives[i] > (Long)obj[i]){
                oneSmaller = true;
            }
            if(!Objects.equals(objectives[i], (Long)obj[i])) {
                oneEqual_1 = false;
            }
            if(Math.abs((Long)objectives[i] - (Long)obj[i]) > epsilon2.getPerturbator()) {
                tooClose = false;
            }
        }
        if(((oneBiggerB && oneSmaller) || oneEqual_1) && !tooClose) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dominates(Solution ps, Scalarizer scal){
        if(scal == null) {
            return isDominated(ps);
        }
        double scal0_ = scal.scalarize((Long[])objectives);
        double scal1_ = scal.scalarize((Long[])ps.objectives);
        //
        //double scal1 = scal.scalarize(ps.objectives);
        if(scal0_ < scal1_){ // && minimization) {
            return true;
        }
        //if(scal0 > scal1 && !minimization) {
        //    return true;
        //}
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
                scal0[i] += redWeights[i][j] * (Long)objectives[j];
                scal1[i] += redWeights[i][j] * (Long)ps.objectives[j];
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
        
        if(oneBiggerB && !oneSmaller){ // && minimization) {
            return true;
        }
        //if(!oneBiggerB && oneSmaller && !minimization) {
        //    return true;
        //}
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
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            for(int i=0;i<objectives.length;i++) {
                if ((Long)objectives[i] > (Long)s1.objectives[i]) {
                    oneSmaller = true;
                    equalB = false;
                } else if ((Long)objectives[i] < (Long)s1.objectives[i]) {
                    oneBiggerB = true;
                    equalB = false;
                }
            }
            if((oneBiggerB && oneSmaller) || equalB) {
                incomp = true;
            } else 
            if(oneBiggerB) {
                domin = true;
            }
            else if(oneSmaller) {
                isDomin = true;
            }
        }
        if(domin && !isDomin){ // && minimization) {
            return true;
        }
        //if(!domin && isDomin && !minimization) {
        //    return true;
        //}
        return false;
    }

    @Override
    public boolean dominates(ArchiveSolutions ps, Scalarizer scal){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        //
        double scal0_ = scal.scalarize((Long[])objectives);
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            double scal1_ = scal.scalarize((Long[])s1.objectives);

            //this dominates one of the parents
            if(scal0_ < scal1_){ // && minimization) {
                oneBiggerB = true;
            }
            if(scal0_ > scal1_){ // && minimization) {
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
        boolean domin = false;
        boolean isDomin = false;
        //
        scal0 = new double[redWeights.length];
        scal1 = new double[redWeights.length];
        for(int i = 0; i < scal0.length; i++){
            scal0[i] = 0;
            //scal1[i] = 0;
            for(int j = 0; j < objectives.length; j++){
                scal0[i] += redWeights[i][j] * (Long)objectives[j];
                //scal1[i] += redWeights[i][j] * ps.objectives[j];
            }
        }
        
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();

            for(int i = 0; i < scal0.length; i++){
                scal1[i] = 0;
                for(int j = 0; j < objectives.length; j++){
                    scal1[i] += redWeights[i][j] * (Long)s1.objectives[j];
                }
            }

            oneBiggerB=false;
            oneSmaller = false;
            //equalB = true;
            for(int i=0;i<scal0.length;i++) {
                if (scal0[i] > scal1[i]) {
                    oneSmaller = true;
                    //equalB = false;
                } else if (scal0[i] < scal1[i]) {
                    oneBiggerB = true;
                    //equalB = false;
                }
            }

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
    public boolean isDominated(Object[] obj){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        for(int i=0;i<obj.length;i++){
            if((Long)objectives[i] < (Long)obj[i]){
                oneBiggerB=true;
            }
            if ((Long)objectives[i] > (Long)obj[i]){
                oneSmaller = true;
            }
        }
        if(!oneBiggerB && oneSmaller) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDominated(ArchiveSolutions ps, Scalarizer scal){
        boolean oneBiggerB=false;
        boolean oneSmaller = false;
        //
        double scal0_ = scal.scalarize((Long[])objectives);
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            double scal1_ = scal.scalarize((Long[])s1.objectives);

            //this dominates one of the parents
            if(scal0_ < scal1_) {
                oneBiggerB = true;
            }
            if(scal0_ > scal1_) {
                oneSmaller = true;
            }
        }
        if(!oneBiggerB && oneSmaller) {
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
                scal0[i] += redWeights[i][j] * (Long)objectives[j];
             }
        }

        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();

            for(int i = 0; i < scal0.length; i++){
                scal1[i] = 0;
                for(int j = 0; j < s1.objectives.length; j++){
                    scal1[i] += redWeights[i][j] * (Long)s1.objectives[j];
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
        if(!domin && isDomin) {
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
        double scal0_ = scal.scalarize((Long[])objectives);
        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            double scal1_ = scal.scalarize((Long[])s1.objectives);

            //this dominates one of the parents
            if(scal0_ < scal1_) {
                oneBiggerB = true;
            }
            if(scal0_ > scal1_) {
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
                scal0[i] += redWeights[i][j] * (Long)objectives[j];
            }
        }

        Iterator<Solution> iter = ps.iterator();
        while(iter.hasNext()){
            Solution_QAPs s1 = (Solution_QAPs)iter.next();

            for(int i = 0; i < scal0.length; i++){
                scal1[i] = 0;
                for(int j = 0; j < objectives.length; j++){
                    scal1[i] += redWeights[i][j] * (Long)s1.objectives[j];
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
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
 
            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            tooClose = true;
            for(int i=0;i<objectives.length;i++){
                 if ((Long)objectives[i] > (Long)s1.objectives[i]){
                     oneSmaller = true;
                     equalB = false;
                 }
                 else if((Long)objectives[i] < (Long)s1.objectives[i]){
                     oneBiggerB=true;
                     equalB = false;
                 }
                 if(Math.abs((Long)objectives[i] - (Long)s1.objectives[i]) >  epsilon2.getPerturbator()) {
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
            Solution_QAPs s1 = (Solution_QAPs)iter.next();
            oneBiggerB=false;
            oneSmaller = false;
            equalB = true;
            for(int i=0;i<objectives.length;i++) {
                if ((Long)objectives[i] > (Long)s1.objectives[i]){
                    oneSmaller = true;
                    equalB = false;
                }
                else if((Long)objectives[i] < (Long)s1.objectives[i]){
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
        if(!domin && isDomin)
            return true;
        //if(domin && !isDomin && !minimization)
        //    return true;
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
              if (Math.abs((Long)objectives[i] - (Long)o.objectives[i]) == 0) {
                    return -1;
                }
          }
        }
      
        for(int i=0;i<this.objectives.length;i++){
                if((Long)o.objectives[i] < (Long)objectives[i]) {
                oneBigger++;
            }
                else if (Math.abs((Long)o.objectives[i]-(Long)objectives[i]) == 0) {
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
              if (Math.abs((Long)objectives[i] - (Long)((Solution_QAPs)o).objectives[i]) ==0) {
                    return 1;
                }
          }            
        } else if(flagOperation == 3 | flagOperation == 4){
            int lengthO = o.objectives.length;
            oneBigger=0;
            oneEqual = 0;
            for(int i=0;i<lengthO;i++){
                if((Long)o.objectives[i] < (Long)objectives[i]) {
                    oneBigger++;
                }
                else if (Math.abs((Long)o.objectives[i] - (Long)objectives[i]) == 0) {
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
    public boolean equals(Object o){
        if (this==o) return true;
        for(int i=0;i < objectives.length;i++){
            if (!Objects.equals(objectives[i], ((Solution)o).objectives[i]))
                return false;
        }

          for(int i = 0; i < items.length; i++){
              if(((Solution)o).items[i] != items[i]){
                  return false;
              }
          }
          return true;
    }

    @Override
    public long getIdentifNumber(){
        double temp = 0;
        for(int i = 0; i < objectives.length; i++){
            temp += Math.pow(10, 8*i)*(Long)objectives[i];
        }
        return (long)temp;
    }

    //minimum number of interchanges between two solutions
    @Override
    public int getDistance(Solution s){
       //test the difference between the two strings
       int dist = 0;
       cyclePosition.clear();
       if(cycles == null){
           cycles = new int[items.length];
       }

       Arrays.fill(cycles,-1);

       //find the cycles in the two
       for(int j = 0; j < this.items.length;j++)
           if(cycles[j] == -1){
                int tempJ = j;
                    cycles[j] = tempJ;
                    if(Objects.equals(items[tempJ], s.items[tempJ])){
                        int[] tempA = new int[1];
                        tempA[0] = (Integer)items[tempJ];
                        cyclePosition.add(tempA);
                        continue;
                    }
                    tempArray.clear();
                    tempArray.add(tempJ);
                    while(true){
                        boolean flagContinue = false;
                        for(int k = j+1; k< this.items.length; k++)
                            if(tempJ != k & Objects.equals(items[k], s.items[tempJ])){
                                cycles[k] = k;
                                tempJ = k;
                                tempArray.add(tempJ);
                                flagContinue = true;
                                break;
                            }
                        if(!flagContinue | s.items[tempJ] == items[j])
                            break;
                    }
                    //find
                    int[] tempArray1 = new int[tempArray.size()];
                    int i = 0;
                    Iterator<Integer> thisIter = tempArray.iterator();
                    while(thisIter.hasNext()){
                        tempArray1[i++] = thisIter.next();
                    }
                    dist += tempArray1.length - 1;
                    Arrays.sort(tempArray1);
                    cyclePosition.add(tempArray1);
             }

         return dist; //items.length - cyclePosition.size();
        }

    //minimum number of interchanges between two solutions
    @Override
    public int getDistance(Object[] temp){
       //test the difference between the two strings
       int dist = 0;
       cyclePosition.clear();
       if(cycles == null){
           cycles = new int[items.length];
       }

       Arrays.fill(cycles,-1);

       //find the cycles in the two
       for(int j = 0; j < this.items.length;j++)
           if(cycles[j] == -1){
                    int tempJ = j;
                    cycles[j] = tempJ;
                    if(Objects.equals(items[tempJ], (Integer)temp[tempJ])){
                        int[] tempA = new int[1];
                        tempA[0] = (Integer)items[tempJ];
                        cyclePosition.add(tempA);
                        continue;
                    }
                    tempArray.clear();
                    tempArray.add(tempJ);
                    while(true){
                        boolean flagContinue = false;
                        for(int k = j+1; k< this.items.length; k++)
                            if(tempJ != k & Objects.equals(items[k], (Integer)temp[tempJ])){
                                cycles[k] = k;
                                tempJ = k;
                                tempArray.add(tempJ);
                                flagContinue = true;
                                break;
                            }
                        if(!flagContinue | Objects.equals(items[j], (Integer)temp[tempJ]))
                            break;
                    }
                    //find
                    int[] tempArray1 = new int[tempArray.size()];
                    int i = 0;
                    Iterator<Integer> thisIter = tempArray.iterator();
                    while(thisIter.hasNext()){
                        tempArray1[i++] = thisIter.next();
                    }
                    Arrays.sort(tempArray1);
                    dist += tempArray1.length - 1;
                    cyclePosition.add(tempArray1);
             }

         return dist; //items.length - cyclePosition.size();
        }

    //minimum number of interchanges between two solutions
    @Override
    public int getDistance(Object[] temp, Object[] temp2){
       //test the difference between the two strings
       cyclePosition.clear();
       if(cycles == null){
           cycles = new int[temp2.length];
       }
       int dist = 0;

       Arrays.fill(cycles,-1);

       //find the cycles in the two
       for(int j = 0; j < temp2.length;j++)
           if(cycles[j] == -1){
                    int tempJ = j;
                    cycles[j] = tempJ;
                    if(temp2[tempJ] == temp[tempJ]){
                        int[] tempA = new int[1];
                        tempA[0] = (Integer)temp2[tempJ];
                        cyclePosition.add(tempA);
                        continue;
                    }
                    tempArray.clear();
                    tempArray.add(tempJ);
                    while(true){
                        boolean flagContinue = false;
                        for(int k = j+1; k< temp2.length; k++)
                            if(tempJ != k & temp2[k] == temp[tempJ]){
                                cycles[k] = k;
                                tempJ = k;
                                tempArray.add(tempJ);
                                flagContinue = true;
                                break;
                            }
                        if(!flagContinue | temp2[j] == temp[tempJ])
                            break;
                    }
                    //find
                    int[] tempArray1 = new int[tempArray.size()];
                    int i = 0;
                    Iterator<Integer> thisIter = tempArray.iterator();
                    while(thisIter.hasNext()){
                        tempArray1[i++] = thisIter.next();
                    }
                    Arrays.sort(tempArray1);
                    dist+= tempArray1.length - 1;
                    cyclePosition.add(tempArray1);
             }

         return dist; //temp2.length - cyclePosition.size();
        }

    @Override
    public Solution getASolution(){
        int N;
        if(set == null)
            return clone();
                
            N = r.nextInt(set.size()+1);
            if(N == 0)
                return clone();
            
            int i =0;
            Iterator<PartialSolution_QAPs> iter = set.iterator();
            while(iter.hasNext() & i++ < N-1)
                iter.next();
            
            return clone(iter.next());
        
    }

    @Override public Solution clone(){
        Solution_QAPs newS = new Solution_QAPs(); //epsilon);
        newS.items = Arrays.copyOf(items, this.items.length);
        newS.objectives = Arrays.copyOf(objectives, this.objectives.length);
        //newS.problem = problem;
        //flag is not copied
        newS.flagVisited = flagVisited;
        return newS;
    }

    public Solution_QAPs clone(PartialSolution_QAPs original){
        Solution_QAPs sol = new Solution_QAPs(); //epsilon);
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
        flagOperation = f;
    }
    @Override
    public int getFlagOperation(){
        return flagOperation;
    }

    //////////////////////////
    //aditionale; specifice
    @Override
    public Object[] dominatingObj(Object[] obj){
        for(int j=0;j<obj.length;j++){
            if((Long)objectives[j]< (Long)obj[j])
                obj[j] = objectives[j];
        }
        return obj;
    }

}
