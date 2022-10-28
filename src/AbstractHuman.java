import java.util.ArrayList;

public abstract class AbstractHuman<T> implements Human{
    protected int id;
    protected int index;
    protected T boundedTo;
    protected ArrayList<T> default_list;
    protected ArrayList<T> pref_list;

    /**  Get id of entity */
    @Override
    public int getId(){
        return this.id;
    }

    /**  Print entity prefs */
    @Override
    public abstract void printPrefList();

    /**  Get the entity partner */
    public T getBounded(){
        return this.boundedTo;
    }

    /**  Set entity partner */
    protected void setBounded(T t){
        this.boundedTo = t;
    }

    /**  Set a list of prefs by giving a list of a specific entity */
    public void setPrefList(ArrayList<T> humans){
        this.pref_list = new ArrayList<>(humans);
        this.default_list = new ArrayList<>(humans);
    }

    /** Premier element de la liste de pref */
    public T getFirstPrefList(){
        return this.pref_list.get(index);   // premier element de la liste de pref
    }

    /** Liste de preference */
    public ArrayList<T> getPrefList(){
        return this.pref_list;
    }

    /** Passer a l'element suivant dans la prefList */
    public void removePrefList(){
        this.index++;
    }

    /**  Generate randomly set of prefs */
    protected abstract void generateRandomPrefList(int range, ArrayList<T> humans, int seed);

    /** Remets l'objet a 0 */
    public void clear(){
        this.boundedTo = null;
        this.index = 0;
    }


}
