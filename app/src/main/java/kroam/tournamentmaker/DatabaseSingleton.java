package kroam.tournamentmaker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rushil Perera on 11/20/2015.
 */
public class DatabaseSingleton extends SQLiteOpenHelper {

    public static final String TOURNAMENTS_NAME = "NAME";
    public static final String TOURNAMENTS_TYPE = "TYPE";
    public static final String TOURNAMENTS_TEAMS = "TEAMS";
    public static final String TOURNAMENTS_MAX_SIZE = "SIZE";
    public static final String TOURNAMENTS_COMPLETED = "FINISHED";
    public static final String TOURNAMENTS_TABLE = "TOURNAMENTS";

    public static final String STATS_TABLE = "STATS";
    public static final String STATS_KEY = "KEY";
    public static final String STATS_TOURNAMENT_NAMES = "TOURNAMENT_NAMES";
    public static final String STATS_VALUES = "KEY_VALUES";

    private static final String NAME = "TOURNAMENT_MAKER_DB";
    private static final int VERSION = 2;
    private static final String CREATE_TOURNAMENTS_TABLE = "CREATE TABLE " + TOURNAMENTS_TABLE + "(" +
            TOURNAMENTS_NAME + " TEXT, " + TOURNAMENTS_TYPE + " TEXT, " + TOURNAMENTS_TEAMS + " TEXT, " +
            TOURNAMENTS_MAX_SIZE + " INT, " + TOURNAMENTS_COMPLETED + " INT, UNIQUE(" + TOURNAMENTS_NAME + "));";
    private static final String CREATE_STATS_TABLE = "CREATE TABLE " + STATS_TABLE + "(" + STATS_KEY + " TEXT, " +
            STATS_VALUES + " TEXT, " + STATS_TOURNAMENT_NAMES + " TEXT, " + "UNIQUE(" + STATS_KEY + "));";
    private static DatabaseSingleton instance;

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

    public static DatabaseSingleton getInstance() {
        return instance;
    }

    public static void createInstance(Context context) {
        if (instance == null)
            instance = new DatabaseSingleton(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TOURNAMENTS_TABLE);
        db.execSQL(CREATE_STATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TOURNAMENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_STATS_TABLE);
        onCreate(db);
    }
}
