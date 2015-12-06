package kroam.tournamentmaker.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import kroam.tournamentmaker.Match;
import kroam.tournamentmaker.Util;

/**
 * Created by Rushil Perera on 12/1/2015.
 * <p/>
 * Singleton class for the Match Section of the Database
 */
public class MatchDataSource {
    private static final String TAG = "MatchDataSource";
    private static MatchDataSource ourInstance = new MatchDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DatabaseSingleton.MATCHES_TEAM_1, DatabaseSingleton.MATCHES_TEAM_2, DatabaseSingleton
            .MATCHES_COMPLETED, DatabaseSingleton.MATCHES_TOURNAMENT_NAME};

    private MatchDataSource() {
    }

    public synchronized static MatchDataSource getInstance() {
        return ourInstance;
    }

    public void createMatch(Match match) {
        database = DatabaseSingleton.getInstance().openDatabase();
        Log.i(TAG, "createMatch: open");

        String query = "INSERT INTO " + DatabaseSingleton.MATCHES_TABLE + " VALUES(?,?,?,?)";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();

        statement.bindString(1, match.getHomeTeam().getName());
        statement.bindString(2, match.getAwayTeam().getName());
        statement.bindLong(3, match.isCompleted() ? 1 : 0);
        statement.bindString(4, match.getAssociatedTournament().getName());
        statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
        close();
    }

    public ArrayList<Match> getMatches() {
        ArrayList<Match> matches = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.MATCHES_TABLE, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Match match = Util.cursorToMatch(cursor);
                matches.add(match);
            } while (cursor.moveToNext());
        }
        close();
        return matches;
    }

    public ArrayList<Match> getMatchesForTournament(String tournamentName) {
        ArrayList<Match> matches = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.MATCHES_TABLE, columns, columns[3] + "=?", new
                String[]{tournamentName}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Match match = Util.cursorToMatch(cursor);
                matches.add(match);
            } while (cursor.moveToNext());
        }
        return matches;
    }

    public ArrayList<Match> getFinishedMatches(String tournamentName) {
        ArrayList<Match> finishedMatches = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.MATCHES_TABLE, columns, columns[2] + "=? AND " + columns[3]
                + "=?", new String[]{"1", tournamentName}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Match match = Util.cursorToMatch(cursor);
                finishedMatches.add(match);
            } while (cursor.moveToNext());
        }
        return finishedMatches;
    }

    private void close() {
        DatabaseSingleton.getInstance().closeDatabase();
    }

    public Match endMatch(Match match) {
        database = DatabaseSingleton.getInstance().openDatabase();
        Log.i(TAG, "endMatch: open");

        String query = "UPDATE " + DatabaseSingleton.MATCHES_TABLE + " SET " + columns[2] + "=? WHERE " + columns[3]
                + "=? AND " + columns[0] + "=? AND " + columns[1] + "=?";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();
        statement.bindLong(1, 1);
        statement.bindString(2, match.getAssociatedTournament().getName());
        statement.bindString(3, match.getHomeTeam().getName());
        statement.bindString(4, match.getAwayTeam().getName());
        statement.executeUpdateDelete();

        database.setTransactionSuccessful();
        database.endTransaction();
        close();
        Log.i(TAG, "endMatch: close");
        return match;
    }

    public ArrayList<Match> getUnfinishedMatches(String tournamentName) {
        ArrayList<Match> unfinishedMatches = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.MATCHES_TABLE, columns, columns[2] + "=? AND " + columns[3]
                + "=?", new String[]{"0", tournamentName}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Match match = Util.cursorToMatch(cursor);
                unfinishedMatches.add(match);
            } while (cursor.moveToNext());
        }
        return unfinishedMatches;
    }
}
