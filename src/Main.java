import java.io.FileWriter;
import java.util.*;

public class Main {
    final static int PLAYSET = 3;
    static int SEED = 1;

    public static void main(String[] args) throws Exception {
        int cheater = 0;

        ArrayList<Femme> defaultRes;
        ArrayList<Femme> cheatedRes;
        Femme f_without_cheat;
        Femme f_with_cheat;

        System.out.println("La SEED vaut " + SEED);
        FileWriter fichier = new FileWriter("data_gs");
        for(int s = 0; s < 1000; s++) {

            fichier.write("SEED " + SEED + ":\n");
            System.out.println("SEED " + SEED + "\n");

            List<Object> res;

            res = retrieveData(false, PLAYSET, SEED, true); // useful data to perform statistics on

            // Recast Objects
            defaultRes = new ArrayList<>((ArrayList<Femme>) res.get(1));
            cheatedRes = new ArrayList<>((ArrayList<Femme>) res.get(2));

            // Récupérer la tricheuse dans la situation initiale puis de triche
            f_without_cheat = Human.retrieveHuman(defaultRes, cheater);
            f_with_cheat = Human.retrieveHuman(cheatedRes, cheater);

            assert f_without_cheat != null;
            int defaultMan = f_without_cheat.getBounded().getId();

            assert f_with_cheat != null;
            int cheatedMan = f_with_cheat.getBounded().getId();

            if(Human.getPosition(f_without_cheat.getDefaultPrefList(), defaultMan) > Human.getPosition(f_without_cheat.getDefaultPrefList(), cheatedMan)){
                fichier.write(f_without_cheat.getId() + " " + defaultMan + "\n");
                fichier.write(f_without_cheat.getId() + " " + cheatedMan + "\n");
                fichier.write("Amelioration sur la seed " + SEED + "\n");
            }

            fichier.write("===================================================\n");

            SEED ++;
        }
        fichier.close();
    }

    /** Initialisation d'entités femmes et hommes avec génération aléatoire de listes de préférences */
    public static void initHumans(List<Homme> men, List<Femme> women, int PLAYSET, int SEED){
        men.clear();
        women.clear();

        // Init groups
        HumanFactory.generateHumans(men, "man", PLAYSET);
        HumanFactory.generateHumans(women, "woman", PLAYSET);

        // For each woman/man, generate random set of prefs
        Human.generateRandomPrefLists(men, women, PLAYSET, SEED + PLAYSET);
        Human.generateRandomPrefLists(women, men, PLAYSET, SEED);
    }

    /** Application GS sans triche, puis avec, et on récupère les mariages dans les 2 situations pour faire de l'analyse */
    public static List<Object> retrieveData(boolean random_cheater, int PLAYSET, int SEED, boolean debug){

        ArrayList<Homme> men = new ArrayList<>();
        ArrayList<Femme> women = new ArrayList<>();

        /* EXECUTION */
        // Premiere execution, résultat par défaut, pire choix pour les femmes

        initHumans(men, women, PLAYSET, SEED);

        if(debug){
            for(int k = 0; k < PLAYSET; k++){ // Print prefs
                Homme m = men.get(k);
                System.out.println("HOMME " + k);
                m.printPrefList();
                Femme f = women.get(k);
                System.out.println("FEMME " + k);
                f.printPrefList();
            }
        }


        ArrayList<Femme> c = new ArrayList<>(GaleShapley.galeShapley(men));

        if(debug) System.out.println("Mariage par défaut stable ? " + GaleShapley.isStable(women, men));

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

        if(debug) {
            for (Femme f : celebrations)
                System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
        }

        /* ===================================================================== */

        int nb_iter = GaleShapley.galeShapleyWithCheater(men, women, celebrations, random_cheater, SEED, PLAYSET, true);

        /* ===================================================================== */

        if(debug) {
            System.out.print("================================\n");
            System.out.print("RÉSULTAT SANS TRICHE PUIS TRICHE\n");
            System.out.print("================================\n");

            for (Femme f : initial_state) {
                System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
            }
            System.out.print("======================\n");
            for (Femme f : celebrations) {
                System.out.println("Homme " + f.getBounded().getId() + " Femme " + f.getId());
            }
        }

        ArrayList<Object> res = new ArrayList<>();
        res.add(nb_iter);
        res.add(initial_state);
        res.add(celebrations);
        return res;
    }
}