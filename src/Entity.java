import java.util.List;

public interface Entity<T> {
    int getId();

    /**  Get the entity partner */
    T getBounded();

    /**  Set entity partner */
    void setBounded(T t);

    /**  Set a list of prefs by giving an array of a specific entity */
    void setPrefList(List<T> humans);

    T getCurrentPref();

    List<T> getPrefList();

    List<T> getDefaultPrefList();

    /** Shift to the right the index on pref list */
    void shiftPrefList();

    void resetIndex();

    void divorce();

    void reset();

    /**  Generate randomly set of prefs */
    List<T> generateRandomPrefList(int range, List<T> humans, int SEED);

    /**  When a woman receive a proposition, she looks if the proposer is a better partner for her.
     * If so, replace her bounded partner to the new one */
    T compare(T m);

    /**  Man ask his better pref of woman, if she accepts, they are bounded, else nothing happen  */
    Entity<T> ask(T f);

    /**  Print entity prefs */
    void printPrefList();
}
