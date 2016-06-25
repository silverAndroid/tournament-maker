package kroam.tournamentmaker;

import android.database.Cursor;

import java.util.ArrayList;

import kroam.tournamentmaker.database.DBColumns;

/**
 * Created by Rushil Perera on 11/21/2015.
 */
public class Tournament {

    private static final String TAG = "Tournament";
    private String name;
    private String type;
    private int maxSize;
    private boolean finished;
    private boolean registrationClosed;
    private int currentRound;
    private ArrayList<Participant> participants;
    private ArrayList<Stat> stats;
    private ArrayList<Match> matches;
    private long winningStatID;

    public Tournament(String name, String type, int maxSize) {
        this.name = name;
        this.type = type;
        this.maxSize = maxSize;
        participants = new ArrayList<>(maxSize);
        stats = new ArrayList<>();
        matches = new ArrayList<>();
        currentRound = 1;
    }

    public Tournament(Cursor cursor) {
        name = cursor.getString(cursor.getColumnIndex(DBColumns.NAME));
        type = cursor.getString(cursor.getColumnIndex(DBColumns.TYPE));
        maxSize = cursor.getInt(cursor.getColumnIndex(DBColumns.MAX_SIZE));
        participants = new ArrayList<>(maxSize);
        stats = new ArrayList<>();
        matches = new ArrayList<>();
        currentRound = cursor.getInt(cursor.getColumnIndex(DBColumns.CURRENT_ROUND));
        finished = cursor.getInt(cursor.getColumnIndex(DBColumns.FINISHED)) == 1;
        registrationClosed = cursor.getInt(cursor.getColumnIndex(DBColumns.REGISTRATION_CLOSED)) == 1;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public void addParticipants(ArrayList<Participant> participants) {
        this.participants.addAll(participants);
    }

    public ArrayList<Stat> getStats() {
        return stats;
    }

    public void addStat(Stat stat, boolean winningStat) {
        stats.add(stat);
        if (winningStat) {
            winningStatID = stat.getID();
        }
    }

    public void addStats(ArrayList<Stat> stats, int winningStatIndex) {
        this.stats.addAll(stats);
        if (winningStatIndex != -1) {
            winningStatID = stats.get(winningStatIndex).getID();
        }
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public void addMatches(ArrayList<Match> matches) {
        this.matches.addAll(matches);
    }

    public long getWinningStatID() {
        return winningStatID;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void nextRound() {
        currentRound++;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Tournament) {
            Tournament tournament = (Tournament) o;
            return tournament.name.equals(name);
        }
        return false;
    }

    public void closeRegistration() {
        registrationClosed = true;
    }

    public void closeRegistration(boolean registrationClosed) {
        this.registrationClosed = registrationClosed;
    }

    public boolean isCompleted() {
        return finished;
    }

    public boolean isRegistrationClosed() {
        return registrationClosed;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
