package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import kroam.tournamentmaker.database.DBColumns;
import kroam.tournamentmaker.database.DBTables;
import kroam.tournamentmaker.database.DatabaseSingleton;

/**
 * Created by silve on 2016-05-24.
 */
public class TournamentsMatchesRelation {
    private static final String TAG = "TMR";
    private static TournamentsMatchesRelation ourInstance = new TournamentsMatchesRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.TOURNAMENT_NAME, DBColumns.MATCH_ID, DBColumns.ROUND};

    private TournamentsMatchesRelation() {
    }

    public static TournamentsMatchesRelation getInstance() {
        return ourInstance;
    }

    public void addTournamentMatchRelation(String tournamentName, String matchID, int round) {
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("INSERT INTO %s VALUES (?, ?, ?);", DBTables.TOURNAMENTS_MATCHES);
        Log.i(TAG, "addTournamentMatchRelation: " + query);

        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, tournamentName);
        statement.bindString(2, matchID);
        statement.bindLong(3, round);
        statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
