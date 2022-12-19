import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
// Seed 1592
public class Main {
    final static int PLAYSET_etudiant = 100;
    final static int PLAYSET_formation = 20;
    final static int nbr_choix = 10;

    final static int capacite_max_formation = 5;


    public static int init_random(int increment) {
        return 1 + increment;
    }

    public static void main(String[] args) {
        try {
            int SEED = 2;
            FileWriter fichierComparaison = null;
            FileWriter fichierPreferenceEtudiant = null;
            fichierComparaison = new FileWriter("Comparaison.txt");
            fichierPreferenceEtudiant = new FileWriter("PreferenceEtudiant.txt");
            FileWriter fichierPreferenceFormation = null;
            fichierPreferenceFormation = new FileWriter("PreferenceFormation.txt");
            int ameliorationEtudiant0=0;
            int nbrTest;
            for (nbrTest = 0; nbrTest < 1; nbrTest++) {
                SEED++;
                fichierComparaison.write("Seed : " + SEED + " \n");
                fichierPreferenceEtudiant.write("Seed : " + SEED + " \n");
                fichierPreferenceFormation.write("Seed : " + SEED + " \n");
                Strategie strategieA = new StrategieA();
                Strategie strategieRefus = new StrategieRefus();
                ArrayList<Etudiant> ListeEtudiant1 = new ArrayList<>();
                ArrayList<Formation> ListeFormation1 = new ArrayList<>();
                for (int i = 0; i < PLAYSET_formation; i++) {
                    Formation formationi = new Formation(i, capacite_max_formation, 10);
                    ListeFormation1.add(formationi);
                }
                for (int i = 0; i < PLAYSET_etudiant; i++) {
                    Etudiant etudianti = new Etudiant(i, nbr_choix);
                    ListeEtudiant1.add(etudianti);
                }
                ParcoursupEtudiant(ListeEtudiant1, ListeFormation1, strategieA, strategieA, SEED,fichierPreferenceFormation);
                for(int n = 0 ; n < PLAYSET_etudiant ; n ++) {
                    fichierPreferenceEtudiant.write("Etudiant " + n + " : ");
                    ArrayList<Formation> listePref = new ArrayList<>(ListeEtudiant1.get(n).getListePreference());
                    for (int m = 0; m < listePref.size(); m++) {
                        fichierPreferenceEtudiant.write(" "+listePref.get(m).getId() + " ,");
                    }
                    fichierPreferenceEtudiant.write("\n");
                }
                ArrayList<Etudiant> ListeEtudiant2 = new ArrayList<>();
                ArrayList<Formation> ListeFormation2 = new ArrayList<>();
                for (int i = 0; i < PLAYSET_formation; i++) {
                    Formation formationi = new Formation(i, capacite_max_formation, 10);
                    ListeFormation2.add(formationi);
                }
                for (int i = 0; i < PLAYSET_etudiant; i++) {
                    Etudiant etudianti = new Etudiant(i, nbr_choix);
                    ListeEtudiant2.add(etudianti);
                }

                ParcoursupEtudiant(ListeEtudiant2, ListeFormation2, strategieRefus, strategieA, SEED,fichierPreferenceFormation);
                for(int n = 0 ; n < PLAYSET_etudiant ; n++)
                {
                    ArrayList<Formation> listePref2 = new ArrayList<>(ListeEtudiant2.get(n).getListePreference());
                    fichierPreferenceEtudiant.write("Etudiant " + n + " : ");
                    for (int m = 0; m < listePref2.size(); m++) {
                        fichierPreferenceEtudiant.write(" " + listePref2.get(m).getId() + " ,");
                    }
                    fichierPreferenceEtudiant.write("\n");
                }

                for (int i = 0; i < PLAYSET_etudiant; i++) {
                    Etudiant etudiant1 = ListeEtudiant1.get(i);
                    Etudiant etudiant2 = ListeEtudiant2.get(i);
                    if (etudiant1.getListeAcceptation().size() > 0 && etudiant2.getListeAcceptation().size() > 0) {
                        fichierComparaison.write("Etudiant : " + i + " Cas defaut : " + etudiant1.getListeAcceptation().get(0).getId() + " Cas Refus : " + etudiant2.getListeAcceptation().get(0).getId() + "\n");
                        if (etudiant1.getListeAcceptation().get(0).getId() != etudiant2.getListeAcceptation().get(0).getId()) {
                            ArrayList<Formation> liste1 = new ArrayList<>(etudiant2.getListePreference());
                            Formation formationEtu1 = etudiant1.getListeAcceptation().get(0);
                            Formation formationEtu2 = etudiant2.getListeAcceptation().get(0);
                            for (int x = 0; x < liste1.size(); x++) {
                                if (liste1.get(x).getId() == formationEtu1.getId()) {
                                    System.out.println("Cas difference : Echec : Seed :" + SEED + "Etudiant : " + i);
                                    break;
                                } else {
                                    if(i==0)
                                    {
                                        System.out.println("Cas difference : Reussite de l'étudiant 0 : Seed :" + SEED + "Etudiant : " + i);
                                        ameliorationEtudiant0++;
                                    }
                                    else {
                                        System.out.println("Cas difference : Reussite : Seed :" + SEED + "Etudiant : " + i);
                                    }
                                    break;
                                }
                            }
                        }

                    } else if (etudiant1.getListeAcceptation().size() == 0) {
                        fichierComparaison.write("Etudiant : " + i + " Cas defaut : " + "rien" + " Cas Refus : " + etudiant2.getListeAcceptation().get(0).getId() + "\n");
                        if(i==0)
                        {
                            System.out.println("Cas aucun resultat : Reussite de l'étudiant 0 : Seed :" + SEED + "Etudiant : " + i);
                            ameliorationEtudiant0++;
                        }
                        else {
                            System.out.println("Cas aucun resultat : Reussite : Seed :" + SEED + "Etudiant : " + i);
                        }

                    } else if (etudiant2.getListeAcceptation().size() == 0) {
                        fichierComparaison.write("Etudiant : " + i + " Cas defaut : " + etudiant1.getListeAcceptation().get(0).getId() + " Cas Refus : " + "rien" + "\n");
                        System.out.println("Cas echec total : Echec : Seed :" + SEED + "Etudiant : " + i);
                    } else {
                        fichierComparaison.write("Etudiant : " + i + " Cas defaut : " + "rien" + " Cas Refus : " + "rien" + "\n");
                    }
                }
            }
            fichierComparaison.close();
            fichierPreferenceFormation.close();
            fichierPreferenceEtudiant.close();
            System.out.println("L'etudiant 0 c'est amélioré : "+ameliorationEtudiant0+" fois sur "+nbrTest);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void ParcoursupEtudiant(ArrayList<Etudiant> Etudiant,ArrayList<Formation> Formation,Strategie strategieEtudiant0,Strategie strategieReste,int SEED,FileWriter fichierPreferenceFormation) {

        ArrayList<Etudiant> etudiantStratRefus = new ArrayList<>();
        etudiantStratRefus.add(Etudiant.get(0));
        phaseDemande(Etudiant, Formation, SEED);

        phaseTrieInitial(Formation, SEED,fichierPreferenceFormation);
        affichageListeFormation(Formation);

        for(int nbrSemaine = 0 ; nbrSemaine < 10 ; nbrSemaine++) {
            phaseTrie(Formation);
            affichageListeFormation(Formation);
            premierAppel(Etudiant, Formation);
            affichageListeEtudiant(Etudiant);
            applicationStrategie(strategieEtudiant0, etudiantStratRefus);
            applicationStrategie(strategieReste, new ArrayList<>(Etudiant.subList(1, PLAYSET_etudiant)));
            phaseRefus(Etudiant);
        }
       // affichageListeEtudiant(Etudiant);
    }

    public static void phaseRefus(ArrayList<Etudiant> Etudiant)
    {
        for(int i = 0 ; i < Etudiant.size() ; i++)
        {
            ArrayList<Formation> listeRefus = Etudiant.get(i).getListeRefus();
            for(int j = 0 ; j < listeRefus.size() ; j++)
            {
                listeRefus.get(j).addEtudiantRefus(Etudiant.get(i));
            }
        }
    }
    public static void affichageListeEtudiant(ArrayList<Etudiant> Etudiant)
    {
        for(int i = 0 ; i < PLAYSET_etudiant;i++)
        {
            System.out.println("-----------------");
            System.out.println("Etudiant : "+i);
            System.out.print("Preference [ ");
            for(int j = 0; j < Etudiant.get(i).getListePreference().size(); j++)
            {
                System.out.print(" "+Etudiant.get(i).getListePreference().get(j).getId()+", ");
            }
            System.out.println(" ]");
            System.out.print("Accepte [ ");
            for(int j = 0; j < Etudiant.get(i).getListeAcceptation().size(); j++)
            {
                System.out.print(" "+Etudiant.get(i).getListeAcceptation().get(j).getId()+", ");
            }
            System.out.println(" ]");
            System.out.print("Attente[ ");
            for(int j = 0; j < Etudiant.get(i).getListeEnAttente().size(); j++)
            {
                System.out.print(" "+Etudiant.get(i).getListeEnAttente().get(j).getId()+", ");
            }
            System.out.println(" ]");
            System.out.print("Refus[ ");
            for(int j = 0; j < Etudiant.get(i).getListeRefus().size(); j++)
            {
                System.out.print(" "+Etudiant.get(i).getListeRefus().get(j).getId()+", ");
            }
            System.out.println(" ]");
        }
        ///affichage///
    }
    public static void phaseDemande(ArrayList<Etudiant> Etudiant,ArrayList<Formation> Formation,int SEED) {
        for (int i = 0; i < PLAYSET_etudiant; i++) {
            Etudiant.get(i).genererListePreference(Etudiant.get(i).capacite, Formation, SEED + i);
            for (int j = 0; j < Etudiant.get(i).getListePreference().size(); j++) // renomme demande
            {
                Formation.get(Etudiant.get(i).getListePreference().get(j).getId()).add_Etudiant(Etudiant.get(i));
            }
        }
    }
    public static void phaseTrieInitial(ArrayList<Formation> Formation,int SEED,FileWriter fichierPreferenceFormation) {

        for (int i = 0; i < PLAYSET_formation; i++) {
            Formation.get(i).generateListePreference(SEED + i);
            // Formation.get(i).setListeAccepte();
            //Formation.get(i).setListeAttente();
            //Formation.get(i).setListeRefus();
        }
        try{
        for(int i = 0 ; i < PLAYSET_formation ; i++)
        {
            ArrayList<Etudiant> liste = new ArrayList<>(Formation.get(i).getListePreference());
            fichierPreferenceFormation.write("Formation "+i+" :");
            for(int j = 0 ; j < liste.size();j++)
            {
                fichierPreferenceFormation.write(" "+liste.get(j).getId()+" ,");
            }
            fichierPreferenceFormation.write("\n");
        }
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void phaseTrie(ArrayList<Formation> Formation) {

        for (int i = 0; i < PLAYSET_formation; i++) {
            //Formation.get(i).generateListePreference(SEED + i);
            Formation.get(i).setListeAccepte();
            Formation.get(i).setListeAttente();
            Formation.get(i).setListeRefus();
        }
    }
        public static void affichageListeFormation(ArrayList<Formation> Formation) {
            for (int i = 0; i < PLAYSET_formation; i++) {
                System.out.println("Formation : " + i);
                System.out.print("Ordre de Preference : [ ");
                for (int j = 0; j < Formation.get(i).getListePreference().size(); j++) {
                    System.out.print(" " + Formation.get(i).getListePreference().get(j).getId() + ", ");
                }
                System.out.println(" ]");
                System.out.print("Ordre d'acceptation : [ ");

                for (int j = 0; j < Formation.get(i).getListeAcceptation().size(); j++) {
                    System.out.print(" " + Formation.get(i).getListeAcceptation().get(j).getId() + ", ");
                }
                System.out.println(" ]");
                System.out.print("Ordre d'attente : [ ");
                for (int j = 0; j < Formation.get(i).getListeAttente().size(); j++) {
                    System.out.print(" " + Formation.get(i).getListeAttente().get(j).getId() + ", ");
                }
                System.out.println(" ]");
                System.out.print("Ordre de refus : [ ");
                for (int j = 0; j < Formation.get(i).getListeRefus().size(); j++) {
                    System.out.print(" " + Formation.get(i).getListeRefus().get(j).getId() + ", ");
                }
                System.out.println(" ]");
                System.out.println("----------------------");
            }
        }





    /*
    Parcoursup
     */
    public static void premierAppel(ArrayList<Etudiant> Etudiant, ArrayList<Formation> Formation)
    {
        for(int i = 0 ; i < PLAYSET_formation; i++)
        {
            for(int j = 0; j < Formation.get(i).getListeAcceptation().size(); j++)
            {
                Etudiant.get(Formation.get(i).getListeAcceptation().get(j).getId()).add_accepte(Formation.get(i));
            }
            for(int j = 0; j < Formation.get(i).getListeAttente().size(); j++)
            {
                Etudiant.get(Formation.get(i).getListeAttente().get(j).getId()).add_attente(Formation.get(i),j);
            }
            for(int j = 0; j < Formation.get(i).getListeRefus().size(); j++)
            {
                Etudiant.get(Formation.get(i).getListeRefus().get(j).getId()).add_refus(Formation.get(i));
            }
        }
    }

    public static void applicationStrategie(Strategie strategie, ArrayList<Etudiant> Etudiant)
    {
        //
        for(int i = 0 ; i < Etudiant.size();i++) // tout les etudiant
        {
            ArrayList<Formation> listeAttente = strategie.choixParmiAttente(Etudiant.get(i));
            //ArrayList<Integer> listePositionAttente = new ArrayList<>(Etudiant.get(i).getListePositionListeAttente());
            ArrayList<Formation> formationRefuse = new ArrayList<>();
            for(int k = 0 ; k < Etudiant.get(i).getListeEnAttente().size();k++) {
                boolean aGarder=false;
                for (int j = 0; j < listeAttente.size(); j++) {
                    if( listeAttente.get(j).equals(Etudiant.get(i).getListeEnAttente().get(k)))
                    {
                        aGarder=true;
                        break;
                    }
                }
                if(!aGarder)
                {
                   // listePositionAttente.remove(k);
                    formationRefuse.add(Etudiant.get(i).getListeEnAttente().get(k));
                }
            }
          //  Etudiant.get(i).setListePositionListeAttente(listePositionAttente);
            Etudiant.get(i).set_List_refus(formationRefuse);
            Etudiant.get(i).set_List_attente(listeAttente);
            ///// acceptation
            Formation formationAccepte = strategie.choixParmiAcceptation(Etudiant.get(i));
            ArrayList<Formation> formationAcceptation = new ArrayList<>(Etudiant.get(i).getListeAcceptation());
            if(formationAccepte!=null)
            {
                for(int j = 0 ; j < Etudiant.get(i).getListeAcceptation().size(); j++) // toute la liste acceptation d'un étudiant
                {
                    if(formationAccepte.getId() != Etudiant.get(i).getListeAcceptation().get(j).getId() )
                    {
                        formationRefuse.add(Etudiant.get(i).getListeAcceptation().get(j));
                        formationAcceptation.remove(Etudiant.get(i).getListeAcceptation().get(j));
                    }
                }
                Etudiant.get(i).set_List_refus(formationRefuse);
                Etudiant.get(i).set_List_accepte(formationAcceptation);
            }
            else
            {
                for(int x = 0 ; x < formationAcceptation.size();x++ )
                {
                    formationRefuse.add(formationAcceptation.get(x));
                }
                Etudiant.get(i).set_List_refus(formationRefuse);
                Etudiant.get(i).set_List_accepte(new ArrayList<Formation>());
            }
        }
    }
}
