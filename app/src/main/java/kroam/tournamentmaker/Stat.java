package kroam.tournamentmaker;

import android.database.Cursor;

import kroam.tournamentmaker.database.DBColumns;

/**
 * Created by Rushil Perera on 11/23/2015.
 * <p>
 * An instance of Stat represents one particular match statistic. Each instance of Stat is defined
 * when chosen to be "used" in a Tournament.
 */
public class Stat {

    private long id;
    private String key;
    private int value;
    private String tournamentName;

    public Stat() {
        id = -1;
        value = -1;
    }

    public Stat(String key) {
        this.key = key;
        id = -1;
        value = -1;
    }

    public Stat(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex(DBColumns.ID));
        key = cursor.getString(cursor.getColumnIndex(DBColumns.KEY));
        int valueColumnIndex;
        if ((valueColumnIndex = cursor.getColumnIndex(DBColumns.STAT_VALUE)) != -1) {
            value = cursor.getInt(valueColumnIndex);
        } else {
            value = -1;
        }
    }

    public long getID() {
        return id;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getKey() {
        return key;
    }

    public void setID(long id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
