package kroam.tournamentmaker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 11/22/2015.
 */
public class TournamentsDataSource {

    private static TournamentsDataSource instance = new TournamentsDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DatabaseSingleton.TOURNAMENTS_NAME, DatabaseSingleton.TOURNAMENTS_TYPE,
            DatabaseSingleton.TOURNAMENTS_TEAMS, DatabaseSingleton.TOURNAMENTS_MAX_SIZE, DatabaseSingleton
            .TOURNAMENTS_COMPLETED};

    private TournamentsDataSource() {
    }

    public static TournamentsDataSource getInstance() {
        return instance;
    }

    public Tournament createTournament(Tournament tournament) {
        database = DatabaseSingleton.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columns[0], tournament.getName());
        values.put(columns[1], tournament.getType());
        values.put(columns[2], Util.convertArrayToString(tournament.getTeams()));
        values.put(columns[3], tournament.getMaxSize());
        values.put(columns[4], tournament.isCompleted() ? 1 : 0);
        database.insertOrThrow(DatabaseSingleton.TOURNAMENTS_TABLE, null, values);
        close();
        return tournament;
    }

    public ArrayList<Tournament> getTournaments() {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DatabaseSingleton.TOURNAMENTS_TABLE, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Tournament tournament = Util.cursorToTournament(cursor);
                tournaments.add(tournament);
            } while (cursor.moveToNext());
        }
        close();
        return tournaments;
    }

    private void close() {
        database.close();
    }
}
