package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;

import kroam.tournamentmaker.database.DBColumns;

/**
 * Created by silve on 2016-05-24.
 */
public class TournamentsMatchesRelation {
    private static TournamentsMatchesRelation ourInstance = new TournamentsMatchesRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.TOURNAMENT_NAME, DBColumns.MATCH_ID};

    private TournamentsMatchesRelation() {
    }

    public static TournamentsMatchesRelation getInstance() {
        return ourInstance;
    }
}
