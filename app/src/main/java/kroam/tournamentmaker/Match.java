package kroam.tournamentmaker;

/**
 * Created by Rushil Perera on 12/1/2015.
 */
public class Match {
    private final Team homeTeam;
    private final Team awayTeam;
    private boolean completed;
    private Tournament associatedTournament;

    public Match(Team team1, Team team2) {
        homeTeam = team1;
        awayTeam = team2;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Tournament getAssociatedTournament() {
        return associatedTournament;
    }

    public void setAssociatedTournament(Tournament associatedTournament) {
        this.associatedTournament = associatedTournament;
    }
}
