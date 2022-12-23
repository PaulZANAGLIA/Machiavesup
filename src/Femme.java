import java.util.ArrayList;

public class Femme extends Human<Homme> {
    public Femme(int id){
        this.id = id;
        this.pref_list = new ArrayList<>();
        this.boundedTo = null;
        this.index = 0;
    }

    public Homme compare(Homme m){
        if(this.getBounded() == null || this.pref_list.indexOf(m) < this.pref_list.indexOf(this.getBounded())){
            Homme evince = this.getBounded();
            this.setBounded(m);
            return evince;
        }
        return m;
    }

    public Femme ask(Homme m){
        Femme evince = m.compare(this);
        if (evince == null || evince.getId() != this.getId()){
            this.setBounded(m);
        }
        return evince;
    }
}