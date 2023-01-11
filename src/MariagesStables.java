import java.io.FileWriter;
import java.util.ArrayList;

public class MariagesStables {

    public void allMariagesStables(ArrayList<Homme> hommes, ArrayList<Femme> femmes, int size) {
        // if size becomes 1 then prints the obtained
        // permutation
        ArrayList<Homme> h1 = new ArrayList<>(); //copie
        ArrayList<Femme> f1 = new ArrayList<>();

        for(int i=0 ; i<femmes.size();i++){
            h1.add(hommes.get(i));
            f1.add(femmes.get(i));
        }

        if (size == 1 ){
            ArrayList<Homme> h = new ArrayList<>();
            ArrayList<Femme> f = new ArrayList<>();
            ArrayList<String> couples = new ArrayList<>();
            for (int i = 0; i < femmes.size(); i++){
                h.add(hommes.get(i));
                f.add(femmes.get(i));
                hommes.get(i).setBounded(femmes.get(i));
                femmes.get(i).setBounded(hommes.get(i));
            }
            if(Main.is_Stable(f)){
                for(int i=0; i<h.size();i++){
                    couples.add(h.get(i).getId()+""+f.get(i).getId());
                }
                //System.out.println(couples);
            }
        }

        for (int i = 0; i < size; i++) {
            allMariagesStables(h1,f1,size - 1);
            // if size is odd, swap 0th i.e (first) and
            // (size-1)th i.e (last) element
            if (size % 2 == 1) {
                Femme temp = femmes.get(0);
                f1.set(0,f1.get(size-1));
                f1.set(size-1,temp);
            }
            // If size is even, swap ith
            // and (size-1)th i.e last element
            else {
                Femme temp = femmes.get(i);
                f1.set(i,f1.get(size-1));
                f1.set(size-1,temp);
            }
        }
    }

    public void allMariagesStablesToFile(ArrayList<Homme> hommes, ArrayList<Femme> femmes, int size, FileWriter file) throws Exception{
        // if size becomes 1 then prints the obtained
        // permutation
        if (size == 1 ){
            ArrayList<Homme> h = new ArrayList<>();
            ArrayList<Femme> f = new ArrayList<>();
            ArrayList<String> couples = new ArrayList<>();
            for (int i = 0; i < femmes.size(); i++){
                //f.get(i).getBounded().set(f.get(i));
                //h.get(i).set(f.get(i));
                h.add(hommes.get(i));
                f.add(femmes.get(i));
                hommes.get(i).setBounded(femmes.get(i));
                femmes.get(i).setBounded(hommes.get(i));
            }
            if(Main.is_Stable(f)){
                for(int i=0; i<h.size();i++){
                    couples.add(h.get(i).getId()+""+f.get(i).getId());
                }
                file.write(couples+"\n");
            }
        }

        for (int i = 0; i < size; i++) {
            allMariagesStablesToFile(hommes,femmes,size - 1,file);
            // if size is odd, swap 0th i.e (first) and
            // (size-1)th i.e (last) element
            if (size % 2 == 1) {
                Femme temp = femmes.get(0);
                femmes.set(0,femmes.get(size-1));
                femmes.set(size-1,temp);
            }
            // If size is even, swap ith
            // and (size-1)th i.e last element
            else {
                Femme temp = femmes.get(i);
                femmes.set(i,femmes.get(size-1));
                femmes.set(size-1,temp);
            }
        }




    }

}
