import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GaleShapley {
    /** Gale_Shapley algo, procédure mariage stable classique */
    public static <T extends Human<F>, F extends  Human<T>> List<F> galeShapley(List<T> humans){
        ArrayList<F> celebration = new ArrayList<>();
        LinkedList<Human<F>> Q = new LinkedList<>(humans);

        while(Q.size() > 0){
            Human<F> homme = Q.pop();
            if(homme.getBounded() == null){
                F pref = homme.getCurrentPref();
                Human<F> evince = homme.ask(pref);
                if(evince != null){
                    evince.shiftPrefList();
                    evince.setBounded(null);
                    Q.add(evince);
                }
            }
        }

        for (Human<F> m : humans)
            celebration.add(m.getBounded());


        return celebration;
    }

    /** Regarde si les couples sont stables */
    public static <T extends Human<F>, F extends  Human<T>> boolean isStable(List<F> femmes, List<T> hommes){ // on passe le tableau femme, car elle garde intacte la liste des hommes
        for(F f: femmes){ // Pour chaque femme, vérifier si elle est en situation de jalousie justifiée
            T bounded = f.getBounded();

            for(T h : hommes){ // Pour chaque homme
                if(h.getId() == bounded.getId()) break;

                // S'il préfère la femme courante, regarde s'il y a une situation de jalousie justifiée
                if(h.getPrefList().indexOf(f) < h.getPrefList().indexOf(h.getBounded())){
                    h.printPrefList();
                    System.out.println("f " + f.getId() + " courante " +  h.getPrefList().indexOf(f) + "femme " + h.getPrefList().indexOf(h.getBounded()));
                    return false;
                }
            }
        }
        return true;
    }


    public static <T extends Human<F>, F extends  Human<T>> int galeShapleyWithCheater(List<T> men, List<F> women, List<F> celebrations, boolean random_cheater, int SEED, int PLAYSET, boolean debug){
        // Reset groups
        for(int k = 0; k < men.size(); k++) {
            men.get(k).reset();
            women.get(k).reset();
        }

        // Define cheater randomly
        int cheater_id = 0;
        if(PLAYSET > 0 && random_cheater){
            Random r = new Random();
            cheater_id = Math.abs(r.nextInt() % PLAYSET);
        }

        if(debug) System.out.println("Tricheuse est " + cheater_id);

        ArrayList<F> current_celebrations = null;

        if(debug) System.out.println("============ TRICHE ============");

        int nbIter=0;
        F cheater = Human.retrieveHuman(celebrations, cheater_id); // Récupérer la femme qui triche

        assert cheater != null;
        T man = cheater.getBounded(); // L'homme auquel elle est couplée initialement

        int initial_husband_id = man.getId();
        int initial_husband_pos = Human.getPosition(cheater.getPrefList(), initial_husband_id);

        do{
            nbIter++;

            if(debug) System.out.println("============ Tour " + nbIter + " ============");

            // STORAGE
            if (nbIter > 1) {
                celebrations.clear();
                for(F f : current_celebrations) {
                    F ff = (F) HumanFactory.generateHuman(HumanFactory.getHumanType(f), f.getId());
                    ff.setBounded(f.getBounded());
                    celebrations.add(ff); // On recrée chaque femme en se basant sur l'id de l'ancien
                }
            }

            // Reset groups
            for (int k = 0; k < men.size(); k++) {
                men.get(k).reset();
                women.get(k).reset();
            }

            if(nbIter == 1){
                // CREATION DES OMEGAS
                T OMEGA_H = (T) HumanFactory.generateHuman(HumanFactory.getHumanType(man), PLAYSET);
                F OMEGA_F = (F) HumanFactory.generateHuman(HumanFactory.getHumanType(cheater), PLAYSET);

                OMEGA_H.setPrefList(OMEGA_H.generateRandomPrefList(PLAYSET, women, SEED - 2));
                OMEGA_H.getPrefList().add(OMEGA_F);
                OMEGA_F.setPrefList(OMEGA_F.generateRandomPrefList(PLAYSET, men, SEED - 1));
                OMEGA_F.getPrefList().add(OMEGA_H);

                for(int k = 0; k < PLAYSET; k++){
                    T m = men.get(k);
                    m.getPrefList().add(OMEGA_F);
                    F f = women.get(k);
                    if(k != cheater_id){
                        f.getPrefList().add(OMEGA_H);
                    } else { // Si c'est la tricheuse elle place OMEGA_H en avant-dernier
                        f.getPrefList().add(initial_husband_pos, OMEGA_H);
                    }
                }

                men.add(OMEGA_H);
                women.add(OMEGA_F);

            } else { // On déplace OMEGA_H pour la tricheuse vers la gauche
                F f = women.get(cheater_id);
                for(int k = 0; k <= PLAYSET; k++){
                    if(f.getPrefList().get(k).getId() == PLAYSET) {
                        f.getPrefList().add(initial_husband_pos - nbIter + 1, f.getPrefList().get(k));
                        f.getPrefList().remove(k+1);
                        break;
                    }
                }
            }

            for(int k = 0; k < men.size(); k++){ // Print prefs
                T m = men.get(k);
                m.printPrefList();
                F f = women.get(k);

                if(debug){
                    System.out.println("HOMME " + k);
                    System.out.println("FEMME " + k);
                }

                f.printPrefList();
            }
            current_celebrations = new ArrayList<>(GaleShapley.galeShapley((men)));
        } while(men.get(PLAYSET).getBounded() == women.get(PLAYSET)); // On tourne l'algo tant qu'on peut améliorer la situation de la femme avant un cas de sacrifice


        if(debug) {
            System.out.println("Il y a eu nbIter: " + nbIter);
            System.out.println("LAST CELEBRATION SEEN WITHOUT SACRIFICE");

            for(F f : celebrations){
                if(f.getBounded() != null) System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
            }

            System.out.println("LAST CELEBRATION SEEN");

            for (Human<?> f : current_celebrations)
                System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
        }

        return nbIter;
    }

}
