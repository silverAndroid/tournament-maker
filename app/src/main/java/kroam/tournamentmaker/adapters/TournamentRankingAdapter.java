package kroam.tournamentmaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.Tournament;

/**
 * Created by Kyle on 2015-12-05.
 */
public class TournamentRankingAdapter extends ArrayAdapter<Tournament> {
    private ArrayList<Tournament> tournaments;
    private Team team;

    public TournamentRankingAdapter(Context context, int resource, List<Tournament> matchList, Team team) {
        super(context, resource, matchList);
        this.team = team;
        tournaments = new ArrayList<>();
        tournaments.addAll(matchList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team_ranking, parent, false);
        }

        TournamentViewHolder holder = new TournamentViewHolder(convertView);
        Tournament tournament = tournaments.get(position);
        holder.tournament.setText(tournament.getName());
//        holder.ranking.setText(tournament.getRankingOf(team));//waiting for ocean's getranking
        return convertView;
    }

    class TournamentViewHolder {

        TextView tournament;
        TextView ranking;

        public TournamentViewHolder(View itemView) {
            tournament = (TextView) itemView.findViewById(R.id.tournament);
            ranking = (TextView) itemView.findViewById(R.id.ranking);
        }
    }
}
