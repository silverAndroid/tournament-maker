package kroam.tournamentmaker.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import kroam.tournamentmaker.Player;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.Util;
import kroam.tournamentmaker.database.relationships.ParticipantsTeamsRelation;

/**
 * Created by Kyle on 2015-11-28.
 */
public class ParticipantsDataSource {

    private static final String TAG = "ParticipantsData";
    private static ParticipantsDataSource instance = new ParticipantsDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.ID, DBColumns.NAME, DBColumns.TYPE};

    private ParticipantsDataSource() {
    }

    public synchronized static ParticipantsDataSource getInstance() {
        return instance;
    }

    public void createPlayer(Player player) {

    }

    public long createTeam(Team team) {
        if (!Util.validateColumn(team.getName()))
            throw new MissingColumnException(columns[1]);

        final String TYPE = "Team";
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", DBTables.PARTICIPANTS,
                columns[1], columns[2]);
        Log.i(TAG, "createTeam: " + query);

        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, team.getName());
        statement.bindString(2, TYPE);
        long id = statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
        ParticipantsTeamsRelation.getInstance().createParticipantTeamRelation(id, team.getLogoPath());
        return id;
    }

    public ArrayList<Team> getTeams() {
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        ArrayList<Team> teams = new ArrayList<>();
        String query = String.format("SELECT p.*, pt.%s FROM %s p JOIN %s pt ON p.%s = pt.%s", DBColumns
                        .LOGO_PATH, DBTables.PARTICIPANTS, DBTables.PARTICIPANTS_TEAMS, DBColumns.ID,
                DBColumns.PARTICIPANT_ID);
        Log.i(TAG, "getParticipants: " + query);

        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                teams.add(new Team(cursor));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed())
            cursor.close();
        return teams;
    }

    public Team getTeam(long teamID) {
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Team team = null;
        String query = String.format("SELECT p.*, pt.%s FROM %s p JOIN %s pt ON p.%s = pt.%s WHERE p.%s=?",
                DBColumns.LOGO_PATH, DBTables.PARTICIPANTS, DBTables.PARTICIPANTS_TEAMS, DBColumns.ID,
                DBColumns.PARTICIPANT_ID, DBColumns.ID);
        Log.i(TAG, "getTeam: " + query);

        Cursor cursor = database.rawQuery(query, new String[]{Long.toString(teamID)});
        if (cursor.moveToFirst()) {
            team = new Team(cursor);
        }
        if (!cursor.isClosed())
            cursor.close();

        return team;
    }
}
