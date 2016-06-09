package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import kroam.tournamentmaker.Participant;
import kroam.tournamentmaker.database.DBColumns;
import kroam.tournamentmaker.database.DBTables;
import kroam.tournamentmaker.database.DatabaseSingleton;

/**
 * Created by silve on 2016-05-24.
 */
public class TournamentsParticipantsRelation {
    private static final String TAG = "TPR";
    private static TournamentsParticipantsRelation ourInstance = new TournamentsParticipantsRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.TOURNAMENT_NAME, DBColumns.PARTICIPANT_ID, DBColumns.RANKING};

    private TournamentsParticipantsRelation() {
    }

    public static TournamentsParticipantsRelation getInstance() {
        return ourInstance;
    }

    public void createTournamentParticipantRelation(String tournamentName, int participantID) {
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", DBTables
                .TOURNAMENTS_PARTICIPANTS_TABLE, DBColumns.TOURNAMENT_NAME, DBColumns.PARTICIPANT_ID);
        database.beginTransaction();
        Log.i(TAG, "createTournamentParticipationRelation: " + query);

        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, tournamentName);
        statement.bindLong(2, participantID);
        statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void deleteTournamentParticipantRelations(String tournamentName) {
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("DELETE FROM %s WHERE %s=?;", DBTables.TOURNAMENTS_PARTICIPANTS_TABLE,
                columns[0]);
        Log.i(TAG, "deleteTournamentParticipantRelations: " + query);

        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, tournamentName);
        statement.executeUpdateDelete();
        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
