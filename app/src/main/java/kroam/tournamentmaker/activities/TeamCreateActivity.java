package kroam.tournamentmaker.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.database.TeamDataSource;


/**
 * Created by ArsaniStuff on 2015-11-28.
 */
public class TeamCreateActivity extends AppCompatActivity implements View.OnClickListener {

    Team team;
    EditText name;
    EditText captainName;
    EditText phoneNumber;
    EditText email;
    ImageButton teamLogo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_creation);
        Button confirm;
        Button cancel;

        confirm = (Button) findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);

        name = (EditText) findViewById(R.id.edit_team_name);
        captainName = (EditText) findViewById(R.id.edit_captain_name);
        phoneNumber = (EditText) findViewById(R.id.edit_phone);
        email = (EditText) findViewById(R.id.edit_email);
        teamLogo = (ImageButton) findViewById(R.id.btn_teamlogo);

        if (getIntent().hasExtra("name")) {
            Team team = TeamDataSource.getInstance().getTeam(getIntent().getStringExtra("name"));
            name.setText(team.getName());
            captainName.setText(team.getCaptainName());
            email.setText(team.getCaptainEmail());
            phoneNumber.setText(team.getPhoneNumber());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /* case R.id.edit_team_logo:
                team.setLogo(
             */
            case R.id.btn_confirm:
                team = new Team(name.getText().toString(), captainName.getText().toString(), email.getText().toString
                        (), phoneNumber.getText().toString());
                try {
                    TeamDataSource.getInstance().createTeam(team);
                } catch (SQLiteConstraintException e) {
                    TeamDataSource.getInstance().updateTeam(team);
                }

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
