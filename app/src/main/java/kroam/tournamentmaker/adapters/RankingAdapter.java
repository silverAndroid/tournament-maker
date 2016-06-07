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
 * Created by Rushil Perera on 12/7/2015.
 */
public class RankingAdapter extends ArrayAdapter<Team> {

    private final Tournament tournament;
    private ArrayList<Team> teams;

    public RankingAdapter(Context context, int resource, List<Team> objects, final Tournament tournament) {
        super(context, resource, objects);
        this.tournament = tournament;
        teams = new ArrayList<>();
        /*Collections.sort(objects, new Comparator<Team>() {
            @Override
            public int compare(Team lhs, Team rhs) {
                return tournament.getRankingOf(lhs) - tournament.getRankingOf(rhs);
            }
        });*/
        teams.addAll(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ranking, parent, false);
        }

        RankingViewHolder holder = new RankingViewHolder(convertView);
        Team team = teams.get(position);
//        holder.rank.setText(String.format("%d.", tournament.getRankingOf(team)));
        holder.teamName.setText(team.getName());
        return convertView;
    }

    class RankingViewHolder {
        TextView rank;
        TextView teamName;

        RankingViewHolder(View view) {
            rank = (TextView) view.findViewById(R.id.rank);
            teamName = (TextView) view.findViewById(R.id.team_name);
        }
    }
}
