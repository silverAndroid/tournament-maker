package kroam.tournamentmaker.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import kroam.tournamentmaker.Stat;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.Util;

/**
 * Created by Rushil Perera on 11/22/2015.
 */
public class TournamentDataSource {

    private static final String TAG = "TournamentData";
    private static TournamentDataSource instance = new TournamentDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DatabaseSingleton.TOURNAMENTS_NAME, DatabaseSingleton.TOURNAMENTS_TYPE,
            DatabaseSingleton.TOURNAMENTS_TEAMS, DatabaseSingleton.TOURNAMENTS_MAX_SIZE, DatabaseSingleton
            .TOURNAMENTS_COMPLETED, DatabaseSingleton.TOURNAMENTS_CLOSED, DatabaseSingleton.TOURNAMENTS_WIN_STAT,
            DatabaseSingleton.TOURNAMENTS_CURRENT_ROUND, DatabaseSingleton.TOURNAMENTS_MATCHES, DatabaseSingleton
            .TOURNAMENTS_RANKINGS, DatabaseSingleton.TOURNAMENTS_WINS};

    private TournamentDataSource() {
    }

    public synchronized static TournamentDataSource getInstance() {
        return instance;
    }

    public Tournament createTournament(Tournament tournament) throws SQLiteConstraintException {
        database = DatabaseSingleton.getInstance().openDatabase();
        Log.i(TAG, "createTournament: open");
        try {
            String query = "INSERT INTO " + DatabaseSingleton.TOURNAMENTS_TABLE + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    " ?, ?)";

            SQLiteStatement statement = database.compileStatement(query);
            database.beginTransaction();
            statement.bindString(1, tournament.getName());
            statement.bindString(2, tournament.getType());
            statement.bindString(3, Util.convertArrayToString(tournament.getTeams().toArray()));
            statement.bindLong(4, tournament.getMaxSize());
            statement.bindLong(5, tournament.isCompleted() ? 1 : 0);
            statement.bindLong(6, tournament.isRegistrationClosed() ? 1 : 0);
            statement.bindString(7, tournament.getWinningStat() == null ? "" : tournament.getWinningStat().getKey());
            statement.bindLong(8, tournament.getCurrentRound());
            statement.bindString(9, Util.convert2DListToString(tournament.getMatches()));
            statement.bindString(10, Util.convertStatValueHashMapToString(tournament.getRankings()));
            statement.bindString(11, Util.convertStatValueHashMapToString(tournament.getWins()));
            statement.executeInsert();

            database.setTransactionSuccessful();
            database.endTransaction();
            close();
        } catch (SQLiteConstraintException e) {
            database.endTransaction();
            close();
            updateTournament(tournament);
        }
        return tournament;
    }

    public Tournament updateTournament(Tournament tournament) {
        database = DatabaseSingleton.getInstance().openDatabase();
        Log.i(TAG, "updateTournament: open");
        String query = "UPDATE " + DatabaseSingleton.TOURNAMENTS_TABLE + " SET " + columns[1] + "=?, " + columns[2] +
                "=?, " + columns[3] + "=?, " + columns[4] + "=?, " + columns[5] + "=?, " + columns[6] + "=?, " +
                columns[7] + "=?, " + columns[8] + "=?, " + columns[9] + "=?, " + columns[10] + "=? WHERE " + columns[0]
                + "=?";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();

        statement.bindString(1, tournament.getType());
        statement.bindString(2, Util.convertArrayToString(tournament.getTeams().toArray()));
        statement.bindLong(3, tournament.getMaxSize());
        statement.bindLong(4, tournament.isCompleted() ? 1 : 0);
        statement.bindLong(5, tournament.isRegistrationClosed() ? 1 : 0);
        Stat winningStat = tournament.getWinningStat();
        statement.bindString(6, winningStat == null ? "" : tournament.getWinningStat().getKey());
        statement.bindLong(7, tournament.getCurrentRound());
        statement.bindString(8, Util.convert2DListToString(tournament.getMatches()));
        statement.bindString(9, Util.convertStatValueHashMapToString(tournament.getRankings()));
        statement.bindString(10, Util.convertStatValueHashMapToString(tournament.getWins()));
        statement.bindString(11, tournament.getName());
        statement.executeUpdateDelete();

        database.setTransactionSuccessful();
        database.endTransaction();
        close();
        Log.i(TAG, "updateTournament: close");
        return tournament;
    }

    public ArrayList<Team> getTeamsFromTournament(String tournamentName) {
        ArrayList<Team> teams = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, columns[0] + "=?", new
                String[]{tournamentName}, null, null, null);
        if (cursor.moveToFirst()) {
            Tournament tournament = Util.cursorToTournament(cursor);
            teams.addAll(tournament.getTeams());
        }
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
        return tournaments;
    }

    private void close() {
        DatabaseSingleton.getInstance().closeDatabase();
    }

    public Tournament getTournament(String name) {
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, columns[0] + "=?", new
                String[]{name}, null, null, null);
        if (cursor.moveToFirst()) {
            return Util.cursorToTournament(cursor);
        }
        return null;
    }

    public Tournament getTournamentFromMatch(String id) {
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, columns[8] + " LIKE ?", new
                String[]{"%" + id + "%"}, null, null, null);
        if (cursor.moveToFirst()) {
            return Util.cursorToTournament(cursor);
        }
        return null;
    }
}
