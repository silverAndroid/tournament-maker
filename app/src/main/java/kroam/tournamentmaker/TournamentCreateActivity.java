package kroam.tournamentmaker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
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

public class TournamentCreateActivity extends AppCompatActivity implements View.OnClickListener {

//<<<<<<< HEAD
    public static final String ROUND_ROBIN = "Round Robin";
    public static final String KNOCKOUT = "Knockout";
    public static final String COMBINATION = "Combination";
//=======
//    private static final String ROUND_ROBIN = "Round Robin";
//    private static final String KNOCKOUT = "Knockout";
//    private static final String COMBINATION = "Combination";
    private final ViewTeamsAdapter[] viewTeamsAdapter = new ViewTeamsAdapter[1];
//>>>>>>> ce9884ab68f8b473571f871ab5f157eedae4f44d
    StatsAdapter adapter;
    NumberPicker sizePicker;
    RadioGroup typeGroup;
    RadioButton roundRobin;
    EditText name;
    private AlertDialog selectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_creation);
        RecyclerView stats;
        Button add;
        Button confirm;
        Button cancel;
        Button selectTeams;

        name = (EditText) findViewById(R.id.edit_tournament_name);

        stats = (RecyclerView) findViewById(R.id.stats_list);
        stats.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(getBaseContext()));
        stats.setAdapter(adapter = new StatsAdapter());

        sizePicker = (NumberPicker) findViewById(R.id.numberPicker);
        sizePicker.setMinValue(1);
        sizePicker.setMaxValue(16);

        typeGroup = (RadioGroup) findViewById(R.id.tournament_type_group);

        roundRobin = (RadioButton) findViewById(R.id.radio_round_robin);
        roundRobin.setChecked(true);

        add = (Button) findViewById(R.id.btn_add);
        add.setOnClickListener(this);

        confirm = (Button) findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);

        selectTeams = (Button) findViewById(R.id.btn_select_teams);
        selectTeams.setOnClickListener(this);

        if (getIntent().hasExtra("name")) {
            String name = getIntent().getStringExtra("name");
            Tournament tournament = TournamentDataSource.getInstance().getTournament(name);
            this.name.setText(tournament.getName());
            sizePicker.setValue(tournament.getMaxSize());
            String type = tournament.getType();
            switch (type) {
                case ROUND_ROBIN:
                    roundRobin.setChecked(true);
                    break;
                case KNOCKOUT:
                    RadioButton knockout = (RadioButton) findViewById(R.id.radio_knockout);
                    knockout.setChecked(true);
                    break;
                case COMBINATION:
                    RadioButton combination = (RadioButton) findViewById(R.id.radio_combination);
                    combination.setChecked(true);
                    break;
            }
            RecyclerView teamNameList = (RecyclerView) findViewById(R.id.selected_teams);
            teamNameList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager
                    (getBaseContext()));
            teamNameList.setAdapter(viewTeamsAdapter[0] = new ViewTeamsAdapter(tournament.getTeams()));
            stats.setAdapter(adapter = new StatsAdapter(StatsDataSource.getInstance().getTournamentStats(name)));
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
            case R.id.btn_add:
                adapter.addItem();
                break;
            case R.id.btn_confirm:
                String tournamentName = name.getText().toString();
                adapter.setTournamentName(tournamentName);
                StatsDataSource.getInstance().addStats(adapter.getItems());
                int selectedRadioButtonID = typeGroup.getCheckedRadioButtonId();
                String tournamentType = selectedRadioButtonID == R.id.radio_round_robin ? ROUND_ROBIN :
                        selectedRadioButtonID == R.id.radio_knockout ? KNOCKOUT : COMBINATION;
                Tournament tournament = new Tournament(tournamentName, tournamentType, viewTeamsAdapter[0] != null ?
                        viewTeamsAdapter[0].getSelectedTeams() : new ArrayList<Team>(), sizePicker.getValue());
                try {
                    TournamentDataSource.getInstance().createTournament(tournament);
                } catch (SQLiteConstraintException e) {
                    TournamentDataSource.getInstance().updateTournament(tournament);
                }

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btn_cancel:
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
                RecyclerView selectedTeams = (RecyclerView) selectDialog.findViewById(R.id.teams);
                selectedTeams.setAdapter(new SelectTeamsAdapter(TeamDataSource.getInstance().getTeams()));
            }
        }
    }

    private void selectTeams() {
        final SelectTeamsAdapter[] selectTeamsAdapter = new SelectTeamsAdapter[1];
        selectDialog = new AlertDialog.Builder(TournamentCreateActivity.this, R.style.DialogTheme)
                .setView(R.layout.select_team_panel)
                .setTitle("Select Teams")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecyclerView teamNameList = (RecyclerView) findViewById(R.id
                                .selected_teams);
                        teamNameList.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager
                                (getBaseContext()));
                        teamNameList.setAdapter(viewTeamsAdapter[0] = new ViewTeamsAdapter(selectTeamsAdapter[0]
                                .getSelectedTeams()));
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
                RecyclerView teams = (RecyclerView) view.findViewById(R.id.teams);
                teams.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                teams.setAdapter(selectTeamsAdapter[0] = new SelectTeamsAdapter(TeamDataSource.getInstance().getTeams()));
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
}
