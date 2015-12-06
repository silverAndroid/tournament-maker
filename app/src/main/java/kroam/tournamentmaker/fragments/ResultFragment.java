package kroam.tournamentmaker.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.adapters.ResultsAdapter;
import kroam.tournamentmaker.database.MatchDataSource;

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

            setListAdapter(new ResultsAdapter(getContext(), R.layout.result_row, MatchDataSource.getInstance()
                    .getFinishedMatches(tournamentName)));
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void refresh() {
        setListAdapter(new ResultsAdapter(getContext(), R.layout.result_row, MatchDataSource.getInstance()
                .getFinishedMatches(tournamentName)));
    }
}
