/*
* @author Madalina Drugan: 2010-2018
*/
package Archives;

import general.Scalarization.*;
import general.*;
import java.util.*;

public class SingleValuedArchive extends NonDominatedArchive implements ArchiveScalSolutions{

    //////////////////////
    // scalarizer and their best solutions
    private Stack<Solution> solutions = new Stack<Solution>();
    private SetScalarizer scal;

    //private int currentScal;
    static Random r = new Random();
    //public PerturbatorStrategies epsilon;
    
    public SingleValuedArchive(NonDominatedArchive nda){
        add(nda);
    }

    //public SingleValuedArchive(){
    //}
    
    public SingleValuedArchive(SetScalarizer sc){
        //solutions.add(s1);
        scal = sc;
        if(solutions == null) {
            solutions = new Stack<Solution>();
        }
        for(int i = 0; i < scal.size(); i++) {
            solutions.add(null);
        }
        //currentScal = sc.chooseScalarizer();
    }

    private boolean tempR;

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
   
    public boolean add(Solution s, int indentif){
       Solution thisS = solutions.get(indentif);
       Scalarizer sc = this.scal.getScalarizer(indentif);
       if(s.dominates(thisS,sc)){
            //solutions.remove(indentif);
            solutions.set(indentif, s);
            return true;
       }
       return false;
   }

   public boolean add(Scalarizer sc){
       if(!scal.contains(sc)){
            scal.add(sc);
            solutions.add(null);
            return true;
       }
       return false;
   }
   
   public void setScalarizer(int indentif){
       //currentScal = indentif;
       scal.setScalarizer(indentif);
   }

   public void setScalarizer(Scalarizer sc){
       if(scal.contains(sc)){
           scal.setScalarizer(sc);
           //currentScal = scal.getCurrentIndex();
       }
   }

   /* */
   public Solution get(int indentif){
        return solutions.get(indentif);
   }

   public Solution get(Scalarizer sc){
        int index = scal.indexOf(sc);
        if(index > -1) {
            return solutions.get(index);
        }
        return null;
   }

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

   @Override public int typeArchive(){
        return 1;
    }

   //datas from super classs
   /*@Override public boolean add(Solution s){
        //currentScal = scal.getCurrentIndex();

        //tempR = add(s,currentScal);
        //if(tempR){
        return super.add(s);
            //solutions.set(currentScal, s);
        //}
        //return tempR;
    }

    @Override public void add(ArchiveSolutions newNDA){
        super.add(newNDA);
    }*/
    

}
