package net.saoju.dramatown;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.saoju.dramatown.Adapters.SectionsPagerAdapter;
import net.saoju.dramatown.Models.Episode;
import net.saoju.dramatown.Models.Reviews;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EpisodeActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;

    private List<Fragment> fragments;
    private List<String> titles;

    private EpisodeDetailFragment episodeDetailFragment;
    private EpisodeReviewsFragment episodeReviewsFragment;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        imageView = (ImageView) findViewById(R.id.header);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.container);

        episodeDetailFragment = new EpisodeDetailFragment();
        episodeReviewsFragment = new EpisodeReviewsFragment();

        int episode = getIntent().getIntExtra("id", 0);

        Bundle bundle = new Bundle();
        bundle.putInt("id", episode);
        episodeDetailFragment.setArguments(bundle);
        episodeReviewsFragment.setArguments(bundle);

        fragments = new ArrayList<>();
        fragments.add(episodeDetailFragment);
        fragments.add(episodeReviewsFragment);

        titles = new ArrayList<>();
        titles.add("详情");
        titles.add("评论");

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments, titles);

        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setData(final Episode episode) {
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.drama_episode_title,
                episode.getDrama().getTitle(), episode.getTitle(), episode.getAlias()));
        if (episode.getReviews() > 0) {
            tabLayout.getTabAt(1).setText("评论（" + episode.getReviews() + "）");
        }
        context = this;
        if (!episode.getPoster_url().isEmpty()) {
            Picasso.with(context).load(episode.getPoster_url()).into(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null);
                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                ImageView img1 = (ImageView)imgEntryView.findViewById(R.id.large_image);
                Picasso.with(context).load(episode.getPoster_url()).into(img1);
                dialog.setView(imgEntryView);
                dialog.show();
                imgEntryView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        dialog.cancel();
                    }
                });
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
