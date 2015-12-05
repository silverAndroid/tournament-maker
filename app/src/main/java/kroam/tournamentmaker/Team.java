package kroam.tournamentmaker;

import java.io.Serializable;

/**
 * Created by Kyle on 2015-11-28.
 */
public class Team implements Serializable {
    private String name;
    private String captainName;
    private String captainEmail;
    private String phoneNumber;
    private int selected;

    public Team(String name, String captainName, String captainEmail, String phoneNumber) {
        this.name = name;
        this.captainName = captainName;
        this.captainEmail = captainEmail;
        this.phoneNumber = phoneNumber;
        selected = -1;
    }

    public String getName() {
        return name;
    }

    public String getCaptainName() {
        return captainName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCaptainEmail() {
        return captainEmail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public void setCaptainEmail(String captainEmail) {
        this.captainEmail = captainEmail;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
