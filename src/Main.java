import java.util.ArrayList;
import java.util.Random;

public class Main {
    final static int PLAYSET = 12;

    static Random r = new Random();

    public static void main(String[] args) {

        int seed = r.nextInt();
        //int seed = 56529939;
        ArrayList<Homme> men = new ArrayList<>(); // Empty List of men
        ArrayList<Femme> women = new ArrayList<>(); // Empty List of women

        for(int i = 0; i < PLAYSET; i++){ // Init groups
            Homme m = new Homme(i);
            Femme f = new Femme(i);
            men.add(m);
            women.add(f);
        }

        for(int k = 0; k < PLAYSET; k++){ // For each woman/man, generate random set of prefs
            men.get(k).generateRandomPrefList(PLAYSET, women,seed+k+PLAYSET);
            //seed++;
            women.get(k).generateRandomPrefList(PLAYSET, men,seed+k);
            //seed++;
        }

        /*for(int k = 0; k < PLAYSET; k++){ // Print prefs
            Homme m = men.get(k);
            System.out.println("HOMME " + k);
            m.printPrefList();
            Femme f = women.get(k);
            System.out.println("FEMME " + k);
            f.printPrefList();
        }*/

        checkPermutations(men,women,men.size());
        //Gale_Shapley(men);
        /*System.out.println("Sans tricher " +  is_Stable(women));*/

        /** Regarde si l'instabilité est detecté (sur 3 personnes)*/
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

    /** Fonctions qui affiche toutes les permutations */
    public static void checkPermutations(ArrayList<Homme> hommes, ArrayList<Femme> femmes, int size) {
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
            checkPermutations(hommes,femmes,size - 1);
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
}