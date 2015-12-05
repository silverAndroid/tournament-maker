package kroam.tournamentmaker.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import kroam.tournamentmaker.Match;
import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Stat;
import kroam.tournamentmaker.StatValue;

/**
 * Created by Rushil Perera on 12/4/2015.
 */
public class EnterStatsAdapter extends RecyclerView.Adapter<EnterStatsAdapter.StatsViewHolder> {

    private ArrayList<Stat> stats;
    private Match match;

    public EnterStatsAdapter(ArrayList<Stat> stats, Match match) {
        this.match = match;
        this.stats = new ArrayList<>();
        this.stats.addAll(stats);
    }

    @Override
    public StatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_stats_row, parent, false);
        return new StatsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatsViewHolder holder, int position) {
        holder.stat.setText(stats.get(position).getKey());
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public ArrayList<Stat> getStats() {
        return stats;
    }

    class StatsViewHolder extends RecyclerView.ViewHolder {

        TextView stat;
        EditText team1Stat;
        EditText team2Stat;

        public StatsViewHolder(View itemView) {
            super(itemView);
            stat = (TextView) itemView.findViewById(R.id.stat);
            team1Stat = (EditText) itemView.findViewById(R.id.team1_stat);
            team2Stat = (EditText) itemView.findViewById(R.id.team2_stat);

            team1Stat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    stats.get(getAdapterPosition()).addValue(new StatValue(match.getAssociatedTournament().getName(),
                            match.getHomeTeam().getName(), s.toString().equals("") ? -1 : Integer.parseInt(s.toString()
                    )));
                }
            });

            team2Stat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    stats.get(getAdapterPosition()).addValue(new StatValue(match.getAssociatedTournament().getName(),
                            match.getAwayTeam().getName(), s.toString().equals("") ? -1 : Integer.parseInt(s.toString
                            ())));
                }
            });
        }
    }
}
