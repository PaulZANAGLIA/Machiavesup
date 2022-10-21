import java.util.ArrayList;
import java.util.Random;

public class Homme extends AbstractHuman<Femme> {
    public Homme(int id){
        this.id = id;
        this.pref_list = new ArrayList<>();
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

    @Override protected void generateRandomPrefList(int range, ArrayList<Femme> humans){
        ArrayList<Femme> pref = new ArrayList<>();
        Random rand = new Random();
        int val;
        while(pref.size() < Main.PLAYSET){
            val = Math.abs(rand.nextInt()) % range;
            if(!pref.contains(humans.get(val))){
                pref.add(humans.get(val));
            }
        }

        this.setPrefList(pref);
    }

    @Override public void printPrefList(){
        System.out.print("[ ");
        for(Femme f : this.pref_list){
            System.out.printf("%d, ", f.getId());
        }
        System.out.print("]\n");
    }
}
