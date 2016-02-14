package net.saoju.dramatown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

import me.gujun.android.taggroup.TagGroup;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DramaFavoriteReviewActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RatingBar ratingBar;
    private TagGroup tagGroup;
    private EditText titleView;
    private EditText contentView;
    private View mProgressView;
    private Button saveButton;
    private int dramaId;
    private boolean isUpdate;
    private SaojuService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_favorite_review);
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

        tagGroup = (TagGroup) findViewById(R.id.tag_group);
        TagGroup userTags = (TagGroup) findViewById(R.id.user_tags);
        userTags.setTags(getIntent().getStringArrayExtra("user_tags"));
        userTags.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                ArrayList<String> newTagList = new ArrayList<>();
                String[] tags = tagGroup.getTags();
                for (String t : tags) {
                    if (t.equals(tag)) {
                        Toast.makeText(DramaFavoriteReviewActivity.this, "标签已存在", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        newTagList.add(t);
                    }
                }
                newTagList.add(tag);
                tagGroup.setTags(newTagList);
            }
        });

        titleView = (EditText) findViewById(R.id.title);
        contentView = (EditText) findViewById(R.id.content);

        saveButton = (Button) findViewById(R.id.save_button);

        mProgressView = findViewById(R.id.progress_bar);

        dramaId = getIntent().getIntExtra("drama_id", 0);
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
            ratingBar.setRating(getIntent().getFloatExtra("rating", 0));
            String tags = getIntent().getStringExtra("tags");
            if (tags != null && !tags.isEmpty()) {
                tagGroup.setTags(tags.split(","));
            }
            Call<Review> reviewCall = service.editFavoriteReview(String.valueOf(dramaId));
            saveButton.setEnabled(false);
            reviewCall.enqueue(new Callback<Review>() {
                @Override
                public void onResponse(Response<Review> response) {
                    Review review = response.body();
                    titleView.setText(review.getTitle());
                    contentView.setText(review.getContent());
                    saveButton.setEnabled(true);
                }

                @Override
                public void onFailure(Throwable t) {
                    saveButton.setEnabled(true);
                }
            });
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFavoriteReview();
            }
        });
    }

    private void saveFavoriteReview() {
        saveButton.setEnabled(false);
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
        final float rating = ratingBar.getRating();

        titleView.setError(null);
        contentView.setError(null);

        final String title = titleView.getText().toString();
        final String content = contentView.getText().toString();

        StringBuilder stringBuilder = new StringBuilder();
        for (String tag:tagGroup.getTags()) {
            stringBuilder.append(tag);
            stringBuilder.append(',');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        final String tags =  stringBuilder.toString();

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
                    Call<ResponseResult> call;
                    if (isUpdate) {
                        call = service.updateFavoriteReview(String.valueOf(dramaId), token.getToken(),
                                type, rating, tags, title, content);
                    } else {
                        call = service.addFavoriteReview(token.getToken(), dramaId,
                                type, rating, tags, title, content);
                    }
                    call.enqueue(new Callback<ResponseResult>() {
                        @Override
                        public void onResponse(Response<ResponseResult> response) {
                            mProgressView.setVisibility(View.GONE);
                            if (!response.isSuccess()) {
                                Toast.makeText(DramaFavoriteReviewActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(DramaFavoriteReviewActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("type", type);
                            intent.putExtra("rating", rating);
                            intent.putExtra("tags", tags);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    saveButton.setEnabled(true);
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
