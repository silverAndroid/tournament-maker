package kroam.tournamentmaker;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Ocean on 5/18/2016.
 */
public class Player extends Participant{
    /*
   inherited attributes:
       private String name
       private ArrayList<Tournament> associatedTournaments
    */
    private static final int EMAIL = 1;
    private static final int PHONE = 2;
    private String email;
    private String phoneNumber;
    private ArrayList<Team> associatedTeams;

    public Player(String name, String email, String phoneNumber){
        super.setName(name);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Player(String name){
        this(name, "N/A", "N/A");
    }

    public Player(String name, String emailOrPhone, int mode){
        super.setName(name);
        switch(mode){
            case EMAIL:
                setEmail(emailOrPhone);
                break;
            case PHONE:
                setPhoneNumber(emailOrPhone);
                break;
            default:
                //Warning: "emailOrPhone" was not saved; specified mode is unknown;
        }
    }

    public String getEmail(){return email;}

    public void setEmail(String email){this.email = email;}

    public String getPhoneNumber(){return phoneNumber;}

    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}

    public void addToTeam(Team t){
        if(associatedTeams == null) {
            associatedTeams = new ArrayList<>();
        }
        //Create util method to check that two teams are not in the same tournament
        {associatedTeams.add(t);}
        //Add warning: "Player cannot join t. Player is already in the tournament where t is participating"
    }

    @Override
    public void removeFromTournament(Tournament t) {
        if(associatedTournaments == null){
            //Add Warning: "Player is not participating individually in any tournaments!"
            return;
        }
        else if(!associatedTournaments.contains(t)){
            //Add Warning: "Player is not in tournament 't'"
            return;
        }
        associatedTournaments.remove(t);
    }

    @Override
    public void addTournament(Tournament t) {
        if(associatedTournaments == null){ createTournamentList(); }
        if(associatedTournaments.contains(t)){
            //Add Warning: "Player is already in tournament t"

            return;
        }
        associatedTournaments.add(t);
    }

    //Still needs to be changed
    public void addTournaments(ArrayList<Tournament> ts) {
        for(Tournament t : ts){
            addTournament(t);
        }
    }
}
