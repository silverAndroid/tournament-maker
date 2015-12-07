package kroam.tournamentmaker;

import kroam.tournamentmaker.database.TournamentDataSource;

/**
 * Created by Rushil Perera on 12/1/2015.
 */
public class Match {
    private final Team homeTeam;
    private final Team awayTeam;
    private boolean completed;
    private Team winner;
    private int homeScore, awayScore;
    private final String id;

    public Match(Team team1, Team team2, String id) {
        homeTeam = team1;
        awayTeam = team2;
        this.id = id;
        if (team2 == null)       //Ocean:    Implementation added for the creation of Matches (see Util::generateMatches(Tournament))
            completed = true;   //          homeTeam should not be given a win in this case
        homeScore = -1;
        awayScore = -1;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed) {
            setWinner();
        }
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setWinner() {
        if (homeScore < awayScore) {
            winner = awayTeam;
        } else {                            //Ties go to home team
            winner = homeTeam;
        }
    }

    public Team getWinner() {
        return winner;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int score) {
        homeScore = score;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int score) {
        awayScore = score;
    }

    @Override
    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }
}

