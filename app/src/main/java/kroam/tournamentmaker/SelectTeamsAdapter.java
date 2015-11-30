package kroam.tournamentmaker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 11/28/2015.
 */
public class SelectTeamsAdapter extends RecyclerView.Adapter<SelectTeamsAdapter.TeamViewHolder> {

    private ArrayList<Team> teams;

    public SelectTeamsAdapter(ArrayList<Team> teams) {
        this.teams = new ArrayList<>(teams.size());
        this.teams.addAll(teams);
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_team_row, parent, false);
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

    public ArrayList<String> getTeams() {
        ArrayList<String> selectedTeams = new ArrayList<>();
        for (Team team : teams) {
            if (team.isSelected() == 1) {
                selectedTeams.add(team.getName());
            }
        }
        return selectedTeams;
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
                    team.setSelected(chkTeam.isChecked() ? 1 : 0);
                }
            });
        }

        public void setTeam(Team team) {
            this.team = team;
            chkTeam.setText(team.getName());
        }
    }
}
