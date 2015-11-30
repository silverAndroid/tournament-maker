package kroam.tournamentmaker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 11/29/2015.
 */
public class ViewTeamsAdapter extends RecyclerView.Adapter<ViewTeamsAdapter.ViewTeamHolder> {

    private ArrayList<String> teamNames;

    public ViewTeamsAdapter(ArrayList<String> teamNames) {
        this.teamNames = new ArrayList<>();
        this.teamNames.addAll(teamNames);
    }

    @Override
    public ViewTeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_view_row, parent,
                false);
        return new ViewTeamHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewTeamHolder holder, int position) {
        holder.teamName.setText(teamNames.get(position));
    }

    @Override
    public int getItemCount() {
        return teamNames.size();
    }

    class ViewTeamHolder extends RecyclerView.ViewHolder {

        TextView teamName;

        public ViewTeamHolder(View itemView) {
            super(itemView);
            teamName = (TextView) itemView.findViewById(R.id.txt_team_name);
        }
    }
}