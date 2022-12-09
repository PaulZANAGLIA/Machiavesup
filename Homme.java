import java.util.ArrayList;
import java.util.Random;

public class Homme extends AbstractHuman<Femme> {
    public Homme(int id){
        this.id = id;
        this.indexe = 0;
        this.boundedTo = null;
    }

    /**  Man ask his better pref of woman, if she accepts, they are bounded, else nothing happen  */
    public Homme ask(Femme f){
        Homme evince = f.compare(this);
        if (evince == null || evince.getId() != this.getId()){
            this.set(f);
        }
        return evince;
    }
    public int isItBetter(int id) {
        return -1; // pas utilisé
    }
    @Override protected void generateRandomPrefList(int range, ArrayList<Femme> humans, int seed){
        ArrayList<Femme> pref = new ArrayList<>();
        Random rand = new Random(seed);
        int val;
        while(pref.size() < range){
            val = Math.abs(rand.nextInt()) % range;
            if(!pref.contains(humans.get(val))){
                pref.add(humans.get(val));
            }
        }

        this.setBothPrefList(pref);
    }

    @Override public void printPrefList(){
        System.out.print("[ ");
        for(Femme f : this.List_eff){
            System.out.printf("%d, ", f.getId());
        }
        System.out.print("]\n");
    }
}
