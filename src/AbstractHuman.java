import java.util.ArrayList;

public abstract class AbstractHuman<T> implements Human{
    protected int id;
    protected T boundedTo;
    protected ArrayList<T> prefList;

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
        this.prefList = humans;
    }
    public T getPrefList(){
        return this.prefList.get(0);
    }

    public ArrayList<T> getList(){
        return this.prefList;
    }
    public void removeFirstPrefList(){
        this.prefList.remove(0);
    }

    /**  Generate randomly set of prefs */
    protected abstract void generateRandomPrefList(int range, ArrayList<T> humans);

    /**  Print entity prefs */
    @Override public abstract void printPrefList();
}
