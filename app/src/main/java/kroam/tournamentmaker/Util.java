package kroam.tournamentmaker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Rushil Perera on 11/24/2015.
 */
public class Util {

    public static final int TOURNAMENT_REQUEST_CODE = 0;
    public static final int TEAM_REQUEST_CODE = 1;

    public static String convertArrayToString(Object[] array) {
        return Arrays.toString(array).replace("[", "").replace("]", "");
    }

    public static String convertTeamArrayToString(Team[] array) {
        String[] teamnames = new String[array.length];
        for (int i = 0; i < array.length; ++i) {
            teamnames[i] = array[i].getName();
        }
        return Util.convertArrayToString(teamnames);
    }

    public static ArrayList<Team> convertStringtoTeamArray(String teamArrayString) {
        SQLiteDatabase database = DatabaseSingleton.getInstance().getReadableDatabase();
        String names[] = Util.convertStringToArray(teamArrayString);
        ArrayList<Team> teams = new ArrayList<Team>();

        String columns[] = {DatabaseSingleton.TEAMS_NAME, DatabaseSingleton.TEAMS_CAPTAIN_NAME, DatabaseSingleton.TEAMS_EMAIL, DatabaseSingleton.TEAMS_PHONE_NUMBER};

        for (int i = 0; i < names.length; i++) {
            Cursor cursor = database.query(DatabaseSingleton.TEAMS_TABLE, columns, columns[0] + "=?", new String[]{names[i]}, null, null, null);
            if (cursor.moveToFirst())
                teams.add(cursorToTeam(cursor));
        }
        database.close();
        return teams;

    }

    public static String[] convertStringToArray(String arrayString) {
        return arrayString.split(", ");
    }

    public static Tournament cursorToTournament(Cursor cursor) {
        Tournament tournament = new Tournament(cursor.getString(0), cursor.getString(1), Util
                .convertStringtoTeamArray(cursor.getString(2)), cursor.getInt(3));
        tournament.setCompleted(cursor.getInt(4) == 1);
        tournament.setRegistrationClosed(cursor.getInt(5) == 1);
        return tournament;
    }

    public static Stat cursorToStat(Cursor cursor) {
        return new Stat(cursor.getString(0), new ArrayList<>(Arrays.asList(Util.convertStringToArray(cursor
                .getString(1)))), new ArrayList<>(Arrays.asList(Util.convertStringToArray(cursor.getString(2)))));
    }

    public static Team cursorToTeam(Cursor cursor) {
        return new Team(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
    }

    public static Match cursorToMatch(Cursor cursor) {
        TeamDataSource teamDatabase = TeamDataSource.getInstance();
        Match match = new Match(teamDatabase.getTeam(cursor.getString(0)), teamDatabase.getTeam(cursor.getString(1)));
        match.setCompleted(cursor.getInt(2) == 1);
        return null;
    }
    /*
    * Instantiates Matches with randomly matched teams that are in <code>tournament</code>.
    * Method currently only does Knockout Format(still requires Round Robin and Combinations)
    * */
    public static void generateMatches(Tournament tournament){
        ArrayList<Team> teams = TeamDataSource.getInstance().getTeamFromTournament(tournament.getName());
        Collections.shuffle(teams);
        Iterator<Team> teamIterator = teams.listIterator();

        switch(tournament.getType()){
            case TournamentCreateActivity.KNOCKOUT:
                while(teamIterator.hasNext())
                    MatchDataSource.getInstance().createMatch(new Match(teamIterator.next(), teamIterator.next()));
                break;  //this is used to generate the first round of matches

            case TournamentCreateActivity.ROUND_ROBIN:
                for(int aTeam = 0; aTeam < teams.size() - 1; aTeam++){
                    for(int otherTeam = aTeam +1; otherTeam < teams.size(); otherTeam++)
                        MatchDataSource.getInstance().createMatch(new Match(teams.get(aTeam), teams.get(otherTeam)));
                }
                break;

            case TournamentCreateActivity.COMBINATION:
                for(int aTeam = 0; aTeam < teams.size() - 1; aTeam++){
                    for(int otherTeam = aTeam +1; otherTeam < teams.size(); otherTeam++)
                        MatchDataSource.getInstance().createMatch(new Match(teams.get(aTeam), teams.get(otherTeam)));
                }
                break;
                //generates the first round of Combination format, in Round Robin. Next rounds will be held
                //knockout format.
        }

    }

    /*
    * Method utlized to generate 2nd+ round of a tournament with Knockout or Combination format
    * */
    public static void generateMatches(Tournament tournament, ArrayList<Team> qualifyingTeams){
        switch(tournament.getType()){
            case TournamentCreateActivity.ROUND_ROBIN:
                System.out.print("Exceptional case. Round Robin only has 1 round of matches\n");
                generateMatches(tournament);
                break;
            default:
                Iterator<Team> teamIterator = qualifyingTeams.listIterator();
                while(teamIterator.hasNext())
                    MatchDataSource.getInstance().createMatch(new Match(teamIterator.next(), teamIterator.next()));
                break;
        }
    }
}
