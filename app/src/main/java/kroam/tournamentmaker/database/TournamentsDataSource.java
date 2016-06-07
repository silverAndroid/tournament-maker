package kroam.tournamentmaker.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import kroam.tournamentmaker.Participant;
import kroam.tournamentmaker.Stat;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.Util;
import kroam.tournamentmaker.database.relationships.TournamentsParticipantsRelation;
import kroam.tournamentmaker.database.relationships.TournamentsStatsRelation;

/**
 * Created by Rushil Perera on 11/22/2015.
 */
public class TournamentsDataSource {

    private static final String TAG = "TournamentData";
    private static TournamentsDataSource instance = new TournamentsDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.NAME, DBColumns.TYPE, DBColumns.MAX_SIZE, DBColumns.FINISHED,
            DBColumns.REGISTRATION_CLOSED, DBColumns.CURRENT_ROUND};

    private TournamentsDataSource() {
    }

    public synchronized static TournamentsDataSource getInstance() {
        return instance;
    }

    public void createTournament(Tournament tournament) {
        if (!Util.validateColumn(tournament.getName())) {
            throw new MissingColumnException(columns[0]);
        }

        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", DBTables
                .TOURNAMENTS_TABLE, columns[0], columns[1], columns[2]);
        database.beginTransaction();
        Log.i(TAG, "createTournament: " + query);

        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, tournament.getName());
        statement.bindString(2, tournament.getType());
        statement.bindLong(3, tournament.getMaxSize());
        statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
        ArrayList<Stat> stats = tournament.getStats();
        ArrayList<Participant> participants = tournament.getTeams();

        stats = StatsDataSource.getInstance().addStats(stats);
        for (Stat stat : stats) {
            TournamentsStatsRelation.getInstance().createTournamentStatRelation(tournament.getName(), stat
                    .getID(), tournament.getWinningStatID() == stat.getID());
        }
        for (Participant participant : participants) {
            TournamentsParticipantsRelation.getInstance().createTournamentParticipantRelation(tournament
                    .getName(), participant.getID());
        }
    }

    public ArrayList<Tournament> getTournaments() {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        database = DatabaseSingleton.getInstance().getReadableDatabase();
        Cursor cursor = database.query(DBTables.TOURNAMENTS_TABLE, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Tournament tournament = new Tournament(cursor);
                tournament = getRelations(tournament);
                tournaments.add(tournament);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tournaments;
    }

    private Tournament getRelations(Tournament tournament) {
        String name = tournament.getName();
        StatsIndex statsIndex = getStatsFromTournament(name);
        tournament.addStats(statsIndex.stats, statsIndex.winningStatIndex);
        tournament.addTeams(getTeamsFromTournament(name, tournament.getMaxSize()));
        return tournament;
    }

    private ArrayList<Participant> getTeamsFromTournament(String tournamentName, int maxSize) {
        //TODO: if maxSize is 1, do query for players instead of teams
        ArrayList<Participant> participants = new ArrayList<>();
        String query = String.format("SELECT p.*, pt.%s FROM %s p JOIN %s pt ON p.%s = pt.%s JOIN %s tp ON " +
                "p.%s = tp.%s WHERE %s=?;", DBColumns.LOGO_PATH, DBTables.PARTICIPANTS_TABLE, DBTables
                .PARTICIPANTS_TEAMS_TABLE, DBColumns.ID, DBColumns.PARTICIPANT_ID, DBTables
                .TOURNAMENTS_PARTICIPANTS_TABLE, DBColumns.ID, DBColumns.PARTICIPANT_ID, DBColumns
                .TOURNAMENT_NAME);
        Cursor cursor = database.rawQuery(query, new String[]{tournamentName});
        if (cursor.moveToFirst()) {
            do {
                participants.add(new Team(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return participants;
    }

    private StatsIndex getStatsFromTournament(String tournamentName) {
        StatsIndex statsIndex;
        ArrayList<Stat> stats = new ArrayList<>();
        int winningStatIndex = 0;
        String query = String.format("SELECT s.*, ts.%s FROM %s s JOIN %s ts ON s.%s = ts.%s WHERE %s=?;",
                DBColumns.WINNING_STAT, DBTables.STATS_TABLE, DBTables.TOURNAMENTS_STATS_TABLE, DBColumns
                        .ID, DBColumns.STAT_ID, DBColumns.TOURNAMENT_NAME);
        Cursor cursor = database.rawQuery(query, new String[]{tournamentName});
        for (int i = 0; cursor.moveToNext(); i++) {
            Stat stat = new Stat(cursor);
            if (cursor.getInt(cursor.getColumnIndex(DBColumns.WINNING_STAT)) == 1)
                winningStatIndex = i;
            stats.add(stat);
        }
        cursor.close();
        statsIndex = new StatsIndex(stats, winningStatIndex);
        return statsIndex;
    }

    public Tournament getTournament(String tournamentName) {
        Cursor cursor = database.query(DBTables.TOURNAMENTS_TABLE, columns, columns[0] + "=?",
                new String[]{tournamentName}, null, null, null);
        Tournament tournament = null;
        if (cursor.moveToFirst()) {
            tournament = new Tournament(cursor);
            tournament = getRelations(tournament);
        }
        cursor.close();
        return tournament;
    }

    class StatsIndex {
        ArrayList<Stat> stats;
        int winningStatIndex;

        StatsIndex(ArrayList<Stat> stats, int winningStatIndex) {
            this.stats = new ArrayList<>();
            this.stats.addAll(stats);
            this.winningStatIndex = winningStatIndex;
        }
    }
}
