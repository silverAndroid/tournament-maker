package kroam.tournamentmaker.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;

import kroam.tournamentmaker.Match;
import kroam.tournamentmaker.R;
import kroam.tournamentmaker.adapters.EnterStatsAdapter;
import kroam.tournamentmaker.adapters.ScheduleAdapter;
import kroam.tournamentmaker.database.MatchDataSource;
import kroam.tournamentmaker.database.StatsDataSource;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ScheduleFragment extends ListFragment {

    private static final String ARG_PARAM1 = "tournament_name";

    private String tournamentName;
    private ArrayList<Match> matches;

    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance(String tournamentName) {
        ScheduleFragment fragment = new ScheduleFragment();
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

            setListAdapter(new ScheduleAdapter(getContext(), R.layout.schedule_match_row, matches = MatchDataSource
                    .getInstance().getMatches()));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        final EnterStatsAdapter[] adapter = new EnterStatsAdapter[1];
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Enter Stats")
                .setView(R.layout.game_stats)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StatsDataSource.getInstance().updateStats(adapter[0].getStats());
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
                TextView homeTeam = (TextView) view.findViewById(R.id.home_team);
                TextView awayTeam = (TextView) view.findViewById(R.id.away_team);
                Match currentMatch = matches.get(position);
                homeTeam.setText(currentMatch.getHomeTeam().getName());
                awayTeam.setText(currentMatch.getAwayTeam().getName());
                RecyclerView statsList = (RecyclerView) view.findViewById(R.id.stats_list);
                statsList.setLayoutManager(new LinearLayoutManager(getContext()));
                statsList.setAdapter(adapter[0] = new EnterStatsAdapter(StatsDataSource.getInstance().getTournamentStats
                        (tournamentName), matches.get(position)));
            }
        });
        dialog.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
