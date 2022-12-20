package main.java.machiavelsup;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StrategieA implements Strategie {


    /*
public Formation choixParmiAcceptation(Etudiant etudiant)
    {
        int[] tab = {};
        ArrayList<Formation> listeAccepte = new ArrayList<>(etudiant.getListeAcceptation());
        ArrayList<Formation> listePreference = new ArrayList<>(etudiant.getListePreference());
        for(int i = 0 ; i < listeAccepte.size(); i ++)
        {
            tab[i] = listePreference.indexOf(listeAccepte.get(i));
        }
        return listePreference.get(Arrays.stream(tab).min().getAsInt());
    }*/
    @Override
    public Formation choixParmiAcceptation(Etudiant etudiant)
    {
        if(etudiant.getListeAcceptation().size()==0)
        {
            return null;
        }
        Function<Formation, Integer> indexPref = (formation -> etudiant.getListePreference().indexOf(formation));
        Stream<Integer> listIndexPref = etudiant.getListeAcceptation().stream().map(indexPref);
        int minIndex = listIndexPref.min(Integer::compare).get();
        return etudiant.getListePreference().get(minIndex);
    }
    @Override
    public ArrayList<Formation> choixParmiAttente(Etudiant etudiant)
    {
        if(etudiant.getListeEnAttente().size()==0)
        {
            ArrayList<Formation> retourNull = new ArrayList<>();
            return retourNull;
        }
        Formation formationAccepte = this.choixParmiAcceptation(etudiant);
        if(formationAccepte == null)
        {
            return etudiant.getListeEnAttente(); // si je suis accepter nulle part, je garde tout
        }

        int indiceMeilleurFormationActuel = etudiant.getListePreference().indexOf(formationAccepte);
        return new ArrayList<>(etudiant.getListeEnAttente().stream().filter(c-> etudiant.getListePreference().indexOf(c) < indiceMeilleurFormationActuel).collect(Collectors.toList()));
    }

    /*public ArrayList<Formation> choixParmiAttente(Etudiant etudiant) {
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
    }*/
}
//[ 5,  16,  15,  6,  8,  9,  3,  19,  0,  18, ]    preference
// [  8,  15,  ]                                    attente
//  [  18,  0,  19,  3,  ]                          accepte
// choixAcceptation = 3
// choix attente : [ 15 ]