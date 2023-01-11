import java.util.ArrayList;

public class GaleShapley {

    public void Gale_Shapley(ArrayList<Homme> hommes){
        ArrayList<Homme> Q = new ArrayList<>();
        Q.addAll(hommes);

        while (Q.size() > 0){
            Homme homme = Q.remove(0);
            if(homme.getBounded() == null) {
                Femme pref = homme.getFirstPrefList();
                Homme evince = homme.ask(pref);
                if(evince != null){
                    evince.removePrefList();
                    evince.setBounded(null);
                    Q.add(evince);
                }
            }
        }
        for(int k = 0; k < hommes.size(); k++){ // Print couples
            Homme m = hommes.get(k);
            //System.out.println("HOMME " + m.getId() + " & FEMME " + m.getBounded().getId());
        }
    }

    public void Gale_Shapley_inverse(ArrayList<Femme> femmes){
        ArrayList<Femme> Q = new ArrayList<>();
        Q.addAll(femmes);

        while (Q.size() > 0){
            Femme femme = Q.remove(0);
            if(femme.getBounded() == null) {
                Homme pref = femme.getFirstPrefList();
                Femme evince = femme.ask(pref);
                if(evince != null){
                    evince.removePrefList();
                    evince.setBounded(null);
                    Q.add(evince);
                }
            }
        }
        for(int k = 0; k < femmes.size(); k++){ // Print couples
            Femme m = femmes.get(k);
            //System.out.println("FEMME " + m.getId() + " & HOMME " + m.getBounded().getId());
        }
    }
}
