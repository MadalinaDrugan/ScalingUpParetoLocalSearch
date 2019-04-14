/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PLS.LocalSearch;

import Performance.ImprovNDA;
import general.*;
import general.Scalarization.Scalarizer;
import java.util.HashMap;
/**
 *
 * @author madalina
 */
public class One_PLS implements ImplementPLS{

    private final static long serialVersionUID = 11239585071342354L;//10795; //
    //private final static java.util.Random r = new java.util.Random();
    

    private Neighbours neigh;
    private IteratorSolutions iter;

    private Solution s1;
    private Solution p;

    ////////////////////////
    // memory of the local search
    public boolean usedMemory = false;

    private HashMap<Long,Counter> tableSolVisited = new HashMap<>();
    private HashMap<Long,Counter> tableSolExpand = new HashMap<>();    
    private HashMap<Long,Object[]> tableSolValue = new HashMap<>();    

    int level = 0;
    int size = 0;
    double sizeNDA = 0;
    double varSizeNDA = 0;

    //scalarization
    //private Scalarizer scal;
    //private Reduction red;
    
    public One_PLS(){}

    @Override public void setVariator(Neighbours n, IteratorSolutions it){
        neigh = n;
        iter = it;
     }

    /////////////////////
    // include also a adaptive operator for chosing an iterator
    //////////////////////////
    ArchiveSolutions tempS;
    @Override public ArchiveSolutions localSearch(ArchiveSolutions tempA, Variator v, Scalarizer scal){
        
        level = 0;
        long tempNDA = 0;
        long varTempNDA = 0;

        iter.restart();
        
        while(true){
            if(v.getNumberOfSwaps() > v.getMaxNumberOfSwaps()){
                break;
            }
                    
            s1 = tempA.getRandomNotVisitedSolution();
            if(s1 == null) {
                break;
            }
            Long temp = s1.getIdentifNumber();
            
            // used memory
            if(this.usedMemory){
                if(tableSolExpand.containsKey(temp)){
                    tableSolExpand.get(temp).index++;
                }else{
                    Counter temps = new Counter();
                    temps.index=1;
                    tableSolExpand.put(temp, temps);
                    tableSolValue.put(temp, s1.objectives);
                }
            }

            // neighborhood exploration
            if(!usedMemory) {
                if(scal != null){
                    tempA.add(s1, scal);
                    neigh.neighbours(s1, tempA, v, iter,scal);
                } else {
                    neigh.neighbours(s1, tempA, v, iter);
                }
            }
            else {
                neigh.neighbours(s1, tempA, v, iter, usedMemory, tableSolVisited);
            }
               
            level++;
            tempNDA += neigh.getExpandSol();
            varTempNDA += Math.pow(neigh.getExpandSol(), 2.0);
        }

        if(level == 0) {
            return tempA;
        }
        
        v.addDeepLevel(level,tempNDA);
        size = tempA.size();

        sizeNDA = tempNDA/level;
        varSizeNDA = Math.sqrt(varTempNDA/level - Math.pow(sizeNDA,2));
        
        tempA.addSolution(scal);
        return tempA;
     }

     @Override public ArchiveSolutions localSearchReduction(ArchiveSolutions tempA, Variator v, int index){
        
        level = 0;
        long tempNDA = 0;
        long varTempNDA = 0;

        iter.restart();

        while(true){

            if(v.getNumberOfSwaps() > v.getMaxNumberOfSwaps()){
                break;
            }
                    
            s1 = tempA.getRandomSolutionInRed(index);
            if(s1 == null) {
                break;
            }

            Long temp = s1.getIdentifNumber();
            
            // used memory
            if(this.usedMemory){
                if(this.tableSolExpand.containsKey(temp)){
                    tableSolExpand.get(temp).index++;
                }else{
                    Counter temps = new Counter();
                    temps.index=1;
                    tableSolExpand.put(temp, temps);
                    tableSolValue.put(temp, s1.objectives);
                }
            }

            // neighborhood exploration
            if(!usedMemory) {
                //if(scal != null){
                    tempA.addSolInRed(s1, index);
                    neigh.neighboursReduction(s1, tempA, v, iter, index);
                //} else {
                //    neigh.neighbours(s1, tempA, v, iter);
                //}
            }
            else {
                neigh.neighbours(s1, tempA, v, iter, usedMemory, tableSolVisited);
            }

            level++;
            tempNDA += neigh.getExpandSol();
            varTempNDA += Math.pow(neigh.getExpandSol(), 2.0);

        }

        if(level == 0) {
            return tempA;
        }
        
        //if(scal!= null){
            tempA.addReduction(index);
        //}
        //tempA.add(tempNDA);
        
        v.addDeepLevel(level,tempNDA);
        size = tempA.size();

        sizeNDA = tempNDA/level;
        varSizeNDA = Math.sqrt(varTempNDA/level - Math.pow(sizeNDA,2));
        
        //tempA.addReduction(index);
        return tempA;
     }

    @Override public ArchiveSolutions localSearch(ArchiveSolutions tempA, Variator v){

        level = 0;
        long tempNDA = 0;
        long varTempNDA = 0;

        iter.restart();
        
        while(true){
            if(v.getNumberOfSwaps() > v.getMaxNumberOfSwaps()){
                break;
            }
                    
            s1 = tempA.getRandomNotVisitedSolution();
            if(s1 == null) {
                break;
            }
            Long temp = s1.getIdentifNumber();
            
            // used memory
            if(this.usedMemory){
                if(tableSolExpand.containsKey(temp)){
                    tableSolExpand.get(temp).index++;
                }else{
                    Counter temps = new Counter();
                    temps.index=1;
                    tableSolExpand.put(temp, temps);
                    tableSolValue.put(temp, s1.objectives);
                }
            }

            // neighborhood exploration
            if(!usedMemory) {
                neigh.neighbours(s1, tempA, v, iter);
            }
            else {
                neigh.neighbours(s1, tempA, v, iter, usedMemory, tableSolVisited);
            }
            
            level++;
            tempNDA += neigh.getExpandSol();
            varTempNDA += Math.pow(neigh.getExpandSol(), 2.0);
        }

        if(level == 0) {
            return tempA;
        }
        
        v.addDeepLevel(level,tempNDA);
        size = tempA.size();

        sizeNDA = tempNDA/level;
        varSizeNDA = Math.sqrt(varTempNDA/level - Math.pow(sizeNDA,2));
        
        return tempA;
     }

    private long getIdentif(Solution s1){
        return s1.getIdentifNumber();
    }
    
    @Override public ArchiveSolutions localSearch(ArchiveSolutions tempA){
        level = 0;
        long tempNDA = 0;
        long varTempNDA = 0;

        iter.restart();
        
        while(true){
            s1 = tempA.getRandomNotVisitedSolution();
            if(s1 == null) {
                break;
            }
            if(this.usedMemory){
                Long temp = getIdentif(s1);

                if(this.tableSolExpand.containsKey(temp)){
                    tableSolExpand.get(temp).index++;
                }else{
                    Counter temps = new Counter();
                    temps.index=1;
                    tableSolExpand.put(temp, temps);
                    tableSolValue.put(temp, s1.objectives);
                }
            }

            //tempS = tempA.clone();
            neigh.neighbours(s1, tempA, iter,usedMemory,tableSolVisited);
            level++;
            tempNDA += neigh.getExpandSol();
            varTempNDA += Math.pow(neigh.getExpandSol(), 2.0);
        }

        size = tempA.size();
        sizeNDA = tempNDA/level;
        varSizeNDA = Math.sqrt(varTempNDA/level - Math.pow(sizeNDA,2));

        return tempA;
     }

    //set and get structures
    public void setNeighbour(Neighbours n){
        neigh = n;
    }

    public Neighbours getNeighbour(){
        return neigh;
    }

    @Override
    public void reset(){
        if(usedMemory){
            tableSolExpand = new HashMap<>();
            tableSolVisited = new HashMap<>();
            tableSolValue = new HashMap<>();
        }
    }

    @Override
    public HashMap<Long,Counter> getVisited(){
        return this.tableSolVisited;
    }

    @Override
    public HashMap<Long,Counter> getExplored(){
        return this.tableSolExpand;
    }

    @Override
    public HashMap<Long,Object[]> getSolutions(){
        return this.tableSolValue;
    }
    
    @Override
    public int getLevel(){
        return level;
    }

    @Override
    public double getSizeNDA(){
        return this.sizeNDA;
    }

    @Override
    public int sizeNDA(){
        return size;
    }

    @Override
    public double getVarSizeNDA(){
        return varSizeNDA;
    }
    
}
