package main.java.machiavelsup;

import java.util.ArrayList;
import java.util.List;

public class StrategieA implements Strategie {

    @Override
    public Formation choixParmiAcceptation(Etudiant etudiant) {

        ArrayList<Formation> listeAcceptation =new ArrayList<>(etudiant.getListeAcceptation());
        ArrayList<Formation> listePreference=new ArrayList<>(etudiant.getListePreference());
        if(listeAcceptation.size()==0)
        {
            return null;
        }
        Formation formationMax= listeAcceptation.get(0);
        for(int i = 1 ; i < listeAcceptation.size();i++)
        {
            Formation formation = listeAcceptation.get(i);
            for(int j  = 0 ; i < listePreference.size();j++)
            {
                Formation formationtmp = listePreference.get(j);
                if(formationtmp.getId()==formation.getId())
                {
                    formationMax=formationtmp;
                    break;
                } else if (formationtmp.getId()==formationMax.getId()) {
                    break;
                }
            }
        }
        return formationMax;
    }

    @Override
    public ArrayList<Formation> choixParmiAttente(Etudiant etudiant) {
        ArrayList<Formation> listeAttente = new ArrayList<>(etudiant.getListeEnAttente());
        ArrayList<Formation> listePreference = new ArrayList<>(etudiant.getListePreference());
        ArrayList<Formation> listeAGarder = new ArrayList<>();
        if(listeAttente.size()==0)
        {
            return new ArrayList<>();
        }
        Formation formation = choixParmiAcceptation(etudiant);
        Formation formationMax;
        if(formation==null) // si je ne suis accepter nulle part, je garde toute ma liste d'attente
        {
            return listeAttente;
        }
        else
        {
            formationMax = formation;
        }
        for(int i = 0; i<listeAttente.size();i++)
        {
            for(int j = 0 ; j<listePreference.size();j++)
            {
                if(listePreference.get(j).getId()==formationMax.getId()) {
                    int recherche = j;
                    while (recherche > 0) {
                        recherche--;
                        if (listePreference.get(recherche) == listeAttente.get(i)) {
                            listeAGarder.add(listeAttente.get(i));
                            recherche = 0;
                        }
                    }
                }
            }
        }

        return listeAGarder;
    }
}
//[ 5,  16,  15,  6,  8,  9,  3,  19,  0,  18, ]    preference
// [  8,  15,  ]                                    attente
//  [  18,  0,  19,  3,  ]                          accepte
// choixAcceptation = 3
// choix attente : [ 15 ]