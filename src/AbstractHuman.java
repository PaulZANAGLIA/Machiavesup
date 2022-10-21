import java.util.ArrayList;

public abstract class AbstractHuman<T> implements Human{
    protected int id;
    protected T boundedTo;
    protected ArrayList<T> pref_list;

    /**  Get id of entity */
    @Override public int getId(){
        return this.id;
    }

    /**  Get the entity partner */
    public T getBounded(){
        return this.boundedTo;
    }

    /**  Set entity partner */
    protected void set(T t){
        this.boundedTo = t;
    }

    /**  Set a list of prefs by giving an array of a specific entity */
    public void setPrefList(ArrayList<T> humans){
        this.pref_list = humans;
    }
    public T getPrefList(){
        return this.pref_list.get(0);
    }

    public ArrayList<T> getList(){
        return this.pref_list;
    }
    public void removeFirstPrefList(){
        this.pref_list.remove(0);
    }

    /**  Generate randomly set of prefs */
    protected abstract void generateRandomPrefList(int range, ArrayList<T> humans);

    /**  Print entity prefs */
    @Override public abstract void printPrefList();
}
