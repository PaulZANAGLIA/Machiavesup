import java.util.ArrayList;
import java.util.List;
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
    @Override public T getBounded(){
        return this.boundedTo;
    }

    /**  Set entity partner */
    @Override public void setBounded(Human t){
        this.boundedTo = (T) t;
    }

    /**  Set a list of prefs by giving an array of a specific entity */
    @Override public void setPrefList(List humans){
        this.default_pref_list = new ArrayList<>(humans);
        this.pref_list.addAll(default_pref_list);
    }

    @Override public T getCurrentPref(){
        return this.pref_list.get(this.index);
    }

    @Override public List<T> getPrefList(){
        return this.pref_list;
    }

    @Override public List<T> getDefaultPrefList(){
        return this.default_pref_list;
    }

    /** Shift to the right the index on pref list */
    @Override public void shiftPrefList(){
        if(this.index < this.default_pref_list.size())
            this.index ++;
    }

    @Override public void resetIndex(){
        this.index = 0;
    }

    @Override public void divorce(){
        this.setBounded(null);
    }

    @Override public void reset(){
        this.resetIndex();
        this.divorce();
    }

    /**  Generate randomly set of prefs */
    @Override public void generateRandomPrefList(int range, List humans, int SEED){
        System.out.println("VOICI LA SEED STEP" + SEED);
        ArrayList<T> pref = new ArrayList<>();
        Random rand = new Random(SEED);
        int val;
        while(pref.size() < Main.PLAYSET){
            val = Math.abs(rand.nextInt()) % range;
            if(!pref.contains(humans.get(val))){
                pref.add((T) humans.get(val));
            }
        }
        this.setPrefList(pref);
    }

    /**  Print entity prefs */
    public abstract void printPrefList();
}