package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;

import kroam.tournamentmaker.database.DBColumns;

/**
 * Created by silve on 2016-05-24.
 */
public class ParticipantsStatsRelation {
    private static ParticipantsStatsRelation ourInstance = new ParticipantsStatsRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.PARTICIPANT_ID, DBColumns.STAT_KEY, DBColumns.STAT_VALUE};

    private ParticipantsStatsRelation() {
    }

    public static ParticipantsStatsRelation getInstance() {
        return ourInstance;
    }
}
