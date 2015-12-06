package kroam.tournamentmaker;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import importedLibraries.MultiMap;
import kroam.tournamentmaker.activities.TournamentCreateActivity;
import kroam.tournamentmaker.database.MatchDataSource;
import kroam.tournamentmaker.database.StatsDataSource;
import kroam.tournamentmaker.database.TeamDataSource;
import kroam.tournamentmaker.database.TournamentDataSource;

/**
 * Created by Rushil Perera on 11/24/2015.
 */
public class Util {

    public static final int TOURNAMENT_REQUEST_CODE = 0;
    public static final int TEAM_REQUEST_CODE = 1;
    private static final String TAG = "Util";

    public static String convertArrayToString(Object[] array) {
        return Arrays.toString(array).replace("[", "").replace("]", "");
    }

    public static ArrayList<Team> convertStringToTeamArraylist(String teamArrayString) {
        String names[] = Util.convertStringToArray(teamArrayString);
        ArrayList<Team> teams = new ArrayList<>();

        for (String name : names) {
            teams.add(TeamDataSource.getInstance().getTeam(name));
        }
        return teams;
    }

    public static ArrayList<Tournament> convertStringToTournamentArraylist(String arrayString) {
        String[] tournamentNames = Util.convertStringToArray(arrayString);
        ArrayList<Tournament> tournaments = new ArrayList<>();

        for (String name : tournamentNames) {
            tournaments.add(TournamentDataSource.getInstance().getTournament(name));
        }
        return tournaments;
    }

    public static String[] convertStringToArray(String arrayString) {
        return arrayString.split(", ");
    }

    public static Tournament cursorToTournament(Cursor cursor) {
        Tournament tournament = new Tournament(cursor.getString(0), cursor.getString(1), Util
                .convertStringToTeamArraylist(cursor.getString(2)), cursor.getInt(3));
        tournament.setCompleted(cursor.getInt(4) == 1);
        tournament.setRegistrationClosed(cursor.getInt(5) == 1);
        tournament.setWinningStat(StatsDataSource.getInstance().getStat(cursor.getString(6)));
        return tournament;
    }

    public static Stat cursorToStat(Cursor cursor) {
        return new Stat(cursor.getString(0), new ArrayList<>(Arrays.asList(Util.convertStringToArray(cursor
                .getString(1)))), Util.convertStatValuesToMap(cursor.getString(2)));
    }

    public static Team cursorToTeam(Cursor cursor) {
        Team team = new Team(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        team.addTournaments(Util.convertStringToTournamentArraylist(cursor.getString(4)));
        return team;
    }

    public static Match cursorToMatch(Cursor cursor) {
        TeamDataSource teamDatabase = TeamDataSource.getInstance();
        Match match = new Match(teamDatabase.getTeam(cursor.getString(0)), teamDatabase.getTeam
                (cursor.getString(1)));
        match.setCompleted(cursor.getInt(2) == 1);
        match.setAssociatedTournament(TournamentDataSource.getInstance().getTournament(cursor
                .getString(3)));
        return match;
    }

    private static HashMap<String, StatValue> convertStatValuesToMap(String values) {
        HashMap<String, StatValue> valuesMap = new HashMap<>();
        String[] valuesSplit = values.split(", ");
        if (valuesSplit.length != 1) {
            for (String value : valuesSplit) {
                String[] statsValues = value.split(": ");
                if (statsValues.length != 1) {
                    StatValue statValue = new StatValue(statsValues[0], statsValues[1], Integer.parseInt
                            (statsValues[2]));
                    valuesMap.put(statValue.toString(), statValue);
                }
            }
        }
        return valuesMap;
    }

    public static String convertHashMapToString(HashMap<String, StatValue> stats) {
        String statString = "";
        ArrayList<StatValue> values = new ArrayList<>(stats.values());
        if (values.size() != 0) {
            statString += values.get(0).getTournamentName() + ": " + values.get(0).getTeamName() + ": " + values.get(0)
                    .getValue();
            for (int i = 1; i < values.size(); i++) {
                statString += ", ";
                StatValue statValue = values.get(i);
                statString += statValue.getTournamentName() + ": " + statValue.getTeamName() + ": " + statValue
                        .getValue();
            }
        }
        return statString;
    }

    /*
    * Instantiates Matches with randomly matched teams that are in <code>tournament</code>.
    * Method currently only does Knockout Format(still requires Round Robin and Combinations)
    *
    */
    public static ArrayList<Match> generateMatches(Tournament tournament) {
        ArrayList<Team> teams = TournamentDataSource.getInstance().getTeamsFromTournament(tournament.getName());
        Collections.shuffle(teams);
        Iterator<Team> teamIterator = teams.listIterator();
        ArrayList<Match> matches = new ArrayList<>();
        Match newMatch;

        switch (tournament.getType()) {
            case TournamentCreateActivity.KNOCKOUT:
                while (teamIterator.hasNext()) {
                    newMatch = new Match(tournament, teamIterator.next(), teamIterator.hasNext() ? teamIterator
                            .next() : null);
                    matches.add(newMatch);
                    MatchDataSource.getInstance().createMatch(newMatch);
                }
                break;  //this is used to generate the first round of matches

            case TournamentCreateActivity.ROUND_ROBIN:
                for (int aTeam = 0; aTeam < teams.size() - 1; aTeam++) {
                    for (int otherTeam = aTeam + 1; otherTeam < teams.size(); otherTeam++) {
                        newMatch = new Match(tournament, teams.get(aTeam), teams.get(otherTeam));
                        matches.add(newMatch);
                        MatchDataSource.getInstance().createMatch(newMatch);
                    }
                }
                break;

            case TournamentCreateActivity.COMBINATION:
                for (int aTeam = 0; aTeam < teams.size() - 1; aTeam++) {
                    for (int otherTeam = aTeam + 1; otherTeam < teams.size(); otherTeam++) {
                        newMatch = new Match(tournament, teams.get(aTeam), teams.get(otherTeam));
                        matches.add(newMatch);
                        MatchDataSource.getInstance().createMatch(newMatch);
                    }
                }
                break;
            //generates the first round of Combination format, in Round Robin. Next rounds will be held
            //knockout format.
        }
        return matches;

    }

    /*
    * Method utilized to generate 2nd+ round of a tournament with Knockout or Combination format
    * */
    public static ArrayList<Match> generateMatches(Tournament tournament, ArrayList<Team> qualifyingTeams) {
        Match newMatch;
        ArrayList<Match> matches = new ArrayList<>();

        switch (tournament.getType()) {
            case TournamentCreateActivity.ROUND_ROBIN:
                System.out.println("Exceptional case. Round Robin only has 1 round of matches\n");
                return generateMatches(tournament);
            default:
                Iterator<Team> teamIterator = qualifyingTeams.listIterator();
                while (teamIterator.hasNext()) {
                    newMatch = new Match(tournament, teamIterator.next(), teamIterator.next());
                    matches.add(newMatch);
                    MatchDataSource.getInstance().createMatch(newMatch);
                }
                return matches;
        }
    }

    public static ArrayList<Team> getListOfQualifiers(Tournament tournament, ArrayList<Match> matches, int currentRound) {
        ArrayList<Team> winners = new ArrayList<>();

        //qualifiers of the first round (RoundRobin format) of Combination
        if(     tournament.getType().equals(TournamentCreateActivity.COMBINATION)
                && currentRound == 0){
            //positions in this arrays correspond to respective position in the ArrayList<Team> in tournament
            int[] amountOfWins = new int[tournament.getTeams().size()];
            Team[] teamWinInstances = new Team[matches.size()];
            MultiMap<Integer, Team> mapOfWinsAndTeams = new MultiMap<Integer, Team>();

            for (int matchPos = 0; matchPos < matches.size(); matchPos++){
                teamWinInstances[matchPos] = matches.get(matchPos).getWinner();
            }

            for (int i = 0; i < teamWinInstances.length; i++){
                amountOfWins[ tournament.getTeams().indexOf(teamWinInstances[i]) ]++;
            }

            for(int k = 0; k < amountOfWins.length; k++){
                mapOfWinsAndTeams.put(amountOfWins[k], tournament.getTeams().get(k));
            }

            int swap;
            for (int c = 0; c < ( amountOfWins.length - 1 ); c++) {
                for (int d = 0; d < amountOfWins.length - c - 1; d++) {
                    if (amountOfWins[d] > amountOfWins[d+1]) /* For descending order use < */
                    {
                        swap       = amountOfWins[d];
                        amountOfWins[d]   = amountOfWins[d+1];
                        amountOfWins[d+1] = swap;
                    }
                }
            }

            //Array that contains the wins of the qualifying teams. Used to get teams that qualify from mapOfWinsAndTeams
            int[] qualifyingWins = new int[generateNumQualifiers(tournament.getTeams())];
            for(int i = 0; i < qualifyingWins.length; i++){
                qualifyingWins[i] = amountOfWins[i];
            }

            for(int i = 0; i < qualifyingWins.length; i++){
                if( i > 0 && qualifyingWins[i-1] == qualifyingWins[i]){
                    continue;
                }
                List<Team> qualifiers = mapOfWinsAndTeams.get(qualifyingWins[i]);
                for(int q = 0; q <qualifiers.size(); q++){
                    winners.add(qualifiers.get(q));
                }
            }

            return winners;
        }

        for (int aWinner = 0; aWinner < matches.size(); aWinner++) {
            winners.add(matches.get(aWinner).getWinner());
        }
        return winners;
    }

    public static int generateNumQualifiers(ArrayList<Team> roundOfRR){
        int sizeExponent = 0;
        while(Math.pow(2, sizeExponent) < roundOfRR.size()){
            sizeExponent++;
        }
        sizeExponent--;
        return (int)Math.pow(2, sizeExponent);
    }

    public static int getRankingOf(Team team){
        return 0;
    }
}
