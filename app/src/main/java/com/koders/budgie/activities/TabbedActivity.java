package com.koders.budgie.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.koders.budgie.R;
import com.koders.budgie.fragments.EighthFragment;
import com.koders.budgie.fragments.FifthFragment;
import com.koders.budgie.fragments.FirstFragment;
import com.koders.budgie.fragments.FourthFragment;
import com.koders.budgie.fragments.SecondFragment;
import com.koders.budgie.fragments.SeventhFragment;
import com.koders.budgie.fragments.SixthFragment;
import com.koders.budgie.fragments.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

public class TabbedActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstFragment(), "First");
        adapter.addFragment(new SecondFragment(), "Second");
        adapter.addFragment(new ThirdFragment(), "Third");
        adapter.addFragment(new FourthFragment(), "Fourth");
        adapter.addFragment(new FifthFragment(), "Fifth");
        adapter.addFragment(new SixthFragment(), "Sixth");
        adapter.addFragment(new SeventhFragment(), "Seventh");
        adapter.addFragment(new EighthFragment(), "Eighth");
        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
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

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}