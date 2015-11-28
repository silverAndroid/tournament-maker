package kroam.tournamentmaker;

/**
 * Created by Kyle on 2015-11-28.
 */
public class Team {
    private String name;
    private String captainName;
    private String captainEmail;
    private int phoneNumber;

    public Team(String name, String captainName, String captainEmail, int phoneNumber) {
        this.name = name;
        this.captainName = captainName;
        this.captainEmail = captainEmail;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {return name;}

    public String getCaptainName() {return captainName;}

    public int getPhoneNumber() {return phoneNumber;}

    public String getCaptainEmail() {return captainEmail;}

    public void setName(String name){this.name = name;}

    public void setCaptainName(String captainName) {this.captainName = captainName;}

    public void setCaptainEmail(String captainEmail) {this.captainEmail = captainEmail;}

    public void setPhoneNumber(int phoneNumber) {this.phoneNumber = phoneNumber;}

    @Override
    public String toString() {
        return name;
    }
}
