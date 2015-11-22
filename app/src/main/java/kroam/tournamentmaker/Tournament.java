package kroam.tournamentmaker;

/**
 * Created by Rushil Perera on 11/21/2015.
 */
public class Tournament {

    private final String name;
    private final String type;
    private final String[] teams;
    private final int maxSize;
    private boolean completed;

    public Tournament(String name, String type, String[] teams, int maxSize) {
        this.name = name;
        this.type = type;
        this.teams = new String[teams.length];
        System.arraycopy(teams, 0, this.teams, 0, teams.length);
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

    public String[] getTeams() {
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
