package kroam.tournamentmaker;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kroam.tournamentmaker.database.TournamentDataSource;
import libraries.guava.MultiMap;

/**
 * Created by Rushil Perera on 11/21/2015.
 */
public class Tournament {

    private static final String TAG = "Tournament";
    private final String name;
    private final String type;
    private final ArrayList<Team> teams;
    private final int maxSize;
    private boolean registrationClosed;
    private boolean completed;
    private int currentRound;
    private ArrayList<ArrayList<Match>> roundsOfMatches;        //ArrayLists within roundOfMatches represent a round
    private Stat winningStat;
    private HashMap<String, StatValue> rankOfTeams;            //updated Dec 6 by Ocean
    private HashMap<String, StatValue> winsOfTeams;            //updated Dec 6 by Ocean

    public Tournament(String name, String type, ArrayList<Team> teams, int maxSize) {
        this(name, type, teams, maxSize, -1);
    }

    public Tournament(String name, String type, ArrayList<Team> teams, int maxSize, int currentRound) {
        this.name = name;
        this.type = type;
        this.teams = new ArrayList<>(teams.size());
        this.teams.addAll(teams);
        this.maxSize = maxSize;
        roundsOfMatches = new ArrayList<>();
        this.currentRound = currentRound;
        rankOfTeams = new HashMap<>(teams.size());
        winsOfTeams = new HashMap<>(teams.size());
        for (Team team : teams) {
            // 0 means that there is no (int)rank assigned yet
            rankOfTeams.put(team.getName(), new StatValue(name, team.getName(), 0));
            winsOfTeams.put(team.getName(), new StatValue(name, team.getName(), 0));
        }
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
        return currentRound;
    }

    /*  !!!
    * Amount of qualifiers out of the first round of Combination needs to be specified
    * Need Stat that will define the standings of the first round of Combination
    */
    public void generateNextRoundOfMatches() {
        Log.d(TAG, "generateNextRoundOfMatches: " + currentRound);
        if (currentRound == -1) {
            roundsOfMatches.add(Util.generateMatches(this));
        } else {
            //getListOfQualifiers currently only works properly for KnockOut format
            roundsOfMatches.add(Util.generateMatches(this, Util.getListOfQualifiers(this, getCurrentRoundOfMatches(),
                    currentRound)));
        }
        currentRound++;
        TournamentDataSource.getInstance().updateTournament(this);
    }

    public ArrayList<Match> getRoundOfMatches(int round) {
        return roundsOfMatches.get(round);
    }

    public ArrayList<Match> getCurrentRoundOfMatches() {
        return roundsOfMatches.get(currentRound);
    }

    public ArrayList<Match> getCurrentRoundOfActiveMatches() {
        ArrayList<Match> activeMatches = new ArrayList<>();
        for (Match match : getCurrentRoundOfMatches()) {
            if (!match.isCompleted())
                activeMatches.add(match);
        }
        return activeMatches;
    }

    public ArrayList<Match> getInactiveMatches() {
        ArrayList<Match> inactiveMatches = new ArrayList<>();
        for (ArrayList<Match> matches : getMatches()) {
            for (Match match : matches) {
                if (match != null && match.isCompleted())
                    inactiveMatches.add(match);
            }
        }
        return inactiveMatches;
    }

    public Stat getWinningStat() {
        return winningStat;
    }

    public void setWinningStat(Stat winningStat) {
        this.winningStat = winningStat;
    }

    public ArrayList<ArrayList<Match>> getMatches() {
        return roundsOfMatches;
    }

    public void addRoundsOfMatches(ArrayList<ArrayList<Match>> matches) {
        roundsOfMatches.addAll(matches);
    }

    //updated Dec 6 by Ocean
    public int getRankingOf(Team team) {
        return rankOfTeams.get(name + ": " + team.getName()).getValue();
    }

    //updated Dec 6 by Ocean
    public void updateRankOf(Team team) {
        winsOfTeams.put(name + ": " + team.getName(), new StatValue(name, team.getName(), winsOfTeams.get(team.getName
                ()).getValue() + 1)); //updates the amount of wins that team has
        updateRanksOfAll();
    }

    //updated Dec 6 by Ocean
    public void updateRanksOfAll() {
        MultiMap<Integer, Team> teamsScores = new MultiMap<>();
        int[] wins = new int[teams.size()];
        List<Team> listFromTeamsScores;

        for (Team currentTeam : teams) {
            teamsScores.put(winsOfTeams.get(name + ": " + currentTeam.getName()).getValue(), currentTeam);
            wins[teams.indexOf(currentTeam)] = winsOfTeams.get(name + ": " + currentTeam.getName()).getValue();
        }

        int swap;
        for (int c = 0; c < (wins.length - 1); c++) {
            for (int d = 0; d < wins.length - c - 1; d++) {
                if (wins[d] < wins[d + 1]) /* For descending order use < */ {
                    swap = wins[d];
                    wins[d] = wins[d + 1];
                    wins[d + 1] = swap;
                }
            }
        }

        for (int arbitraryIndex = 0; arbitraryIndex < wins.length; arbitraryIndex++) {
            if (arbitraryIndex > 0 && wins[arbitraryIndex - 1] == wins[arbitraryIndex]) {
                continue;
            }
            listFromTeamsScores = teamsScores.get(wins[arbitraryIndex]);
            for (int i = 0; i < listFromTeamsScores.size(); i++) {
                String teamName = listFromTeamsScores.get(i).getName();
                rankOfTeams.put(name + ": " + teamName, new StatValue(name, teamName, arbitraryIndex + 1));
            }
        }
        TournamentDataSource.getInstance().updateTournament(this);
    }

    public HashMap<String, StatValue> getRankings() {
        return rankOfTeams;
    }

    public void setRankings(HashMap<String, StatValue> rankings) {
        rankOfTeams.putAll(rankings);
    }

    public HashMap<String, StatValue> getWins() {
        return winsOfTeams;
    }

    public void setWins(HashMap<String, StatValue> wins) {
        winsOfTeams.putAll(wins);
    }
}
