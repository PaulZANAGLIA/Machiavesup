import java.util.ArrayList;
import java.util.Random;

public class Femme extends AbstractHuman<Homme>{
    public Femme(int id){
        this.id = id;
        this.prefList = new ArrayList<>();
        this.boundedTo = null;
    }

    /**  When a woman receive a proposition, she looks if the proposer is a better partner for her.
     * If so, she replace her bounded partner to the new one and return true, else return false and nothing happen. */
    protected Homme compare(Homme m){
        if(this.getBounded() == null || this.prefList.indexOf(m) < this.prefList.indexOf(this.getBounded())){
            Homme evince = this.getBounded();
            this.set(null);
            this.set(m);
            return evince;
        }
        return m;
    }
    @Override protected void generateRandomPrefList(int range, ArrayList<Homme> humans){
        ArrayList<Homme> pref = new ArrayList<>();
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

    @Override public void printPrefList() {
        System.out.print("[ ");
        for(Homme h : this.prefList){
            System.out.printf("%d, ", h.getId());
        }
        System.out.print("]\n");
    }
}

