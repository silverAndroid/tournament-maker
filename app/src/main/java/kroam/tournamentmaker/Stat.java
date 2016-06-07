package kroam.tournamentmaker;

import android.database.Cursor;

/**
 * Created by Rushil Perera on 11/23/2015.
 * <p>
 * An instance of Stat represents one particular match statistic. Each instance of Stat is defined
 * when chosen to be "used" in a Tournament.
 */
public class Stat {

    private long id;
    private String key;
    private String tournamentName;

    public Stat() {
    }

    public Stat(String key) {
        this.key = key;
    }

    public Stat(Cursor cursor) {

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
}
