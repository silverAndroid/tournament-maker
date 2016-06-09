package kroam.tournamentmaker.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import kroam.tournamentmaker.Participant;
import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.Util;
import kroam.tournamentmaker.adapters.SelectTeamsAdapter;
import kroam.tournamentmaker.adapters.StatsAdapter;
import kroam.tournamentmaker.adapters.ViewTeamsAdapter;
import kroam.tournamentmaker.database.DBColumns;
import kroam.tournamentmaker.database.MissingColumnException;
import kroam.tournamentmaker.database.ParticipantsDataSource;
import kroam.tournamentmaker.database.StatsDataSource;
import kroam.tournamentmaker.database.TournamentsDataSource;
import kroam.tournamentmaker.fragments.TeamFragment;

public class TournamentCreateActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ROUND_ROBIN = "Round Robin";
    public static final String KNOCKOUT = "Knockout";
    public static final String COMBINATION = "Combination";
    private final ViewTeamsAdapter[] viewTeamsAdapter = new ViewTeamsAdapter[1];
    private final SelectTeamsAdapter[] selectTeamsAdapter = new SelectTeamsAdapter[1];

    StatsAdapter adapter;
    NumberPicker sizePicker;
    RadioGroup typeGroup;
    RadioButton roundRobin;
    EditText name;
    private AlertDialog selectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_create);
        RecyclerView stats;
        Button confirm;
        Button cancel;
        Button selectTeams;

        name = (EditText) findViewById(R.id.edit_tournament_name);

        stats = (RecyclerView) findViewById(R.id.stats_rv);
        if (stats != null) {
            stats.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            stats.setAdapter(adapter = new StatsAdapter());
        }

        sizePicker = (NumberPicker) findViewById(R.id.numberPicker);
        if (sizePicker != null) {
            sizePicker.setMinValue(2);
            sizePicker.setMaxValue(16);
        }

        typeGroup = (RadioGroup) findViewById(R.id.tournament_type_group);

        roundRobin = (RadioButton) findViewById(R.id.radio_round_robin);
        if (roundRobin != null) {
            roundRobin.setChecked(true);
        }

        confirm = (Button) findViewById(R.id.btn_confirm);
        if (confirm != null) {
            confirm.setOnClickListener(this);
        }

        cancel = (Button) findViewById(R.id.btn_cancel);
        if (cancel != null) {
            cancel.setOnClickListener(this);
        }

        selectTeams = (Button) findViewById(R.id.btn_select_teams);
        if (selectTeams != null) {
            selectTeams.setOnClickListener(this);
        }

        viewTeamsAdapter[0] = new ViewTeamsAdapter(new ArrayList<Participant>());

        if (getIntent().hasExtra("name")) {
            String name = getIntent().getStringExtra("name");
            Tournament tournament = TournamentsDataSource.getInstance().getTournament(name);
            this.name.setText(tournament.getName());
            this.name.setEnabled(false);
            sizePicker.setValue(tournament.getMaxSize());
            String type = tournament.getType();
            switch (type) {
                case ROUND_ROBIN:
                    roundRobin.setChecked(true);
                    break;
                case KNOCKOUT:
                    RadioButton knockout = (RadioButton) findViewById(R.id.radio_knockout);
                    if (knockout != null) {
                        knockout.setChecked(true);
                    }
                    break;
                case COMBINATION:
                    RadioButton combination = (RadioButton) findViewById(R.id.radio_combination);
                    if (combination != null) {
                        combination.setChecked(true);
                    }
                    break;
            }
            RecyclerView teamNameList = (RecyclerView) findViewById(R.id.rv);
            if (teamNameList != null) {
                teamNameList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                teamNameList.setAdapter(viewTeamsAdapter[0] = new ViewTeamsAdapter(tournament.getTeams()));
            }
            if (stats != null) {
                stats.setAdapter(adapter = new StatsAdapter(tournament.getStats()));
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                Util.generateDialog(TournamentCreateActivity.this, "End Registration", "Would you like to " +
                        "close registration?")
                        .setPositiveButton("Close registration!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO: Get someone to check message
                                Util.generateDialog(TournamentCreateActivity.this, "Closing Registration",
                                        "Are you sure you want to close registration?\nIf you do, you will " +
                                                "not be able to edit this tournament again.")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                closeRegistration();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        })
                        .setNegativeButton("Leave registration open!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveTournament(false);
                                TeamFragment.getInstance().refresh();
                                finish();
                            }
                        }).show();
                break;
            case R.id.btn_cancel:
                TeamFragment.getInstance().refresh();
                finish();
                break;
            case R.id.btn_select_teams:
                selectTeams();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Util.TEAM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                RecyclerView selectedTeams = (RecyclerView) selectDialog.findViewById(R.id.rv);
                Team newTeam = ParticipantsDataSource.getInstance().getTeam(data.getIntExtra("teamID", -1));
                selectTeamsAdapter[0].addTeam(newTeam);
                if (selectedTeams != null) {
                    selectedTeams.setAdapter(selectTeamsAdapter[0]);
                }
            }
        }
    }

    private void selectTeams() {
        //TODO: Fix weird dialog padding issue
        selectDialog = Util.generateDialog(TournamentCreateActivity.this, "Select Teams")
                .setView(R.layout.select_team_panel)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecyclerView teamNameList = (RecyclerView) findViewById(R.id.rv);
                        if (teamNameList != null) {
                            teamNameList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                            teamNameList.setAdapter(viewTeamsAdapter[0] = new ViewTeamsAdapter
                                    (selectTeamsAdapter[0].getSelectedTeams()));
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        selectDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Dialog view = (Dialog) dialog;
                RecyclerView teams = (RecyclerView) view.findViewById(R.id.rv);
                teams.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                teams.setAdapter(selectTeamsAdapter[0] = new SelectTeamsAdapter(ParticipantsDataSource
                        .getInstance().getTeams(), viewTeamsAdapter[0].getSelectedTeams()));
                FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), TeamCreateActivity.class);
                        startActivityForResult(intent, Util.TEAM_REQUEST_CODE);
                    }
                });
            }
        });
        selectDialog.show();
    }

    private void saveTournament(boolean registrationCompleted) {
        String tournamentName = name.getText().toString();
        adapter.setTournamentName(tournamentName);
        int selectedRadioButtonID = typeGroup.getCheckedRadioButtonId();
        String tournamentType = selectedRadioButtonID == R.id.radio_round_robin ? ROUND_ROBIN :
                selectedRadioButtonID == R.id.radio_knockout ? KNOCKOUT : COMBINATION;
        Tournament tournament = new Tournament(tournamentName, tournamentType, sizePicker.getValue());
        tournament.closeRegistration(registrationCompleted);
        tournament.addStats(adapter.getItems(), adapter.getWinningStatPosition());
        if (viewTeamsAdapter[0] != null)
            tournament.addTeams(viewTeamsAdapter[0].getSelectedTeams());
        try {
            TournamentsDataSource.getInstance().createTournament(tournament);
        } catch (MissingColumnException e) {
            if (e.getMessage().equals(DBColumns.NAME)) {
                name.setError("Name cannot be empty!");
            } else if (e.getMessage().equals(DBColumns.TOURNAMENT_NAME)) {
                name.setError("Name cannot be empty!");
            } else
                throw e;
            return;
        }
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
    }

    private void closeRegistration() {
        saveTournament(true);
        TeamFragment.getInstance().refresh();
    }
}
