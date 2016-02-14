package net.saoju.dramatown;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import net.saoju.dramatown.Adapters.SectionsPagerAdapter;
import net.saoju.dramatown.Models.Drama;
import net.saoju.dramatown.Models.Favorite;
import net.saoju.dramatown.Models.Tag;
import net.saoju.dramatown.Models.Tagmap;
import net.saoju.dramatown.Models.Token;
import net.saoju.dramatown.Utils.AddCookiesInterceptor;
import net.saoju.dramatown.Utils.ReceivedCookiesInterceptor;
import net.saoju.dramatown.Utils.ResponseResult;

import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DramaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private TextView alias;
    private TextView type;
    private TextView era;
    private TextView genre;
    private TextView origianl;
    private TextView count;
    private TextView state;
    private TextView sc;
    private TextView introduction;
    private TextView commtags;
    private LinearLayout favoriteLayout;
    private LinearLayout favoriteTagsLayout;
    private TextView favoriteType;
    private RatingBar ratingBar;
    private TagGroup tagGroup;
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

    private DramaEpisodesFragment dramaEpisodesFragment;
    private DramaReviewsFragment dramaReviewsFragment;

    private SaojuService service;

    private int dramaId;
    private Drama drama;

    private boolean fabMenuOpened;

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

        alias = (TextView) findViewById(R.id.alias);
        type = (TextView) findViewById(R.id.type);
        era = (TextView) findViewById(R.id.era);
        genre = (TextView) findViewById(R.id.genre);
        origianl = (TextView) findViewById(R.id.original);
        count = (TextView) findViewById(R.id.count);
        state = (TextView) findViewById(R.id.state);
        sc = (TextView) findViewById(R.id.sc);
        introduction = (TextView) findViewById(R.id.introduction);
        commtags = (TextView) findViewById(R.id.commtags);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        favoriteLayout = (LinearLayout) findViewById(R.id.favorite_layout);
        favoriteTagsLayout = (LinearLayout) findViewById(R.id.favorite_tags);
        favoriteType = (TextView) findViewById(R.id.favorite_type);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        tagGroup = (TagGroup) findViewById(R.id.tag_group);
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

        dramaId = getIntent().getIntExtra("id", 0);

        dramaEpisodesFragment = new DramaEpisodesFragment();
        dramaReviewsFragment = new DramaReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", dramaId);
        dramaReviewsFragment.setArguments(bundle);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(dramaEpisodesFragment);
        fragments.add(dramaReviewsFragment);

        List<String> titles = new ArrayList<>();
        titles.add("分集");
        titles.add("评论");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments, titles);

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
        Call<Drama> dramaCall = service.getDrama(String.valueOf(dramaId));
        dramaCall.enqueue(new Callback<Drama>() {
            @Override
            public void onResponse(Response<Drama> response) {
                if (!response.isSuccess()) {
                    Toast.makeText(DramaActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                setData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setData(Drama data) {
        drama = data;
        toolbar.setTitle(drama.getTitle());
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryLight));
        SpannableString spanString = new SpannableString(getResources().getString(R.string.drama_alias, drama.getAlias()));
        spanString.setSpan(span, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        alias.setText(spanString);
        spanString = new SpannableString(getResources().getString(R.string.drama_type, drama.getTypeString()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        type.setText(spanString);
        spanString = new SpannableString(getResources().getString(R.string.drama_era, drama.getEraString()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        era.setText(spanString);
        spanString = new SpannableString(getResources().getString(R.string.drama_genre, drama.getGenre()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        genre.setText(spanString);
        spanString = new SpannableString(getResources().getString(R.string.drama_original, drama.getOriginalString()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        origianl.setText(spanString);
        spanString = new SpannableString(getResources().getString(R.string.drama_count, drama.getCount()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        count.setText(spanString);
        spanString = new SpannableString(getResources().getString(R.string.drama_state, drama.getStateString()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        state.setText(spanString);
        spanString = new SpannableString(getResources().getString(R.string.drama_sc, drama.getSc()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sc.setText(spanString);
        if (drama.getIntroduction().isEmpty()) {
            introduction.setVisibility(View.GONE);
        } else {
            introduction.setText(drama.getIntroduction());
        }
        spanString = new SpannableString(getResources().getString(R.string.drama_commtags, drama.getCommtagsString()));
        spanString.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        commtags.setText(spanString);

        if (drama.getUserFavorite() != null) {
            updateFavorite(drama.getUserFavorite().getType(),
                    drama.getUserFavorite().getRating(), drama.getUserFavorite().getTags());
        } else {
            favoriteLayout.setVisibility(View.GONE);
            favoriteTagsLayout.setVisibility(View.GONE);
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
                Intent intent = new Intent(DramaActivity.this, DramaFavoriteReviewActivity.class);
                intent.putExtra("drama_id", dramaId);
                intent.putExtra("is_update", false);
                intent.putExtra("user_tags", getUserTags());
                DramaActivity.this.startActivityForResult(intent, 1);
            }
        });
        editFavoriteReviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseFabMenu();
                Intent intent = new Intent(DramaActivity.this, DramaFavoriteReviewActivity.class);
                intent.putExtra("drama_id", dramaId);
                intent.putExtra("is_update", true);
                intent.putExtra("type", drama.getUserFavorite().getType());
                intent.putExtra("rating", drama.getUserFavorite().getRating());
                intent.putExtra("tags", drama.getUserFavorite().getTags());
                intent.putExtra("user_tags", getUserTags());
                DramaActivity.this.startActivityForResult(intent, 1);
            }
        });
        addFavoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseFabMenu();
                final View dialogView = LayoutInflater.from(DramaActivity.this).inflate(R.layout.dialog_favorite, null);
                setFavoriteDialog(dialogView, new Favorite(2, 0, null));
                new AlertDialog.Builder(DramaActivity.this)
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
                final View dialogView = LayoutInflater.from(DramaActivity.this).inflate(R.layout.dialog_favorite, null);
                setFavoriteDialog(dialogView, drama.getUserFavorite());
                new AlertDialog.Builder(DramaActivity.this)
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
                new AlertDialog.Builder(DramaActivity.this)
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
                Intent intent = new Intent(DramaActivity.this, WriteReviewActivity.class);
                intent.putExtra("drama_id", dramaId);
                DramaActivity.this.startActivityForResult(intent, 0);
            }
        });

        if (drama.getReviews() > 0) {
            tabLayout.getTabAt(1).setText("评论（" + drama.getReviews() + "）");
        }
        dramaEpisodesFragment.setData(drama.getEpisodes());
        dramaReviewsFragment.refresh();
    }

    private void updateFavorite(int type, float rating, String tags) {
        if (drama.getUserFavorite() == null) {
            drama.setUserFavorite(new Favorite(type, rating, tags));
        } else {
            drama.getUserFavorite().setType(type);
            drama.getUserFavorite().setRating(rating);
            drama.getUserFavorite().setTags(tags);
        }
        favoriteLayout.setVisibility(View.VISIBLE);
        addFavoriteReviewLayout.setVisibility(View.GONE);
        editFavoriteReviewLayout.setVisibility(View.VISIBLE);
        addFavoriteLayout.setVisibility(View.GONE);
        editFavoriteLayout.setVisibility(View.VISIBLE);
        deleteFavoriteLayout.setVisibility(View.VISIBLE);
        favoriteType.setText(drama.getUserFavorite().getTypeString());
        if (drama.getUserFavorite().getRating() != 0) {
            ratingBar.setRating(drama.getUserFavorite().getRating());
            ratingBar.setVisibility(View.VISIBLE);
        } else {
            ratingBar.setVisibility(View.GONE);
        }
        if (drama.getUserFavorite().getTags() != null && !drama.getUserFavorite().getTags().isEmpty()) {
            String[] tagList = drama.getUserFavorite().getTags().split(",");
            tagGroup.setTags(tagList);
            favoriteTagsLayout.setVisibility(View.VISIBLE);
        } else {
            favoriteTagsLayout.setVisibility(View.GONE);
        }
    }

    private void setFavoriteDialog(View view, Favorite favorite) {
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
            case 1:
                radioGroup.check(R.id.favorite_type_1);
                break;
            case 2:
                radioGroup.check(R.id.favorite_type_2);
                break;
            case 3:
                radioGroup.check(R.id.favorite_type_3);
                break;
            case 4:
                radioGroup.check(R.id.favorite_type_4);
                break;
        }
        ratingBar.setRating(favorite.getRating());
        final TagGroup tagGroup = (TagGroup) view.findViewById(R.id.tag_group);
        if (favorite.getTags() != null && !favorite.getTags().isEmpty()) {
            tagGroup.setTags(favorite.getTags().split(","));
        }
        TagGroup userTagGroup = (TagGroup) view.findViewById(R.id.user_tags);
        userTagGroup.setTags(getUserTags());
        userTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                ArrayList<String> newTagList = new ArrayList<>();
                String[] tags = tagGroup.getTags();
                for (String t:tags) {
                    if (t.equals(tag)) {
                        Toast.makeText(DramaActivity.this, "标签已存在", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        newTagList.add(t);
                    }
                }
                newTagList.add(tag);
                tagGroup.setTags(newTagList);
            }
        });
    }

    private String[] getUserTags() {
        ArrayList<String> userTags = new ArrayList<>();
        for (Tagmap tag:drama.getUserTags()) {
            userTags.add(tag.getTag().getName());
        }
        return userTags.toArray(new String[userTags.size()]);
    }

    private void saveFavorite(View view, final boolean isUpdate) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.favorite_type);
        final int type;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.favorite_type_0:
                type = 0;
                break;
            case R.id.favorite_type_1:
                type = 1;
                break;
            case R.id.favorite_type_2:
                type = 2;
                break;
            case R.id.favorite_type_3:
                type = 3;
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
        TagGroup tagGroup = (TagGroup) view.findViewById(R.id.tag_group);
        StringBuilder stringBuilder = new StringBuilder();
        for (String tag:tagGroup.getTags()) {
            stringBuilder.append(tag);
            stringBuilder.append(',');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        final String tags =  stringBuilder.toString();
        Call<Token> tokenCall = service.getToken();
        tokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                Token token = response.body();
                Call<ResponseResult> call;
                if (isUpdate) {
                    call = service.editFavorite(String.valueOf(drama.getUserFavorite().getId()),
                            token.getToken(), type, rating, tags);
                } else {
                    call = service.addFavorite(token.getToken(), dramaId, type, rating, tags);
                }
                call.enqueue(new Callback<ResponseResult>() {
                    @Override
                    public void onResponse(Response<ResponseResult> response) {
                        if (!response.isSuccess()) {
                            Toast.makeText(DramaActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        updateFavorite(type, rating, tags);
                        Toast.makeText(DramaActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
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

    private void deleteFavorite() {
        Call<Token> tokenCall = service.getToken();
        tokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                Token token = response.body();
                Call<ResponseResult> call = service.deleteFavorite(String.valueOf(drama.getUserFavorite().getId()),
                        "DELETE", token.getToken());
                call.enqueue(new Callback<ResponseResult>() {
                    @Override
                    public void onResponse(Response<ResponseResult> response) {
                        if (!response.isSuccess()) {
                            Toast.makeText(DramaActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        drama.setUserFavorite(null);
                        favoriteLayout.setVisibility(View.GONE);
                        favoriteTagsLayout.setVisibility(View.GONE);
                        addFavoriteReviewLayout.setVisibility(View.VISIBLE);
                        editFavoriteReviewLayout.setVisibility(View.GONE);
                        addFavoriteLayout.setVisibility(View.VISIBLE);
                        editFavoriteLayout.setVisibility(View.GONE);
                        deleteFavoriteLayout.setVisibility(View.GONE);
                        Toast.makeText(DramaActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
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
                    dramaReviewsFragment.refresh();
                    break;
                case 1://添加或修改收藏及写评
                    dramaReviewsFragment.refresh();
                    int type = data.getExtras().getInt("type");
                    float rating = data.getExtras().getFloat("rating");
                    String tags = data.getExtras().getString("tags");
                    updateFavorite(type, rating, tags);
                    break;
            }
        }
    }

    private void openOrCloseFabMenu() {
        ObjectAnimator animator = fabMenuOpened ? ObjectAnimator.ofFloat(fab, "rotation", 45F, 0F) :
                ObjectAnimator.ofFloat(fab, "rotation", 0F, 45F);
        animator.setDuration(50);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        fab_menu.setVisibility(fabMenuOpened ? View.GONE : View.VISIBLE);
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
