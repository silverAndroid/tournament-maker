package kroam.tournamentmaker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 11/22/2015.
 */
public class TournamentDataSource {

    private static final String TAG = "TOURNAMENTDATA";
    private static TournamentDataSource instance = new TournamentDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DatabaseSingleton.TOURNAMENTS_NAME, DatabaseSingleton.TOURNAMENTS_TYPE,
            DatabaseSingleton.TOURNAMENTS_TEAMS, DatabaseSingleton.TOURNAMENTS_MAX_SIZE, DatabaseSingleton
            .TOURNAMENTS_COMPLETED, DatabaseSingleton.TOURNAMENTS_CLOSED, DatabaseSingleton.TOURNAMENTS_WIN_STAT};

    private TournamentDataSource() {
    }

    public static TournamentDataSource getInstance() {
        return instance;
    }

    public Tournament createTournament(Tournament tournament) throws SQLiteConstraintException {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columns[0], tournament.getName());
        values.put(columns[1], tournament.getType());
        values.put(columns[2], Util.convertArrayToString(tournament.getTeams().toArray()));
        values.put(columns[3], tournament.getMaxSize());
        values.put(columns[4], tournament.isCompleted() ? 1 : 0);
        values.put(columns[5], tournament.isRegistrationClosed() ? 1 : 0);
        database.insertOrThrow(DatabaseSingleton.TOURNAMENTS_TABLE, null, values);
        close();
        return tournament;
    }

    public Tournament updateTournament(Tournament tournament) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        String query = "UPDATE " + DatabaseSingleton.TOURNAMENTS_TABLE + " SET " + columns[1] + "=?, " + columns[2] +
                "=?, " + columns[3] + "=?, " + columns[4] + "=?, " + columns[5] + "=?, " + columns[6] + "=? WHERE " +
                columns[0] + "=?";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();
        statement.bindString(1, tournament.getType());
        statement.bindString(2, Util.convertArrayToString(tournament.getTeams().toArray()));
        statement.bindLong(3, tournament.getMaxSize());
        statement.bindLong(4, tournament.isCompleted() ? 1 : 0);
        statement.bindLong(5, tournament.isRegistrationClosed() ? 1 : 0);
        Stat winningStat = tournament.getWinningStat();
        statement.bindString(6, winningStat == null ? "" : tournament.getWinningStat().getKey());
        statement.bindString(7, tournament.getName());
        Log.i(TAG, "updateTournament: " + statement.toString());
        statement.executeUpdateDelete();

        database.setTransactionSuccessful();
        database.endTransaction();
        close();
        return tournament;
    }

    public ArrayList<Team> getTeamsFromTournament(String tournamentName) {
        ArrayList<Team> teams = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, columns[2] + "=?", new
                String[]{tournamentName}, null, null, null);
        if (cursor.moveToFirst()) {
            Tournament tournament = Util.cursorToTournament(cursor);
            teams.addAll(tournament.getTeams());
        }
        close();
        return teams;
    }

    public ArrayList<Tournament> getTournaments() {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Tournament tournament = Util.cursorToTournament(cursor);
                tournaments.add(tournament);
            } while (cursor.moveToNext());
        }
        close();
        return tournaments;
    }

    private void close() {
        database.close();
    }

    public Tournament getTournament(String name) {
        Tournament tournament = null;
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, columns[0] + "=?", new
                String[]{name}, null, null, null);
        if (cursor.moveToFirst()) {
            tournament = Util.cursorToTournament(cursor);
        }
        close();
        return tournament;
    }
}
