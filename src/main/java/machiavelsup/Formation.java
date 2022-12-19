package main.java.machiavelsup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Formation {
    protected int id;
    protected int capacite;
    protected ArrayList<Etudiant> listePreference;
    protected ArrayList<Etudiant> listeAcceptation;
    protected ArrayList<Etudiant> listeAttente;
    protected ArrayList<Etudiant> listeDemande;
    protected ArrayList<Etudiant> listeRefus;
    protected int capacite_attente;

    public ArrayList<Etudiant> getListePreference() {
        return listePreference;
    }

    public ArrayList<Etudiant> getListeAcceptation() {
        return listeAcceptation;
    }

    public ArrayList<Etudiant> getListeAttente() {
        return listeAttente;
    }

    public ArrayList<Etudiant> getListeDemande() {
        return listeDemande;
    }

    public ArrayList<Etudiant> getListeRefus() {
        return listeRefus;
    }

    public Formation(int id, int capacite, int capacite_attente) {
        this.capacite = capacite;
        this.capacite_attente = capacite_attente;
        this.id = id;
        this.listeDemande = new ArrayList<Etudiant>();
        this.listeAcceptation=new ArrayList<>();
        this.listeAttente=new ArrayList<>();
        this.listeRefus=new ArrayList<>();
    }

    /**
     * Man ask his better pref of woman, if she accepts, they are bounded, else nothing happen
     */

    public void add_Etudiant(Etudiant f) {
        this.listeDemande.add(f);
    }
    public void addEtudiantRefus(Etudiant e){
        if (listeAcceptation.contains(e))
        {
            this.listePreference.remove(e);
            this.listeAcceptation.remove(e);
            //this.capacite++;
        }
        else if(listeAttente.contains(e))
        {
            this.listePreference.remove(e);
            this.listeAttente.remove(e);
            //this.capacite_attente++;
        }
        this.listeRefus.add(e);
    }
    public void generateListePreference(int seed) {
        ArrayList<Etudiant> pref = new ArrayList<>(this.listeDemande);
        Random rand = new Random(seed);
        Collections.shuffle(pref, rand);
        this.listePreference = pref;


    }


    public ArrayList<Etudiant> setListeAccepte() {
        ArrayList<Etudiant> accepte = new ArrayList<>();
        if (this.listePreference.size() == 0) {
            this.listeAcceptation = accepte;
            return null;
        }
        int max_capacite_liste;
        if (this.capacite > listePreference.size()) {
            max_capacite_liste = listePreference.size();
        } else {
            max_capacite_liste = this.capacite;
        }
        for (int i = 0; i < max_capacite_liste; i++) {
            accepte.add(this.listePreference.get(i));
//            this.List_trier.remove(i);
        }
        this.listeAcceptation = accepte;
        //this.capacite-=this.listeAcceptation.size();
        return this.listeAcceptation;
    }


    public ArrayList<Etudiant> setListeAttente() {
        ArrayList<Etudiant> attente = new ArrayList<>();
        if (listePreference.size() == 0) {
            this.listeAttente = attente;
            return null;
        }
        for (int i = this.capacite; i < Math.min(this.listePreference.size(), this.capacite + this.capacite_attente); i++) {
            attente.add(this.listePreference.get(i));
        }
        this.listeAttente = attente;
        //this.capacite_attente-=this.listeAttente.size();
        return this.listeAttente;
    }

    public ArrayList<Etudiant> setListeRefus() {
        ArrayList<Etudiant> refus = new ArrayList<>();
        if (listePreference.size() == 0) {
            this.listeRefus = refus;
            return null;
        }
        for (int i = this.capacite + this.capacite_attente; i < this.listePreference.size(); i++) {
            refus.add(this.listePreference.get(i));
        }

        this.listeRefus = refus;
        return this.listeRefus;
    }


    public int getId() {
        return id;
    }

    public int getCapacite() {
        return capacite;
    }

    public int getCapacite_attente() {
        return capacite_attente;
    }

    @Override
    public boolean equals(Object obj) {
        Formation tocompare = (Formation) obj;
        if (tocompare.getId() == this.getId()) {
            return true;
        }
        return false;
    }
}
