package main.java.machiavelsup;

import java.util.ArrayList;
import java.util.List;

public interface Strategie {
    Formation choixParmiAcceptation(Etudiant etudiant);
    ArrayList<Formation> choixParmiAttente(Etudiant etudiant);
}
