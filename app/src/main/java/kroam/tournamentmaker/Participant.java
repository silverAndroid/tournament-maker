package kroam.tournamentmaker;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ocean on 5/18/2016.
 */
public abstract class Participant implements Serializable {

    protected ArrayList<Tournament> associatedTournaments;
    private ArrayList<Stat> stats;
    private long id;
    private String name;

    public Participant(String name) {
        this.name = name;
        stats = new ArrayList<>();
    }

    public Participant(int id, String name) {
        this.id = id;
        this.name = name;
        stats = new ArrayList<>();
    }

    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Stat> getStats() {
        return stats;
    }

    public void addStat(Stat stat) {
        stats.add(stat);
    }

    public void addStats(ArrayList<Stat> stats) {
        this.stats.addAll(stats);
    }

    public ArrayList<Tournament> getAssociatedTournaments() {
        return associatedTournaments;
    }

    //Only to be used by Team and Player Class.
    //Team will instantiate associatedTournaments.
    //Players will instantiate associatedTournaments only if they're participating individually
    protected void createTournamentList() {
        associatedTournaments = new ArrayList<>();
    }

    //"Delete Tournament" activity must call this method to ensure that Participants won't
    //be associated to a Tournament nullptr (
    public abstract void removeFromTournament(Tournament t);

    //Add guard to prevent adding a Tournament already in associatedTournaments
    public abstract void addTournament(Tournament t);

    public abstract void addTournaments(ArrayList<Tournament> ts);

    public long getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Participant) {
            Participant participant = (Participant) o;
            return participant.getID() == id;
        }
        return false;
    }
}
