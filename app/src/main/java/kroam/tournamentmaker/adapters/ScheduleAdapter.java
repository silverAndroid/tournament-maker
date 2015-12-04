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

/**
 * Created by Rushil Perera on 12/4/2015.
 */
public class ScheduleAdapter extends ArrayAdapter<Match> {

    private ArrayList<Match> matches;

    public ScheduleAdapter(Context context, int resource, List<Match> matchList) {
        super(context, resource, matchList);
        matches = new ArrayList<>();
        matches.addAll(matchList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_match_row, parent, false);
        }

        MatchViewHolder holder = new MatchViewHolder(convertView);
        Match match = matches.get(position);
        holder.team1.setText(match.getHomeTeam().getName());
        holder.team2.setText(match.getAwayTeam().getName());
        return convertView;
    }

    class MatchViewHolder {

        TextView team1;
        TextView team2;

        public MatchViewHolder(View itemView) {
            team1 = (TextView) itemView.findViewById(R.id.team1);
            team2 = (TextView) itemView.findViewById(R.id.team2);
        }
    }
}
