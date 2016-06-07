package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;

import kroam.tournamentmaker.database.DBColumns;

/**
 * Created by silve on 2016-05-24.
 */
public class ParticipantsCaptainsRelation {
    private static ParticipantsCaptainsRelation ourInstance = new ParticipantsCaptainsRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.PARTICIPANT_ID};

    private ParticipantsCaptainsRelation() {
    }

    public static ParticipantsCaptainsRelation getInstance() {
        return ourInstance;
    }
}
