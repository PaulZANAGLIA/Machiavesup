import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractHuman<T> implements Human{
    protected int id;
    protected T boundedTo;
    protected ArrayList<T> default_pref_list;
    protected ArrayList<T> pref_list;
    protected int index;

    /**  Get id of entity */
    @Override public int getId(){
        return this.id;
    }

    /**  Get the entity partner */
    public T getBounded(){
        return this.boundedTo;
    }

    /**  Set entity partner */
    protected void setBounded(T t){
        this.boundedTo = t;
    }

    /**  Set a list of prefs by giving an array of a specific entity */
    public void setPrefList(ArrayList<T> humans){
        this.default_pref_list = new ArrayList<>(humans);
        this.pref_list.addAll(default_pref_list);
    }

    public T getCurrentPref(){
        return this.pref_list.get(this.index);
    }

    public ArrayList<T> getPrefList(){
        return this.pref_list;
    }

    public ArrayList<T> getDefaultPrefList(){
        return this.default_pref_list;
    }

    /** Shift to the right the index on pref list */
    public void shiftPrefList(){
        if(this.index < this.default_pref_list.size())
            this.index ++;
    }

    public void resetIndex(){
        this.index = 0;
    }

    public void divorce(){
        this.setBounded(null);
    }

    public void reset(){
        this.resetIndex();
        this.divorce();
    }

    /**  Generate randomly set of prefs */
    public void generateRandomPrefList(int range, ArrayList<T> humans, int SEED){
        ArrayList<T> pref = new ArrayList<>();
        Random rand = new Random(SEED);
        int val;
        while(pref.size() < Main.PLAYSET){
            val = Math.abs(rand.nextInt()) % range;
            if(!pref.contains(humans.get(val))){
                pref.add(humans.get(val));
            }
        }
        this.setPrefList(pref);
    }

    /**  Print entity prefs */
    @Override public abstract void printPrefList();
}