package kroam.tournamentmaker.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.adapters.TournamentRankingAdapter;

/**
 * Created by Kyle on 2015-12-05.
 */
public class TeamTournamentRankingFragment extends ListFragment {// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "team_name";
    private static TournamentFragment instance;
    private String teamName;
    private ArrayList<Tournament> associatedTournaments;
    private Team team;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamTournamentRankingFragment() {
    }

    public static TournamentFragment newInstance(String teamName) {
        TournamentFragment fragment = new TournamentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, teamName);
        fragment.setArguments(args);
        instance = fragment;
        return fragment;
    }

    public static TournamentFragment getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            teamName = getArguments().getString(ARG_PARAM1);

//            team = ParticipantsDataSource.getInstance().getTeam(teamName);
            associatedTournaments = new ArrayList<>();
            associatedTournaments.addAll(team.getAssociatedTournaments());
            setListAdapter(new TournamentRankingAdapter(getContext(), R.layout.row_team_ranking,
                    associatedTournaments, team));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_listview, container, false);
    }
}
