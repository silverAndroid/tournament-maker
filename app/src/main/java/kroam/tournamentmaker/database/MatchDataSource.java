package kroam.tournamentmaker.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import kroam.tournamentmaker.Match;
import kroam.tournamentmaker.Tournament;
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
            .MATCHES_COMPLETED, DatabaseSingleton.MATCHES_TEAM_1_SCORE, DatabaseSingleton.MATCHES_TEAM_2_SCORE,
            DatabaseSingleton.MATCHES_ID};

    private MatchDataSource() {
    }

    public synchronized static MatchDataSource getInstance() {
        return ourInstance;
    }

    public void createMatch(Match match) {
        database = DatabaseSingleton.getInstance().openDatabase();
        Log.i(TAG, "createMatch: open");

        String query = "INSERT INTO " + DatabaseSingleton.MATCHES_TABLE + " VALUES(?,?,?,?,?,?)";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();

        statement.bindString(1, match.getHomeTeam().getName());
        statement.bindString(2, match.getAwayTeam() == null ? "" : match.getAwayTeam().getName());
        statement.bindLong(3, match.isCompleted() ? 1 : 0);
        statement.bindLong(4, match.getHomeScore());
        statement.bindLong(5, match.getAwayScore());
        statement.bindString(6, match.getId());
        statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
        close();
        Log.i(TAG, "createMatch: close");
    }

    public Match getMatch(String id) {
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.MATCHES_TABLE, columns, columns[5] + "=?", new String[]{id},
                null, null, null);
        if (cursor.moveToFirst()) {
            return Util.cursorToMatch(cursor);
        }
        return null;
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
        String[] columns = {DatabaseSingleton.TOURNAMENTS_NAME, DatabaseSingleton.TOURNAMENTS_TYPE, DatabaseSingleton
                .TOURNAMENTS_TEAMS, DatabaseSingleton.TOURNAMENTS_MAX_SIZE, DatabaseSingleton.TOURNAMENTS_COMPLETED,
                DatabaseSingleton.TOURNAMENTS_CLOSED, DatabaseSingleton.TOURNAMENTS_WIN_STAT, DatabaseSingleton
                .TOURNAMENTS_CURRENT_ROUND, DatabaseSingleton.TOURNAMENTS_MATCHES};
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, columns[0] + "=?", new
                String[]{tournamentName}, null, null, null);
        if (cursor.moveToFirst()) {
            Tournament tournament = Util.cursorToTournament(cursor);
            matches.addAll(tournament.getCurrentRoundOfMatches());
        }
        return matches;
    }

    public ArrayList<Match> getFinishedMatches(String tournamentName) {
        ArrayList<Match> finishedMatches = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        String[] columns = {DatabaseSingleton.TOURNAMENTS_NAME, DatabaseSingleton.TOURNAMENTS_TYPE, DatabaseSingleton
                .TOURNAMENTS_TEAMS, DatabaseSingleton.TOURNAMENTS_MAX_SIZE, DatabaseSingleton.TOURNAMENTS_COMPLETED,
                DatabaseSingleton.TOURNAMENTS_CLOSED, DatabaseSingleton.TOURNAMENTS_WIN_STAT, DatabaseSingleton
                .TOURNAMENTS_CURRENT_ROUND, DatabaseSingleton.TOURNAMENTS_MATCHES};
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, columns[0] + "=?", new
                String[]{tournamentName}, null, null, null);
        if (cursor.moveToFirst()) {
            Tournament tournament = Util.cursorToTournament(cursor);
            for (Match match: tournament.getCurrentRoundOfMatches()) {
                if (match.isCompleted())
                    finishedMatches.add(match);
            }
        }
        return finishedMatches;
    }

    private void close() {
        DatabaseSingleton.getInstance().closeDatabase();
    }

    public Match endMatch(Match match) {
        database = DatabaseSingleton.getInstance().openDatabase();
        Log.i(TAG, "endMatch: open");

        String query = "UPDATE " + DatabaseSingleton.MATCHES_TABLE + " SET " + columns[2] + "=?, " + columns[3] +
                "=?, " + columns[4] + "=? WHERE " + columns[5] + "=?";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();
        statement.bindLong(1, 1);
        statement.bindLong(2, match.getHomeScore());
        statement.bindLong(3, match.getAwayScore());
        statement.bindString(4, match.getId());
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
        String[] columns = {DatabaseSingleton.TOURNAMENTS_NAME, DatabaseSingleton.TOURNAMENTS_TYPE, DatabaseSingleton
                .TOURNAMENTS_TEAMS, DatabaseSingleton.TOURNAMENTS_MAX_SIZE, DatabaseSingleton.TOURNAMENTS_COMPLETED,
                DatabaseSingleton.TOURNAMENTS_CLOSED, DatabaseSingleton.TOURNAMENTS_WIN_STAT, DatabaseSingleton
                .TOURNAMENTS_CURRENT_ROUND, DatabaseSingleton.TOURNAMENTS_MATCHES};
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, columns[0] + "=?", new
                String[]{tournamentName}, null, null, null);
        if (cursor.moveToFirst()) {
            Tournament tournament = Util.cursorToTournament(cursor);
            for (Match match: tournament.getCurrentRoundOfMatches()) {
                if (!match.isCompleted())
                    unfinishedMatches.add(match);
            }
        }
        return unfinishedMatches;
    }
}
