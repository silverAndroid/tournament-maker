package kroam.tournamentmaker;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rushil Perera on 11/21/2015.
 */
public class Tournament implements Serializable {

    private final String name;
    private final String type;
    private final ArrayList<Team> teams;
    private final int maxSize;
    private boolean completed;

    public Tournament(String name, String type, ArrayList<Team> teams, int maxSize) {
        this.name = name;
        this.type = type;
        this.teams = new ArrayList<>(teams.size());
        this.teams.addAll(teams);
        this.maxSize = maxSize;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return name;
    }
}
