package net.saoju.dramatown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import net.saoju.dramatown.Models.Review;
import net.saoju.dramatown.Models.Token;
import net.saoju.dramatown.Utils.AddCookiesInterceptor;
import net.saoju.dramatown.Utils.ReceivedCookiesInterceptor;
import net.saoju.dramatown.Utils.ResponseResult;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EpisodeFavoriteReviewActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RatingBar ratingBar;
    private EditText titleView;
    private EditText contentView;
    private CheckBox visibleView;
    private View mProgressView;
    private Button saveButton;
    private int dramaId;
    private int episodeId;
    private boolean isUpdate;
    private SaojuService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_favorite_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = (RadioGroup) findViewById(R.id.favorite_type);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        final LinearLayout ratingLayout = (LinearLayout) findViewById(R.id.rating_layout);
        RadioButton favoriteType0 = (RadioButton) findViewById(R.id.favorite_type_0);
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
        ImageButton imageButton = (ImageButton) findViewById(R.id.reset_rating);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setRating(0);
            }
        });

        titleView = (EditText) findViewById(R.id.title);
        contentView = (EditText) findViewById(R.id.content);
        visibleView = (CheckBox) findViewById(R.id.visible);

        saveButton = (Button) findViewById(R.id.save_button);

        mProgressView = findViewById(R.id.progress_bar);

        dramaId = getIntent().getIntExtra("drama_id", 0);
        episodeId = getIntent().getIntExtra("episode_id", 0);
        isUpdate = getIntent().getBooleanExtra("is_update", false);

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

        if (isUpdate) {
            switch (getIntent().getIntExtra("type", 2)) {
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
            ratingBar.setRating(getIntent().getFloatExtra("rating", 0));
            Call<Review> reviewCall = service.editEpfavReview(String.valueOf(episodeId));
            saveButton.setEnabled(false);
            mProgressView.setVisibility(View.VISIBLE);
            reviewCall.enqueue(new Callback<Review>() {
                @Override
                public void onResponse(Response<Review> response) {
                    Review review = response.body();
                    titleView.setText(review.getTitle());
                    contentView.setText(review.getContent());
                    visibleView.setChecked(review.getVisible() == 1);
                    saveButton.setEnabled(true);
                    mProgressView.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Throwable t) {
                    saveButton.setEnabled(true);
                    mProgressView.setVisibility(View.GONE);
                }
            });
        }

        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEpfavReview();
            }
        });
    }

    private void saveEpfavReview() {
        saveButton.setEnabled(false);
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
        final float rating = ratingBar.getRating();

        titleView.setError(null);
        contentView.setError(null);

        final String title = titleView.getText().toString();
        final String content = contentView.getText().toString();
        final int visible = visibleView.isChecked() ? 1 : 0;

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(title) && title.length() > 255) {
            titleView.setError(getString(R.string.error_invalid_title));
            focusView = titleView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            contentView.setError(getString(R.string.error_invalid_content));
            focusView = contentView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            saveButton.setEnabled(true);
        } else {
            mProgressView.setVisibility(View.VISIBLE);

            Call<Token> tokenCall = service.getToken();
            tokenCall.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Response<Token> response) {
                    Token token = response.body();
                    if (!token.isAuth()) {
                        EpisodeFavoriteReviewActivity.this.startActivity(new Intent(EpisodeFavoriteReviewActivity.this, LoginActivity.class));
                        return;
                    }
                    Call<ResponseResult> call;
                    if (isUpdate) {
                        call = service.updateEpfavReview(String.valueOf(episodeId), token.getToken(),
                                dramaId, type, rating, title, content, visible);
                    } else {
                        call = service.addEpfavReview(token.getToken(), episodeId,
                                dramaId, type, rating, title, content, visible);
                    }
                    call.enqueue(new Callback<ResponseResult>() {
                        @Override
                        public void onResponse(Response<ResponseResult> response) {
                            mProgressView.setVisibility(View.GONE);
                            if (!response.isSuccess()) {
                                Toast.makeText(EpisodeFavoriteReviewActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(EpisodeFavoriteReviewActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("type", type);
                            intent.putExtra("rating", rating);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            saveButton.setEnabled(true);
                            mProgressView.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    saveButton.setEnabled(true);
                    mProgressView.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

