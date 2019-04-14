/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Scalarization;

import PLS.LocalSearch.One_PLS;
import PLS.LocalSearch.*;
import general.*;
import general.Scalarization.*;
import java.util.HashMap;
/**
 *
 * @author madalina
 */
public class Scalarized_PLS extends One_PLS implements ImplementScal_PLS{

    private final static long serialVersionUID = 11239585071342354L;//10795; //
    private final static java.util.Random r = new java.util.Random();
    
    private Neighbours neigh;
    private IteratorSolutions iter;

    private Solution s1;
    //private Solution p;

    ////////////////////////
    // memory of the local search
    public boolean usedMemory = false;

    private HashMap<Long,Counter> tableSolVisited = new HashMap<>();
    private HashMap<Long,Counter> tableSolExpand = new HashMap<>();

    private int level = 0;
    private int size = 0;
    private double sizeNDA = 0;
    private double varSizeNDA = 0;

    private Scalarizer scal;
    
    public Scalarized_PLS(){}

    @Override public void setVariator(Neighbours neigh, IteratorSolutions it){
        this.neigh = neigh;
        iter = it;
     }

    //@Override public void setVariator(Neighbours neigh, IteratorSolutions[] it){
    //    this.neigh = neigh;
    //    iter = it;
    // }

    //@Override
    //public void setScalarizer(Scalarizer s){
    //    this.scal = s;
    //}
    
    
    @Override public ArchiveSolutions localSearch(ArchiveSolutions tempA, Variator v, Scalarizer scal){
        //this.newNDA = tempA.clone();
        //
        ArchiveSolutions tempS;
        level = 0;
        long tempNDA = 0;
        long varTempNDA = 0;

        while(true){
            s1 = tempA.getRandomNotVisitedSolution();
            if(s1 == null) {
                break;
            }

            long temp = s1.getIdentifNumber();
            // used memory
            if(this.usedMemory){
                if(this.tableSolExpand.containsKey(temp)){
                    tableSolExpand.get(temp).index++;
                }else{
                    Counter temps = new Counter();
                    temps.index=1;
                    tableSolExpand.put(temp, temps);
                }
            }

            // neighborhood exploration
            //tempS = tempA.clone();
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
                //tempA.add(neigh.neighbours(s1, tempS, v, iter,usedMemory,tableSolVisited));
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

    @Override public ArchiveSolutions localSearch(ArchiveSolutions tempA, Variator v){
        //this.newNDA = tempA.clone();
        //
        ArchiveSolutions tempS;
        level = 0;
        long tempNDA = 0;
        long varTempNDA = 0;

        while(true){
            s1 = tempA.getRandomNotVisitedSolution();
            if(s1 == null) {
                break;
            }

            long temp = s1.getIdentifNumber();
            // used memory
            if(this.usedMemory){
                if(this.tableSolExpand.containsKey(temp)){
                    tableSolExpand.get(temp).index++;
                }else{
                    Counter temps = new Counter();
                    temps.index=1;
                    tableSolExpand.put(temp, temps);
                }
            }

            // neighborhood exploration
            //tempS = tempA.clone();
            if(!usedMemory) {
                neigh.neighbours(s1, tempA, v, iter);
            }
            else {
                neigh.neighbours(s1, tempA, v, iter, usedMemory, tableSolVisited);
            }
                //tempA.add(neigh.neighbours(s1, tempS, v, iter,usedMemory,tableSolVisited));
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
        Long temp = (long)0;
        for(int i = 0; i < s1.objectives.length; i++) {
            
            temp +=(long) ((Double)s1.objectives[0]*Math.pow(10.0, i));
        }
        return temp;
    }
    
    @Override public ArchiveSolutions localSearch(ArchiveSolutions tempA){
        //this.newNDA = tempA.clone();
        //
        ArchiveSolutions tempS;
        level = 0;
        long tempNDA = 0;
        long varTempNDA = 0;

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
    @Override
    public void setNeighbour(Neighbours n){
        neigh = n;
    }

    @Override
    public Neighbours getNeighbour(){
        return neigh;
    }

    @Override
    public void reset(){
        if(usedMemory){
            tableSolExpand = new HashMap<>();
            tableSolVisited = new HashMap<>();
        }
    }

    @Override
    public HashMap<Long,Counter> getVisited(){
        return tableSolVisited;
    }

    @Override
    public HashMap<Long,Counter> getExplored(){
        return tableSolExpand;
    }

    @Override
    public int getLevel(){
        return level;
    }

    @Override
    public double getSizeNDA(){
        return sizeNDA;
    }

    @Override
    public double getVarSizeNDA(){
        return varSizeNDA;
    }
    
    @Override
    public int sizeNDA(){
        return size;
    }
}
