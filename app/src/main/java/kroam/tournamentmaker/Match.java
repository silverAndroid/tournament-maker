package kroam.tournamentmaker;

/**
 * Created by Rushil Perera on 12/1/2015.
 */
public class Match {
    private final Team homeTeam;
    private final Team awayTeam;
    private boolean completed;
<<<<<<< HEAD
    private Team winner;
    private int homeScore, awayScore;
    private Tournament belongingTournament;
=======
    private Tournament associatedTournament;
>>>>>>> ce9884ab68f8b473571f871ab5f157eedae4f44d

    public Match(Tournament tournament, Team team1, Team team2) {
        belongingTournament = tournament;
        homeTeam = team1;
        awayTeam = team2;
        if(team2 == null)       //Ocean:    Implementation added for the creation of Matches (see Util::generateMatches(Tournament))
            completed = true;   //          homeTeam should not be given a win in this case
    }

    public Match(Team team1, Team team2){
        homeTeam = team1;
        awayTeam = team2;
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

<<<<<<< HEAD
    public void setWinner(){
        if(homeScore < awayScore)
            winner = awayTeam;          //Exception: Ties not handled
        else                            //Ties go to home team
            winner = homeTeam;
    }

    public Team getWinner(){
        return winner;
=======
    public Tournament getAssociatedTournament() {
        return associatedTournament;
    }

    public void setAssociatedTournament(Tournament associatedTournament) {
        this.associatedTournament = associatedTournament;
>>>>>>> ce9884ab68f8b473571f871ab5f157eedae4f44d
    }
}

