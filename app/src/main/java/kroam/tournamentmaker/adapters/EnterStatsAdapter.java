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
import kroam.tournamentmaker.Tournament;

/**
 * Created by Rushil Perera on 12/4/2015.
 */
public class EnterStatsAdapter extends RecyclerView.Adapter<EnterStatsAdapter.StatsViewHolder> {

    private ArrayList<Stat> stats;
    private Match match;
    private Tournament tournament;
    private int winningStatPosition;

    public EnterStatsAdapter(ArrayList<Stat> stats, Match match, Tournament tournament) {
        this.match = match;
        this.tournament = tournament;
        this.stats = new ArrayList<>();
        this.stats.addAll(stats);
        /*Stat winningStat = tournament.getWinningStat();
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i).getKey().equals(winningStat.getKey())) {
                winningStatPosition = i;
                break;
            }
        }*/
    }

    @Override
    public StatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_game_stats, parent, false);
        return new StatsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatsViewHolder holder, int position) {
//        holder.stat.setText(stats.get(position).getKey());
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public ArrayList<Stat> getStats() {
        return stats;
    }

    /*public int[] getScores() {
        return new int[]{match.getHomeScore(), match.getAwayScore()};
    }*/

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
                    int score;
                    /*stats.get(getAdapterPosition()).addValue(new StatValue(tournament.getName(), match.getHomeTeam()
                            .getName(), score = s.toString().equals("") ? -1 : Integer.parseInt(s.toString())));
                    if (getAdapterPosition() == winningStatPosition)
                        match.setHomeScore(score);*/
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
                    int score;
                    /*stats.get(getAdapterPosition()).addValue(new StatValue(tournament.getName(), match.getAwayTeam()
                            .getName(), score = s.toString().equals("") ? -1 : Integer.parseInt(s.toString())));
                    if (getAdapterPosition() == winningStatPosition)
                        match.setAwayScore(score);*/
                }
            });
        }
    }
}
