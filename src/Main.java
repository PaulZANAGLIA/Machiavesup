import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    final static int PLAYSET = 7;

    static int amelioration = 0;
    static int deception = 0;
    static int egalite = 0;

    public static void main(String[] args) throws Exception{

        FileWriter mariagesStables = new FileWriter("Mariages-Stables");
        FileWriter galeShapley = new FileWriter("Gale-Shapley");

        for(int j=0;j<40000;j++){
            /**     Creation de la liste d'Hommmes et la liste de Femmes, initialement vides    */
            ArrayList<Homme> men = new ArrayList<>(); // Empty List of men
            ArrayList<Femme> women = new ArrayList<>(); // Empty List of women

            int seed = j;
            //System.out.println("**************** SEED "+seed+" ************");
            //mariagesStables.write("\n---- Seed numéro " + seed +" -----\n");

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
                women.get(k).generateRandomPrefList(PLAYSET, men, seed + k);
            }

            /** Affichage des listes de preferences pour chaque Human */
            //affichage_Listes_Pref(men,women);

            /** Gale Shapley */
            //System.out.println("------ GALE SHAPLEY ------");
            GaleShapley algorithme = new GaleShapley();
            algorithme.Gale_Shapley(men);

            Homme mariGaleShapley[] = new Homme[PLAYSET];
            for(int i =0; i< mariGaleShapley.length;i++){
                mariGaleShapley[women.get(i).getId()] = women.get(i).getBounded();
                //System.out.println("Pour Femme "+i+" son mari est "+mariGaleShapley[i].getId());
            }
            // Nettoyage
            nettoyer(men,women);


            /** Mariages Stables */
            //System.out.println("-------  MARIAGES STABLES  -------");
            MariagesStables mariagesStables1 = new MariagesStables();
            mariagesStables1.allMariagesStables(men,women,men.size());


            // Nettoyage
            nettoyer(men,women);

            /** Gale Shapley inversé */
            //System.out.println("-------  GALE SHAP INV  -------");
            algorithme.Gale_Shapley_inverse(women); //Femmes qui proposent

            Homme mariGaleShapleyInverse[] = new Homme[PLAYSET];
            for(int i =0; i< mariGaleShapleyInverse.length;i++){
                mariGaleShapleyInverse[women.get(i).getId()] = women.get(i).getBounded();
                //System.out.println("Pour Femme "+i+" son mari est "+mariGaleShapleyInverse[i].getId());
            }

            for(int i=0 ; i<PLAYSET;i++){
                Femme f = women.get(i);
                //System.out.println("femme.get "+f.getId());
                if(f.getPrefList().indexOf(mariGaleShapleyInverse[f.getId()])<f.getPrefList().indexOf(mariGaleShapley[f.getId()])){
                    //System.out.println("Amelioration pour femme "+f.getId());
                    amelioration++;
                } else if(f.getPrefList().indexOf(mariGaleShapleyInverse[f.getId()])==f.getPrefList().indexOf(mariGaleShapley[f.getId()])){
                    //System.out.println("Egalite pour femme "+f.getId());
                    egalite++;
                }
                else {
                    //System.out.println("Deception pour femme "+f.getId());
                    deception++;
                }

            }


            nettoyer(men,women);



            /*System.out.println("PRIIIIIIIIINT");
            ArrayList<Femme> w = new ArrayList<>();  // Femmes avec la list de pref modifiee

            for(int k=0; k<PLAYSET;k++){  //set les pref lists
                Femme temp = women.get(k);
                for(int i=0 ; i<temp.pref_list.size();i++){
                    Homme mari = temp.getBounded();
                    if(mari == temp.pref_list.get(i)){
                        temp.pref_list.remove(i);
                        temp.pref_list.add(0,mari);
                        w.add(temp);
                    }
                }
            }*/
            /*for(int k=0; k<PLAYSET;k++){
                Femme f = w.get(k);
                System.out.println("FEMME "+ k + " est mariee a "+ f.getBounded().getId());
            }*/

            //System.out.println("Pref apres modification");

            /*for(int k = 0; k < PLAYSET; k++){ // Print prefs
                Homme m = men.get(k);
                System.out.println("HOMME " + k);
                galeShapley.write("H"+k+" " + m.pref_list+"\n");

                m.printPrefList();
                Femme f = w.get(k);
                System.out.println("FEMME " + k);
                //f.setPrefList();
                galeShapley.write("F"+k+" " + f.pref_list+"\n");
                galeShapley.write("\n");
                f.printPrefList();
            }

            for(int k = 0; k < PLAYSET; k++){
                men.get(k).clear();
                women.get(k).clear();
            }
            Gale_Shapley(men);

            System.out.println();
            System.out.println();*/

            /*ArrayList<Integer> list = new ArrayList<>();

            for(int i = 0; i <10 ; i++){
                list.add(i);
            }

            for(int i=0;i<10;i++){
                if(list.get(i)==2){
                    list.remove(i);
                    list.add(0,2);
                }
            }



            System.out.println(list);*/


            /*
            System.out.println();
            for(int k =0; k<PLAYSET; k++){
                Femme f = women.get(k);
                ArrayList<Homme> pref2 = new ArrayList<>();
                pref2.add(f.getBounded());
                for(int i = 0 ; i<f.pref_list.size();i++){
                    if(f.pref_list.get(i).getId() != f.getBounded().getId()){
                        pref2.add(f.pref_list.get(i));
                    }
                }
                f.setPrefList(pref2);
                f.printPrefList();

                //System.out.println();
                //System.out.println("Femme" + k);
                //f.printPrefList();
            }*/


            //System.out.println("Sans tricher " +  is_Stable(women));

            //Gale_Shapley(men);

        }

        System.out.println("Amelioration "+amelioration);
        System.out.println("Egalite "+egalite);
        System.out.println("Deception "+deception);

        mariagesStables.close();
        galeShapley.close();
    }
    public static void checkAmelioration(ArrayList<Homme> men, ArrayList<Femme> women){

    }

    /** Gale_Shapley algo, procédure mariage stable classique */
    public static void affichage_Listes_Pref(ArrayList<Homme> men, ArrayList<Femme> women){
        for(int k = 0; k < PLAYSET; k++){ // Print prefs
            Homme m = men.get(k);
            System.out.println("HOMME " + k);
            m.printPrefList();

            Femme f = women.get(k);
            System.out.println("FEMME " + k);
            f.printPrefList();
        }
    }

    public static void nettoyer(ArrayList<Homme> men, ArrayList<Femme> women){
        for(int k = 0; k < PLAYSET; k++){
            men.get(k).clear();
            women.get(k).clear();
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