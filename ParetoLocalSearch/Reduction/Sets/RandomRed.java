package Reduction.Sets;

import Reduction.ReduceScalarizer;
import general.Reduction.Reduction;
import general.Reduction.SetReductions;
import java.util.Stack;


public class RandomRed implements SetReductions {

    Stack<Reduction> reduction = new Stack<>();
    final static java.util.Random r = new java.util.Random();
    final long identificationNumber = r.nextLong();

    int currentScal;

    public RandomRed(Stack<Reduction> scal){
        for(int i = 0; i < scal.size(); i++){
            reduction.add(scal.get(i));
        }
        currentScal = r.nextInt(scal.size());
    }

    /*public RandomRed(Stack<CollectScalarizers> s){
        reduction = scal;
        currentScal = r.nextInt(scal.size());
    }*/

    public RandomRed(){}

    //public abstract boolean adaptation(Solution[] s, long[] refPoint);
    @Override
    public void setReduction(Reduction scal){
        if(contains(scal)) {
            currentScal = indexOf(scal);
        }
        else {
            add(scal);
        }
    }

    @Override
    public void setReduction(int indentif){
        if(indentif < reduction.size()) {
            currentScal = indentif;
        }
    }

    @Override
    public Reduction getReduction(){
        currentScal = r.nextInt(reduction.size());
        return reduction.get(currentScal);
    }

    @Override
    public int getCurrentIndex(){
        return currentScal;
    }

    @Override
    public Reduction getReduction(int indentif){
        currentScal = indentif;
        return reduction.get(indentif);
    }

    @Override
    public int indexOf(Reduction sc){
        for(int i = 0; i < reduction.size(); i++){
            if(reduction.get(i).compareTo(sc) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Reduction sc){
        for(int i = 0; i < reduction.size(); i++){
            if(reduction.get(i).compareTo(sc) == 0) {
                return true;
            }
        }
        return false;
    }

    // add and remove scalarizers
    @Override
    public boolean add(Reduction scal){
        if(!contains(scal)){
            return reduction.add(scal);
        }
        return false;
    }

    @Override
    public Reduction remove(int indentif){
        if(reduction.size() > indentif){
            return reduction.remove(indentif);
        }
        return null;
    }

    @Override
    public void set(int indentif, Reduction scal){
        reduction.set(indentif, scal);
    }

    @Override
    public int chooseReduction(){
        double rand = r.nextDouble();
        currentScal = (int)Math.floor(rand*reduction.size());
        return currentScal;
    }

    @Override
    public boolean adaptation(){
        return false;
    }

    @Override
    public void restart(){
        currentScal = r.nextInt(reduction.size());
    }

    //public void choseScalarization(){}
    //
    @Override public String toString(){
	StringBuilder sb = new StringBuilder();
        for(int i = 0; i < reduction.size(); i++){
            sb.append(reduction.get(i).toString());
            sb.append("\n");
        }
	return sb.toString();
    }

    ///////////////////////////
    //////////////////////////////
    // scalarizer
    public int compareTo(Reduction o){
        if(o == this) {
            return 0;
        }
        for(int i = 0; i < reduction.size(); i++) {
            if(reduction.get(i).compareTo(o) == 0) {
                return 0;
            }
        }
        return 1;
    }

    public int compare(Reduction o, Reduction o1){
        if(o == o1) {
            return 0;
        }
        Stack<ReduceScalarizer> scal = o.getReductions();
        Stack<ReduceScalarizer> scal1 = o1.getReductions();
        for(int i = 0; i < scal.size(); i++) {
            if(!scal.elementAt(i).equals(scal1.elementAt(i))) {
                return 1;
            }
        }
        return 0;
    }

    @Override public int hashCode(){
        return (int) this.identificationNumber;
    }

    @Override public RandomRed clone(){
        RandomRed newS = new RandomRed(this.reduction);
        return newS;
    }

    @Override public boolean equals(Object o){
        if (this==o) {
            return true;
        }
        if(reduction.size() != ((RandomRed) o).reduction.size()) {
            return false;
        }
        for(int i=0;i<reduction.size();i++){
              boolean eq = false;
              for(int j = 0; j < reduction.size(); j++){
                if(reduction.get(i).equals(((RandomRed)o).reduction.get(i))){
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
        return reduction.size();
    }
    
    @Override
    public Reduction[] toArray(){
        Reduction[] tempS = new Reduction[reduction.size()];
        for(int i = 0; i < reduction.size(); i++){
            tempS[i] = reduction.get(i);
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
