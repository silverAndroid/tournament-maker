package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;

import kroam.tournamentmaker.database.DBColumns;

/**
 * Created by silve on 2016-05-24.
 */
public class ParticipantsPeopleRelation {
    private static final String TAG = "PPR";
    private static ParticipantsPeopleRelation ourInstance = new ParticipantsPeopleRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.PARTICIPANT_ID, DBColumns.EMAIL, DBColumns.PHONE_NUMBER};

    private ParticipantsPeopleRelation() {
    }

    public static ParticipantsPeopleRelation getInstance() {
        return ourInstance;
    }
}
