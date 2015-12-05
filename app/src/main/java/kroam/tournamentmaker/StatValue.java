package kroam.tournamentmaker;

/**
 * Created by Rushil Perera on 12/4/2015.
 */
public class StatValue {

    private final String tournamentName;
    private final String teamName;
    private final int value;

    public StatValue(String tournamentName, String teamName, int value) {
        this.tournamentName = tournamentName;
        this.teamName = teamName;
        this.value = value;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return tournamentName + ": " + teamName;
    }
}
