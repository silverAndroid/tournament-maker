package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;

import kroam.tournamentmaker.database.DBColumns;

/**
 * Created by silve on 2016-05-24.
 */
public class StatsParticipantsRelation {
    private static final String TAG = "SPR";
    private static StatsParticipantsRelation ourInstance = new StatsParticipantsRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.PARTICIPANT_ID, DBColumns.STAT_ID, DBColumns.STAT_VALUE};

    private StatsParticipantsRelation() {
    }

    public static StatsParticipantsRelation getInstance() {
        return ourInstance;
    }
}
