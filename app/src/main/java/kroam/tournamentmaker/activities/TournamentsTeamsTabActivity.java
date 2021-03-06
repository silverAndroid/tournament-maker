package kroam.tournamentmaker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Util;
import kroam.tournamentmaker.database.DatabaseSingleton;
import kroam.tournamentmaker.database.TournamentsDataSource;
import kroam.tournamentmaker.fragments.TeamFragment;
import kroam.tournamentmaker.fragments.TournamentFragment;


public class TournamentsTeamsTabActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_tournaments_teams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //COPY PASTA
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
        viewPager.setCurrentItem(getIntent().getIntExtra("tabNumber", 0));

        DatabaseSingleton.createInstance(getApplicationContext());
        Stetho.initializeWithDefaults(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (viewPager.getCurrentItem()) {
                        case 0:
                            //add Intent to Activity to send to create tournament
                            Intent intent = new Intent(TournamentsTeamsTabActivity.this,
                                    TournamentCreateActivity.class);
                            startActivityForResult(intent, Util.TOURNAMENT_REQUEST_CODE);
                            break;
                        case 1:
                            //add Intent to Activity to send to create team
                            intent = new Intent(TournamentsTeamsTabActivity.this, TeamCreateActivity.class);
                            startActivityForResult(intent, Util.TEAM_REQUEST_CODE);
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseSingleton.getInstance().closeDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instructions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.instructions) {
            Util.generateDialog(TournamentsTeamsTabActivity.this, "Instructions")
                    .setView(R.layout.instructions)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TournamentFragment.newInstance(), "Tournaments");
        adapter.addFragment(TeamFragment.newInstance(), "Teams");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Util.TOURNAMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                TournamentFragment.getInstance().setListAdapter(new ArrayAdapter<>(this, android.R.layout
                        .simple_list_item_1, android.R.id.text1, TournamentsDataSource.getInstance()
                        .getTournaments()));
        } else if (requestCode == Util.TEAM_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                TeamFragment.getInstance().refresh();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
