package kroam.tournamentmaker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.activities.TournamentCreateActivity;
import kroam.tournamentmaker.activities.UpcomingResultRankingTabActivity;
import kroam.tournamentmaker.database.TournamentsDataSource;

public class TournamentFragment extends ListFragment {

    private static TournamentFragment instance;
    private ArrayList<Tournament> tournaments;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TournamentFragment() {
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

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id
                .text1, tournaments = TournamentsDataSource.getInstance().getTournaments()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_listview, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent;
        Tournament tournament = tournaments.get(position);
        if (tournament.isCompleted()) {

        } else {
            if (tournament.isRegistrationClosed())
                intent = new Intent(getContext(), UpcomingResultRankingTabActivity.class);
            else
                intent = new Intent(getContext(), TournamentCreateActivity.class);
            intent.putExtra("name", tournament.getName());
            startActivity(intent);
        }
    }
}
