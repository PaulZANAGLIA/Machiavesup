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
        ArrayList<Formation> retour = new ArrayList<>();
        if(etudiant.getListeEnAttente().size()==0) // si pas de liste d'attente, retourne liste vide
        {
            return retour;
        }
        Function<Formation, Integer> indexPref = (formation -> etudiant.getListePreference().indexOf(formation));
        Stream<Integer> listeIndexAttentePref = etudiant.getListeEnAttente().stream().map(indexPref);
        int minIndexAttente = listeIndexAttentePref.min(Integer::compare).get();
        if(!etudiant.getListeAcceptation().isEmpty()) // si pas de choix accepter, retourne le min des choix en attente;
        {
            Stream<Integer> listeIndexAcceptePref = etudiant.getListeAcceptation().stream().map(indexPref);
            int minIndexAccepte = listeIndexAcceptePref.min(Integer::compare).get();
            if(minIndexAccepte < minIndexAttente)
            {
                return retour; // c'est que choixParmiAccepte a déjà retourné le meilleur choix parmi les deux
            }
        } // sinon retourne le min entre attente et accepte, comme choixAcceptation;

        retour.add(etudiant.getListePreference().get(minIndexAttente));
        return retour;
    }
}
