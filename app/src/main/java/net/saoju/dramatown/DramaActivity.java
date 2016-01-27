package net.saoju.dramatown;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.saoju.dramatown.Adapters.SectionsPagerAdapter;
import net.saoju.dramatown.Models.Drama;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DramaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;

    private List<Fragment> fragments;
    private List<String> titles;

    private DramaDetailFragment dramaDetailFragment;
    private DramaReviewsFragment dramaReviewsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.container);

        dramaDetailFragment = new DramaDetailFragment();
        dramaReviewsFragment = new DramaReviewsFragment();

        int drama = getIntent().getIntExtra("id", 0);

        Bundle bundle = new Bundle();
        bundle.putInt("id", drama);
        dramaReviewsFragment.setArguments(bundle);

        fragments = new ArrayList<>();
        fragments.add(dramaDetailFragment);
        fragments.add(dramaReviewsFragment);

        titles = new ArrayList<>();
        titles.add("详情");
        titles.add("评论");

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments, titles);

        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SaojuService service = retrofit.create(SaojuService.class);
        Call<Drama> dramaCall = service.getDrama(String.valueOf(drama));
        dramaCall.enqueue(new Callback<Drama>() {
            @Override
            public void onResponse(Response<Drama> response) {
                Drama drama = response.body();
                toolbar.setTitle(drama.getTitle());
                if (drama.getReviews() > 0) {
                    tabLayout.getTabAt(1).setText("评论（" + drama.getReviews() + "）");
                }
                dramaDetailFragment.updateData(drama);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_episode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
