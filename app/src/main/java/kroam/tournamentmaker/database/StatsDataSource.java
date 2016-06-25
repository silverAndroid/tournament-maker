package kroam.tournamentmaker.database;

import android.database.Cursor;
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
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("INSERT INTO %s (%s) VALUES (?);", DBTables.STATS, columns[1]);
        Log.i(TAG, "addStats: " + query);

        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(query);
        for (int i = 0; i < stats.size(); i++) {
            Stat stat;
            if ((stat = getStat(stats.get(i).getKey())) == null) {
                statement.bindString(1, stats.get(i).getKey());
                long rowID = statement.executeInsert();
                stats.get(i).setID(rowID);
            } else {
                stats.get(i).setID(stat.getID());
            }
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        return stats;
    }

    private Stat getStat(String key) {
        Stat stat = null;
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DBTables.STATS, columns, columns[1] + "=?", new String[]{key}, null,
                null, null);
        if (cursor.moveToFirst()) {
            stat = new Stat(cursor);
        }
        cursor.close();
        return stat;
    }

    private Stat updateStat(Stat stat) {
        database = DatabaseSingleton.getInstance().openDatabase();
        return null;
    }
}
