package kroam.tournamentmaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Rushil Perera on 11/20/2015.
 */
public class DatabaseSingleton extends SQLiteOpenHelper {

    public static final String TOURNAMENTS_NAME = "NAME";
    public static final String TOURNAMENTS_TYPE = "TYPE";
    public static final String TOURNAMENTS_TEAMS = "TEAMS";
    public static final String TOURNAMENTS_MAX_SIZE = "SIZE";
    public static final String TOURNAMENTS_COMPLETED = "FINISHED";
    public static final String TOURNAMENTS_CLOSED = "REGISTRATION_CLOSED";
    public static final String TOURNAMENTS_TABLE = "TOURNAMENTS";
    public static final String TOURNAMENTS_WIN_STAT = "STAT_WIN";

    public static final String TEAMS_TABLE = "TEAMS";
    public static final String TEAMS_NAME = "NAME";
    public static final String TEAMS_CAPTAIN_NAME = "CAPTAIN_NAME";
    public static final String TEAMS_EMAIL = "CAPTAIN_EMAIL";
    public static final String TEAMS_PHONE_NUMBER = "PHONE_NUMBER";

    public static final String STATS_TABLE = "STATS";
    public static final String STATS_KEY = "KEY";
    public static final String STATS_TOURNAMENT_NAMES = "TOURNAMENT_NAMES";
    public static final String STATS_VALUES = "KEY_VALUES";

    public static final String MATCHES_TABLE = "SCHEDULE";
    public static final String MATCHES_TEAM_1 = "TEAM_1";
    public static final String MATCHES_TEAM_2 = "TEAM_2";
    public static final String MATCHES_TOURNAMENT_NAME = "TOURNAMENT";
    public static final String MATCHES_COMPLETED = "COMPLETED";

    private static final String NAME = "TOURNAMENT_MAKER_DB";
    private static final int VERSION = 7;
    private static final String CREATE_TEAMS_TABLE = "CREATE TABLE " + TEAMS_TABLE + "(" +
            TEAMS_NAME + " TEXT, " + TEAMS_CAPTAIN_NAME + " TEXT, " + TEAMS_EMAIL + " TEXT, " +
            TEAMS_PHONE_NUMBER + " TEXT, UNIQUE (" + TEAMS_NAME + "));";
    private static final String CREATE_TOURNAMENTS_TABLE = "CREATE TABLE " + TOURNAMENTS_TABLE + "(" +
            TOURNAMENTS_NAME + " TEXT, " + TOURNAMENTS_TYPE + " TEXT, " + TOURNAMENTS_TEAMS + " TEXT, " +
            TOURNAMENTS_MAX_SIZE + " INT, " + TOURNAMENTS_COMPLETED + " INT, " + TOURNAMENTS_CLOSED + " INT, " +
            TOURNAMENTS_WIN_STAT + " TEXT, UNIQUE(" + TOURNAMENTS_NAME + "));";
    private static final String CREATE_STATS_TABLE = "CREATE TABLE " + STATS_TABLE + "(" + STATS_KEY + " TEXT, " +
            STATS_VALUES + " TEXT, " + STATS_TOURNAMENT_NAMES + " TEXT, UNIQUE(" + STATS_KEY + "));";
    private static final String CREATE_MATCHES_TABLE = "CREATE TABLE " + MATCHES_TABLE + "(" + MATCHES_TEAM_1 + " " +
            "TEXT, " + MATCHES_TEAM_2 + " TEXT, " + MATCHES_COMPLETED + " INT, " + MATCHES_TOURNAMENT_NAME + " TEXT, " +
            "FOREIGN KEY(" + MATCHES_TEAM_1 + ", " + MATCHES_TEAM_2 + ") REFERENCES " + TEAMS_TABLE + "(" +
            TEAMS_NAME + ", " + TEAMS_NAME + "), FOREIGN KEY(" + MATCHES_TOURNAMENT_NAME + ") REFERENCES " +
            TOURNAMENTS_TABLE + "(" + TOURNAMENTS_NAME + "));";
    private static final String TAG = "DatabaseSingleton";
    private static DatabaseSingleton instance;
    private int activeDatabaseCount = 0;
    private SQLiteDatabase connection; // always returns the same connection instance

    private DatabaseSingleton(Context context) {
        this(context, NAME, null, VERSION);
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
    private DatabaseSingleton(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized DatabaseSingleton getInstance() {
        return instance;
    }

    public static synchronized void createInstance(Context context) {
        if (instance == null)
            instance = new DatabaseSingleton(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TOURNAMENTS_TABLE);
        db.execSQL(CREATE_STATS_TABLE);
        db.execSQL(CREATE_TEAMS_TABLE);
        db.execSQL(CREATE_MATCHES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TOURNAMENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STATS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TEAMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MATCHES_TABLE);
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

    public synchronized void closeDatabase() {
        activeDatabaseCount--;
        Log.i(TAG, "openDatabase: " + activeDatabaseCount);
        if (activeDatabaseCount == 0) {
            // Closing database
            connection.close();
        }
    }
}
