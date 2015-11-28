package kroam.tournamentmaker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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
            ContentValues values = new ContentValues();
            values.put(columns[0], team.getName());
            values.put(columns[1], team.getCaptainName());
            values.put(columns[2], team.getCaptainEmail());
            values.put(columns[3], team.getPhoneNumber());
            database.insertOrThrow(DatabaseSingleton.TEAMS_TABLE, null, values);
            close();
            return team;
        }

        public ArrayList<Team> getTeams() {
            ArrayList<Team> Teams = new ArrayList<>();
            database = DatabaseSingleton.getInstance().getReadableDatabase();
            Cursor cursor = database.query(DatabaseSingleton.TEAMS_TABLE, columns, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Team team = Util.cursorToTeam(cursor);
                    Teams.add(team);
                } while (cursor.moveToNext());
            }
            close();
            return Teams;
        }

        private void close() {
            database.close();
        }

}
