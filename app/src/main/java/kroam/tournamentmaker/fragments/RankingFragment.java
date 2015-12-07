package kroam.tournamentmaker.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.adapters.RankingAdapter;
import kroam.tournamentmaker.database.TournamentDataSource;

public class RankingFragment extends ListFragment {

    private static final String ARG_PARAM1 = "param1";

    private static RankingFragment instance;
    private String tournamentName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RankingFragment() {
    }

    // TODO: Rename and change types of parameters
    public static RankingFragment newInstance(String tournamentName) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, tournamentName);
        fragment.setArguments(args);
        instance = fragment;
        return fragment;
    }

    public static RankingFragment getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tournamentName = getArguments().getString(ARG_PARAM1);

            Tournament tournament = TournamentDataSource.getInstance().getTournament(tournamentName);
            setListAdapter(new RankingAdapter(getContext(), R.layout.ranking_row, tournament.getTeams(), tournament));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ranking_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void updateRankings(String tournamentName) {
        Tournament tournament = TournamentDataSource.getInstance().getTournament(tournamentName);
        setListAdapter(new RankingAdapter(getContext(), R.layout.ranking_row, tournament.getTeams(), tournament));
    }
}
