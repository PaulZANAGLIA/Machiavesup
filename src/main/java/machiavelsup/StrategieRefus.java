package main.java.machiavelsup;

import java.util.ArrayList;

public class StrategieRefus implements Strategie {

    @Override
    public Formation choixParmiAcceptation(Etudiant etudiant) {
        ArrayList<Formation> listePreference = etudiant.getListePreference();
        ArrayList<Formation> listeAttente = etudiant.getListeEnAttente();
        ArrayList<Formation> listeAccepte = etudiant.getListeAcceptation();
        ArrayList<Formation> listeRefus = etudiant.getListeRefus();
        if (listeAccepte.size() > 0) {
            if (listeAttente.size() > 0) {
                int meilleurFormationEnAttente = 100; // init à max int
                int indiceDeLaMeilleurFormationEnAttente = 100;
                for (int i = 0; i < listeAttente.size(); i++) {
                    for (int j = 0; j < listePreference.size(); j++) {
                        if (j < meilleurFormationEnAttente) {
                            meilleurFormationEnAttente = j;
                            indiceDeLaMeilleurFormationEnAttente = i;
                        }
                    }
                }
                int meilleurFormationAccepte = 100; // init à max int
                int indiceDeLaMeilleurFormationAccepte = 100;
                for (int i = 0; i < listeAccepte.size(); i++) {
                    for (int j = 0; j < listePreference.size(); j++) {
                        if (j < meilleurFormationAccepte) {
                            meilleurFormationAccepte = j;
                            indiceDeLaMeilleurFormationAccepte = i;
                        }
                    }
                }
                if (meilleurFormationAccepte < meilleurFormationEnAttente) {
                    return listePreference.get(indiceDeLaMeilleurFormationAccepte);
                } else {
                    return null;
                }
            } else {
                int meilleurFormationAccepte = 100; // init à max int
                int indiceDeLaMeilleurFormationAccepte = 100;
                for (int i = 0; i < listeAccepte.size(); i++) {
                    for (int j = 0; j < listePreference.size(); j++) {
                        if (j < meilleurFormationAccepte) {
                            meilleurFormationAccepte = j;
                            indiceDeLaMeilleurFormationAccepte = i;
                        }
                    }
                }
                return listePreference.get(indiceDeLaMeilleurFormationAccepte);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Formation> choixParmiAttente(Etudiant etudiant) {
        ArrayList<Formation> listePreference = etudiant.getListePreference();
        ArrayList<Formation> listeAttente = etudiant.getListeEnAttente();
        if (listeAttente.size() > 0) {
            int meilleurFormationEnAttente = 100; // init à max int
            int indiceDeLaMeilleurFormationEnAttente = 100;
            for (int i = 0; i < listeAttente.size(); i++) {
                for (int j = 0; j < listePreference.size(); j++) {
                    if (j < meilleurFormationEnAttente) {
                        meilleurFormationEnAttente = j;
                        indiceDeLaMeilleurFormationEnAttente = i;
                    }
                }
            }
            ArrayList<Formation> retour = new ArrayList<>();
            retour.add(listePreference.get(meilleurFormationEnAttente));
           // System.out.println("Garde uniquement : " + listePreference.get(meilleurFormationEnAttente).getId());
            return retour;
        }

        ArrayList<Formation> retour = new ArrayList<>();
        return retour;
    }
}
