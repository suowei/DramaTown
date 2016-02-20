package net.saoju.dramatown;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import net.saoju.dramatown.Adapters.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserEpfavsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_epfavs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i <= 4; i += 2) {
            UserEpfavsFragment fragment = new UserEpfavsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("user", getIntent().getIntExtra("user", 0));
            bundle.putInt("type", i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        List<String> titles = new ArrayList<>();
        titles.add("想听");
        titles.add("听过");
        titles.add("抛弃");

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments, titles);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        switch (getIntent().getIntExtra("type", 0)) {
            case 0:
                mViewPager.setCurrentItem(0);
                break;
            case 2:
                mViewPager.setCurrentItem(1);
                break;
            case 4:
                mViewPager.setCurrentItem(2);
                break;
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
}
