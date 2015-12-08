package kroam.tournamentmaker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.activities.TournamentCreateActivity;
import kroam.tournamentmaker.activities.Upcoming_Result_Ranking_TabActivity;
import kroam.tournamentmaker.database.TournamentDataSource;

public class TournamentFragment extends ListFragment {

    private static TournamentFragment instance;

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

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,
                TournamentDataSource.getInstance().getTournaments()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tournament_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent;
        Tournament tournament = TournamentDataSource.getInstance().getTournaments().get(position);
        if (tournament.isCompleted()) {

        } else {
            if (tournament.isRegistrationClosed())
                intent = new Intent(getContext(), Upcoming_Result_Ranking_TabActivity.class);
            else
                intent = new Intent(getContext(), TournamentCreateActivity.class);
            intent.putExtra("name", tournament.getName());
            startActivity(intent);
        }
    }
}
