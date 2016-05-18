package kroam.tournamentmaker;

import java.util.ArrayList;

/**
 * Created by Kyle on 2015-11-28.
 *
 * Changes:
 *  > by Ocean, 2016-05-18:   -Class extends Participant abstract class
 *                  -Removed methods that are now implemented in Participant class
 *                  -Added teamMember attribute: contains Players belonging to this Team
 *                  -Changes to the captain getters
 */
public class Team extends Participant {
    /*
    inherited attributes:
        private String name
        private ArrayList<Tournament> associatedTournaments
     */
    private Player captain;
    private ArrayList<Player> teamMembers;
    private int selected;   //Ocean: What does this do???
    private String logoPath;

    public Team(String name, String captainName, String captainEmail, String phoneNumber) {
        super.setName(name);
        super.createTournamentList();
        captain = new Player(captainName,captainEmail,phoneNumber);
        teamMembers = new ArrayList<>();
        teamMembers.add(captain);
        selected = -1;
    }

    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getCaptainName() {
        return captain.getName();
    }

    public void setCaptainName(String captainName) {
        captain.setName(captainName);
    }

    public String getPhoneNumber() {
        return captain.getPhoneNumber();
    }

    public void setPhoneNumber(String phoneNumber) {
        captain.setPhoneNumber(phoneNumber);
    }

    public String getCaptainEmail() {
        return captain.getEmail();
    }

    public void setCaptainEmail(String captainEmail) {
        captain.setEmail(captainEmail);
    }

    public int isSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public void addPlayer(Player p){
        if(teamMembers.contains(p)){
            //Add notification: "Player p is already in the team"
            return;
        }
        teamMembers.add(p);
    }

    public void kickOutPlayer(Player p){
        if(!teamMembers.contains(p)){
            //Add notification: "Player p is not in this team"
            return;
        }
        teamMembers.remove(p);
    }

    public int getNumMembers(){
        return teamMembers.size();
    }

    public void removeFromTournament(Tournament t) {
        if(!associatedTournaments.contains(t)){
            //Add Warning: "Team is not in Tournament t"
            return;
        }
        associatedTournaments.remove(t);
    }

    public void addTournament(Tournament t) {
        if(associatedTournaments.contains(t)){
            //Add Warning: "Team is already registered in Tournamen t"
            return;
        }
        associatedTournaments.add(t);
    }

    //Still needs to be changed.
    public void addTournaments(ArrayList<Tournament> ts) {
        for(Tournament t: ts){
            addTournament(t);
        }
    }
}
