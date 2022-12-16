import java.util.ArrayList;

public class Main {
    final static int PLAYSET = 3;

    public static void main(String[] args) {
        ArrayList<Homme> men = new ArrayList<>(); // Empty List of men
        ArrayList<Femme> women = new ArrayList<>(); // Empty List of women

        for(int i = 0; i < PLAYSET; i++){ // Init groups
            Homme m = new Homme(i);
            Femme f = new Femme(i);
            men.add(m);
            women.add(f);
        }

        for(int k = 0; k < PLAYSET; k++){ // For each woman/man, generate random set of prefs
            men.get(k).generateRandomPrefList(PLAYSET, women);
            women.get(k).generateRandomPrefList(PLAYSET, men);
        }

        for(int k = 0; k < PLAYSET; k++){ // Print prefs
            Homme m = men.get(k);
            System.out.println("HOMME " + k);
            m.printPrefList();
            Femme f = women.get(k);
            System.out.println("FEMME " + k);
            f.printPrefList();
        }

        galeShapley(men);
        System.out.println("Sans tricher " +  isStable(women));

        /* Regarde si l'instabilité est detecté (sur 3 personnes) */
        Femme f = men.get(2).getBounded();
        men.get(2).set(men.get(1).getBounded());
        men.get(2).getBounded().set(men.get(2));
        f.set(men.get(1));
        men.get(1).set(f);

        Femme f2 = men.get(2).getBounded();
        men.get(2).set(men.get(0).getBounded());
        men.get(2).getBounded().set(men.get(2));
        f2.set(men.get(0));
        men.get(0).set(f2);

        System.out.println("En trichant " +  isStable(women));

        for(int k = 0; k < men.size(); k++){ // Print couples
            Homme m = men.get(k);
            System.out.println("HOMME " + m.getId() + " & FEMME " + m.getBounded().getId());
        }

    }

    /** Gale_Shapley algo, procédure mariage stable classique */
    public static void galeShapley(ArrayList<Homme> hommes){
        ArrayList<Homme> Q = new ArrayList<>();
        Q.addAll(hommes);

        //System.out.println("Q size = " + Q.size());
        while (Q.size() > 0){
            Homme homme = Q.remove(0);
            if(homme.getBounded() == null) {
                Femme pref = homme.getPrefList();
                Homme evince = homme.ask(pref);
                if(evince != null){
                    //System.out.println("homme = " + evince.getId() + " evince");
                    evince.removeFirstPrefList();
                    evince.set(null);
                    Q.add(evince);
                }
            }
        }

        for(int k = 0; k < hommes.size(); k++){ // Print couples
            Homme m = hommes.get(k);
            System.out.println("HOMME " + m.getId() + " & FEMME " + m.getBounded().getId());
        }
    }

    public static boolean isStable(ArrayList<Femme> femmes){ // on passe l'array femme car elle garde intacte la liste des hommes

        for(Femme f : femmes){ // Pour chaque femme, vérifier si elle est en situation de jalousie justifiée
            Homme bound = f.getBounded();

            for(Homme h : f.getList()){ // Pour chaque homme
                if(h.getId() == bound.getId()) break;

                // S'il préfère la femme courante, regarde s'il y a une situation de jalousie justifée
                if(h.getList().indexOf(f) < h.getList().indexOf(h.getBounded())){
                    h.printPrefList();
                    System.out.println("f " + f.getId() + " courante " +  h.getList().indexOf(f) + "femme " + h.getList().indexOf(h.getBounded()));
                    return false;
                }
            }
        }

        return true;
    }
}