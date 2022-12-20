package main.java.machiavelsup;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

public class StrategieRefus implements Strategie {
    @Override
    public Formation choixParmiAcceptation(Etudiant etudiant) {
        if(etudiant.getListeAcceptation().size()==0)
        {
            return null;
        }
        if(etudiant.getListeEnAttente().size()==0)
        {
            StrategieA strategieA= new StrategieA();
            return strategieA.choixParmiAcceptation(etudiant); // si pas de liste d'attente, pas besoin de retourner le min entre attente et accepte
        }
        Function<Formation, Integer> indexPref = (formation -> etudiant.getListePreference().indexOf(formation));
        Stream<Integer> listeIndexAttentePref = etudiant.getListeEnAttente().stream().map(indexPref);
        Stream<Integer> listeIndexAcceptePref = etudiant.getListeAcceptation().stream().map(indexPref);
        int minIndexAttente = listeIndexAttentePref.min(Integer::compare).get();
        int minIndexAccepte = listeIndexAcceptePref.min(Integer::compare).get();
        if(minIndexAccepte < minIndexAttente)
        {
            return etudiant.getListeAcceptation().get(minIndexAccepte);
        }
        return null; // si la bonne formation est dans la liste d'attente, on retourne rien.
        //return etudiant.getListePreference().get(Math.min(minIndexPref,minIndexAttente));
    }
    @Override
    public ArrayList<Formation> choixParmiAttente(Etudiant etudiant) {
        if(etudiant.getListeEnAttente().size()==0) // si pas de liste d'attente, retourne liste vide
        {
            ArrayList<Formation> retourNull = new ArrayList<>();
            return retourNull;
        }
        if(etudiant.getListeAcceptation().size()==0) // si pas de choix accepter, retourne le min des choix en attente;
        {
            Function<Formation, Integer> indexPref = (formation -> etudiant.getListePreference().indexOf(formation));
            Stream<Integer> listeIndexAttentePref = etudiant.getListeEnAttente().stream().map(indexPref);
            int minIndexAttente = listeIndexAttentePref.min(Integer::compare).get();
            ArrayList<Formation> retour = new ArrayList<>();
            retour.add(etudiant.getListePreference().get(minIndexAttente));
            return retour;
        } // sinon retourne le min entre attente et accepte, comme choixAcceptation;
        Function<Formation, Integer> indexPref = (formation -> etudiant.getListePreference().indexOf(formation));
        Stream<Integer> listeIndexAttentePref = etudiant.getListeEnAttente().stream().map(indexPref);
        Stream<Integer> listeIndexAcceptePref = etudiant.getListeAcceptation().stream().map(indexPref);
        int minIndexAttente = listeIndexAttentePref.min(Integer::compare).get();
        int minIndexAccepte = listeIndexAcceptePref.min(Integer::compare).get();
        if(minIndexAccepte < minIndexAttente)
        {
            return null; // c'est que choixParmiAccepte a déjà retourné le meilleur choix parmi les deux
        }
        ArrayList<Formation> retour = new ArrayList<>();
        retour.add(etudiant.getListePreference().get(minIndexAttente));
        return retour;
    }
}
