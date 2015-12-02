package kroam.tournamentmaker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 11/23/2015.
 */
public class StatsDataSource {
    private static final String TAG = "STATSDATA";
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
            statement.bindString(1, stat.getKey());
            statement.bindString(2, Util.convertArrayToString(stat.getTournamentNames().toArray()));
            statement.bindString(3, Util.convertArrayToString(stat.getValues().toArray()));
            Cursor cursor = database.query(DatabaseSingleton.STATS_TABLE, columns, DatabaseSingleton.STATS_KEY
                    + " = ?", new String[]{stat.getKey()}, null, null, null);
            if (cursor.moveToFirst()) {
                String newTournamentNames = cursor.getString(1);
                String newValues = cursor.getString(2);
                query = "UPDATE " + DatabaseSingleton.STATS_TABLE + " SET " + columns[1] + "=\'" + newTournamentNames
                        + ", " + Util.convertArrayToString(stat.getTournamentNames().toArray()) + "\', " + columns[2]
                        + "=\'" + newValues + (newValues.isEmpty() || stat.getValues().isEmpty() ? "" : ", ") + Util
                        .convertArrayToString(stat.getValues().toArray()) + "\';";
                database.execSQL(query);
            } else {
                statement.executeInsert();
            }
            cursor.close();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        close();
    }

    public ArrayList<Stat> getTournamentStats(String name) {
        ArrayList<Stat> stats = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseSingleton.STATS_TABLE + " LIKE \'%?%\'";
        Log.i(TAG, "getTournamentStats: " + query);
        Cursor cursor = database.rawQuery(query, new String[]{name});
        if (cursor.moveToFirst()) {
            do {
                Stat stat = Util.cursorToStat(cursor);
                stats.add(stat);
            } while (cursor.moveToNext());
        }
        close();
        return stats;
    }

    public void updateStats(ArrayList<Stat> stats) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        String query = "UPDATE " + DatabaseSingleton.STATS_TABLE + " SET " + columns[1] + "=\'?\', " + columns[2] +
                "=\'?\';";

        SQLiteStatement statement = database.compileStatement(query);
        database.beginTransaction();
        for (Stat stat : stats) {
            statement.bindString(2, Util.convertArrayToString(stat.getTournamentNames().toArray()));
            statement.bindString(3, Util.convertArrayToString(stat.getValues().toArray()));
            statement.executeUpdateDelete();
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
        close();
    }

    private void close() {
        database.close();
    }
}
