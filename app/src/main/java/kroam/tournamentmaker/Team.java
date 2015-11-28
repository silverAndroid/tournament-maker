package kroam.tournamentmaker;

/**
 * Created by Kyle on 2015-11-28.
 */
public class Team {
    private final String name;
    private final String captainName;
    private final String captainEmail;
    private final int phoneNumber;

    public Team(String name, String captainName, String captainEmail, int phoneNumber) {
        this.name = name;
        this.captainName = captainName;
        this.captainEmail = captainEmail;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getCaptainName() {return captainName;}

    public int getPhoneNumber() {return phoneNumber;}

    public String getCaptainEmail() {return captainEmail;}


    @Override
    public String toString() {
        return name;
    }
}
