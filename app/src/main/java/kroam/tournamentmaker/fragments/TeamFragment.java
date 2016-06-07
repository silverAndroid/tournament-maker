package kroam.tournamentmaker.fragments;

import android.content.Context;
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
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.activities.TeamCreateActivity;
import kroam.tournamentmaker.database.ParticipantsDataSource;

public class TeamFragment extends ListFragment {

    private static TeamFragment instance;
    private ArrayList<Team> teams;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamFragment() {
    }

    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
        instance = fragment;
        return fragment;
    }

    public static TeamFragment getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id
                .text1, teams = ParticipantsDataSource.getInstance().getTeams()));
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

        Intent intent = new Intent(getContext(), TeamCreateActivity.class);
        intent.putExtra("name", teams.get(position).getName());
        startActivity(intent);
    }

    public void refresh() {
        Context context = getContext();
        if (context != null)
            setListAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id
                    .text1, teams = ParticipantsDataSource.getInstance().getTeams()));
    }
}
