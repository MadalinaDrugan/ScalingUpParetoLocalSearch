/*
 @author Madalina Drugan: 2010-2018
*
* NonDominatedArchive for one or more objectives
 * There are only two types of problems: minimization and maximization problems
 * minimization: true is a minimization problem, false otherwise
 * objectives: internal variable to spead up the search contains sorted lists for each separate objective
 * larger, smaller lists for comparision of the current NDA with the new candidate solutions
 *
 * Date or work: September 2010
 */
package Archives;

//import general.Scalarization.Scalarizer;
import general.Genetic.PerturbatorStrategies;
import general.*;
import java.util.*;
import general.Scalarization.*;
import general.Reduction.*;
//import MOInstance_QAPs.Solution_QAPs;

public class NonDominatedArchive implements ArchiveSolutions{

    // the effective nda
    private TreeMap<Long,Solution> avl = new TreeMap<>();
    //type of problem: minimization or maximization
    public boolean minimization = true;

    ///////////////////////
    //with memory or no
    private boolean memory = false;
    // list with the past dominating and non-identical solutions
    private Stack<Solution> dominatedList = new Stack<>();
    //the times an solutions is meet
    //Vector<Long[]> dominatedList_Times = new Vector<Long[]>();
    private int sizeDominatedList = 200;

    private final static Random r = new Random();

    //public PerturbatorStrategies epsilon;
    
    public NonDominatedArchive(){
        //epsilon = e;
    }

    //////////////////////
    //internal structures
    //////////////////////
    //the relationship of this identifier with the current solution
    //vectors of identifies; identifier present if in a relationship in at least one dimension
    //private Vector<Long> larger = new Vector<Long>();
    //private Vector<Long> smaller = new Vector<Long>();
    //private Vector<Long> equal = new Vector<Long>();

    //private TreeMap<Long,Long>[] objectives; //sorted tree: value and identifier
    private int nrObjectives;

    ///////////////////////
    // scalarization is null
    @Override
    public boolean add(Solution s){
        s.setFlagOperation(4);

        if(!avl.isEmpty() && contains(s)) {
            return false;
        }

        if(avl.isEmpty()){
            nrObjectives = s.objectives.length;
            s.setFlagOperation(1);
            long identif = s.getIdentifNumber();
            avl.put(identif,s);
            return true;
        }

        Iterator<Map.Entry<Long,Solution>> inter = avl.entrySet().iterator();
        Stack<Long> removeIdentif = new Stack<>();
        while(inter.hasNext()){
            Map.Entry<Long,Solution> compSol = inter.next();
            Solution sol = compSol.getValue();
            if(sol.dominates(s)) {
                return false;
            }
            if(s.dominates(sol)) {
                removeIdentif.add(compSol.getKey());
            }
        }
        while(!removeIdentif.isEmpty()){
           avl.remove(removeIdentif.remove(0));
        }

        s.setFlagOperation(1);
        long identif = this.generateIdentif(s);
        while(avl.containsKey(identif)){
            identif += r.nextLong();
        }
        avl.put(identif,s);
        return true;
   }
    
    public long generateIdentif(Solution s){
        return s.getIdentifNumber();
    }
    
    /*public long generateIdentif(long[] obj){
        long identif = 0;
        for(int i = 0; i < obj.length; i++) {
            identif += obj[i] * (long) Math.pow(10, 6*i);
        }
        return identif;
    }*/


    @Override
    public void add(ArchiveSolutions newNDA){
        Iterator<Solution> i = newNDA.iterator();
	    while(i.hasNext()){
            Solution s = i.next();
            //if(!
            add(s);//)
            //   this.dominatedListAdd(s);
        }
	}

    @Override
    public Solution getNthSolution(int N){
        if(N < avl.size()){
             Iterator<Map.Entry<Long,Solution>> newI = avl.entrySet().iterator();
             for(int counter = 0; counter < N; counter++)
                    newI.next();
             Map.Entry<Long,Solution> result = newI.next();
             return result.getValue();
        }
        if(memory && N-avl.size() < dominatedList.size()){
            return this.dominatedList.get(N-avl.size());
        }
        return null;
    }
        
    @Override
   public Solution getRandomNotVisitedSolution(){
       //vector with not visited solutioatedns
       Stack<Solution> notVisited = new Stack<>();
       Iterator<Map.Entry<Long,Solution>> newI = avl.entrySet().iterator();
       for(int counter = 0; counter < avl.size(); counter++){
             Solution tempS = newI.next().getValue();
             if(!tempS.flagVisited)
                 notVisited.add(tempS);
       }
       if(notVisited.isEmpty())
           return null;
       // randominzed part
       int sizeT = notVisited.size();
       double rTemp = r.nextDouble();
       int t = (int)Math.round(rTemp*sizeT);
       if(t == sizeT)
           t--;
       return notVisited.get(t);
    }

   ////////////////////////////
   // get solutions from avl that are at some distance in parameter space
   /////////////////////////////
    @Override
   public Stack<Solution[]> getDistanceSol(int mut_rate){
        if(avl.size() < 2)
            return null;
        int N = avl.size();
        Stack<Solution[]> tempList = new Stack<>();
        Iterator<Map.Entry<Long,Solution>> newI = avl.entrySet().iterator();
        for(int counter = 0; counter < N; counter++){
             Solution result = newI.next().getValue();
             Iterator<Map.Entry<Long,Solution>> newJ = avl.entrySet().iterator();
             for(int counter1 = 0; counter1 < N; counter1++){
                 Solution result1 = newJ.next().getValue();
                 if(result1.getDistance(result) >= mut_rate+2){
                     Solution[] newS = new Solution[2];
                     newS[0] = result1;
                     newS[1] = result;
                     tempList.add(newS);
                 }
             }
        }
        return tempList;
   }

   ///////////////////////
   // works
   @Override
   public boolean contains(Solution s){
        s.setFlagOperation(2);
        if(s.objectives == null || avl == null || avl.isEmpty()) {
            return false;
        }

        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
 	while(i.hasNext()){
            Solution v = i.next().getValue();
            if(s.equals(v)){
                return true;
            }
	}

        return false;
   }

   /////////////////////
   // nda contains at least one solutions from the current NDA
   ///////////////////////
    @Override
   public boolean contains(ArchiveSolutions nda){
       Iterator<Solution> iterNDA = nda.iterator();
       while(iterNDA.hasNext()){
           Solution s = iterNDA.next();
           s.setFlagOperation(2);

           if(contains(s))
               return true;
           /*boolean cont = false;
           for(int i = 0; i < this.nrObjectives; i++){
               if(!objectives[i].containsKey(s.objectives[i])){
                   cont = true;
                   break;
               }
           }
           if(cont)
               continue;
           return true;*/
       }
       return false;
    }

   //////////////////////////////////
   // order relationships
   ////////////////////////
    @Override
    public boolean incomparable (Solution s, PerturbatorStrategies epsilon2){
     if(avl.isEmpty()) {
            return false;
        }

     boolean domin = false;
     boolean isDomin = false;
     boolean equalS = true;
     //SortedMap<Long,Long> n1 = firstObjective.headMap(new Long(s.objectives[0]));
     Iterator<Solution> iterator = iterator();
     while(iterator.hasNext()){
         Solution s1 = iterator.next();
         if(s.dominates(s1)){
                isDomin = true;
                //continue;
         } 
         if(s1.dominates(s)) {
                domin = true;
         }
         if(!s.incomparable(s1, epsilon2)) {
                equalS = false;
         }
     }
     if(domin && isDomin) {
            return true;
        }
     return !domin && !isDomin && equalS;
   }


   @Override public boolean incomparable (ArchiveSolutions nda, PerturbatorStrategies epsilon2){
       Iterator<Solution> iterNDA = nda.iterator();
       //when it is not dominated
       boolean domin = false;
       boolean isDomin = false;
       boolean equalS = true;
       while(iterNDA.hasNext()){
           Solution s = iterNDA.next();

           if(dominates(s)) {
                domin = true;
            }
           if(s.dominates(this)) {
                isDomin = true;
            }
           if(!incomparable(s,epsilon2)) {
                equalS = false;
            }
       }
       return (domin && isDomin) || (!domin && !isDomin && equalS);
    }

   //////////////////////////////
   // revise the definition of domination
   //true -- avl dominated s
   //false -- avl do not dominates s
    @Override
   public boolean dominates (Solution s){
     if(avl == null || avl.isEmpty()) {
            return false;
        }

     boolean domin = false;
     boolean isDomin = false;
     //SortedMap<Long,Long> n1 = firstObjective.headMap(new Long(s.objectives[0]));
     Iterator<Solution> iterator_1 = iterator();
     while(iterator_1.hasNext()){
           Solution s1 = iterator_1.next();
           if(s1.dominates(s)) {
                domin = true;
            }
           if(s.dominates(s1)) {
                isDomin = true;
            }
     }
     return domin && !isDomin;
   }

    @Override
   public boolean dominates (ArchiveSolutions nda){
       Iterator<Solution> iterNDA = nda.iterator();
       //when it is not dominated
       boolean domin = false;
       boolean isDomin = false;
       while(iterNDA.hasNext()){
           Solution s = iterNDA.next();

           if(dominates(s)) {
                domin = true;
            }
           if(s.dominates(this)) {
                isDomin = true;
            }
       }
       return domin && !isDomin;
    }

   ////////////////////////////
   // exists one solutions for which this is true
    @Override
   public boolean dominatesAtLeastOne (Solution s){
     if(avl.isEmpty()) {
            return false;
        }

     Iterator<Solution> iterator = iterator();
     while(iterator.hasNext()){
              Solution s1 = iterator.next();
              if(s1.dominates(s)) {
                return true;
            }
     }
     return false;
   }

    @Override
   public boolean dominatesAtLeastOne (ArchiveSolutions nda){
       Iterator<Solution> iterNDA = nda.iterator();
       //when it is not dominated
       while(iterNDA.hasNext()){
           Solution s = iterNDA.next();

           if(dominatesAtLeastOne(s)) {
                return true;
            }
       }
       return false;
    }



   //true -- avl is dominated by s
   // false -- avl is not dominated by s
    @Override
   public boolean isDominated(Solution s){
     if(avl.isEmpty()) {
            return false;
        }

     boolean domin = false;
     boolean isDomin = false;
     Iterator<Solution> iterNDA = iterator();
       //when it is not dominated
       while(iterNDA.hasNext()){
           Solution s1 = iterNDA.next();
           if(s1.dominates(s)) {
                domin = true;
            }
           if(s.dominates(s1)) {
                isDomin = true;
            }
       }
       return !domin && isDomin;
   }

    @Override
   public boolean isDominated (ArchiveSolutions nda){
       Iterator<Solution> iterNDA = nda.iterator();
       //when it is not dominated
       boolean domin = false;
       boolean isDomin = false;
       while(iterNDA.hasNext()){
           Solution s = iterNDA.next();

           if(dominates(s)) {
                domin = true;
            }
           if(s.dominates(this)) {
                isDomin = true;
            }
       }
       return !domin && isDomin;
    }

   /////////////////////////////
   // remove a solution s that is found as beening sominated
   ////////////////////////////
    @Override
   public void remove(Solution s){
        s.setFlagOperation(3);

        if(!this.contains(s)){
            System.err.println("why it is not contained ?" + s.toString() + " into " + avl.toString());
            contains(s);
            return;
        }
        Long identif = generateIdentif(s);
        Solution remove = avl.remove(identif);
        if(remove == null){
            System.out.println(" Remove inexistent solution !" + s.toString());
            System.out.println(" Existent solutions "+ avl.toString());
        }
        //for(int i = 0; i < this.nrObjectives; i++)
        //    objectives[i].remove(s.objectives[i]);
        //add in the other list
    }


    //check the consistency of the tree
    @Override
    public void checkConsistency(Solution s){
        if(avl== null || avl.size() == 0) {
            return;
        }

        System.out.println("Check avl consistency absolite ");
        //first case of inconsistency: firstObjective and secondObjective have different size
        /*if(objectives[0].size() != avl.size()){
            //rebuid first and second obje
           System.err.println("Before " + avl.size() + ", f = " + objectives[0].size() );
           System.err.println("Avl" + avl.toString());
           for(int i = 0; i < this.nrObjectives; i++){
                System.err.println("obj " + i+ " " + objectives[i].toString());
                objectives[i].clear();
           }
           Iterator<Map.Entry<Long,Solution>> newI = avl.entrySet().iterator();
           while(newI.hasNext()){
                Solution sol = newI.next().getValue();
                for(int i = 0; i < this.nrObjectives; i++)
                    objectives[i].put(sol.objectives[i], sol.getIdentifNumber());
            }
            //sysncronize
            System.err.println("Sysncronized " + avl.size() + ", f = " + objectives[0].size());
            return;
        }

        //2. first and second objective have different identification values
        Iterator<Map.Entry<Long,Long>> m = entrySet().iterator();
        while(m.hasNext()){
            Map.Entry<Long,Long> tempL = m.next();
            if(!objectives[0].containsValue(tempL.getValue())){
                System.err.println("Inconsistency in the containt of the first and seconf objective");
                return;
            }
        }

        //3. avl has different objectes than it should
        m = objectives[0].entrySet().iterator();
        while(m.hasNext()){
            Map.Entry<Long,Long> tempL = m.next();
            if(!avl.containsKey(tempL.getValue().longValue())){
                System.err.println("Inconsistency in the containt of the first and seconf objective");
                return;
            } else {
                Solution s1 = avl.get(tempL.getValue().longValue());
                if(s1.objectives[0] != tempL.getKey().doubleValue()){
                    System.err.println("Inconsistency in the containt of the first and seconf objective");
                    return;
                }
                    
            }
         }*/
    }

    //constructs a new archive that is not dominated
        //optimize
    @Override public ArchiveSolutions  clone(){
        NonDominatedArchive newNDA = new NonDominatedArchive();
        Solution tempS;
        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
        while(i.hasNext()){
            
            tempS = i.next().getValue().clone();
            newNDA.add(tempS);                
        }
            //newNDA.setEpsilon();
        return newNDA;
    }
        
        //constructs two new archive: that is not dominated [0]
        //and [1] that is dominating
    @Override
    public ArchiveSolutions[] getNonAndDominated(Solution s){
            NonDominatedArchive[] newNDA = new NonDominatedArchive[3]; // dominated
            newNDA[0] = new NonDominatedArchive(); // incomparable solutions
            newNDA[1] = new NonDominatedArchive(); // dominating
            newNDA[2] = new NonDominatedArchive();
            
            //newNDA[0].add(s);
            Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
            while(i.hasNext()){
                Solution tempS = i.next().getValue().clone();
                if(tempS.dominates(s)) {
                    newNDA[1].add(tempS);
                } else if(!s.dominates(tempS)){
                    newNDA[0].add(tempS);
                }
                if(!s.dominates(tempS) && !tempS.dominates(s)){
                    newNDA[2].add(tempS);
                }
            }            
            return newNDA;
        }
	
    @Override
    public Stack<Solution> getDominated(Solution s){
        Stack<Solution> tempSol = new Stack<>();
        
        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
        while(i.hasNext()){
            Solution tempS = i.next().getValue().clone();
            if(!s.dominates(tempS)){
                tempSol.push(tempS);
            }
        }            
        return tempSol;
    }
	
    @Override
    public ArchiveSolutions[] getNonAndDominated(ArchiveSolutions s){
            NonDominatedArchive[] newNDA = new NonDominatedArchive[3]; // dominated
            newNDA[0] = new NonDominatedArchive(); // incomparable solutions
            newNDA[1] = new NonDominatedArchive(); // dominating
            newNDA[2] = new NonDominatedArchive();

            Iterator<Solution> iter = s.iterator();
            while(iter.hasNext()){
                Solution tempS = iter.next();
                ArchiveSolutions[] tempNDA = getNonAndDominated(tempS);
                newNDA[0].add(tempNDA[0]);
                newNDA[1].add(tempNDA[1]);
                newNDA[2].add(tempNDA[2]);
            }
            return newNDA;
    }

    @Override
    public ArchiveSolutions overlap(ArchiveSolutions  nda){
        ArchiveSolutions tempA = new NonDominatedArchive();
        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
	while(i.hasNext()){
            Solution v = i.next().getValue();
            if(nda.contains(v)){
                tempA.add(v);
            }
	}
	return tempA;
    }

    @Override
    public ArchiveSolutions overlapInValue(ArchiveSolutions  nda){
        ArchiveSolutions tempA = new NonDominatedArchive();
        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
	while(i.hasNext()){
            Solution v = i.next().getValue();
            if(nda.contains(v)){
                tempA.add(v);
            }
            if(!nda.dominates(v) && !v.dominates(nda)){
                tempA.add(v);
            }
	}
	return tempA;
    }

    @Override
    public TreeMap<Long,Solution> getNDA(){
        return avl;
    }

    @Override
    public void setNDA(ArchiveSolutions nda){
        avl = nda.getNDA();
        dominatedList.clear();
    }
    
    @Override public String toString(){
            StringBuilder sb = new StringBuilder();
            Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
            while(i.hasNext()){
                //sb.append("Component individual");
                Solution sol = i.next().getValue();
                for(int j = 0 ; j < sol.objectives.length; j++){
                    sb.append(sol.objectives[j]).append(" ");
                }
                sb.append("\n");
            }
            return sb.toString();
    }

    @Override
    public String ndaToString(){
            StringBuilder sb = new StringBuilder();
            Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
            while(i.hasNext()){
                //sb.append("Component individual");
                Solution sol = i.next().getValue();
                for(int j = 0 ; j < sol.objectives.length; j++){
                    sb.append(sol.objectives[j]).append(" ");
                }
                for(int j = 0 ; j < sol.items.length; j++){
                    sb.append(sol.items[j]).append(" ");
                }
                sb.append("\n");
            }
            return sb.toString();

    }

    @Override
    public Solution[] ndaToArray(){
        Solution[] temp = new Solution[size()];
        int count = 0;
        Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
        while(i.hasNext()){
                //sb.append("Component individual");
                temp[count] = i.next().getValue();
                count++;
        }
            
        return temp;
    }

    @Override
    public void reset(){
            avl.clear();
            
            // in case of scalarization
            if(solutions != null){
                for(int i = 0; i < solutions.size(); i++){
                    solutions.set(i,null);
                }
            }
            
            //in case of reduction
            
            //for(int i = 0; i < this.nrObjectives; i++)
            //    objectives[i].clear();
            //dominatedList.clear();
        }
        
    @Override
    public Iterator<Solution> iterator(){
            Iterator<Map.Entry<Long,Solution>> i = avl.entrySet().iterator();
            Stack<Solution> v = new Stack<>();
            while(i.hasNext()){
                v.add(i.next().getValue());
            }
            return v.iterator();
        }
        
    @Override
    public int size(){
            return avl.size();
        }

        ////////////////////////////
        // set characteristics
    @Override
    public void setTypeProblem(boolean minimization){
            this.minimization = minimization;
        }

    //////////////////////////
    // the memory part of this
    // the solutions that are now in NDA are included in dominatedListSolutions
    //////////////////////////////
    @Override
    public void update(ArchiveSolutions newNDA){
        if(this.sizeDominatedList == 0 || memory == false)
            return;

        Iterator<Solution> iter = newNDA.iterator();
        while(iter.hasNext()){
            Solution s = iter.next();
            if(dominatedList.contains(s))
                continue;
            while(sizeDominatedList <= this.dominatedList.size()){
                // remore the first vector which is also the last one
                dominatedList.remove(0);
                //dominatedList_Times.remove(0);
            }
            dominatedList.add(s);
            //this.dominatedList_Times(time);
        }
    }

    @Override
    public int sizeDominated(){
        return dominatedList.size();
    }

    @Override
   public int typeArchive(){
       return 0;
   }
   /* @Override public void setEpsilon(PerturbatorStrategies epsilon){
        this.epsilon = epsilon;
    }

    @Override public PerturbatorStrategies getEpsilon(){
        return epsilon;
    }*/
   
   //////////////////////////////
   // scalarization
   /////////////////////////////////
      //////////////////////
    // scalarizer and their best solutions
    private Stack<Solution> solutions = new Stack<>();
    private SetScalarizer scal;

    //public SingleValuedArchive(){
    //}
    
    public NonDominatedArchive(SetScalarizer sc){
        //solutions.add(s1);
        scal = sc;
        if(solutions == null) {
            solutions = new Stack<>();
        }
        for(int i = 0; i < scal.size(); i++) {
            solutions.add(null);
        }
        //currentScal = sc.chooseScalarizer();
    }

    //private boolean tempR;

    @Override public boolean add(Solution s, Scalarizer sc){
        
        add(s);
        
        if(scal != null && scal.contains(sc)){
            int index = scal.indexOf(sc);
            Solution thisS = solutions.get(index);
            if(thisS == null || s.dominates(thisS,sc)){
                //solutions.remove(index);
                solutions.set(index, s);
                return true;
            }
            return false;
        }
        solutions.add(s);
        if(scal == null){
            //scal = new 
        }
        scal.add(sc);
        return true;
    }

    @Override
    public boolean addSolution(Scalarizer sc){
        if(scal != null && scal.contains(sc)){
            int index = scal.indexOf(sc);
            Solution thisS = solutions.get(index);
            if(thisS != null){
                add(thisS);
                return true;
            } 
        }
        return false;
    }
   
    @Override
    public boolean addSolInScal(Solution s, int indentif){
       Solution thisS = solutions.get(indentif);
       Scalarizer sc = this.scal.getScalarizer(indentif);
       if(s.dominates(thisS,sc)){
            //solutions.remove(indentif);
            solutions.set(indentif, s);
            return true;
       }
       return false;
   }

    @Override
   public boolean add(Scalarizer sc){
       if(!scal.contains(sc)){
            scal.add(sc);
            solutions.add(null);
            return true;
       }
       return false;
   }
   
    @Override
   public void setScalarizer(int indentif){
       //currentScal = indentif;
       scal.setScalarizer(indentif);
   }

    @Override
   public void setScalarizer(Scalarizer sc){
       if(scal.contains(sc)){
           scal.setScalarizer(sc);
           //currentScal = scal.getCurrentIndex();
       }
   }

   /* */
    @Override
   public Solution get(int indentif){
        return solutions.get(indentif);
   }

    @Override
   public Solution get(Scalarizer sc){
        int index = scal.indexOf(sc);
        if(index > -1) {
            return solutions.get(index);
        }
        return null;
   }

    @Override
   public boolean isDominated(Solution s, Scalarizer sc){
       if(solutions.isEmpty()) {
            return false;
        }
       int index = scal.indexOf(sc);
       if(index > -1) {
            return solutions.get(index).isDominated(s, sc);
        }
       return false;
   }

    @Override
   public boolean dominates (Solution s, Scalarizer sc){
       if(solutions.isEmpty()) {
            return false;
        }
       int index = scal.indexOf(sc);
       if(index > -1 && solutions.get(index) != null) {
            return solutions.get(index).dominates(s, sc);
        }
       return false;
   }

    @Override
   public boolean incomparable(Solution s, Scalarizer sc){
       if(solutions.isEmpty()) {
            return false;
        }
       int index = scal.indexOf(sc);
       if(index > -1) {
            return solutions.get(index).incomparable(s, sc);
        }
       return false;
  }


   //////////////////////////////
   // reduction
   /////////////////////////////////
      //////////////////////
    // scalarizer and their best solutions
    //private Stack<Solution> solutions = new Stack<Solution>();
    private SetReductions red;
    private TreeMap<Long,Solution>[] avlReduction;
    private boolean genetic = false;
    private Stack<double[][]> weightV = new Stack<>();
    public NonDominatedArchive(SetReductions sc, boolean gen){
        //solutions.add(s1);
        red = sc;
        avl.clear();
        int sizeR = red.size();
        avlReduction = new TreeMap[sizeR];
        
        for(int i = 0; i < sizeR; i++) {
            avlReduction[i] = new TreeMap<>();
        }
        
        genetic = gen;
        Reduction[] tempRed = red.toArray();
        //Stack<ReduceScalarizer> tempScal;
        for (Reduction tempRed1 : tempRed) {
            weightV.add(tempRed1.getListWeightVectors());            
        }
    }

    // add in a specific archive
    @Override
    public boolean containsSolInRed(Solution s, int index){
        s.setFlagOperation(2);
        
        //if(genetic){
        //    return contains(s);
        //}
        
        if(s.objectives == null || avlReduction[index] == null || avlReduction[index].isEmpty()) {
            return false;
        }

        Iterator<Map.Entry<Long,Solution>> i = avlReduction[index].entrySet().iterator();
 	while(i.hasNext()){
            Solution v = i.next().getValue();
            if(s.equals(v)){
                return true;
            }
	}
        return false;
    }
        
    ///////////////////////
    // scalarization is null
    Iterator<Map.Entry<Long,Solution>> interSolInRed;
    Stack<Long> removeIdentifSolInRed = new Stack<>();
    Map.Entry<Long,Solution> compSolInRed;
    @Override
    public boolean addSolInRed(Solution s, int index){
        s.setFlagOperation(4);

        //if(genetic){
        //    return add(s);
        //}
        
        if(!avlReduction[index].isEmpty() && containsSolInRed(s,index)) {
            return false;
        }

        // empty list; add and
        if(avlReduction[index].isEmpty()){
            nrObjectives = s.objectives.length;
            s.setFlagOperation(1);
            long identif = s.getIdentifNumber();
            avlReduction[index].put(identif,s);
            return true;
        }

        interSolInRed = avlReduction[index].entrySet().iterator();
        
        tempWeights = weightV.get(index);
        
        while(interSolInRed.hasNext()){
            compSolInRed = interSolInRed.next();
            Solution sol = compSolInRed.getValue();
            if(sol.dominatesReduction(s,tempWeights)) {
                return false;
            }
            if(s.dominatesReduction(sol,tempWeights)) {
                removeIdentifSolInRed.add(compSolInRed.getKey());
            }
       }
       while(!removeIdentifSolInRed.isEmpty()){
           avlReduction[index].remove(removeIdentifSolInRed.remove(0));
       }

        s.setFlagOperation(1);
        long identif = this.generateIdentif(s);
        while(avlReduction[index].containsKey(identif)){
            identif += r.nextLong();
        }
        avlReduction[index].put(identif,s);
        return true;
   }

    //private boolean tempR;
    /*@Override public boolean add(Solution s, Reduction sc){                
        if(genetic){
            return add(s);
        }
        
        // normal case; the rest exceptions
        if(red != null){ // && !red.genetic()){ //red.contains(sc)){
            int index = red.indexOf(sc);
            if(index < 0){
                System.err.println("This is a special case when reduction is not right");
                red.add(sc);
                addSolInRed(s, red.size());
                return true;
            }
            return addSolInRed(s, index);
        }
        
        //exceptions !!!
        System.err.println("This is a special case when reduction is not right");
        red.add(sc);
        addSolInRed(s, red.size());
        return true;
    }*/

    /////////////////////
    //add the solutions from a specific reduction 
    @Override
    public boolean addReduction(int index){ //Reduction sc){
        //if(genetic){
        //    return false;
        //}
        
        if(red != null){ // && red.contains(sc)){
            //int index = red.indexOf(sc);
            if(index < 0){
                return false;
            }
            interSolInRed = avlReduction[index].entrySet().iterator();
        
            while(interSolInRed.hasNext()){
                compSolInRed = interSolInRed.next();
                Solution sol = compSolInRed.getValue();
                add(sol);
            }
            
            avlReduction[index].clear();
            
            return true;
        }
        return false;
    }
    
    /*@Override
    public boolean addSolution(Reduction sc){
        return addReduction(sc);
    }
   
   @Override
   public boolean add(Reduction sc){      
       return addReduction(sc);
   }*/
   
    @Override
   public void setReduction(int indentif){
       //currentScal = indentif;
       red.setReduction(indentif);
   }

    @Override
   public void setReduction(Reduction sc){
       if(red.contains(sc)){
           red.setReduction(sc);
           //currentScal = scal.getCurrentIndex();
       }
   }

   //NonDominatedArchive newRedNDA = new NonDominatedArchive();
   Solution tempRed;
   Iterator<Map.Entry<Long,Solution>> iterator;
   @Override
   public ArchiveSolutions getReduction(Reduction sc){
       int index = 0;
       if(!genetic){
           index = red.indexOf(sc);
       } 
       if(index > -1) {
            NonDominatedArchive newRedNDA = new NonDominatedArchive();
            
            iterator = avlReduction[index].entrySet().iterator();
            while(iterator.hasNext()){
                tempRed = iterator.next().getValue(); //.clone();
                newRedNDA.add(tempRed);                
            }
            //newNDA.setEpsilon();
            return newRedNDA;
       }
       return null;
   }

       @Override
    public ArchiveSolutions getReduction(int index) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        NonDominatedArchive newRedNDA = new NonDominatedArchive();
            
        iterator = avlReduction[index].entrySet().iterator();
        while(iterator.hasNext()){
            tempRed = iterator.next().getValue(); //.clone();
            newRedNDA.add(tempRed);                
        }
            //newNDA.setEpsilon();
        return newRedNDA;    
    }

    double[][] tempWeights;
    @Override
    public boolean isDominatedReduction(Solution s, int index){
       if(index > -1) {
            if(avlReduction[index].isEmpty()) {
                return false;
            }
            
            tempWeights = weightV.get(index);
            
            boolean domin = false;
            boolean isDomin = false;
            iterator = avlReduction[index].entrySet().iterator(); //iterator();
            //when it is not dominated
            while(iterator.hasNext()){
                Solution s1 = iterator.next().getValue();
                if(s1.dominatesReduction(s,tempWeights)) {
                    domin = true;
                }
                if(s.dominatesReduction(s1, tempWeights)) {
                    isDomin = true;
                }
            }
            return !domin && isDomin;
       }
       return false;
   }

    @Override
   public boolean dominatesReduction (Solution s, int index){
       //if(genetic){
       //    return dominates(s);
       //}

       //int index = red.indexOf(sc);
       if(index > -1){
            if(avlReduction[index].isEmpty()) {
                return false;
            }
     
            tempWeights = weightV.get(index);
            
            boolean domin = false;
            boolean isDomin = false;
            //SortedMap<Long,Long> n1 = firstObjective.headMap(new Long(s.objectives[0]));
            iterator = avlReduction[index].entrySet().iterator();
            while(iterator.hasNext()){
                Solution s1 = iterator.next().getValue();
                if(s1.dominatesReduction(s, tempWeights)) {
                    domin = true;
                }
                if(s.dominatesReduction(s1, tempWeights)) {
                    isDomin = true;
                }
            }
            return domin && !isDomin;
       }
       return false;
   }

    @Override
   public boolean incomparableReduction(Solution s, int index){

       //int index = red.indexOf(sc);
       if(index > -1) {
            if(this.avlReduction[index].isEmpty()) {
                return false;
            }
            
            tempWeights = weightV.get(index);
            
            boolean domin = false;
            boolean isDomin = false;
            //SortedMap<Long,Long> n1 = firstObjective.headMap(new Long(s.objectives[0]));
            iterator = avlReduction[index].entrySet().iterator();
            while(iterator.hasNext()){
                Solution s1 = iterator.next().getValue();
                if(s1.dominatesReduction(s, tempWeights)) {
                    domin = true;
                }
                if(s.dominatesReduction(s1, tempWeights)) {
                    isDomin = true;
                }
            }
            return domin && isDomin;
        }
       return false;
  }

   @Override
   public Solution getRandomSolutionInRed(int indexR){
       //vector with not visited solutioatedns
       Stack<Solution> notVisited = new Stack<>();
       Iterator<Map.Entry<Long,Solution>> newI = avlReduction[indexR].entrySet().iterator();
       for(int counter = 0; counter < avlReduction[indexR].size(); counter++){
             Solution tempS = newI.next().getValue();
             if(!tempS.flagVisited)
                 notVisited.add(tempS);
       }
       if(notVisited.isEmpty())
           return null;
       // randominzed part
       int sizeT = notVisited.size();
       double rTemp = r.nextDouble();
       int t = (int)Math.round(rTemp*sizeT);
       if(t == sizeT)
           t--;
       return notVisited.get(t);
    }


}
