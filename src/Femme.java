import java.util.ArrayList;

public class Femme extends AbstractHuman<Homme>{
    public Femme(int id){
        this.id = id;
        this.pref_list = new ArrayList<>();
        this.boundedTo = null;
        this.index = 0;
    }

    /**  When a woman receive a proposition, she looks if the proposer is a better partner for her.
     * If so, replace her bounded partner to the new one and return true, else return false and nothing happen. */
    protected Homme compare(Homme m){
        if(this.getBounded() == null || this.pref_list.indexOf(m) < this.pref_list.indexOf(this.getBounded())){
            Homme evince = this.getBounded();
            this.setBounded(m);
            return evince;
        }
        return m;
    }

    @Override public void printPrefList() {
        System.out.print("[ ");
        for(Homme h : this.pref_list){
            System.out.printf("%d, ", h.getId());
        }
        System.out.print("]\n");
    }
}