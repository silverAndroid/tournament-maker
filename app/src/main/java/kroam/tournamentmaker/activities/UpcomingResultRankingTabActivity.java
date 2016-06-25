package kroam.tournamentmaker.activities;

import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kroam.tournamentmaker.Match;
import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Tournament;
import kroam.tournamentmaker.Util;
import kroam.tournamentmaker.database.MatchesDataSource;
import kroam.tournamentmaker.database.TournamentsDataSource;
import kroam.tournamentmaker.fragments.RankingFragment;
import kroam.tournamentmaker.fragments.ResultFragment;
import kroam.tournamentmaker.fragments.UpcomingFragment;


public class UpcomingResultRankingTabActivity extends AppCompatActivity implements UpcomingFragment
        .FinishListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String name;
    private ArrayList<Match> upcomingMatches;
    private ArrayList<Match> finishedMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_upcoming_results_ranking);
        name = getIntent().getStringExtra("name");
        Tournament tournament = TournamentsDataSource.getInstance().getTournament(name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(name);
        }
        setSupportActionBar(toolbar);

        upcomingMatches = new ArrayList<>();
        upcomingMatches.addAll(MatchesDataSource.getInstance().getMatchesForRound(name, tournament
                .getCurrentRound(), false));
        finishedMatches = new ArrayList<>();
        finishedMatches.addAll(MatchesDataSource.getInstance().getMatchesForRound(name, tournament
                .getCurrentRound(), true));

        //COPY PASTA
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instructions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.instructions) {
            Util.generateDialog(UpcomingResultRankingTabActivity.this, "Instructions")
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
        adapter.addFragment(UpcomingFragment.newInstance(upcomingMatches, name), "Upcoming");
        adapter.addFragment(ResultFragment.newInstance(finishedMatches, name), "Results");
        adapter.addFragment(RankingFragment.newInstance(name), "Rankings");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void endTournament() {
        Toast.makeText(getBaseContext(), "Tournament complete!", Toast.LENGTH_SHORT).show();
        finish();
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
