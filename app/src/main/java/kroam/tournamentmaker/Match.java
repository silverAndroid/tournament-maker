package kroam.tournamentmaker;

/**
 * Created by Rushil Perera on 12/1/2015.
 */
public class Match {
    private final Team homeTeam;
    private final Team awayTeam;
    private boolean completed;
    private Team winner;
    private int homeScore, awayScore;

    public Match(Team team1, Team team2) {
        homeTeam = team1;
        awayTeam = team2;
        if(team2 == null)       //Ocean:    Implementation added for the creation of Matches (see Util::generateMatches(Tournament))
            completed = true;   //          homeTeam should not be given a win in this case
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        if(completed)
            setWinner();
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setWinner(){
        if(homeScore < awayScore)
            winner = awayTeam;          //Exception: Ties not handled
        else                            //Ties go to home team
            winner = homeTeam;
    }
}

