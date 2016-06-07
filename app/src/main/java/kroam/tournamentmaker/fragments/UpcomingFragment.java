package kroam.tournamentmaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kroam.tournamentmaker.Match;
import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.adapters.EnterStatsAdapter;
import kroam.tournamentmaker.adapters.ScheduleAdapter;

public class UpcomingFragment extends ListFragment {

    private static final String ARG_PARAM1 = "tournament_name";

    private String tournamentName;
    private ArrayList<Match> upcomingMatches;
    private Tournament tournament;
    private FinishListener listener;
    private TextView round;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UpcomingFragment() {
    }

    public static UpcomingFragment newInstance(String tournamentName) {
        UpcomingFragment fragment = new UpcomingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, tournamentName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tournamentName = getArguments().getString(ARG_PARAM1);

//            tournament = TournamentsDataSource.getInstance().getTournament(tournamentName);
            upcomingMatches = new ArrayList<>();
//            upcomingMatches.addAll(tournament.getCurrentRoundOfActiveMatches());
            setListAdapter(new ScheduleAdapter(getContext(), R.layout.row_upcoming_match, upcomingMatches));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upcoming, container, false);
        round = (TextView) v.findViewById(R.id.round_name);
        round.setText(String.format("Round %d", tournament.getCurrentRound() + 1));
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        final EnterStatsAdapter[] adapter = new EnterStatsAdapter[1];
        /*AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Enter Stats")
                .setView(R.layout.game_stats)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Match match = upcomingMatches.get(position);
                        int[] scores = adapter[0].getScores();
                        match.setHomeScore(scores[0]);
                        match.setAwayScore(scores[1]);
                        match.setWinner();
                        MatchesDataSource.getInstance().endMatch(upcomingMatches.get(position));
                        StatsDataSource.getInstance().updateStats(adapter[0].getStats());
                        TournamentsDataSource.getInstance().getTournamentFromMatch(match.getId()).updateRankOf(match
                                .getWinner()); //updated Dec 6 by Ocean
                        refresh();
                        if (ResultFragment.getInstance() != null)
                            ResultFragment.getInstance().refresh();
                        if (RankingFragment.getInstance() != null)
                            RankingFragment.getInstance().updateRankings(tournamentName);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Dialog view = (Dialog) dialog;
                view.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams
                        .FLAG_ALT_FOCUSABLE_IM);
                view.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                TextView homeTeam = (TextView) view.findViewById(R.id.home_team);
                TextView awayTeam = (TextView) view.findViewById(R.id.away_team);
                Match currentMatch = upcomingMatches.get(position);
                homeTeam.setText(currentMatch.getHomeTeam().getName());
                awayTeam.setText(currentMatch.getAwayTeam().getName());
                RecyclerView statsList = (RecyclerView) view.findViewById(R.id.stats_list);
                statsList.setLayoutManager(new LinearLayoutManager(getContext()));
                statsList.setAdapter(adapter[0] = new EnterStatsAdapter(StatsDataSource.getInstance().getTournamentStats
                        (tournamentName), currentMatch, tournament));
            }
        });
        dialog.show();*/
    }

    public void refresh() {
        /*Tournament currentTournament = TournamentsDataSource.getInstance().getTournament(tournamentName);
        upcomingMatches = currentTournament.getCurrentRoundOfActiveMatches();
        if (upcomingMatches.size() == 0) {
            currentTournament.generateNextRoundOfMatches();
        } else {
            setListAdapter(new ScheduleAdapter(getContext(), R.layout.row_upcoming_match, upcomingMatches =
                    currentTournament.getCurrentRoundOfActiveMatches()));
            round.setText(String.format("Round %d", tournament.getCurrentRound() + 1));
        }
        if (currentTournament.isCompleted())
            listener.endTournament();*/

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FinishListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    public interface FinishListener {
        void endTournament();
    }
}
