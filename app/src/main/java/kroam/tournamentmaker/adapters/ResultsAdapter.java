package kroam.tournamentmaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kroam.tournamentmaker.Match;
import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.Tournament;

/**
 * Created by Rushil Perera on 12/5/2015.
 */
public class ResultsAdapter extends ArrayAdapter<Match> {

    private ArrayList<Match> matches;

    public ResultsAdapter(Context context, int resource, List<Match> matchList) {
        super(context, resource, matchList);
        matches = new ArrayList<>();
        matches.addAll(matchList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_row, parent, false);
        }

        ResultViewHolder holder = new ResultViewHolder(convertView);
        Match match = matches.get(position);
        holder.homeTeam.setText(match.getHomeTeam().getName());
        holder.awayTeam.setText(match.getAwayTeam().getName());
        Tournament tournament = match.getAssociatedTournament();
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();
        holder.homeTeamWinStat.setText(String.format("%d", tournament.getWinningStat().getValue(tournament.getName()
                + ": " + homeTeam.getName()).getValue()));
        holder.awayTeamWinStat.setText(String.format("%d", tournament.getWinningStat().getValue(tournament.getName()
                + ": " + awayTeam.getName()).getValue()));

        return convertView;
    }

    class ResultViewHolder {
        TextView homeTeam;
        TextView homeTeamWinStat;
        TextView awayTeam;
        TextView awayTeamWinStat;

        public ResultViewHolder(View view) {
            homeTeam = (TextView) view.findViewById(R.id.home_team);
            awayTeam = (TextView) view.findViewById(R.id.away_team);
            homeTeamWinStat = (TextView) view.findViewById(R.id.home_team_win_stat);
            awayTeamWinStat = (TextView) view.findViewById(R.id.away_team_win_stat);
        }
    }
}
