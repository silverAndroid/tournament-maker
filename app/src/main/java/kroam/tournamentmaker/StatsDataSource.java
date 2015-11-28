package kroam.tournamentmaker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 11/23/2015.
 */
public class StatsDataSource {
    private static StatsDataSource instance = new StatsDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DatabaseSingleton.STATS_KEY, DatabaseSingleton.STATS_TOURNAMENT_NAMES,
            DatabaseSingleton.STATS_VALUES};

    private StatsDataSource() {
    }

    public static StatsDataSource getInstance() {
        return instance;
    }

    public void addStat(Stat stat) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues(3);
        values.put(columns[0], stat.getKey());
        values.put(columns[1], stat.getTournamentNames().toString());
        values.put(columns[2], stat.getValues().toString());
        database.insert(DatabaseSingleton.STATS_TABLE, null, values);
        close();
    }

    public void addStats(ArrayList<Stat> stats) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        String query = "INSERT INTO " + DatabaseSingleton.STATS_TABLE + "(" + columns[0] + ", " + columns[1] + ", " +
                columns[2] + ") VALUES (?, ?, ?);";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();
        for (Stat stat : stats) {
            statement.bindString(0, stat.getKey());
            statement.bindString(0, Util.convertArrayToString(stat.getTournamentNames().toArray()));
            statement.bindString(0, Util.convertArrayToString(stat.getValues().toArray()));
            long id = statement.executeInsert();
            if (id == -1) {
                Cursor cursor = database.query(DatabaseSingleton.STATS_TABLE, columns, DatabaseSingleton.STATS_KEY +
                        "=?", new String[]{stat.getKey()}, null, null, null);
                String newTournamentNames = cursor.getString(1);
                String newValues = cursor.getString(2);
                statement.bindString(1, cursor.getString(1) + newTournamentNames);
                statement.bindString(2, cursor.getString(2) + newValues);
                statement.executeUpdateDelete();
                cursor.close();
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        close();
    }

    public ArrayList<Stat> getStats() {
        ArrayList<Stat> stats = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.STATS_TABLE, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Stat stat = Util.cursorToStat(cursor);
                stats.add(stat);
            } while (cursor.moveToNext());
        }
        close();
        return stats;
    }

    public void deleteStat(String key) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        database.delete(DatabaseSingleton.STATS_TABLE, DatabaseSingleton.STATS_KEY + "=?", new String[]{key});
    }

    private void close() {
        database.close();
    }
}
