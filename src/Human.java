import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Human<T extends Human<?>> implements Entity<T> {
    protected int id;
    protected int index;
    protected T boundedTo;
    protected ArrayList<T> default_pref_list;
    protected ArrayList<T> pref_list;

    /**  Get id of entity */
    public int getId(){
        return this.id;
    }

    /**  Get the entity partner */
    public T getBounded(){
        return this.boundedTo;
    }

    /**  Set entity partner */
    public void setBounded(T t){
        this.boundedTo = t;
    }

    /**  Set a list of prefs by giving an array of a specific entity */
    public void setPrefList(List<T> humans){
        this.default_pref_list = new ArrayList<>(humans);
        this.pref_list.addAll(default_pref_list);
    }

    public T getCurrentPref(){
        return this.pref_list.get(this.index);
    }

    public List<T> getPrefList(){
        return this.pref_list;
    }

    public List<T> getDefaultPrefList(){
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
    public List<T> generateRandomPrefList(int range, List<T> humans, int SEED){
        ArrayList<T> pref = new ArrayList<>(humans);
        Collections.shuffle(pref);
        return pref;
    }



    /**  When a woman receive a proposition, she looks if the proposer is a better partner for her.
     * If so, replace her bounded partner to the new one */
    public abstract T compare(T t);

    /**  Man ask his pref woman, if she accepts, they are bounded, else nothing happen  */
    public abstract Human<T> ask(T t);

    /**  Print entity prefs */
    public void printPrefList() {
        System.out.print("[ ");
        for(T human : this.getPrefList()){
            System.out.printf("%d, ", human.getId());
        }
        System.out.print("]\n");
    }


    /** Connaitre la position d'un homme ou d'une femme par son ID dans une liste */
    public static <K extends Human<?>> int getPosition(List<K> humans, int humanId){
        int i = 0;
        while(humans.get(i).getId() != humanId)
            i++;
        return i;
    }

    /** Récupérer un objet (Femme ou Homme) par son ID dans une liste */
    public static <K extends Human<?>> K retrieveHuman(List<K> humans, int x){
        for(K h : humans) {
            if (h.getId() == x)
                return h;
        }
        return null;
    }

    /** Generate random PrefList for each  */
    public static <K extends Human<F>, F extends Human<K>> void generateRandomPrefLists(List<K> humans, List<F> humans2, int PLAYSET, int SEED){
        final AtomicInteger step = new AtomicInteger(SEED);
        humans.forEach((human) -> human.setPrefList(human.generateRandomPrefList(PLAYSET, humans2, step.getAndIncrement())));
    }
}