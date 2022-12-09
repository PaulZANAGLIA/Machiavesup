import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    final static int PLAYSET = 4;

    //static Random r = new Random();

    public static void main(String[] args) throws Exception{

        FileWriter mariagesStables = new FileWriter("Mariages-Stables");
        FileWriter galeShapley = new FileWriter("Gale-Shapley");

        for(int j=1;j<2;j++){
            mariagesStables.write("\n--- Seed "+j+" ---\n");
            /**     Creation de la liste d'Hommmes et la liste de Femmes, initialement vides    */
            ArrayList<Homme> men = new ArrayList<>(); // Empty List of men
            ArrayList<Femme> women = new ArrayList<>(); // Empty List of women

            int seed = j+1;
            mariagesStables.write("\n---- Seed num " + seed +"-----\n");

            /**     Initialiser la liste d'Hommes et de Femmes */
            for (int i = 0; i < PLAYSET; i++) { // Init groups
                Homme m = new Homme(i);
                Femme f = new Femme(i);
                men.add(m);
                women.add(f);

            }

            /** Generation aleatoire des listes de preferences */
            for (int k = 0; k < PLAYSET; k++) { // For each woman/man, generate random set of prefs
                men.get(k).generateRandomPrefList(PLAYSET, women, seed + k + PLAYSET);
                //seed++;
                women.get(k).generateRandomPrefList(PLAYSET, men, seed + k);
                //seed++;
            }

            //allPermutations(women, women.size());
            /** Affichage des listes de preferences pour chaque Human */
            for(int k = 0; k < PLAYSET; k++){ // Print prefs
                Homme m = men.get(k);
                System.out.println("HOMME " + k);
                galeShapley.write("H"+k+" " + m.pref_list+"\n");

                m.printPrefList();
                Femme f = women.get(k);
                System.out.println("FEMME " + k);
                galeShapley.write("F"+k+" " + f.pref_list+"\n");
                galeShapley.write("\n");
                f.printPrefList();
            }

            // Fonction qui affiche tous les mariages stables
            //allMariagesStablesToFile(men,women,men.size(),mariagesStables);


            Gale_Shapley(men);

            for(int k = 0; k < PLAYSET; k++){
                men.get(k).clear();
                women.get(k).clear();
            }

            allMariagesStables(men,women,men.size());

            for(int k = 0; k < PLAYSET; k++){
                men.get(k).clear();
                women.get(k).clear();
            }

            Gale_Shapley_inverse(women);

            for(int k = 0; k < PLAYSET; k++){ // Print prefs
                Homme m = men.get(k);
                System.out.println("HOMME " + k);
                galeShapley.write("H"+k+" " + m.pref_list+"\n");

                m.printPrefList();
                Femme f = women.get(k);
                System.out.println("FEMME " + k);
                //f.setPrefList();
                galeShapley.write("F"+k+" " + f.pref_list+"\n");
                galeShapley.write("\n");
                f.printPrefList();
            }


            //System.out.println("Sans tricher " +  is_Stable(women));

            //Gale_Shapley(men);

        }

        mariagesStables.close();
        galeShapley.close();

        /** Regarde si l'instabilité est detectée (sur 3 personnes)*/
        /*Femme f = men.get(2).getBounded();
        men.get(2).set(men.get(1).getBounded());
        men.get(2).getBounded().set(men.get(2));
        f.set(men.get(1));
        men.get(1).set(f);

        Femme f2 = men.get(2).getBounded();
        men.get(2).set(men.get(0).getBounded());
        men.get(2).getBounded().set(men.get(2));
        f2.set(men.get(0));
        men.get(0).set(f2);

        System.out.println("En trichant " +  is_Stable(women));

        for(int k = 0; k < men.size(); k++){ // Print couples
            Homme m = men.get(k);
            System.out.println("HOMME " + m.getId() + " & FEMME " + m.getBounded().getId());
        }*/

    }

    /** Fonctions qui affiche toutes les permutations de mariages stables */
    public static void allMariagesStablesToFile(ArrayList<Homme> hommes, ArrayList<Femme> femmes, int size, FileWriter file) throws Exception{
        // if size becomes 1 then prints the obtained
        // permutation
        if (size == 1 ){
            ArrayList<Homme> h = new ArrayList<>();
            ArrayList<Femme> f = new ArrayList<>();
            ArrayList<String> couples = new ArrayList<>();
            for (int i = 0; i < femmes.size(); i++){
                //f.get(i).getBounded().set(f.get(i));
                //h.get(i).set(f.get(i));
                h.add(hommes.get(i));
                f.add(femmes.get(i));
                hommes.get(i).setBounded(femmes.get(i));
                femmes.get(i).setBounded(hommes.get(i));
            }
            if(is_Stable(f)){
                for(int i=0; i<h.size();i++){
                    couples.add(h.get(i).getId()+""+f.get(i).getId());
                }

                //System.out.println(couples);
                file.write(couples+"\n");
                //System.out.println(h);
                //System.out.println(f);
                //System.out.println();
            }
            /*for (int i = 0; i < femmes.size(); i++) { // Loop through every name/phone number combo
                array3.add(hommes.get(i).getId()+""+femmes.get(i).getId());
                //array3.add(hommes.get(i));
                //array3.add(femmes.get(i)); // Concat the two, and add it
            }*/
            //is_Stable(femmes);
            //System.out.println(array3);
        }

        for (int i = 0; i < size; i++) {
            allMariagesStablesToFile(hommes,femmes,size - 1,file);
            // if size is odd, swap 0th i.e (first) and
            // (size-1)th i.e (last) element
            if (size % 2 == 1) {
                Femme temp = femmes.get(0);
                femmes.set(0,femmes.get(size-1));
                femmes.set(size-1,temp);
            }
            // If size is even, swap ith
            // and (size-1)th i.e last element
            else {
                Femme temp = femmes.get(i);
                femmes.set(i,femmes.get(size-1));
                femmes.set(size-1,temp);
            }
        }


    }

    /** Gale_Shapley algo, procédure mariage stable classique */

    public static boolean is_Stable(ArrayList<Femme> femmes){ // on passe l'array femme car elle garde intacte la liste des hommes
        for(Femme f : femmes){ // Pour chaque femme, vérifier si elle est en situation de jalousie justifiée
            Homme bound = f.getBounded();

            for(Homme h : f.getPrefList()){ // Pour chaque homme
                if(h.getId() == bound.getId())
                    break;
                // S'il préfere la femme courante, regarde s'il y a une situation de jalousie justifée
                if(h.getPrefList().indexOf(f) < h.getPrefList().indexOf(h.getBounded())){
                    //h.printPrefList();
                    //System.out.println("f " + f.getId() + " courante " +  h.getList().indexOf(f) + " femme " + h.getList().indexOf(h.getBounded()));
                    return false;
                }
            }
        }
        return true;
    }

    public static void Gale_Shapley_inverse(ArrayList<Femme> femmes){
        ArrayList<Femme> Q = new ArrayList<>();
        Q.addAll(femmes);

        //System.out.println("Q size = " + Q.size());
        while (Q.size() > 0){
            Femme femme = Q.remove(0);
            if(femme.getBounded() == null) {
                Homme pref = femme.getFirstPrefList();
                Femme evince = femme.ask(pref);
                if(evince != null){
                    //System.out.println("homme = " + evince.getId() + " evince");
                    evince.removePrefList();
                    evince.setBounded(null);
                    Q.add(evince);
                }
            }
        }
        for(int k = 0; k < femmes.size(); k++){ // Print couples
            Femme m = femmes.get(k);
            System.out.println("FEMME " + m.getId() + " & HOMME " + m.getBounded().getId());
            //m.setPrefList(m.pref_list.add(m.getBounded()));
        }
    }

    public static void Gale_Shapley(ArrayList<Homme> hommes){
        ArrayList<Homme> Q = new ArrayList<>();
        Q.addAll(hommes);

        //System.out.println("Q size = " + Q.size());
        while (Q.size() > 0){
            Homme homme = Q.remove(0);
            if(homme.getBounded() == null) {
                Femme pref = homme.getFirstPrefList();
                Homme evince = homme.ask(pref);
                if(evince != null){
                    //System.out.println("homme = " + evince.getId() + " evince");
                    evince.removePrefList();
                    evince.setBounded(null);
                    Q.add(evince);
                }
            }
        }
        for(int k = 0; k < hommes.size(); k++){ // Print couples
            Homme m = hommes.get(k);
            System.out.println("HOMME " + m.getId() + " & FEMME " + m.getBounded().getId());
        }
    }

    /** All permutations of Pref List */
    public static ArrayList<Integer> allPermutations(ArrayList<Femme> femmes, int size) {
        // if size becomes 1 then prints the obtained
        // permutation
        ArrayList<Integer> permutations = new ArrayList<>();
        if (size == 1 ){
            ArrayList<Femme> f = new ArrayList<>();

            for (int i = 0; i < femmes.size(); i++){
                f.add(femmes.get(i));
                permutations.add(f.get(i).getId());
            }

        }

        for (int i = 0; i < size; i++) {
            allPermutations(femmes,size - 1);
            // if size is odd, swap 0th i.e (first) and
            // (size-1)th i.e (last) element
            if (size % 2 == 1) {
                Femme temp = femmes.get(0);
                femmes.set(0,femmes.get(size-1));
                femmes.set(size-1,temp);
            }
            // If size is even, swap ith
            // and (size-1)th i.e last element
            else {
                Femme temp = femmes.get(i);
                femmes.set(i,femmes.get(size-1));
                femmes.set(size-1,temp);
            }
        }
        return permutations;
    }

    public static void allMariagesStables(ArrayList<Homme> hommes, ArrayList<Femme> femmes, int size) {
        // if size becomes 1 then prints the obtained
        // permutation
        if (size == 1 ){
            ArrayList<Homme> h = new ArrayList<>();
            ArrayList<Femme> f = new ArrayList<>();
            ArrayList<String> couples = new ArrayList<>();
            for (int i = 0; i < femmes.size(); i++){
                //f.get(i).getBounded().set(f.get(i));
                //h.get(i).set(f.get(i));
                h.add(hommes.get(i));
                f.add(femmes.get(i));
                hommes.get(i).setBounded(femmes.get(i));
                femmes.get(i).setBounded(hommes.get(i));
            }
            if(is_Stable(f)){
                for(int i=0; i<h.size();i++){
                    couples.add(h.get(i).getId()+""+f.get(i).getId());
                }
                System.out.println(couples);
                //System.out.println(h);
                //System.out.println(f);
                //System.out.println();
            }
            /*for (int i = 0; i < femmes.size(); i++) { // Loop through every name/phone number combo
                array3.add(hommes.get(i).getId()+""+femmes.get(i).getId());
                //array3.add(hommes.get(i));
                //array3.add(femmes.get(i)); // Concat the two, and add it
            }*/
            //is_Stable(femmes);
            //System.out.println(array3);
        }

        for (int i = 0; i < size; i++) {
            allMariagesStables(hommes,femmes,size - 1);
            // if size is odd, swap 0th i.e (first) and
            // (size-1)th i.e (last) element
            if (size % 2 == 1) {
                Femme temp = femmes.get(0);
                femmes.set(0,femmes.get(size-1));
                femmes.set(size-1,temp);
            }
            // If size is even, swap ith
            // and (size-1)th i.e last element
            else {
                Femme temp = femmes.get(i);
                femmes.set(i,femmes.get(size-1));
                femmes.set(size-1,temp);
            }
        }
    }
}