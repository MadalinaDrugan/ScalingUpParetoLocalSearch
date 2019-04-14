package Scalarization.Sets;

import general.Scalarization.*;
import java.util.*;


public class RandomScal implements SetScalarizer {

    Stack<Scalarizer> scalarizer;
    final static java.util.Random r = new java.util.Random();
    final long identificationNumber = r.nextLong();

    int currentScal;

    public RandomScal(Stack<Scalarizer> scal){
        scalarizer = scal;
        currentScal = r.nextInt(scal.size());
    }

    public RandomScal(){}

    //public abstract boolean adaptation(Solution[] s, long[] refPoint);
    @Override
    public void setScalarizer(Scalarizer scal){
        if(contains(scal)) {
            currentScal = indexOf(scal);
        }
        else {
            add(scal);
        }
    }

    @Override
    public void setScalarizer(int indentif){
        if(indentif < scalarizer.size()) {
            currentScal = indentif;
        }
    }

    @Override
    public Scalarizer getScalarizer(){
        //chooseScalarizer();
        return scalarizer.get(currentScal);
    }

    @Override
    public int getCurrentIndex(){
        return currentScal;
    }

    @Override
    public Scalarizer getScalarizer(int indentif){
        currentScal = indentif;
        return scalarizer.get(indentif);
    }

    @Override
    public int indexOf(Scalarizer sc){
        for(int i = 0; i < scalarizer.size(); i++){
            if(scalarizer.get(i).compareTo(sc) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Scalarizer sc){
        for(int i = 0; i < scalarizer.size(); i++){
            if(scalarizer.get(i).compareTo(sc) == 0) {
                return true;
            }
        }
        return false;
    }

    // add and remove scalarizers
    @Override
    public boolean add(Scalarizer scal){
        if(!contains(scal)){
            return scalarizer.add(scal);
        }
        return false;
    }

    @Override
    public Scalarizer remove(int indentif){
        if(scalarizer.size() > indentif){
            return scalarizer.remove(indentif);
        }
        return null;
    }

    @Override
    public void set(int indentif, Scalarizer scal){
        scalarizer.set(indentif, scal);
    }

    @Override
    public int chooseScalarizer(){
        double rand = r.nextDouble();
        currentScal = (int)Math.floor(rand*scalarizer.size());
        return currentScal;
    }

    @Override
    public boolean adaptation(){
        return false;
    }

    @Override
    public void restart(){
        currentScal = r.nextInt(scalarizer.size());
    }

    //public void choseScalarization(){}
    //
    @Override public String toString(){
	StringBuilder sb = new StringBuilder();
        for(int i = 0; i < scalarizer.size(); i++){
            sb.append(scalarizer.get(i).toString());
            sb.append("\n");
        }
	return sb.toString();
    }

    ///////////////////////////
    //////////////////////////////
    // scalarizer
    public int compareTo(Scalarizer o){
        if(o == this) {
            return 0;
        }
        for(int i = 0; i < scalarizer.size(); i++) {
            if(scalarizer.get(i).compareTo(o) == 0) {
                return 0;
            }
        }
        return 1;
    }

    public int compare(Scalarizer o, Scalarizer o1){
        if(o == o1) {
            return 0;
        }
        double[] scal = o.getWeights();
        double[] scal1 = o1.getWeights();
        for(int i = 0; i < scal.length; i++) {
            if(scal[i] != scal1[i]) {
                return 1;
            }
        }
        return 0;
    }

    @Override public int hashCode(){
        return (int) this.identificationNumber;
    }

    @Override public RandomScal clone(){
        RandomScal newS = new RandomScal(this.scalarizer);
        return newS;
    }

    @Override public boolean equals(Object o){
        if (this==o) {
            return true;
        }
        if(scalarizer.size() != ((RandomScal) o).scalarizer.size()) {
            return false;
        }
        for(int i=0;i<scalarizer.size();i++){
              boolean eq = false;
              for(int j = 0; j < scalarizer.size(); j++){
                if(scalarizer.get(i).equals(((RandomScal)o).scalarizer.get(i))){
                  eq = true;
                }
              }
              if(!eq) {
                return false;
            }
          }
          return true;
    }

    @Override
    public int size(){
        if(scalarizer != null) {
            return scalarizer.size();
        }
        return 0;
    }
    
    @Override
    public Scalarizer[] toArray(){
        Scalarizer[] tempS = new Scalarizer[scalarizer.size()];
        for(int i = 0; i < scalarizer.size(); i++){
            tempS[i] = scalarizer.get(i);
        }
        return tempS;
    }

    @Override
    public boolean genetic() {
        return false;
    }

    @Override
    public boolean solutionGen() {
        return false;
    }

}
