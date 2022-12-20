package main.java.machiavelsup;//import java.util.ArrayList;
import java.util.*;
import main.java.machiavelsup.*;

public class Etudiant {
    protected int id;
    protected int capacite;
    protected ArrayList<Formation> listePreference;
    protected ArrayList<Formation> listeAcceptation;
    protected ArrayList<Formation> listeEnAttente;
    protected ArrayList<Integer> listePositionListeAttente;
    protected ArrayList<Formation> listeRefus;

    public ArrayList<Formation> getListePreference() {
        return listePreference;
    }

    public ArrayList<Formation> getListeAcceptation() {
        return listeAcceptation;
    }

    public ArrayList<Formation> getListeEnAttente() {
        return listeEnAttente;
    }


    public ArrayList<Formation> getListeRefus() {
        return listeRefus;
    }

    public Etudiant(int id, int capacite){

        this.listePositionListeAttente = new ArrayList<>();
        this.capacite = capacite;
        this.id = id;
        this.listeAcceptation =new ArrayList<>();
        this.listeRefus =new ArrayList<>();
        this.listeEnAttente =new ArrayList<>();

    }

    public ArrayList<Formation> genererListePreference(int range, ArrayList<Formation> Liste_non_trier, int seed) {
        ArrayList<Formation> pref = new ArrayList<>();
        Random rand = new Random(seed);
        int val;
        while(pref.size() < range){
            val = Math.abs(rand.nextInt()) % Liste_non_trier.size();
            if(!pref.contains(Liste_non_trier.get(val))){
                pref.add(Liste_non_trier.get(val));
            }
        }
        this.listePreference =pref;
        return pref;
    }
    public void setListePreference(ArrayList<Formation> listePreference) {
        this.listePreference = listePreference;
    }
    public void add_accepte(Formation formation)
    {
        if(this.listeEnAttente.contains(formation))
        {
            this.listeEnAttente.remove(formation);
        }
        if(this.listeAcceptation.contains(formation))
        {return ;}
        this.listeAcceptation.add(formation);
    }
    public void add_refus(Formation formation)
    {
        if (this.listeRefus.contains(formation)) {
            return;
        }
        this.listeRefus.add(formation);
    }
    public void add_attente(Formation formation, int position)
    {
        if(this.listeEnAttente.contains(formation))
        {
            return;
        }
        //this.listePositionListeAttente.add(position);
        this.listeEnAttente.add(formation);
    }

    public ArrayList<Formation> set_List_accepte(ArrayList<Formation> Liste_non_trier) {
        this.listeAcceptation =Liste_non_trier;
        return this.listeAcceptation;
    }


    public ArrayList<Formation> set_List_attente(ArrayList<Formation> Liste_non_trier) {
        this.listeEnAttente =Liste_non_trier;
        return this.listeEnAttente;
    }


    public ArrayList<Formation> set_List_reception(ArrayList<Formation> Liste_non_trier) {
        return null;
    }


    public ArrayList<Formation> set_List_refus(ArrayList<Formation> Liste_non_trier) {
        this.listeRefus =Liste_non_trier;
        return this.listeRefus;
    }

    public int getId() {
        return id;
    }

    public int getCapacite() {
        return capacite;
    }


    public ArrayList<Integer> getListePositionListeAttente() {
        return listePositionListeAttente;
    }

    public void setListePositionListeAttente(ArrayList<Integer> listePositionListeAttente) {
        this.listePositionListeAttente = listePositionListeAttente;
    }
}