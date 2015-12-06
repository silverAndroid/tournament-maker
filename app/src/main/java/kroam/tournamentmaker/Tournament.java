package kroam.tournamentmaker;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 11/21/2015.
 */
public class Tournament {

    private final String name;
    private final String type;
    private final ArrayList<Team> teams;
    private final int maxSize;
    private boolean registrationClosed;
    private boolean completed;
    private int currentRound = -1;
    private ArrayList<ArrayList<Match>> roundsOfMatches;        //ArrayLists within roundOfMatches represent a round
    private Stat winningStat;

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

    public boolean isRegistrationClosed() {
        return registrationClosed;
    }

    public void setRegistrationClosed(boolean registrationClosed) {
        this.registrationClosed = registrationClosed;
    }

    public int getCurrentRound() {
        return currentRound + 1;
    }

    /*  !!!
    * Amount of qualifiers out of the first round of Combination needs to be specified
    * Need Stat that will define the standings of the first round of Combination
    */
    public void generateNextRoundOfMatches() {
        if (currentRound == -1)
            roundsOfMatches.add(Util.generateMatches(this));
        else {
            //getListOfQualifiers currently only works properly for KnockOut format
            Util.generateMatches(this, Util.getListOfQualifiers(this, roundsOfMatches.get(currentRound), currentRound) );
        }
        currentRound++;
    }

    public Stat getWinningStat() {
        return winningStat;
    }

    public void setWinningStat(Stat winningStat) {
        this.winningStat = winningStat;
    }
}
