package kroam.tournamentmaker.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import kroam.tournamentmaker.R;

public class ResultFragment extends ListFragment {

    private static final String ARG_PARAM1 = "param1";
    private static ResultFragment instance;

    private String tournamentName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResultFragment() {
    }

    public static ResultFragment newInstance(String tournamentName) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, tournamentName);
        fragment.setArguments(args);
        instance = fragment;
        return fragment;
    }

    public static ResultFragment getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tournamentName = getArguments().getString(ARG_PARAM1);

            /*Tournament tournament = TournamentsDataSource.getInstance().getTournament(tournamentName);
            setListAdapter(new ResultsAdapter(getContext(), R.layout.row_result, tournament.getInactiveMatches()));*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void refresh() {
        /*Tournament tournament = TournamentsDataSource.getInstance().getTournament(tournamentName);
        setListAdapter(new ResultsAdapter(getContext(), R.layout.row_result, tournament.getInactiveMatches()));*/
    }
}
