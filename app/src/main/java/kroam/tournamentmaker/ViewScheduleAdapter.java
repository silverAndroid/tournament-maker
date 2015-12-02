package kroam.tournamentmaker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 12/1/2015.
 */
public class ViewScheduleAdapter extends RecyclerView.Adapter<ViewScheduleAdapter.MatchViewHolder> {

    private ArrayList<Match> matches;

    public ViewScheduleAdapter(ArrayList<Match> matches) {
        this.matches = new ArrayList<>();
        this.matches.addAll(matches);
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_match_row, parent, false);
        return new MatchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.team1.setText(match.getHomeTeam().getName());
        holder.team2.setText(match.getAwayTeam().getName());
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView team1;
        TextView team2;

        public MatchViewHolder(View itemView) {
            super(itemView);
            team1 = (TextView) itemView.findViewById(R.id.team1);
            team2 = (TextView) itemView.findViewById(R.id.team2);
        }
    }
}
