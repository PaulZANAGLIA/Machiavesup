import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Integer.valueOf;
/*Liste d'attente,
*   Les formations doivent avoir des capacité !!!!!! ; _ ;
* Phase appel :
* -Etudiant previennent les formations qu'elles sont souhaités
* -formation font leur liste d'appel + liste d'attente + liste de refus
* -les formations previennent les élèves de quel sont leur liste
* Phase acceptation / refus
* - Les etudiants recoivent leur acceptation / attente / refus et doivent décidé quoi refuser pour améliorer leur choix si possible
* Modelisation de l'algo parcoursup
*
*  */
public class Main {
    final static int PLAYSET =3;
    public static int init_random(int increment)
    {
        return 1+increment;
    }
    public static void main(String[] args) {
        try {
            int nbr_test_total=100;
            FileWriter Amelioration = new FileWriter("Amelioration.txt");
            FileWriter Egalite = new FileWriter("Egalite.txt");
            FileWriter Deception = new FileWriter("Deception.txt");
            FileWriter Cas_Reference = new FileWriter("Cas_Reference.txt");
            long startTime = System.currentTimeMillis();
            ArrayList<Homme> men = new ArrayList<>(); // Empty List of men
            ArrayList<Femme> women = new ArrayList<>(); // Empty List of women
            for (int i = 0; i < PLAYSET; i++) { // Init groups
                Homme m = new Homme(i);
                Femme f = new Femme(i);
                men.add(m);
                women.add(f);
            }
            Homme OMEGA_H = new Homme(PLAYSET);
            Femme OMEGA_F = new Femme(PLAYSET);
            men.add(OMEGA_H);
            women.add(OMEGA_F);
            ArrayList<Integer> seed_ameliorable=new ArrayList<>();
            ArrayList<Integer> femme_ameliorable=new ArrayList<>();
            for (int change_women = 0; change_women < 1; change_women++) { // Le code est relancé pour les femmes d'indice 0 a 1-1 ( ici uniquement sur femme 0, si change_women < 2, relancer pour femme 0 et 1
                int indice_avec_triche; // indice de l'union avec triche;
                int nombre_amelioration = 0; // nombre de fois que la femme s'améliore avec la triche
                int nombre_deception = 0; // nombre de fois que la femme empire son cas
                int nombre_egalite = 0; // nombre de fois ou le résultat est identique
                int nombre_de_permutation = 0; // plutot que de calculer PLAYSET! on incremente juste à chaque permutation, même resultat, pas la même complexité :p
                int ameliorer_ou_pas;
                int seed_ameliorer=0;
                Cas_Reference.write("--F"+change_women+"--\n");
                Amelioration.write("--F"+change_women+"--\n");
                Deception.write("--F"+change_women+"--\n");
                Egalite.write("--F"+change_women+"--\n");
                for (int nbr_test = 0; nbr_test < nbr_test_total; nbr_test++) { // seed de 0 a nbr_test_total
                    int PLAYSET=3;
                    int IsAmeliorable=0;
                    int SEED = init_random(nbr_test);
                    for (int k = 0; k < PLAYSET; k++) { // For each woman/man, generate random set of prefs
                        men.get(k).generateRandomPrefList(PLAYSET, women, SEED + k + PLAYSET);
                        women.get(k).generateRandomPrefList(PLAYSET, men, SEED + k);
                    }
                    //Homme OMEGA_H = new Homme(PLAYSET);
                    //Femme OMEGA_F = new Femme(PLAYSET);
                    OMEGA_H.generateRandomPrefList(PLAYSET, women, SEED - 2);
                    OMEGA_H.getListEff().add(OMEGA_F);
                    OMEGA_F.generateRandomPrefList(PLAYSET, men, SEED - 1);
                    OMEGA_F.getListEff().add(OMEGA_H);
                    for(int k = 0; k < PLAYSET; k++){
                        Homme m = men.get(k);
                        m.getListEff().add(OMEGA_F);
                        m.getList_de_base().add(OMEGA_F);
                        Femme f = women.get(k);
                        f.getListEff().add(OMEGA_H);
                        f.getList_de_base().add(OMEGA_H);
                    }
                    PLAYSET=4; // doit être égal à PLAYSET  + Omega , soit +1. Mais ne peux pas être se voir affecter PLAYSET+1 sinon celà serait un incrément
                    //////////
                    //// copie de la liste obtenu pour la femme 0 dans un tableau qui va ensuite être permuter
                    int arr[] = new int[PLAYSET];
                    ArrayList<Homme> listdhomme = women.get(0).getListEff();
                    for (int i = 0; i < PLAYSET; i++) {
                        arr[i] = listdhomme.get(i).getId();
                    }
                    Gale_Shapeley(men);  /// gale shapley initial.
                    // int resultat_sans_triche = women.get(0).getBounded().getId();  /// resultat de la femme 0 sans triche
                    int indice_resultat_sans_triche = valueOf(women.get(change_women).getAndSetIndidceBoundedTo(PLAYSET)); // indice de ce resultat, plus simple à vérifié, évite un O(n)^2
                    Cas_Reference.write("--S "+SEED+"--"+"F "+change_women+"\n");
                    for(int k = 0; k < men.size(); k++){ // Print couples
                        Homme m = men.get(k);
                        Cas_Reference.write("H " + m.getId() + " & F" + m.getBounded().getId()+"\n");
                    }
                    for(int k = 0;k<women.size();k++)
                    {
                        Femme F = women.get(k);
                        Cas_Reference.write("F "+k+" [ ");
                        for(Homme h : F.List_eff){
                            Cas_Reference.write(h.getId()+", ");
                        }
                        Cas_Reference.write("]\n");
                    }
                    for(int k = 0;k<men.size();k++)
                    {
                        Homme H = men.get(k);
                        Cas_Reference.write("H "+k+" [ ");
                        for(Femme F : H.List_eff){
                            Cas_Reference.write(F.getId()+", ");
                        }
                        Cas_Reference.write("]\n");
                    }
                    /////////////////////////////TRICHE////////////:
                    //  System.out.println("TRICHE");
                    for (int i = 0; i < PLAYSET; i++) {
                        men.get(i).clear();
                        women.get(i).clear();
                    }
                    AllPermutation perm = new AllPermutation(arr);
                    perm.GetFirst();
                    ArrayList<Homme> pref = new ArrayList<Homme>();
                    for (int i = 0; i < PLAYSET; i++) {
                        pref.add(men.get(arr[i])); // création de la nouvelle liste grace à la permutation
                    }
                    women.get(0).setEffPrefList(pref);
                    Gale_Shapeley(men); // 1ere permutation ( hors boucle )
                    nombre_de_permutation++;
                    indice_avec_triche = valueOf(women.get(change_women).getAndSetIndidceBoundedTo(PLAYSET));
                    ameliorer_ou_pas = women.get(change_women).isItBetter(indice_resultat_sans_triche);
                    //  System.out.println("Indice avec triche :"+indice_avec_triche+"\nameliorer ou pas :"+ameliorer_ou_pas+"\nSans triche : "+indice_resultat_sans_triche);
                    switch (ameliorer_ou_pas) {
                        case 1:
                            if(IsAmeliorable==0)
                            {
                                seed_ameliorer++;
                                IsAmeliorable=1;
                                seed_ameliorable.add(SEED);
                                femme_ameliorable.add(change_women);
                            }
                            nombre_amelioration++;
                            Amelioration.write("--P "+nombre_de_permutation+"\n");
                            Amelioration.write("--S "+SEED+"--"+"F "+change_women+"\n");
                            for(int k = 0; k < PLAYSET; k++){ // Print couples
                                Homme m = men.get(k);
                                Amelioration.write("H " + m.getId() + " & F " + m.getBounded().getId()+"\n");
                            }
                            for(int k = 0;k<PLAYSET;k++)
                            {
                                Femme F = women.get(k);
                                Amelioration.write("F "+k+" [ ");
                                for(Homme h : F.List_eff){
                                    Amelioration.write(h.getId()+", ");
                                }
                                Amelioration.write("]\n");
                            }
                            break;
                        case 0:
                            nombre_egalite++;
                            Egalite.write("--P "+nombre_de_permutation+"\n");
                            Egalite.write("--S "+SEED+"--"+"F "+change_women+"\n");
                            for(int k = 0; k < PLAYSET; k++){ // Print couples
                                Homme m = men.get(k);
                                Egalite.write("H " + m.getId() + " & F " + m.getBounded().getId()+"\n");
                            }
                            for(int k = 0;k<PLAYSET;k++)
                            {
                                Femme F = women.get(k);
                                Egalite.write("F "+k+" [ ");
                                for(Homme h : F.List_eff){
                                    Egalite.write(h.getId()+", ");
                                }
                                Egalite.write("]\n");
                            }
                            break;
                        case -1:
                            nombre_deception++;
                            Deception.write("--P "+nombre_de_permutation+"\n");
                            Deception.write("--S "+SEED+"--"+"F "+change_women+"\n");
                            for(int k = 0; k < men.size(); k++){ // Print couples
                                Homme m = men.get(k);
                                Deception.write("H " + m.getId() + " & F " + m.getBounded().getId()+"\n");
                            }
                            for(int k = 0;k<PLAYSET;k++)
                            {
                                Femme F = women.get(k);
                                Deception.write("F "+k+" [ ");
                                for(Homme h : F.List_eff){
                                    Deception.write(h.getId()+", ");
                                }
                                Deception.write("]\n");
                            }
                    }
                    for (int i = 0; i < PLAYSET; i++) {
                        men.get(i).clear();
                        women.get(i).clear();
                    }
                    while (perm.HasNext()) {
                        pref.clear();
                        perm.GetNext();
                        arr = perm.getIndexes();
                        for (int i = 0; i < PLAYSET; i++) {
                            pref.add(men.get(arr[i]));
                        }
                        women.get(change_women).setEffPrefList(pref);
                        for (int i = 0; i < PLAYSET; i++) {
                            men.get(i).clear();
                            women.get(i).clear();
                        }
                        Gale_Shapeley(men);
                        nombre_de_permutation++;
                        //indice_avec_triche = valueOf(women.get(change_women).getAndSetIndidceBoundedTo(PLAYSET));
                        ameliorer_ou_pas = women.get(change_women).isItBetter(indice_resultat_sans_triche);
                        //  System.out.println("Indice avec triche :"+indice_avec_triche+"\nameliorer ou pas :"+ameliorer_ou_pas+"\nSans triche : "+indice_resultat_sans_triche);
                        switch (ameliorer_ou_pas) {
                            case 1:
                                if(IsAmeliorable==0)
                                {
                                    seed_ameliorer++;
                                    IsAmeliorable=1;
                                    seed_ameliorable.add(SEED);
                                    femme_ameliorable.add(change_women);
                                }
                                nombre_amelioration++;
                                Amelioration.write("--P "+nombre_de_permutation+"\n");
                                Amelioration.write("--S "+SEED+"--"+"F "+change_women+"\n");
                                for(int k = 0; k < men.size(); k++){ // Print couples
                                    Homme m = men.get(k);
                                    Amelioration.write("H " + m.getId() + " & F " + m.getBounded().getId()+"\n");
                                }
                                for(int k = 0;k<women.size();k++)
                                {
                                    Femme F = women.get(k);
                                    Amelioration.write("F "+k+" [ ");
                                    for(Homme h : F.List_eff){
                                        Amelioration.write(h.getId()+", ");
                                    }
                                    Amelioration.write("]\n");
                                }
                                break;
                            case 0:
                                nombre_egalite++;
                                Egalite.write("--P "+nombre_de_permutation+"\n");
                                Egalite.write("--S "+SEED+"--"+"F "+change_women+"\n");
                                for(int k = 0; k < men.size(); k++){ // Print couples
                                    Homme m = men.get(k);
                                    Egalite.write("H " + m.getId() + " & F " + m.getBounded().getId()+"\n");
                                }
                                for(int k = 0;k<women.size();k++)
                                {
                                    Femme F = women.get(k);
                                    Egalite.write("F "+k+" [ ");
                                    for(Homme h : F.List_eff){
                                        Egalite.write(h.getId()+", ");
                                    }
                                    Egalite.write("]\n");
                                }
                                break;
                            case -1:
                                nombre_deception++;
                                Deception.write("--P "+nombre_de_permutation+"\n");
                                Deception.write("--S "+SEED+"--"+"F "+change_women+"\n");
                                for(int k = 0; k < men.size(); k++){ // Print couples
                                    Homme m = men.get(k);
                                    Deception.write("H " + m.getId() + " & F " + m.getBounded().getId()+"\n");
                                }
                                for(int k = 0;k<women.size();k++)
                                {
                                    Femme F = women.get(k);
                                    Deception.write("F "+k+" [ ");
                                    for(Homme h : F.List_eff){
                                        Deception.write(h.getId()+", ");
                                    }
                                    Deception.write("]\n");
                                }
                        }
                        for (int i = 0; i < PLAYSET; i++) {
                            men.get(i).clear();
                            women.get(i).clear();
                        }
                        OMEGA_F.clear();
                        OMEGA_H.clear();
                    }
                    for (int i = 0; i < PLAYSET; i++) {
                        men.get(i).clear();
                        women.get(i).clear();
                    }
                    OMEGA_F.clear();
                    OMEGA_H.clear();
                }
                double pourcentage_amelioration;
                double pourcentage_deception;
                double pourcentage_egalite;
                if (nombre_amelioration > 0) {
                    pourcentage_amelioration = ((double) nombre_amelioration / (double) nombre_de_permutation) * 100.0;
                } else {
                    pourcentage_amelioration = 0;
                }
                if (nombre_deception > 0) {
                    pourcentage_deception = ((double) nombre_deception / (double) nombre_de_permutation) * 100.0;
                } else {
                    pourcentage_deception = 0;
                }
                if (nombre_egalite > 0) {
                    pourcentage_egalite = ((double) nombre_egalite / (double) nombre_de_permutation) * 100;
                } else {
                    pourcentage_egalite = 0;
                }
                System.out.println("-----------------Cas " + change_women + "-----------------------------");
                System.out.println("Amelioration : " + nombre_amelioration + "\nEgalité : " + nombre_egalite + "\nDeception : " + nombre_deception + "\nPermutation : " + nombre_de_permutation);
                System.out.println("Amelioration : " + pourcentage_amelioration + " %\nEgalité : " + pourcentage_egalite + " %\nDeception : " + pourcentage_deception + " %\nPermutation : " + nombre_de_permutation);
                System.out.println("Nombre de seed avec une amélioration : "+seed_ameliorer+" "+((double)seed_ameliorer/(double)nbr_test_total)*100+" %");
            }
            int seedAmeliorer=seed_ameliorable.size();
            for(int i = 0;i<seedAmeliorer;i++)
            {
               Cas_Reference.write("--------\nFemme ameliorable :"+femme_ameliorable.get(i)+" avec la seed"+seed_ameliorable.get(i)+"\n");
            }
            Amelioration.close();
            Deception.close();
            Egalite.close();
            Cas_Reference.close();
            System.out.println("Sur : "+nbr_test_total+" seed "+seedAmeliorer+" sont améliorables, soit "+((double)seedAmeliorer/(double)nbr_test_total)*100+" %");
            System.out.println("Operation took " + (System.currentTimeMillis() - startTime) + " milliseconds");
        }catch(IOException e){
            System.out.println("oopsie");
            e.printStackTrace();
        }
    }
    /** Gale_Shapeley algo, procédure mariage stable classique
     * le mariage devrai être interne à l'algo.*/
    public static void Gale_Shapeley(ArrayList<Homme> hommes){
        ArrayList<Homme> Q = new ArrayList<>();
        Q.addAll(hommes);
       // System.out.println("Q size = " + Q.size());
        int count = 0;
        while (Q.size() > 0){
            Homme homme = Q.remove(0);
           // System.out.println("Count " + count + " homme = " + homme.getId());
            if(homme.getBounded() == null) {
                Femme pref = homme.getFirstList();
                Homme evince = homme.ask(pref);
                if(evince != null){
             //       System.out.println("homme = " + evince.getId() + " evince");
                    evince.removeFirstPrefList();
                    evince.set(null);
                    Q.add(evince);
                }
                count++;
            }
        }
    }
}