package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import kroam.tournamentmaker.Util;
import kroam.tournamentmaker.database.DBColumns;
import kroam.tournamentmaker.database.DBTables;
import kroam.tournamentmaker.database.DatabaseSingleton;
import kroam.tournamentmaker.database.MissingColumnException;

/**
 * Created by silve on 2016-05-24.
 */
public class TournamentsStatsRelation {
    private static final String TAG = "TSR";
    private static TournamentsStatsRelation ourInstance = new TournamentsStatsRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.TOURNAMENT_NAME, DBColumns.STAT_ID, DBColumns.WINNING_STAT};

    private TournamentsStatsRelation() {
    }

    public static TournamentsStatsRelation getInstance() {
        return ourInstance;
    }

    public void createTournamentStatRelation(String tournamentName, long statID, boolean isWinningStat) {
        if (!Util.validateColumn(tournamentName)) {
            throw new MissingColumnException(columns[0]);
        }

        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("INSERT INTO %s VALUES (?, ?, ?)", DBTables.TOURNAMENTS_STATS_TABLE);
        database.beginTransaction();
        Log.i(TAG, "createTournamentStatRelation: " + query);

        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, tournamentName);
        statement.bindLong(2, statID);
        statement.bindLong(3, isWinningStat ? 1 : 0);
        statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void deleteTournamentStatRelations(String tournamentName) {
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("DELETE FROM %s WHERE %s=?;", DBTables.TOURNAMENTS_STATS_TABLE,
                columns[0]);
        Log.i(TAG, "deleteTournamentStatRelations: " + query);

        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, tournamentName);
        statement.executeUpdateDelete();
        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
