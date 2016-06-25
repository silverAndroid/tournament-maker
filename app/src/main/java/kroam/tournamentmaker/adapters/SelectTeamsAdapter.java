package kroam.tournamentmaker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashSet;

import kroam.tournamentmaker.Participant;
import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Team;

/**
 * Created by Rushil Perera on 11/28/2015.
 */
public class SelectTeamsAdapter extends RecyclerView.Adapter<SelectTeamsAdapter.TeamViewHolder> {

    private ArrayList<Team> teams;
    private HashSet<Long> selectedTeamIDs;

    public SelectTeamsAdapter(ArrayList<Team> teams, ArrayList<Participant> participants) {
        this.teams = new ArrayList<>(teams.size());
        selectedTeamIDs = new HashSet<>();
        for (Participant participant: participants) {
            selectedTeamIDs.add(participant.getID());
        }
        this.teams.addAll(teams);
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_team, parent, false);
        return new TeamViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        holder.setTeam(teams.get(position));
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public ArrayList<Team> getSelectedTeams() {
        ArrayList<Team> selectedTeams = new ArrayList<>();
        for (Team team : teams) {
            if (selectedTeamIDs.contains(team.getID())) {
                selectedTeams.add(team);
            }
        }
        return selectedTeams;
    }

    public void addTeam(Team team) {
        teams.add(team);
        notifyItemInserted(teams.size());
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {

        CheckBox chkTeam;
        private Team team;

        public TeamViewHolder(View itemView) {
            super(itemView);
            chkTeam = (CheckBox) itemView.findViewById(R.id.chk_team_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chkTeam.setChecked(!chkTeam.isChecked());
                }
            });
            chkTeam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectedTeamIDs.add(team.getID());
                }
            });
        }

        public void setTeam(Team team) {
            this.team = team;
            chkTeam.setText(team.getName());
            chkTeam.setChecked(selectedTeamIDs.contains(team.getID()));
        }
    }
}
