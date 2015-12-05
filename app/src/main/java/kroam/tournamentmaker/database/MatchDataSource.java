package kroam.tournamentmaker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import kroam.tournamentmaker.Match;
import kroam.tournamentmaker.Util;

/**
 * Created by Rushil Perera on 12/1/2015.
 * <p/>
 * Singleton class for the Match Section of the Database
 */
public class MatchDataSource {
    private static MatchDataSource ourInstance = new MatchDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DatabaseSingleton.MATCHES_TEAM_1, DatabaseSingleton.MATCHES_TEAM_2, DatabaseSingleton
            .MATCHES_COMPLETED, DatabaseSingleton.MATCHES_TOURNAMENT_NAME};

    private MatchDataSource() {
    }

    public static MatchDataSource getInstance() {
        return ourInstance;
    }

    public void createMatch(Match match) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columns[0], match.getHomeTeam().getName());
        values.put(columns[1], match.getAwayTeam().getName());
        values.put(columns[2], match.isCompleted() ? 1 : 0);
        values.put(columns[3], match.getAssociatedTournament().getName());
        database.insertOrThrow(DatabaseSingleton.MATCHES_TABLE, null, values);
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
        return matches;
    }

    public ArrayList<Match> getFinishedMatches() {
        ArrayList<Match> finishedMatches = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.MATCHES_TABLE, columns, columns[2] + "=?", new
                String[]{"1"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Match match = Util.cursorToMatch(cursor);
                finishedMatches.add(match);
            } while (cursor.moveToNext());
        }
        return finishedMatches;
    }
}
