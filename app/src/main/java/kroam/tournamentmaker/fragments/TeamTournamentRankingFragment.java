package kroam.tournamentmaker.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.database.TournamentDataSource;

/**
 * Created by Kyle on 2015-12-05.
 */
public class TeamTournamentRankingFragment extends ListFragment {

    private static TournamentFragment instance;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamTournamentRankingFragment() {
    }

    public static TournamentFragment newInstance() {
        TournamentFragment fragment = new TournamentFragment();
        instance = fragment;
        return fragment;
    }

    public static TournamentFragment getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,
                TournamentDataSource.getInstance().getTournaments()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tournament_ranking_fragment, container, false);
    }
}
