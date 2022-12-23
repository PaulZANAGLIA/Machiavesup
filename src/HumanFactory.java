import java.util.List;

public class HumanFactory {
    /** Generate a human with an id */
    public static Human<?> generateHuman(String genre, int id){
        if(genre.equals("man"))
            return new Homme(id);
        return new Femme(id);
    }

    /** Generate amount of humans in a list */
    public static <T extends Human<?>> void generateHumans(List<T> humans, String genre, int range) {
        for (int i = 0; i < range; i++) { // Init groups
            T hu = (T) generateHuman(genre, i);
            humans.add(hu);
        }
    }

    /** Return string according to human class to generate object of that type */
    public static String getHumanType(Human<?> human){
        if(human instanceof Femme) return "woman";
        return "man";
    }
}
