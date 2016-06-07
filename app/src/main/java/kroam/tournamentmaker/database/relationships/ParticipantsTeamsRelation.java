package kroam.tournamentmaker.database.relationships;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import kroam.tournamentmaker.Util;
import kroam.tournamentmaker.database.DBColumns;
import kroam.tournamentmaker.database.DBTables;
import kroam.tournamentmaker.database.DatabaseSingleton;
import kroam.tournamentmaker.database.MissingColumnException;

/**
 * Created by silve on 2016-05-24.
 */
public class ParticipantsTeamsRelation {
    private static final String TAG = "PTR";
    private static ParticipantsTeamsRelation ourInstance = new ParticipantsTeamsRelation();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.PARTICIPANT_ID, DBColumns.LOGO_PATH};

    private ParticipantsTeamsRelation() {
    }

    public synchronized static ParticipantsTeamsRelation getInstance() {
        return ourInstance;
    }

    public void createParticipantTeamRelation(long participantID, String logoPath) {
        if (!Util.validateColumn(logoPath))
            throw new MissingColumnException(columns[1]);

        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("INSERT INTO %s VALUES (?, ?)", DBTables.PARTICIPANTS_TEAMS_TABLE);
        database.beginTransaction();
        Log.i(TAG, "createParticipantTeamRelation: " + query);

        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, participantID);
        statement.bindString(2, logoPath);
        statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
