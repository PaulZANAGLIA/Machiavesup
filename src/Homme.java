import java.util.ArrayList;

public class Homme extends Human<Femme> {
    public Homme(int id){
        this.id = id;
        this.pref_list = new ArrayList<>();
        this.default_pref_list = new ArrayList<>();
        this.boundedTo = null;
        this.index = 0;
    }

    public Femme compare(Femme w){
        if(this.getBounded() == null || this.pref_list.indexOf(w) < this.pref_list.indexOf(this.getBounded())){
            Femme evince = this.getBounded();
            this.setBounded(w);
            return evince;
        }
        return w;
    }

    public Homme ask(Femme w){
        Homme evince = w.compare(this);
        if (evince == null || evince.getId() != this.getId()){
            this.setBounded(w);
        }
        return evince;
    }
}