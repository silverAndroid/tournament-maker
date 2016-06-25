package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;

import kroam.tournamentmaker.database.DBColumns;

/**
 * Created by silve on 2016-06-10.
 */
public class PeopleTeamsRelation {
    private static final String TAG = "PeTR";
    private static PeopleTeamsRelation ourInstance = new PeopleTeamsRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.PARTICIPANT_ID, DBColumns.TEAM_ID};

    private PeopleTeamsRelation() {
    }

    public synchronized static PeopleTeamsRelation getInstance() {
        return ourInstance;
    }
}
