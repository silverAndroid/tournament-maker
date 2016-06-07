package kroam.tournamentmaker.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Rushil Perera on 12/1/2015.
 * <p/>
 * Singleton class for the Match Section of the Database
 */
public class MatchesDataSource {
    private static final String TAG = "MatchesDataSource";
    private static MatchesDataSource ourInstance = new MatchesDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.ID, DBColumns.PARTICIPANT_ID, DBColumns.SCORE, DBColumns
            .MATCH_COMPLETED};

    private MatchesDataSource() {
    }

    public synchronized static MatchesDataSource getInstance() {
        return ourInstance;
    }
}
