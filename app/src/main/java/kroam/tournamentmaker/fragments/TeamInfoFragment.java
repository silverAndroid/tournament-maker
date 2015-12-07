package kroam.tournamentmaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.database.TeamDataSource;

/**
 * Created by Kyle on 2015-12-04.
 */
public class TeamInfoFragment extends Fragment {
    private static TeamInfoFragment instance;
    TextView name;
    TextView captainName;
    TextView phoneNumber;
    TextView email;
    ImageButton teamLogo;
    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamInfoFragment() {
    }

    public static TeamInfoFragment newInstance() {
        TeamInfoFragment fragment = new TeamInfoFragment();
        instance = fragment;
        return fragment;
    }

    public static TeamInfoFragment getInstance() {
        return instance;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        name = (TextView) getView().findViewById(R.id.edit_team_name);
        captainName = (TextView) getView().findViewById(R.id.edit_captain_name);
        phoneNumber = (TextView) getView().findViewById(R.id.edit_phone);
        email = (TextView) getView().findViewById(R.id.edit_email);
        teamLogo = (ImageButton) getView().findViewById(R.id.btn_team_logo);

        Team team = TeamDataSource.getInstance().getTeam(savedInstanceState.getString("name"));
        name.setText(team.getName());
        captainName.setText(team.getCaptainName());
        email.setText(team.getCaptainEmail());
        phoneNumber.setText(team.getPhoneNumber());
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
        return inflater.inflate(R.layout.team_page, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }
}
