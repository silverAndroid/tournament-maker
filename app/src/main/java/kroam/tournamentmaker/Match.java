package kroam.tournamentmaker;

import android.database.Cursor;

import java.util.ArrayList;

import kroam.tournamentmaker.database.DBColumns;

/**
 * Created by Rushil Perera on 12/1/2015.
 */
public class Match {
    private String id;
    private ArrayList<Participant> participants;
    private ArrayList<Stat> stats;
    private String tournamentName;
    private int round;
    private boolean finished;

    public Match(String id, Participant participant1, Participant participant2, String tournamentName, int
            round) {
        this.id = id;
        this.round = round;
        participants = new ArrayList<>(2);
        participants.add(participant1);
        participants.add(participant2);
        stats = new ArrayList<>();
        this.tournamentName = tournamentName;
    }

    public Match(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(DBColumns.ID));
        finished = cursor.getInt(cursor.getColumnIndex(DBColumns.FINISHED)) == 1;
        participants = new ArrayList<>();
        stats = new ArrayList<>();
    }

    public String getID() {
        return id;
    }

    //TODO: Transition away from 2 participants to multiple participants in 1 match
    public Participant getParticipant1() {
        return participants.get(0);
    }

    public Participant getParticipant2() {
        return participants.get(1);
    }

    public Participant getParticipant(int index) {
        return participants.get(index);
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

    public String getTournamentName() {
        return tournamentName;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}

