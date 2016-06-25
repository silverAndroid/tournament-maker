package kroam.tournamentmaker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import kroam.tournamentmaker.Participant;
import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Team;

/**
 * Created by Rushil Perera on 11/29/2015.
 */
public class ViewTeamsAdapter extends RecyclerView.Adapter<ViewTeamsAdapter.ViewTeamHolder> {

    private ArrayList<Participant> teams;

    public ViewTeamsAdapter(ArrayList<? extends Participant> teams) {
        this.teams = new ArrayList<>();
        if (teams.size() > 0)
            this.teams.addAll(teams);
    }

    @Override
    public ViewTeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_team, parent, false);
        return new ViewTeamHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewTeamHolder holder, int position) {
        holder.teamName.setText(teams.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public ArrayList<Participant> getSelectedTeams() {
        return teams;
    }

    private void deleteItem(int position) {
        teams.remove(position);
        notifyItemRemoved(position);
    }

    class ViewTeamHolder extends RecyclerView.ViewHolder {

        TextView teamName;
        ImageButton delete;

        public ViewTeamHolder(View itemView) {
            super(itemView);
            teamName = (TextView) itemView.findViewById(R.id.txt_team_name);
            teamName.setSelected(true); //TODO: Check what this does
            delete = (ImageButton) itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(getAdapterPosition());
                }
            });
        }
    }
}
