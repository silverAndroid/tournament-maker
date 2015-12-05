package kroam.tournamentmaker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import kroam.tournamentmaker.Stat;
import kroam.tournamentmaker.Util;

/**
 * Created by Rushil Perera on 11/23/2015.
 */
public class StatsDataSource {
    private static final String TAG = "StatsData";
    private static StatsDataSource instance = new StatsDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DatabaseSingleton.STATS_KEY, DatabaseSingleton.STATS_TOURNAMENT_NAMES,
            DatabaseSingleton.STATS_VALUES};

    private StatsDataSource() {
    }

    public synchronized static StatsDataSource getInstance() {
        return instance;
    }

    public void addStat(Stat stat) {
        database = DatabaseSingleton.getInstance().openDatabase();
        ContentValues values = new ContentValues(3);
        values.put(columns[0], stat.getKey());
        values.put(columns[1], Util.convertArrayToString(stat.getTournamentNames().toArray()));
        values.put(columns[2], Util.convertHashMapToString(stat.getValues()));
        database.insert(DatabaseSingleton.STATS_TABLE, null, values);
        close();
    }

    public void addStats(ArrayList<Stat> stats) {
        database = DatabaseSingleton.getInstance().openDatabase();
        Log.i(TAG, "addStats: open");
        String query = "INSERT INTO " + DatabaseSingleton.STATS_TABLE + "(" + columns[0] + ", " + columns[1] + ", " +
                columns[2] + ") VALUES (?, ?, ?);";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();
        for (Stat stat : stats) {
            statement.bindString(1, stat.getKey());
            statement.bindString(2, Util.convertArrayToString(stat.getTournamentNames().toArray()));
            statement.bindString(3, Util.convertHashMapToString(stat.getValues()));
            Cursor cursor = database.query(DatabaseSingleton.STATS_TABLE, columns, DatabaseSingleton.STATS_KEY
                    + "=?", new String[]{stat.getKey()}, null, null, null);
            if (cursor.moveToFirst()) {
                String newTournamentNames = cursor.getString(1);
                String newValues = cursor.getString(2);
                query = "UPDATE " + DatabaseSingleton.STATS_TABLE + " SET " + columns[1] + "=?, " + columns[2] + "=? " +
                        "WHERE " + DatabaseSingleton.STATS_KEY + "=?";
                SQLiteStatement updateStatement = database.compileStatement(query);
                updateStatement.bindString(1, newTournamentNames + ", " + Util.convertArrayToString(stat
                        .getTournamentNames().toArray()));
                updateStatement.bindString(2, newValues + (newValues.isEmpty() || stat.getValues().isEmpty() ? "" :
                        ", ") + Util.convertHashMapToString(stat.getValues()));
                updateStatement.bindString(3, stat.getKey());
                updateStatement.executeUpdateDelete();
            } else {
                statement.executeInsert();
            }
            cursor.close();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        close();
        Log.i(TAG, "addStats: close");
    }

    public ArrayList<Stat> getTournamentStats(String name) {
        ArrayList<Stat> stats = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.STATS_TABLE, columns, DatabaseSingleton.STATS_TOURNAMENT_NAMES
                + " LIKE ?", new String[]{"%" + name + "%"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Stat stat = Util.cursorToStat(cursor);
                stats.add(stat);
            } while (cursor.moveToNext());
        }
        return stats;
    }

    public void updateStats(ArrayList<Stat> stats) {
        database = DatabaseSingleton.getInstance().openDatabase();
        Log.i(TAG, "updateStats: open");
        for (Stat stat : stats) {
            String query = "UPDATE " + DatabaseSingleton.STATS_TABLE + " SET " + columns[1] + "=?, " + columns[2] +
                    "=? WHERE " + DatabaseSingleton.STATS_KEY + "=?;";

            Log.i(TAG, "updateStats: " + query);
            SQLiteStatement statement = database.compileStatement(query);
            database.beginTransaction();
            statement.bindString(1, Util.convertArrayToString(stat.getTournamentNames().toArray()));
            statement.bindString(2, Util.convertHashMapToString(stat.getValues()));
            statement.bindString(3, stat.getKey());
            statement.executeUpdateDelete();
            database.setTransactionSuccessful();
            database.endTransaction();
        }
        close();
        Log.i(TAG, "updateStats: close");
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
        return stats;
    }

    public void deleteStat(String key) {
        database = DatabaseSingleton.getInstance().openDatabase();
        Log.i(TAG, "deleteStat: open");
        database.delete(DatabaseSingleton.STATS_TABLE, DatabaseSingleton.STATS_KEY + "=?", new String[]{key});
        close();
        Log.i(TAG, "deleteStat: close");
    }

    private void close() {
        DatabaseSingleton.getInstance().closeDatabase();
    }

    public Stat getStat(String key) {
        if (key == null)
            return null;
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.STATS_TABLE, columns, columns[0] + "=?", new String[]{key},
                null, null, null);
        if (cursor.moveToFirst())
            return Util.cursorToStat(cursor);
        return null;
    }
}
