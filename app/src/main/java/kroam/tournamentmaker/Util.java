package kroam.tournamentmaker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rushil Perera on 11/24/2015.
 */
public class Util {

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

        for(int i = 0; i< names.length; i++) {
            Cursor cursor = database.query(DatabaseSingleton.TEAMS_TABLE, columns, columns[0] + "=?", new String[]{names[i]}, null, null, null);
            teams.add(cursorToTeam(cursor));
        }
        database.close();
        return teams;

    }

    public static String[] convertStringToArray(String arrayString) {
        return arrayString.split(", ");
    }

    public static Tournament cursorToTournament(Cursor cursor) {
        Tournament tournament = new Tournament(cursor.getString(0), cursor.getString(1), (Team[])(Util.convertStringtoTeamArray(cursor.getString(2)).toArray()), cursor.getInt(3));
        tournament.setCompleted(cursor.getInt(4) == 1);
        return tournament;
    }

    public static Stat cursorToStat(Cursor cursor) {
        Stat stat = new Stat(cursor.getString(0), new ArrayList<>(Arrays.asList(Util.convertStringToArray(cursor
                .getString(1)))), new ArrayList<>(Arrays.asList(Util.convertStringToArray(cursor.getString(2)))));
        return stat;
    }

    public static Team cursorToTeam(Cursor cursor) {
        Team team = new Team(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
    }
}
