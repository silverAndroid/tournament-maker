package kroam.tournamentmaker.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import kroam.tournamentmaker.Match;
import kroam.tournamentmaker.Participant;
import kroam.tournamentmaker.Stat;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.database.relationships.TournamentsMatchesRelation;

/**
 * Created by Rushil Perera on 12/1/2015.
 * <p/>
 * Singleton class for the Match Section of the Database
 */
public class MatchesDataSource {
    private static final String TAG = "MatchesData";
    private static MatchesDataSource ourInstance = new MatchesDataSource();
    private SQLiteDatabase database;
    private String[] columns = {DBColumns.ID, DBColumns.PARTICIPANT_ID, DBColumns.SCORE, DBColumns.FINISHED};

    private MatchesDataSource() {
    }

    public synchronized static MatchesDataSource getInstance() {
        return ourInstance;
    }

    public void createMatch(Match match) {
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", DBTables.MATCHES, columns[0],
                columns[1]);
        Log.i(TAG, "createMatch: " + query);

        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, match.getID());
        statement.bindLong(2, match.getParticipant1().getID());
        statement.executeInsert();
        statement.bindString(1, match.getID());
        statement.bindLong(2, match.getParticipant2().getID());
        statement.executeInsert();

        database.setTransactionSuccessful();
        database.endTransaction();
        addRelations(match);
    }

    private void addRelations(Match match) {
        TournamentsMatchesRelation.getInstance().addTournamentMatchRelation(match.getTournamentName(), match
                .getID(), match.getRound());
    }

    public ArrayList<Match> getMatchesForRound(String tournamentName, int round) {
        ArrayList<Match> matches = new ArrayList<>();
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("SELECT DISTINCT (m.%s), m.%s FROM %s m JOIN %s tm ON m.%s = tm" +
                        ".%s WHERE tm.%s=? AND tm.%s=?", DBColumns.ID, DBColumns.FINISHED, DBTables.MATCHES,
                DBTables.TOURNAMENTS_MATCHES, DBColumns.ID, DBColumns.MATCH_ID, DBColumns.TOURNAMENT_NAME,
                DBColumns.ROUND);
        Log.i(TAG, "getMatchesForRound: " + query);

        Cursor cursor = database.rawQuery(query, new String[]{tournamentName, Integer.toString(round)});
        if (cursor.moveToFirst()) {
            do {
                Match match = new Match(cursor);
                match.setRound(round);
                match = getRelations(match, tournamentName, round);
                matches.add(match);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return matches;
    }

    public ArrayList<Match> getMatchesForRound(String tournamentName, int round, boolean finished) {
        ArrayList<Match> matches = new ArrayList<>();
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("SELECT DISTINCT (m.%s), m.%s FROM %s m JOIN %s tm ON m.%s = tm" +
                ".%s WHERE tm.%s=? AND tm.%s=? AND m.%s=?", DBColumns.ID, DBColumns.FINISHED, DBTables
                .MATCHES, DBTables.TOURNAMENTS_MATCHES, DBColumns.ID, DBColumns.MATCH_ID, DBColumns
                .TOURNAMENT_NAME, DBColumns.ROUND, DBColumns.FINISHED);
        Log.i(TAG, "getMatchesForRound: " + query);

        Cursor cursor = database.rawQuery(query, new String[]{tournamentName, Integer.toString(round),
                Integer.toString(finished ? 1 : 0)});
        if (cursor.moveToFirst()) {
            do {
                Match match = new Match(cursor);
                match.setRound(round);
                match = getRelations(match, tournamentName, round, finished);
                matches.add(match);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return matches;
    }

    private Match getRelations(Match match, String tournamentName, int round) {
        ArrayList<Participant> participants = new ArrayList<>();
        HashMap<Integer, Participant> participantHashMap = new HashMap<>();
        participants.addAll(getTeamsForMatch(tournamentName, round));
        for (Participant participant : participants) {
            participantHashMap.put(participant.getID(), participant);
        }
        getStatsForMatch(tournamentName, round, participantHashMap);
        participants.clear();
        participants.addAll(participantHashMap.values());
        match.addParticipants(participants);
        return match;
    }

    private Match getRelations(Match match, String tournamentName, int round, boolean finished) {
        ArrayList<Participant> participants = new ArrayList<>();
        HashMap<Integer, Participant> participantHashMap = new HashMap<>();
        participants.addAll(getTeamsForMatch(tournamentName, round, finished));
        for (Participant participant : participants) {
            participantHashMap.put(participant.getID(), participant);
        }
        getStatsForMatch(tournamentName, round, participantHashMap, finished);
        participants.clear();
        participants.addAll(participantHashMap.values());
        match.addParticipants(participants);
        return match;
    }

    private ArrayList<Team> getTeamsForMatch(String tournamentName, int round) {
        ArrayList<Team> teams = new ArrayList<>();
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("SELECT p.*, pt.%s FROM %s p JOIN %s m ON p.%s = m.%s JOIN %s tm ON m" +
                        ".%s = tm.%s JOIN %s pt ON p.%s = pt.%s WHERE tm.%s=? AND tm.%s=?;", DBColumns
                        .LOGO_PATH, DBTables.PARTICIPANTS, DBTables.MATCHES, DBColumns.ID, DBColumns
                        .PARTICIPANT_ID, DBTables.TOURNAMENTS_MATCHES, DBColumns.ID, DBColumns.MATCH_ID,
                DBTables.PARTICIPANTS_TEAMS, DBColumns.ID, DBColumns.PARTICIPANT_ID, DBColumns.ROUND,
                DBColumns.TOURNAMENT_NAME);
        Log.i(TAG, "getTeamsForMatch: " + query);

        Cursor cursor = database.rawQuery(query, new String[]{tournamentName, Integer.toString(round)});
        if (cursor.moveToFirst()) {
            do {
                teams.add(new Team(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return teams;
    }

    private ArrayList<Team> getTeamsForMatch(String tournamentName, int round, boolean finished) {
        ArrayList<Team> teams = new ArrayList<>();
        database = DatabaseSingleton.getInstance().openDatabase();
        String query = String.format("SELECT p.*, pt.%s FROM %s p JOIN %s m ON p.%s = m.%s JOIN %s tm ON m" +
                        ".%s = tm.%s JOIN %s pt ON p.%s = pt.%s WHERE tm.%s=? AND tm.%s=? AND m.%s=?;",
                DBColumns.LOGO_PATH, DBTables.PARTICIPANTS, DBTables.MATCHES, DBColumns.ID, DBColumns
                        .PARTICIPANT_ID, DBTables.TOURNAMENTS_MATCHES, DBColumns.ID, DBColumns.MATCH_ID,
                DBTables.PARTICIPANTS_TEAMS, DBColumns.ID, DBColumns.PARTICIPANT_ID, DBColumns.ROUND,
                DBColumns.TOURNAMENT_NAME, DBColumns.FINISHED);
        Log.i(TAG, "getTeamsForMatch: " + query);

        Cursor cursor = database.rawQuery(query, new String[]{tournamentName, Integer.toString(round),
                Integer.toString(finished ? 1 : 0)});
        if (cursor.moveToFirst()) {
            do {
                teams.add(new Team(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return teams;
    }

    private void getStatsForMatch(String tournamentName, int round, HashMap<Integer, Participant>
            participants) {
        database = DatabaseSingleton.getInstance().openDatabase();
        String participantIDColumn = "participant_id";
        String query = String.format("SELECT s.*, sm.%s, p.%s AS %s FROM %s s JOIN %s sm ON s" +
                        ".%s = sm.%s JOIN %s m ON sm.%s = m.%s JOIN %s tm ON m.%s = tm.%s JOIN %s p ON m.%s" +
                        " = p.%s WHERE tm.%s=? AND tm.%s=?;", DBColumns.STAT_VALUE, DBColumns.ID,
                participantIDColumn, DBTables.STATS, DBTables.STATS_MATCHES, DBColumns.ID, DBColumns
                        .STAT_ID, DBTables.MATCHES, DBColumns.MATCH_ID, DBColumns.ID, DBTables
                        .TOURNAMENTS_MATCHES, DBColumns.ID, DBColumns.MATCH_ID, DBTables.PARTICIPANTS,
                DBColumns.PARTICIPANT_ID, DBColumns.ID, DBColumns.ROUND, DBColumns.TOURNAMENT_NAME);
        Cursor cursor = database.rawQuery(query, new String[]{tournamentName, Integer.toString(round)});
        if (cursor.moveToFirst()) {
            do {
                Stat stat = new Stat(cursor);
                int participantID = cursor.getInt(cursor.getColumnIndex(participantIDColumn));
                participants.get(participantID).addStat(stat);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void getStatsForMatch(String tournamentName, int round, HashMap<Integer, Participant>
            participants, boolean finished) {
        database = DatabaseSingleton.getInstance().openDatabase();
        String participantIDColumn = "participant_id";
        String query = String.format("SELECT s.*, sm.%s, p.%s AS %s FROM %s s JOIN %s sm ON s" +
                        ".%s = sm.%s JOIN %s m ON sm.%s = m.%s JOIN %s tm ON m.%s = tm.%s JOIN %s p ON m.%s" +
                        " = p.%s WHERE tm.%s=? AND tm.%s=? AND m.%s=?;", DBColumns.STAT_VALUE, DBColumns.ID,
                participantIDColumn, DBTables.STATS, DBTables.STATS_MATCHES, DBColumns.ID, DBColumns
                        .STAT_ID, DBTables.MATCHES, DBColumns.MATCH_ID, DBColumns.ID, DBTables
                        .TOURNAMENTS_MATCHES, DBColumns.ID, DBColumns.MATCH_ID, DBTables.PARTICIPANTS,
                DBColumns.PARTICIPANT_ID, DBColumns.ID, DBColumns.ROUND, DBColumns.TOURNAMENT_NAME,
                DBColumns.FINISHED);
        Cursor cursor = database.rawQuery(query, new String[]{tournamentName, Integer.toString(round),
                Integer.toString(finished ? 1 : 0)});
        if (cursor.moveToFirst()) {
            do {
                Stat stat = new Stat(cursor);
                int participantID = cursor.getInt(cursor.getColumnIndex(participantIDColumn));
                participants.get(participantID).addStat(stat);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
