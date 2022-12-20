import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static Random r = new Random();
    final static int PLAYSET = 3;
    static int SEED = 1;//Math.abs(r.nextInt());

    public static void main(String[] args) throws Exception {
        int cheater = 0;

        // Variables pour les statistiques
        int betterChoiceAcc = 0;
        int bestChoiceAcc = 0;
        int allWomenBetter = 0;
        int minIter = PLAYSET;
        int maxIter = 0;
        int totIter = 0;

        ArrayList<Femme> defaultRes;
        ArrayList<Femme> cheatedRes;
        Femme f_without_cheat;
        Femme f_with_cheat;

        System.out.println("La SEED vaut " + SEED);
        FileWriter fichier = new FileWriter("data_gs");
        for(int s = 0; s < 1000; s++) {

            fichier.write("SEED " + SEED + ":\n");
            System.out.println("SEED " + SEED + "\n");

            ArrayList<Object> res = null;

            res = retrieveData(false); // useful data to perform statistics on

            Integer nbIter;

            // Recast Objects
            nbIter = (Integer) res.get(0);
            defaultRes = (ArrayList<Femme>) res.get(1);
            cheatedRes = (ArrayList<Femme>) res.get(2);

            // Récupérer la tricheuse dans la situation initiale puis de triche
            f_without_cheat = (Femme) retrieveHuman(defaultRes, cheater);
            f_with_cheat = (Femme) retrieveHuman(cheatedRes, cheater);

            assert f_without_cheat != null;
            int defaultMan = f_without_cheat.getBounded().getId();
            assert f_with_cheat != null;
            int cheatedMan = f_with_cheat.getBounded().getId();

            if(getPosition((ArrayList<Homme>) f_without_cheat.getDefaultPrefList(), defaultMan) > getPosition((ArrayList<Homme>) f_without_cheat.getDefaultPrefList(), cheatedMan)){
                fichier.write(f_without_cheat.getId() + " " + defaultMan + "\n");
                fichier.write(f_without_cheat.getId() + " " + cheatedMan + "\n");
                fichier.write("Amelioration sur la seed " + SEED + "\n");
            }

            fichier.write("===================================================\n");

            SEED ++;
        }
        fichier.close();

        /*
        for(int s = 0; s < 100; s++) {
            System.out.println("La SEED vaut " + SEED);
            ArrayList<Object> res;
            res = retrieveData(false); // useful data to perform statistics on
            Integer nbIter;
            // Recast Objects
            nbIter = (Integer) res.get(0);
            defaultRes = (ArrayList<Femme>) res.get(1);
            cheatedRes = (ArrayList<Femme>) res.get(2);
            // Récupérer position de la tricheuse
            f_without_cheat = retrieve_woman(defaultRes, cheater);
            f_with_cheat = retrieve_woman(cheatedRes, cheater);
            // Look if f0 could cheat and get a better partner
            assert f_without_cheat != null;
            int defaultMan = f_without_cheat.getBounded().getId();
            assert f_with_cheat != null;
            int cheatedMan = f_with_cheat.getBounded().getId();
            if(get_man_position(defaultRes.get(0).getDefaultPrefList(), defaultMan) > get_man_position(defaultRes.get(0).getDefaultPrefList(), cheatedMan)){
                betterChoiceAcc++;
                // Look the min, max and average moves necessary to perform a cheat as f0
                if(minIter > nbIter-1 && nbIter>0){
                    minIter = nbIter-1; // -1, car le dernier décalage vu, mène à une situation de sacrifice
                } else if (maxIter < nbIter-1 && nbIter>0){
                    maxIter = nbIter-1;
                }
                totIter += nbIter;
            }
            // Look if f0 could cheat and get her best choice
            if(defaultMan != cheatedMan && get_man_position(defaultRes.get(0).getPrefList(), cheatedMan) == 0){
                bestChoiceAcc++;
            }
            // Look if the cheat f0 performed upgrade the situation of every woman each times
            allWomenBetter = women_situation_is_globally_better(defaultRes, cheatedRes);
            SEED ++;
        }
        System.out.printf("f%d a amélioré son sort %d/100\n", f_without_cheat.getId(), betterChoiceAcc);
        System.out.printf("f%d a eu son meilleur choix %d/100\n", f_with_cheat.getId(), bestChoiceAcc);
        System.out.printf("Pour un total de %d décalages fructueux, f%d a en moyenne %f décalage de l'indésirable, le plus petit vu est de %d, et le max %d\n", betterChoiceAcc, f_with_cheat.getId(), (float)totIter/(float)betterChoiceAcc, minIter, maxIter);
        System.out.printf("f%d a pu améliorer le sort global des femmes %d/100 en trichant\n", f_with_cheat.getId(), allWomenBetter);
        */
    }

    /** Regarde si les couples sont stables */
    public static boolean isStable(List<Femme> femmes, List<Homme> hommes){ // on passe l'array femme, car elle garde intacte la liste des hommes
        for(Femme f: femmes){ // Pour chaque femme, vérifier si elle est en situation de jalousie justifiée
            Homme bounded = f.getBounded();

            for(Homme h : hommes){ // Pour chaque homme
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

    /** Connaitre la position d'un homme ou d'une femme par son ID dans une liste */
    public static int getPosition(List<? extends Human> humans, int humanId){
        int i = 0;
        while(humans.get(i).getId() != humanId)
            i++;
        return i;
    }

    /** Récupérer un objet (Femme ou Homme) par son ID dans une liste */
    public static Human retrieveHuman(List<? extends Human> humans, int x){
        for(Human h : humans) {
            if (h.getId() == x)
                return h;
        }
        return null;
    }

    /** Generate a human with an id */
    public static Human generateHuman(String genre, int id){
        if(genre == "man")
            return new Homme(id);
        return new Femme(id);
    }

    /** Generate amount of humans in a list */
    public static void generateHumans(List<? extends Human> humans, String genre, int range){
        for(int i = 0; i < range; i++){ // Init groups
            Human hu = generateHuman(genre, i);
            if(genre == "man"){
                Homme h = (Homme) hu;
                List<Homme> men = (List<Homme>) humans;
                men.add(h);
            } else {
                Femme f = (Femme) hu;
                List<Femme> women = (List<Femme>) humans;
                women.add(f);
            }
        }
    }

    /** Generate random PrefList for each  */
    public static void generateRandomPrefList(List<? extends Human> humans, List<? extends Human> humans2, int PLAYSET, int SEED){
        // Throw exception if any of the lists are empty
        assert(!(humans.size() > 0 && humans2.size() > 0));

        // Throw exception if humans and humans2 lists are in the same type
        assert(!(humans.get(0) instanceof Homme && humans2.get(0) instanceof Homme || humans.get(0) instanceof Femme && humans2.get(0) instanceof Femme));

        final AtomicInteger step = new AtomicInteger((humans2.get(0) instanceof Femme) ? SEED + PLAYSET : SEED);


        humans.forEach((human) -> human.generateRandomPrefList(PLAYSET, humans2, step.getAndIncrement()));
    }

    /** Initialisation d'entités femmes et hommes avec génération aléatoire de listes de préférences */
    public static void initHumans(List<Homme> men, List<Femme> women, int PLAYSET, int SEED){
        men.clear();
        women.clear();

        // Init groups
        generateHumans(men, "man", PLAYSET);
        generateHumans(women, "woman", PLAYSET);

        // For each woman/man, generate random set of prefs
        generateRandomPrefList(men, women, PLAYSET, SEED);
        generateRandomPrefList(women, men, PLAYSET, SEED);

        /*
        for(int k = 0; k < PLAYSET; k++){
            men.get(k).generateRandomPrefList(PLAYSET, women, SEED + k + PLAYSET);
            women.get(k).generateRandomPrefList(PLAYSET, men, SEED + k);
        }
        */
    }



    /** Gale_Shapley algo, procédure mariage stable classique */
    public static ArrayList<Femme> galeShapley(ArrayList<Homme> hommes){
        ArrayList<Femme> celebration = new ArrayList<>();
        LinkedList<Homme> Q = new LinkedList<>(hommes);

        while (Q.size() > 0){
            Homme homme = Q.pop();
            if(homme.getBounded() == null) {
                Femme pref = homme.getCurrentPref();
                Homme evince = homme.ask(pref);
                if(evince != null){
                    evince.shiftPrefList();
                    evince.setBounded(null);
                    Q.add(evince);
                }
            }
        }

        for (Homme m : hommes) { // Print couples
            celebration.add(m.getBounded());
        }

        return celebration;
    }
    public static int galeShapleyWithCheater(ArrayList<Homme> men, ArrayList<Femme> women, ArrayList<Femme> celebrations, boolean random_cheater){
        // Reset groups
        for(int k = 0; k < men.size(); k++) {
            men.get(k).reset();
            women.get(k).reset();
        }

        // Define cheater randomly
        int cheater_id = 0;
        if(PLAYSET > 0 && random_cheater)
            cheater_id = Math.abs(r.nextInt() % PLAYSET);

        System.out.println("Tricheuse est " + cheater_id);

        ArrayList<Femme> current_celebrations = null;

        System.out.println("============ TRICHE ============");
        int nbIter=0;
        Femme cheater = (Femme) retrieveHuman(celebrations, cheater_id); // Récuperer la femme qui triche
        Homme man = cheater.getBounded(); // L'homme auquel elle est couplée initialement
        int initial_husband_id = man.getId();
        int initial_husband_pos = getPosition((ArrayList<Homme>) cheater.getPrefList(), initial_husband_id);

        do{
            nbIter++;
            System.out.println("============ Tour " + nbIter + " ============");

            // STORAGE
            if (nbIter > 1) {
                celebrations.clear();
                for(Femme f : current_celebrations) {
                    Femme ff = new Femme(f.getId());
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
                Homme OMEGA_H = new Homme(PLAYSET);
                Femme OMEGA_F = new Femme(PLAYSET);

                OMEGA_H.generateRandomPrefList(PLAYSET, women, SEED - 2);
                OMEGA_H.getPrefList().add(OMEGA_F);
                OMEGA_F.generateRandomPrefList(PLAYSET, men, SEED - 1);
                OMEGA_F.getPrefList().add(OMEGA_H);

                for(int k = 0; k < PLAYSET; k++){
                    Homme m = men.get(k);
                    m.getPrefList().add(OMEGA_F);
                    Femme f = women.get(k);
                    if(k != cheater_id){
                        f.getPrefList().add(OMEGA_H);
                    } else { // Si c'est la tricheuse elle place OMEGA_H en avant-dernier
                        f.getPrefList().add(initial_husband_pos, OMEGA_H);
                    }
                }

                men.add(OMEGA_H);
                women.add(OMEGA_F);

            } else { // On déplace OMEGA_H pour la tricheuse vers la gauche
                Femme f = women.get(cheater_id);
                for(int k = 0; k <= PLAYSET ;k++){
                    if(f.getPrefList().get(k).getId() == PLAYSET) {
                        f.getPrefList().add(initial_husband_pos - nbIter + 1, f.getPrefList().get(k));
                        f.getPrefList().remove(k+1);
                        break;
                    }
                }
            }

            for(int k = 0; k < men.size(); k++){ // Print prefs
                Homme m = men.get(k);
                System.out.println("HOMME " + k);
                m.printPrefList();
                Femme f = women.get(k);
                System.out.println("FEMME " + k);
                f.printPrefList();
            }
            current_celebrations = galeShapley(men);
        } while(men.get(PLAYSET).getBounded() == women.get(PLAYSET)); // On tourne l'algo tant qu'on peut améliorer la situation de la femme avant un cas de sacrifice

        System.out.println("Il y a eu nbIter: " + nbIter);

        System.out.println("LAST CELEBRATION SEEN WITHOUT SACRIFICE");
        for(Femme f : celebrations){
            if(f.getBounded() != null) System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
        }

        System.out.println("LAST CELEBRATION SEEN");
        for(Femme f : current_celebrations){
            System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
        }
        return nbIter;
    }

    /** Application GS sans triche, puis avec, et on récupères les mariages dans les 2 situations pour faire de l'analyse */
    public static ArrayList<Object> retrieveData(boolean random_cheater){
        ArrayList<Homme> men = new ArrayList<>(); // Empty List of men
        ArrayList<Femme> women = new ArrayList<>(); // Empty List of women

        /* EXECUTION */
        // Premiere execution, résultat par défaut, pire choix pour les femmes

        initHumans(men, women, PLAYSET, SEED);

        for(int k = 0; k < PLAYSET; k++){ // Print prefs
            Homme m = men.get(k);
            System.out.println("HOMME " + k);
            m.printPrefList();
            Femme f = women.get(k);
            System.out.println("FEMME " + k);
            f.printPrefList();
        }

        ArrayList<Femme> c = galeShapley(men);
        System.out.println("Mariage par défaut stable ? " + isStable(women, men));

        ArrayList<Femme> celebrations = new ArrayList<>();
        ArrayList<Femme> initial_state = new ArrayList<>();
        for(Femme f : c){
            Femme f1 = new Femme(f.getId());
            Femme f2 = new Femme(f.getId());
            f1.setBounded(f.getBounded());
            f1.setPrefList(f.getDefaultPrefList());
            f2.setBounded(f.getBounded());
            f2.setPrefList(f.getDefaultPrefList());
            celebrations.add(f1); // On recrée chaque femme en se basant sur l'id de l'ancien
            initial_state.add(f2);
        }
        for(Femme f : celebrations){
            System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
        }

        /* ===================================================================== */

        int nb_iter = galeShapleyWithCheater(men, women, celebrations, random_cheater);
        /* ===================================================================== */
        System.out.print("================================\n");
        System.out.print("RESULTAT SANS TRICHE PUIS TRICHE\n");
        System.out.print("================================\n");
        for(Femme f : initial_state){
            System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
        }
        System.out.print("======================\n");
        for(Femme f : celebrations) {
            System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
        }
        ArrayList<Object> res = new ArrayList<>();
        res.add(nb_iter);
        res.add(initial_state);
        res.add(celebrations);
        return res;
    }
}