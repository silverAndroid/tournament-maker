//package kroam.tournamentmaker.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import kroam.tournamentmaker.R;
//import kroam.tournamentmaker.Util;
//import kroam.tournamentmaker.database.DatabaseSingleton;
//import kroam.tournamentmaker.fragments.TeamFragment;
//import kroam.tournamentmaker.fragments.TournamentFragment;
//
///**
// * Created by Kyle on 2015-12-04.
// */
//public class Team_Tabs_Activity  extends AppCompatActivity {
//
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tournaments_teams_tabs);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        //COPY PASTA
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//        viewPager.setCurrentItem(getIntent().getIntExtra("tabNumber", 0));
//
//        DatabaseSingleton.createInstance(getApplicationContext());
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (viewPager.getCurrentItem()) {
//                    case 0:
//                        //add Intent to Activity to send to create tournament
//                        Intent intent = new Intent(Team_Tabs_Activity.this, TournamentCreateActivity.class);
//                        startActivityForResult(intent, Util.TOURNAMENT_REQUEST_CODE);
//                        break;
//                    case 1:
//                        //add Intent to Activity to send to create team
//                        intent = new Intent(Team_Tabs_Activity.this, TeamCreateActivity.class);
//                        startActivityForResult(intent, Util.TEAM_REQUEST_CODE);
//                        break;
//                }
//            }
//        });
//    }
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(TournamentFragment.newInstance(), "Tournaments");
//        adapter.addFragment(TeamFragment.newInstance(), "Teams");
//        viewPager.setAdapter(adapter);
//    }
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//
//    }
//}
