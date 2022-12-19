//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Arrays.*;
import java.util.Random;

public class Femme extends AbstractHuman<Homme>{
    public Femme(int id){

        this.id = id;
        this.boundedTo=null;
        this.indexe= 0;
    }

    /**  When a woman receive a proposition, she looks if the proposer is a better partner for her.
     * If so, she replace her bounded partner to the new one and return true, else return false and nothing happen. */
    protected Homme compare(Homme m){
        if(this.getBounded() == null || this.List_eff.indexOf(m) < this.List_eff.indexOf(this.getBounded())){
            Homme evince = this.getBounded();
            this.set(null);
            this.set(m);
            return evince;
        }
        return m;
    }

    @Override
    public int isItBetter(int id_homme_sans_triche) {
        if( this.indiceBoundedTo < id_homme_sans_triche)
        {
            return 1; // amélioré ! gg
        }
        if(id_homme_sans_triche == this.indiceBoundedTo)
        {
            return 0; // dommage égalité
        }
        else return -1 ;// si ce n'est si supérieur, ni égal, c'est qu'on a chopé pire, F
    }
    public int getAndSetIndidceBoundedTo(int playset)
    {
        int indice=-1;
        for(int i = 0 ; i < playset +1; i++) // +1 pour la collab
        {
            if(this.List_de_base.get(i).getId()==this.boundedTo.getId())
            {
                indice=i;
                break;
            }
        }
        this.indiceBoundedTo=indice;
        return indice;
    }
    @Override protected void generateRandomPrefList(int range, ArrayList<Homme> humans, int seed){
        ArrayList<Homme> pref = new ArrayList<>();
        Random rand = new Random(seed);
        int val;
        while(pref.size() < range){
            val = Math.abs(rand.nextInt()) % range;
            if(!pref.contains(humans.get(val))){
                pref.add(humans.get(val));
            }
        }
        this.setBothPrefList(pref);
        //this.List_de_base=new ArrayList<>(pref);
    }

    @Override public void printPrefList() {
        System.out.print("[ ");
        for(Homme h : this.List_eff){
            System.out.printf("%d, ", h.getId());
        }
        System.out.print("]\n");
    }
}

