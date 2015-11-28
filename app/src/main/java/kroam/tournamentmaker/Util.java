package kroam.tournamentmaker;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rushil Perera on 11/24/2015.
 */
public class Util {

    public static String convertArrayToString(Object[] array) {
        return Arrays.toString(array).replace("[", "").replace("]", "");
    }

    public static String[] convertStringToArray(String arrayString) {
        return arrayString.split(", ");
    }

    public static Tournament cursorToTournament(Cursor cursor) {
        Tournament tournament = new Tournament(cursor.getString(0), cursor.getString(1), Util.convertStringToArray
                (cursor.getString(2)), cursor.getInt(3));
        tournament.setCompleted(cursor.getInt(4) == 1);
        return tournament;
    }

    public static Stat cursorToStat(Cursor cursor) {
        Stat stat = new Stat(cursor.getString(0), new ArrayList<>(Arrays.asList(Util.convertStringToArray(cursor
                .getString(1)))), new ArrayList<>(Arrays.asList(Util.convertStringToArray(cursor.getString(2)))));
        return stat;
    }
}
