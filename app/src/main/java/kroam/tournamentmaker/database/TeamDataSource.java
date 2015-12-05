package kroam.tournamentmaker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.Util;

/**
 * Created by Kyle on 2015-11-28.
 */
public class TeamDataSource {

    private static TeamDataSource instance = new TeamDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DatabaseSingleton.TEAMS_NAME, DatabaseSingleton.TEAMS_CAPTAIN_NAME,
            DatabaseSingleton.TEAMS_EMAIL, DatabaseSingleton.TEAMS_PHONE_NUMBER};

    private TeamDataSource() {
    }

    public static TeamDataSource getInstance() {
        return instance;
    }

    public Team createTeam(Team team) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        String query = "INSERT INTO " + DatabaseSingleton.TEAMS_TABLE + " VALUES(?, ?, ?, ?)";
        database.beginTransaction();

        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, team.getName());
        statement.bindString(2, team.getCaptainName());
        statement.bindString(3, team.getCaptainEmail());
        statement.bindString(4, team.getPhoneNumber());
        statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
        close();
        return team;
    }

    public ArrayList<Team> getTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.TEAMS_TABLE, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Team team = Util.cursorToTeam(cursor);
                teams.add(team);
            } while (cursor.moveToNext());
        }
        close();
        return teams;
    }

    public ArrayList<Team> getTeamsFromTournament(String tournamentName) {
        ArrayList<Team> teams = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        String[] columns = {DatabaseSingleton.TOURNAMENTS_NAME, DatabaseSingleton.TOURNAMENTS_TYPE,
                DatabaseSingleton.TOURNAMENTS_TEAMS, DatabaseSingleton.TOURNAMENTS_MAX_SIZE, DatabaseSingleton
                .TOURNAMENTS_COMPLETED, DatabaseSingleton.TOURNAMENTS_CLOSED, DatabaseSingleton.TOURNAMENTS_WIN_STAT};
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, columns[0] + "=?", new
                String[]{tournamentName}, null, null, null);
        if (cursor.moveToFirst()) {
            Tournament tournament = Util.cursorToTournament(cursor);
            teams.addAll(tournament.getTeams());
        }
        close();
        return teams;
    }

    private void close() {
        database.close();
    }

    public Team getTeam(String teamName) {
        Team team;
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.TEAMS_TABLE, columns, columns[0] + "=?", new
                String[]{teamName}, null, null, null);
        if (cursor.moveToFirst()) {
            team = Util.cursorToTeam(cursor);
            return team;
        }
        close();
        return null;
    }

    public Team updateTeam(Team team) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        String query = "UPDATE " + DatabaseSingleton.TEAMS_TABLE + " SET " + columns[1] + "=?, " + columns[2] + "=?, " +
                "" + columns[3] + "=? WHERE " + columns[0] + "=?";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();

        statement.bindString(1, team.getCaptainName());
        statement.bindString(2, team.getCaptainEmail());
        statement.bindString(3, team.getPhoneNumber());
        statement.bindString(4, team.getName());
        statement.executeUpdateDelete();

        database.setTransactionSuccessful();
        database.endTransaction();
        close();
        return team;
    }
}
