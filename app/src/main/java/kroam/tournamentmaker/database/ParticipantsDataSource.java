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

    private static final String TAG = "ParticipantsDataSource";
    private static final String TYPE = "Team";
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

    public void createTeam(Team team) {
        if (!Util.validateColumn(team.getName()))
            throw new MissingColumnException(columns[1]);

        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", DBTables.PARTICIPANTS_TABLE,
                columns[1], columns[2]);
        database.beginTransaction();
        Log.i(TAG, "createTeam: " + query);

        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, team.getName());
        statement.bindString(2, TYPE);
        long id = statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
        ParticipantsTeamsRelation.getInstance().createParticipantTeamRelation(id, team.getLogoPath());
    }

    public ArrayList<Team> getTeams() {
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        ArrayList<Team> teams = new ArrayList<>();
        String query = String.format("SELECT p.*, pt.%s FROM %s p JOIN %s pt ON p.%s = pt.%s", DBColumns
                        .LOGO_PATH, DBTables.PARTICIPANTS_TABLE, DBTables.PARTICIPANTS_TEAMS_TABLE,
                DBColumns.ID, DBColumns.PARTICIPANT_ID);
        Log.i(TAG, "getTeams: " + query);

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
}
