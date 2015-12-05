package kroam.tournamentmaker;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kyle on 2015-11-28.
 */
public class Team implements Serializable {
    private String name;
    private String captainName;
    private String captainEmail;
    private String phoneNumber;
    private int selected;
    private ArrayList<Tournament> associatedTournaments;

    public Team(String name, String captainName, String captainEmail, String phoneNumber) {
        this.name = name;
        this.captainName = captainName;
        this.captainEmail = captainEmail;
        this.phoneNumber = phoneNumber;
        selected = -1;
        associatedTournaments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCaptainEmail() {
        return captainEmail;
    }

    public void setCaptainEmail(String captainEmail) {
        this.captainEmail = captainEmail;
    }

    @Override
    public String toString() {
        return name;
    }

    public int isSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void addTournament(Tournament tournament) {
        associatedTournaments.add(tournament);
    }

    public ArrayList<Tournament> getAssociatedTournaments() {
        return associatedTournaments;
    }

    public void addTournaments(ArrayList<Tournament> tournaments) {
        associatedTournaments.addAll(tournaments);
    }
}
