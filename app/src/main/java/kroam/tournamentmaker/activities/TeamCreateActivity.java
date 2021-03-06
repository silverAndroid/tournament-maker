package kroam.tournamentmaker.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Team;
import kroam.tournamentmaker.Util;
import kroam.tournamentmaker.database.DBColumns;
import kroam.tournamentmaker.database.MissingColumnException;
import kroam.tournamentmaker.database.ParticipantsDataSource;


/**
 * Created by ArsaniStuff on 2015-11-28.
 */
public class TeamCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String RESOURCE_PREFIX = "android.resource://kroam.tournamentmaker/drawable/";
    private boolean update;

    Team team;
    EditText name;
    EditText captainName;
    EditText phoneNumber;
    EditText email;
    ImageView teamLogo;
    AlertDialog dialog;
    String path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_creation);
        Button confirm = (Button) findViewById(R.id.btn_confirm);
        Button cancel = (Button) findViewById(R.id.btn_cancel);
        name = (EditText) findViewById(R.id.edit_team_name);
        captainName = (EditText) findViewById(R.id.edit_captain_name);
        phoneNumber = (EditText) findViewById(R.id.edit_phone);
        email = (EditText) findViewById(R.id.edit_email);
        teamLogo = (ImageView) findViewById(R.id.btn_team_logo);

        if (confirm != null) {
            confirm.setOnClickListener(this);
        }
        if (cancel != null) {
            cancel.setOnClickListener(this);
        }
        if (teamLogo != null) {
            teamLogo.setOnClickListener(this);
        }

        if (getIntent().hasExtra("id")) {
            Team team = ParticipantsDataSource.getInstance().getTeam(getIntent().getIntExtra("id", -1));
            if (team != null) {
                name.setText(team.getName());
                name.setEnabled(false);
                /*captainName.setText(team.getCaptainName());
                email.setText(team.getCaptainEmail());
                phoneNumber.setText(team.getPhoneNumber());*/
                try {
                    Util.loadImage(teamLogo, Uri.parse(path = team.getLogoPath()), getContentResolver());
                } catch (SecurityException e) {
                    requestPermissionManageDocuments();
                }
                update = true;
            } else {
                update = false;
            }
        } else {
            update = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_team_logo:
                dialog = new AlertDialog.Builder(TeamCreateActivity.this)
                        .setTitle("Choose a logo")
                        .setView(R.layout.select_team_logo_panel)
                        .setPositiveButton("Browse...", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chooseImage();
                            }
                        }).show();
                break;
            case R.id.btn_confirm:
                team = new Team(name.getText().toString(), captainName.getText().toString(), email.getText
                        ().toString(), phoneNumber.getText().toString());
                team.setLogoPath(path);
                long id;
                try {
                    if (update) {
                        id = ParticipantsDataSource.getInstance().updateTeam(team);
                    } else {
                        id = ParticipantsDataSource.getInstance().createTeam(team);
                    }
                } catch (MissingColumnException e) {
                    if (e.getMessage().equals(DBColumns.NAME)) {
                        name.setError("Name cannot be empty!");
                    } else if (e.getMessage().equals(DBColumns.LOGO_PATH)) {
                        Util.generateDialog(this, "Error", "Please select a logo!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    } else
                        throw e;
                    return;
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("teamID", id);
                setResult(id == -1 ? RESULT_CANCELED : RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Util.IMAGE_LOCAL_CODE) {
                final Uri selectedImageUri = data.getData();
                new Runnable() {
                    @Override
                    public void run() {
                        path = selectedImageUri.toString();
                        Util.loadImage(teamLogo, selectedImageUri, getContentResolver());
                    }
                }.run();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (requestCode == Util.PERMISSION_MANAGE_DOCUMENTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Util.loadImage(teamLogo, Uri.parse(path), getContentResolver());
            } else {
                Toast.makeText(getBaseContext(), "Couldn't load team logo!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void imageClick(View v) {
        ImageView clickedImage = (ImageView) v;
        teamLogo.setImageDrawable(clickedImage.getDrawable());
        path = RESOURCE_PREFIX + clickedImage.getTag();
        dialog.dismiss();
    }

    private void chooseImage() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, Util.IMAGE_LOCAL_CODE);
        } else {
            intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Util
                    .IMAGE_LOCAL_CODE);
        }
    }

    private void requestPermissionManageDocuments() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Activity activity = TeamCreateActivity.this;
            final String permission = Manifest.permission.MANAGE_DOCUMENTS;
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager
                    .PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    Util.generateDialog(activity, "Requesting Permission", "This is being shown because you" +
                            " are constantly denying the permission this app needs to use to access your " +
                            "pictures on a separate application like Google Drive, Google Photos, etc.")
                            .setPositiveButton("I understand!", new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity, new String[]{permission}, Util
                                            .PERMISSION_MANAGE_DOCUMENTS);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, Util
                            .PERMISSION_MANAGE_DOCUMENTS);
                }
            }
        }
    }
}
