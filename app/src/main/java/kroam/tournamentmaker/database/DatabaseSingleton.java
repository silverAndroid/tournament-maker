package kroam.tournamentmaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Rushil Perera on 11/20/2015.
 */
public class DatabaseSingleton extends SQLiteOpenHelper {

    private static final String DB_NAME = "TOURNAMENT_MAKER_DB";
    private static final int VERSION = 1;
    private static final String TAG = "DatabaseSingleton";

    private static final String CREATE_TOURNAMENTS_TABLE = String.format("CREATE TABLE %s (%s TEXT PRIMARY " +
                    "KEY, %s TEXT NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL DEFAULT 0, %s INTEGER " +
                    "NOT NULL DEFAULT 0, %s INTEGER NOT NULL DEFAULT 1);", DBTables.TOURNAMENTS_TABLE,
            DBColumns.NAME, DBColumns.TYPE, DBColumns.MAX_SIZE, DBColumns.FINISHED, DBColumns
                    .REGISTRATION_CLOSED, DBColumns.CURRENT_ROUND);
    private static final String CREATE_STATS_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY" +
            " AUTOINCREMENT, %s TEXT NOT NULL UNIQUE);", DBTables.STATS_TABLE, DBColumns.ID, DBColumns.KEY);
    private static final String CREATE_PARTICIPANTS_TABLE = String.format("CREATE TABLE %s (%s INTEGER " +
            "PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT NULL);", DBTables
            .PARTICIPANTS_TABLE, DBColumns.ID, DBColumns.NAME, DBColumns.TYPE);
    private static final String CREATE_MATCHES_TABLE = String.format("CREATE TABLE %s (%s TEXT, %s INTEGER," +
                    " %s INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(%s, %s) FOREIGN KEY (%s) REFERENCES %s " +
                    "(%s));", DBTables.MATCHES_TABLE, DBColumns.ID, DBColumns.PARTICIPANT_ID, DBColumns
                    .MATCH_COMPLETED, DBColumns.ID, DBColumns.PARTICIPANT_ID, DBColumns.PARTICIPANT_ID,
            DBTables.PARTICIPANTS_TABLE, DBColumns.ID);
    private static final String CREATE_TOURNAMENTS_PARTICIPANTS_TABLE = String.format("CREATE TABLE %s (%s " +
                    "TEXT, %s INTEGER, %s INTEGER, PRIMARY KEY (%s, %s), FOREIGN KEY (%s) REFERENCES %s " +
                    "(%s), FOREIGN KEY (%s) REFERENCES %s (%s));", DBTables.TOURNAMENTS_PARTICIPANTS_TABLE,
            DBColumns.TOURNAMENT_NAME, DBColumns.PARTICIPANT_ID, DBColumns.RANKING, DBColumns
                    .TOURNAMENT_NAME, DBColumns.PARTICIPANT_ID, DBColumns.TOURNAMENT_NAME, DBTables
                    .TOURNAMENTS_TABLE, DBColumns.NAME, DBColumns.PARTICIPANT_ID, DBTables
                    .PARTICIPANTS_TABLE, DBColumns.ID);
    private static final String CREATE_TOURNAMENTS_STATS_TABLE = String.format("CREATE TABLE %s (%s TEXT, " +
                    "%s INTEGER, %s INTEGER NOT NULL DEFAULT 0, PRIMARY KEY (%s, %s), FOREIGN KEY (%s) " +
                    "REFERENCES %s (%s), FOREIGN KEY (%s) REFERENCES %s (%s));", DBTables
                    .TOURNAMENTS_STATS_TABLE, DBColumns.TOURNAMENT_NAME, DBColumns.STAT_ID, DBColumns
                    .WINNING_STAT, DBColumns.TOURNAMENT_NAME, DBColumns.STAT_ID, DBColumns.TOURNAMENT_NAME,
            DBTables.TOURNAMENTS_TABLE, DBColumns.NAME, DBColumns.STAT_ID, DBTables.STATS_TABLE, DBColumns
                    .ID);
    private static final String CREATE_TOURNAMENTS_MATCHES_TABLE = String.format("CREATE TABLE %s (%s TEXT," +
                    " %s INTEGER, PRIMARY KEY (%s, %s), FOREIGN KEY (%s) REFERENCES %s (%s), FOREIGN KEY " +
                    "(%s) REFERENCES %s (%s));", DBTables.TOURNAMENTS_MATCHES_TABLE, DBColumns
                    .TOURNAMENT_NAME, DBColumns.MATCH_ID, DBColumns.TOURNAMENT_NAME, DBColumns.MATCH_ID,
            DBColumns.TOURNAMENT_NAME, DBTables.TOURNAMENTS_TABLE, DBColumns.NAME, DBColumns.MATCH_ID,
            DBTables.MATCHES_TABLE, DBColumns.ID);
    private static final String CREATE_PARTICIPANTS_TEAMS_TABLE = String.format("CREATE TABLE %s (%s " +
            "INTEGER PRIMARY KEY, %s TEXT NOT NULL, FOREIGN KEY (%s) REFERENCES %s (%s));", DBTables
            .PARTICIPANTS_TEAMS_TABLE, DBColumns.PARTICIPANT_ID, DBColumns.LOGO_PATH, DBColumns
            .PARTICIPANT_ID, DBTables.PARTICIPANTS_TABLE, DBColumns.ID);
    private static final String CREATE_PARTICIPANTS_PEOPLE_TABLE = String.format("CREATE TABLE %s (%s " +
                    "INTEGER, %s TEXT, %s TEXT, %s INTEGER, PRIMARY KEY (%s, %s, %s, %s), FOREIGN KEY (%s) " +
                    "REFERENCES %s (%s), FOREIGN KEY (%s) REFERENCES %s (%s));", DBTables
                    .PARTICIPANTS_PEOPLE_TABLE, DBColumns.PARTICIPANT_ID, DBColumns.EMAIL, DBColumns
                    .PHONE_NUMBER, DBColumns.TEAM_ID, DBColumns.PARTICIPANT_ID, DBColumns.EMAIL, DBColumns
                    .PHONE_NUMBER, DBColumns.TEAM_ID, DBColumns.PARTICIPANT_ID, DBTables
                    .PARTICIPANTS_TABLE, DBColumns.ID, DBColumns.TEAM_ID, DBTables.PARTICIPANTS_TABLE,
            DBColumns.ID);
    private static final String CREATE_PARTICIPANTS_CAPTAINS_TABLE = String.format("CREATE TABLE %s (%s " +
                    "INTEGER PRIMARY KEY, FOREIGN KEY (%s) REFERENCES %s (%s));", DBTables
                    .PARTICIPANTS_CAPTAINS_TABLE, DBColumns.PARTICIPANT_ID, DBColumns.PARTICIPANT_ID,
            DBTables.PARTICIPANTS_PEOPLE_TABLE, DBColumns.PARTICIPANT_ID);
    private static final String CREATE_PARTICIPANTS_STATS_TABLE = String.format("CREATE TABLE %s (%s " +
            "INTEGER, %s TEXT, %s INTEGER NOT NULL DEFAULT 0, PRIMARY KEY (%s, %s), FOREIGN KEY (%s) " +
            "REFERENCES %s (%s));", DBTables.PARTICIPANTS_STATS_TABLE, DBColumns.PARTICIPANT_ID, DBColumns
            .STAT_KEY, DBColumns.STAT_VALUE, DBColumns.PARTICIPANT_ID, DBColumns.STAT_KEY, DBColumns
            .PARTICIPANT_ID, DBTables.PARTICIPANTS_TABLE, DBColumns.ID);

    private static DatabaseSingleton instance;
    private int activeDatabaseCount = 0;
    private SQLiteDatabase connection; // always returns the same connection instance
    private static Context context;

    private DatabaseSingleton(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    private DatabaseSingleton(Context context, String name, SQLiteDatabase.CursorFactory factory, int
            version) {
        super(context, name, factory, version);
    }

    public static synchronized DatabaseSingleton getInstance() {
        if (instance == null)
            instance = new DatabaseSingleton(context);
        return instance;
    }

    public static synchronized void createInstance(Context context) {
        if (instance == null)
            instance = new DatabaseSingleton(context);
        DatabaseSingleton.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: " + CREATE_TOURNAMENTS_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_STATS_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_PARTICIPANTS_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_MATCHES_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_TOURNAMENTS_PARTICIPANTS_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_TOURNAMENTS_STATS_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_TOURNAMENTS_MATCHES_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_PARTICIPANTS_TEAMS_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_PARTICIPANTS_PEOPLE_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_PARTICIPANTS_CAPTAINS_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_PARTICIPANTS_STATS_TABLE);
        Log.d(TAG, "onCreate: " + CREATE_MATCHES_TABLE);
        db.execSQL(CREATE_TOURNAMENTS_TABLE);
        db.execSQL(CREATE_STATS_TABLE);
        db.execSQL(CREATE_PARTICIPANTS_TABLE);
        db.execSQL(CREATE_MATCHES_TABLE);
        db.execSQL(CREATE_TOURNAMENTS_PARTICIPANTS_TABLE);
        db.execSQL(CREATE_TOURNAMENTS_STATS_TABLE);
        db.execSQL(CREATE_TOURNAMENTS_MATCHES_TABLE);
        db.execSQL(CREATE_PARTICIPANTS_TEAMS_TABLE);
        db.execSQL(CREATE_PARTICIPANTS_PEOPLE_TABLE);
        db.execSQL(CREATE_PARTICIPANTS_CAPTAINS_TABLE);
        db.execSQL(CREATE_PARTICIPANTS_STATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.TOURNAMENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.STATS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.PARTICIPANTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.MATCHES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.TOURNAMENTS_PARTICIPANTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.TOURNAMENTS_STATS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.TOURNAMENTS_MATCHES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.PARTICIPANTS_TEAMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.PARTICIPANTS_PEOPLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.PARTICIPANTS_CAPTAINS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBTables.PARTICIPANTS_STATS_TABLE);
        onCreate(db);
    }

    public synchronized SQLiteDatabase openDatabase() {
        activeDatabaseCount++;
        Log.i(TAG, "openDatabase: " + activeDatabaseCount);
        if (activeDatabaseCount == 1) {
            // Opening new database
            connection = instance.getWritableDatabase();
        }
        return connection;
    }

    synchronized void closeDatabase() {
        activeDatabaseCount--;
        Log.i(TAG, "closeDatabase: " + activeDatabaseCount);
        if (activeDatabaseCount == 0) {
            // Closing database
            connection.close();
        }
    }
}
