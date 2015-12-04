package kroam.tournamentmaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import kroam.tournamentmaker.DatabaseSingleton;
import kroam.tournamentmaker.R;
import kroam.tournamentmaker.TeamDataSource;
import kroam.tournamentmaker.TeamFragment;
import kroam.tournamentmaker.TournamentDataSource;
import kroam.tournamentmaker.TournamentFragment;
import kroam.tournamentmaker.Util;


public class Tournaments_Teams_TabActivity extends AppCompatActivity implements TeamFragment.OnFragmentInteractionListener, TournamentFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournaments_teams_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //COPY PASTA
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(getIntent().getIntExtra("tabNumber", 0));

        DatabaseSingleton.createInstance(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        //add Intent to Activity to send to create tournament
                        Intent intent = new Intent(Tournaments_Teams_TabActivity.this, TournamentCreateActivity.class);
                        startActivityForResult(intent, Util.TOURNAMENT_REQUEST_CODE);
                        break;
                    case 1:
                        //add Intent to Activity to send to create team
                        intent = new Intent(Tournaments_Teams_TabActivity.this, TeamCreateActivity.class);
                        startActivityForResult(intent, Util.TEAM_REQUEST_CODE);
                        break;
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TournamentFragment.newInstance(), "Tournaments");
        adapter.addFragment(TeamFragment.newInstance(), "Teams");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Util.TOURNAMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                TournamentFragment.getInstance().setListAdapter(new ArrayAdapter<>(this, android.R.layout
                        .simple_list_item_1, android.R.id.text1, TournamentDataSource.getInstance().getTournaments()));
        } else if (requestCode == Util.TEAM_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                TeamFragment.getInstance().setListAdapter(new ArrayAdapter<>(this, android.R.layout
                        .simple_list_item_1, android.R.id.text1, TeamDataSource.getInstance().getTeams()));
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
