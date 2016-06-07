package kroam.tournamentmaker.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import kroam.tournamentmaker.Stat;

/**
 * Created by Rushil Perera on 11/23/2015.
 */
public class StatsDataSource {
    private static final String TAG = "StatsData";
    private static StatsDataSource instance = new StatsDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.ID, DBColumns.KEY};

    private StatsDataSource() {
    }

    public synchronized static StatsDataSource getInstance() {
        return instance;
    }

    public ArrayList<Stat> addStats(ArrayList<Stat> stats) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        String query = String.format("INSERT INTO %s (%s) VALUES (?);", DBTables.STATS_TABLE, columns[1]);
        Log.i(TAG, "addStats: " + query);

        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(query);
        for (int i = 0; i < stats.size(); i++) {
            statement.bindString(1, stats.get(i).getKey());
            long rowID = statement.executeInsert();
            stats.get(i).setID(rowID);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        return stats;
    }
}
