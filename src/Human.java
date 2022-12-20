import java.util.List;

interface Human<T> {
    int getId();
    T getBounded();

    void setBounded(Human t);

    void setPrefList(List<T> humans);

    T getCurrentPref();

    List<T> getPrefList();

    List<T> getDefaultPrefList();

    void shiftPrefList();

    void resetIndex();

    void divorce();

    void reset();

    void generateRandomPrefList(int range, List<T> humans, int SEED);
}
