import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class AbstractHuman<T> implements Human{
    protected int id;
    protected T boundedTo;
    protected int indiceBoundedTo;
    protected int indexe;
    protected ArrayList<T> List_eff;
    protected ArrayList<T> List_de_base;
    public ArrayList<T> getList_de_base(){return this.List_de_base;}
    /**  Get id of entity */
    @Override public int getId(){
        return this.id;
    }
    public void clear()
    {
        this.boundedTo = null;
        this.indexe = 0;
    }
    /**  Get the entity partner */
    public T getBounded(){
        return this.boundedTo;
    }

    /**  Set entity partner */
    protected void set(T t){
        this.boundedTo = t;
        //this.indiceBoundedTo=indexe; // plus facile à comprendre, mais completement inutile.
    }
    protected  ArrayList<T> getList(){return this.List_de_base;}
    protected  ArrayList<T> getListEff(){return this.List_eff;}
    /**  Set a list of prefs by giving an array of a specific entity */
    public void setBothPrefList(ArrayList<T> humans){
        this.List_eff = new ArrayList<T>(humans);
        this.List_de_base=new ArrayList<T>(humans);
    }
    public void setEffPrefList(ArrayList<T> humans){
        this.List_eff = new ArrayList<T>(humans);
      //  this.List_de_base=new ArrayList<T>(humans);
    }
    public T getFirstList(){
        return this.List_eff.get(indexe);
    }
    public void removeFirstPrefList(){
        this.indexe++;
    }
    public abstract int isItBetter(int id_sans_triche);



    /**  Generate randomly set of prefs */
    protected abstract void generateRandomPrefList(int range, ArrayList<T> humans,int seed);

    /**  Print entity prefs */
    @Override public abstract void printPrefList();

}
