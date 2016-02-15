package net.saoju.dramatown;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.saoju.dramatown.Adapters.SectionsPagerAdapter;
import net.saoju.dramatown.Models.Episode;
import net.saoju.dramatown.Models.EpisodeFavorite;
import net.saoju.dramatown.Models.Token;
import net.saoju.dramatown.Utils.AddCookiesInterceptor;
import net.saoju.dramatown.Utils.ReceivedCookiesInterceptor;
import net.saoju.dramatown.Utils.ResponseResult;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EpisodeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private ImageView posterExpanded;
    private TabLayout tabLayout;
    private LinearLayout favoriteLayout;
    private TextView favoriteType;
    private RatingBar ratingBar;
    private FloatingActionButton fab;
    private LinearLayout fab_menu;
    private LinearLayout addFavoriteReviewLayout;
    private LinearLayout editFavoriteReviewLayout;
    private LinearLayout addFavoriteLayout;
    private LinearLayout editFavoriteLayout;
    private LinearLayout deleteFavoriteLayout;
    private FloatingActionButton addFavoriteReviewFab;
    private FloatingActionButton editFavoriteReviewFab;
    private FloatingActionButton addFavoriteFab;
    private FloatingActionButton editFavoriteFab;
    private FloatingActionButton deleteFavoriteFab;
    private FloatingActionButton createReviewFab;

    private EpisodeDetailFragment episodeDetailFragment;
    private EpisodeReviewsFragment episodeReviewsFragment;

    private SaojuService service;

    private int episodeId;
    private Episode episode;

    private boolean fabMenuOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageView = (ImageView) findViewById(R.id.poster);
        posterExpanded = (ImageView) findViewById(R.id.poster_expanded);
        posterExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posterExpanded.setVisibility(View.GONE);
            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        favoriteLayout = (LinearLayout) findViewById(R.id.favorite_layout);
        favoriteType = (TextView) findViewById(R.id.favorite_type);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_menu = (LinearLayout) findViewById(R.id.fab_menu);
        addFavoriteReviewLayout = (LinearLayout) findViewById(R.id.add_favorite_review_layout);
        editFavoriteReviewLayout = (LinearLayout) findViewById(R.id.edit_favorite_review_layout);
        addFavoriteLayout = (LinearLayout) findViewById(R.id.add_favorite_layout);
        editFavoriteLayout = (LinearLayout) findViewById(R.id.edit_favorite_layout);
        deleteFavoriteLayout = (LinearLayout) findViewById(R.id.delete_favorite_layout);
        addFavoriteReviewFab = (FloatingActionButton) findViewById(R.id.add_favorite_review_fab);
        editFavoriteReviewFab = (FloatingActionButton) findViewById(R.id.edit_favorite_review_fab);
        addFavoriteFab = (FloatingActionButton) findViewById(R.id.add_favorite_fab);
        editFavoriteFab = (FloatingActionButton) findViewById(R.id.edit_favorite_fab);
        deleteFavoriteFab = (FloatingActionButton) findViewById(R.id.delete_favorite_fab);
        createReviewFab = (FloatingActionButton) findViewById(R.id.create_review_fab);

        fabMenuOpened = false;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOrCloseFabMenu();
            }
        });
        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseFabMenu();
            }
        });

        episodeId = getIntent().getIntExtra("id", 0);

        episodeDetailFragment = new EpisodeDetailFragment();

        episodeReviewsFragment = new EpisodeReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", episodeId);
        episodeReviewsFragment.setArguments(bundle);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(episodeDetailFragment);
        fragments.add(episodeReviewsFragment);

        List<String> titles = new ArrayList<>();
        titles.add("详情");
        titles.add("评论");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments, titles);

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor(this))
                .addInterceptor(new ReceivedCookiesInterceptor(this))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(SaojuService.class);
        Call<Episode> episodeCall = service.getEpisode(String.valueOf(episodeId));
        episodeCall.enqueue(new Callback<Episode>() {
            @Override
            public void onResponse(Response<Episode> response) {
                if (!response.isSuccess()) {
                    Toast.makeText(EpisodeActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                setData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void setData(Episode data) {
        episode = data;
        toolbar.setTitle(getResources().getString(R.string.drama_episode_title,
                episode.getDrama().getTitle(), episode.getTitle(), episode.getAlias()));
        if (!episode.getPoster_url().isEmpty()) {
            Picasso.with(EpisodeActivity.this).load(episode.getPoster_url()).into(imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(EpisodeActivity.this).load(episode.getPoster_url()).into(posterExpanded);
                posterExpanded.setVisibility(View.VISIBLE);
                ObjectAnimator imageAnimator = ObjectAnimator.ofFloat(posterExpanded, "alpha", 0F, 1F);
                imageAnimator.setDuration(150);
                imageAnimator.setInterpolator(new LinearInterpolator());
                imageAnimator.start();
            }
        });

        if (episode.getUserFavorite() != null) {
            updateFavorite(episode.getUserFavorite().getType(), episode.getUserFavorite().getRating());
        } else {
            favoriteLayout.setVisibility(View.GONE);
            addFavoriteReviewLayout.setVisibility(View.VISIBLE);
            editFavoriteReviewLayout.setVisibility(View.GONE);
            addFavoriteLayout.setVisibility(View.VISIBLE);
            editFavoriteLayout.setVisibility(View.GONE);
            deleteFavoriteLayout.setVisibility(View.GONE);
        }
        addFavoriteReviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseFabMenu();
                Intent intent = new Intent(EpisodeActivity.this, EpisodeFavoriteReviewActivity.class);
                intent.putExtra("drama_id", episode.getDrama_id());
                intent.putExtra("episode_id", episodeId);
                intent.putExtra("is_update", false);
                EpisodeActivity.this.startActivityForResult(intent, 1);
            }
        });
        editFavoriteReviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseFabMenu();
                Intent intent = new Intent(EpisodeActivity.this, EpisodeFavoriteReviewActivity.class);
                intent.putExtra("drama_id", episode.getDrama_id());
                intent.putExtra("episode_id", episodeId);
                intent.putExtra("is_update", true);
                intent.putExtra("type", episode.getUserFavorite().getType());
                intent.putExtra("rating", episode.getUserFavorite().getRating());
                EpisodeActivity.this.startActivityForResult(intent, 1);
            }
        });
        addFavoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseFabMenu();
                final View dialogView = LayoutInflater.from(EpisodeActivity.this).inflate(R.layout.dialog_epfav, null);
                setFavoriteDialog(dialogView, new EpisodeFavorite(2, 0));
                new AlertDialog.Builder(EpisodeActivity.this)
                        .setTitle("添加收藏")
                        .setView(dialogView)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveFavorite(dialogView, false);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        editFavoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseFabMenu();
                final View dialogView = LayoutInflater.from(EpisodeActivity.this).inflate(R.layout.dialog_epfav, null);
                setFavoriteDialog(dialogView, episode.getUserFavorite());
                new AlertDialog.Builder(EpisodeActivity.this)
                        .setTitle("修改收藏")
                        .setView(dialogView)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveFavorite(dialogView, true);
                            }
                        }).setNegativeButton("取消", null)
                        .show();
            }
        });
        deleteFavoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseFabMenu();
                new AlertDialog.Builder(EpisodeActivity.this)
                        .setTitle("删除收藏")
                        .setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFavorite();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        createReviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseFabMenu();
                Intent intent = new Intent(EpisodeActivity.this, WriteReviewActivity.class);
                intent.putExtra("drama_id", episode.getDrama_id());
                intent.putExtra("episode_id", episode.getId());
                EpisodeActivity.this.startActivityForResult(intent, 0);
            }
        });

        if (episode.getReviews() > 0) {
            tabLayout.getTabAt(1).setText("评论（" + episode.getReviews() + "）");
        }
        episodeDetailFragment.setData(episode);
        episodeReviewsFragment.refresh();
    }

    private void updateFavorite(int type, float rating) {
        if (episode.getUserFavorite() == null) {
            episode.setUserFavorite(new EpisodeFavorite(type, rating));
        } else {
            episode.getUserFavorite().setType(type);
            episode.getUserFavorite().setRating(rating);
        }
        favoriteLayout.setVisibility(View.VISIBLE);
        addFavoriteReviewLayout.setVisibility(View.GONE);
        editFavoriteReviewLayout.setVisibility(View.VISIBLE);
        addFavoriteLayout.setVisibility(View.GONE);
        editFavoriteLayout.setVisibility(View.VISIBLE);
        deleteFavoriteLayout.setVisibility(View.VISIBLE);
        favoriteType.setText(episode.getUserFavorite().getTypeString());
        if (episode.getUserFavorite().getRating() != 0) {
            ratingBar.setRating(episode.getUserFavorite().getRating());
            ratingBar.setVisibility(View.VISIBLE);
        } else {
            ratingBar.setVisibility(View.GONE);
        }
    }

    private void saveFavorite(View view, final boolean isUpdate) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.favorite_type);
        final int type;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.favorite_type_0:
                type = 0;
                break;
            case R.id.favorite_type_2:
                type = 2;
                break;
            case R.id.favorite_type_4:
                type = 4;
                break;
            default:
                Toast.makeText(this, "请选择收藏类型", Toast.LENGTH_SHORT).show();
                return;
        }
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating);
        final float rating = ratingBar.getRating();
        Call<Token> tokenCall = service.getToken();
        tokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                Token token = response.body();
                if (!token.isAuth()) {
                    EpisodeActivity.this.startActivity(new Intent(EpisodeActivity.this, LoginActivity.class));
                    return;
                }
                Call<ResponseResult> call;
                if (isUpdate) {
                    call = service.editEpfav(String.valueOf(episodeId), token.getToken(), type, rating);
                } else {
                    call = service.addEpfav(token.getToken(), episodeId, type, rating);
                }
                call.enqueue(new Callback<ResponseResult>() {
                    @Override
                    public void onResponse(Response<ResponseResult> response) {
                        if (!response.isSuccess()) {
                            Toast.makeText(EpisodeActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        updateFavorite(type, rating);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setFavoriteDialog(View view, EpisodeFavorite favorite) {
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating);
        final LinearLayout ratingLayout = (LinearLayout) view.findViewById(R.id.rating_layout);
        RadioButton favoriteType0 = (RadioButton) view.findViewById(R.id.favorite_type_0);
        favoriteType0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ratingBar.setRating(0);
                    ratingLayout.setVisibility(View.GONE);
                } else {
                    ratingLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.reset_rating);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setRating(0);
            }
        });
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.favorite_type);
        switch (favorite.getType()) {
            case 0:
                radioGroup.check(R.id.favorite_type_0);
                break;
            case 2:
                radioGroup.check(R.id.favorite_type_2);
                break;
            case 4:
                radioGroup.check(R.id.favorite_type_4);
                break;
        }
        ratingBar.setRating(favorite.getRating());
    }

    private void deleteFavorite() {
        Call<Token> tokenCall = service.getToken();
        tokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                Token token = response.body();
                if (!token.isAuth()) {
                    EpisodeActivity.this.startActivity(new Intent(EpisodeActivity.this, LoginActivity.class));
                    return;
                }
                Call<ResponseResult> call = service.deleteEpfav(String.valueOf(episodeId), "DELETE", token.getToken());
                call.enqueue(new Callback<ResponseResult>() {
                    @Override
                    public void onResponse(Response<ResponseResult> response) {
                        if (!response.isSuccess()) {
                            Toast.makeText(EpisodeActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        episode.setUserFavorite(null);
                        favoriteLayout.setVisibility(View.GONE);
                        addFavoriteReviewLayout.setVisibility(View.VISIBLE);
                        editFavoriteReviewLayout.setVisibility(View.GONE);
                        addFavoriteLayout.setVisibility(View.VISIBLE);
                        editFavoriteLayout.setVisibility(View.GONE);
                        deleteFavoriteLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0://添加评论
                    episodeReviewsFragment.refresh();
                    break;
                case 1://添加或修改收藏及写评
                    episodeReviewsFragment.refresh();
                    int type = data.getExtras().getInt("type");
                    float rating = data.getExtras().getFloat("rating");
                    updateFavorite(type, rating);
                    break;
            }
        }
    }

    private void openOrCloseFabMenu() {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        if (sharedPref.getInt("ID", 0) == 0) {
            EpisodeActivity.this.startActivity(new Intent(EpisodeActivity.this, LoginActivity.class));
            return;
        }
        ObjectAnimator animator = fabMenuOpened ? ObjectAnimator.ofFloat(fab, "rotation", 45F, 0F) :
                ObjectAnimator.ofFloat(fab, "rotation", 0F, 45F);
        animator.setDuration(150);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        fab_menu.setVisibility(fabMenuOpened ? View.GONE : View.VISIBLE);
        ObjectAnimator menuAnimator = fabMenuOpened ? ObjectAnimator.ofFloat(fab_menu, "alpha", 1F, 0F) :
                ObjectAnimator.ofFloat(fab_menu, "alpha", 0F, 1F);
        menuAnimator.setDuration(150);
        menuAnimator.setInterpolator(new LinearInterpolator());
        menuAnimator.start();
        fabMenuOpened = !fabMenuOpened;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_episode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
