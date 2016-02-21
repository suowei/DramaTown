package net.saoju.dramatown;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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

public class ReviewActivity extends AppCompatActivity {

    private Review review;

    private TextView title;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        review = getIntent().getParcelableExtra("review");

        TextView info = (TextView) findViewById(R.id.info);
        TextView created_at = (TextView) findViewById(R.id.created_at);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);

        if (review.getDrama() == null) {
            if (review.getEpisode() == null) {
                info.setText(review.getUser().getName());
            } else {
                info.setText(getResources().getString(R.string.drama_review_info,
                        review.getUser().getName(),
                        review.getEpisode_id() == 0 ? "" : review.getEpisode().getTitle()));
            }
        } else if (review.getUser() == null) {
            info.setText(getResources().getString(R.string.drama_episode_title_short,
                    review.getDrama().getTitle(), review.getEpisode() != null ? review.getEpisode().getTitle() : ""));
        } else {
            info.setText(getResources().getString(R.string.review_info,
                    review.getUser().getName(), review.getDrama().getTitle(),
                    review.getEpisode() != null ? review.getEpisode().getTitle() : ""));
        }
        created_at.setText(review.getCreated_at());
        title.setText(review.getTitle());
        if (review.getTitle().isEmpty()) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
        }
        content.setText(review.getContent());
    }

    private void deleteReview() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor(this))
                .addInterceptor(new ReceivedCookiesInterceptor(this))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        final SaojuService service = retrofit.create(SaojuService.class);
        Call<Token> tokenCall = service.getToken();
        tokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                Token token = response.body();
                if (!token.isAuth()) {
                    ReviewActivity.this.startActivity(new Intent(ReviewActivity.this, LoginActivity.class));
                    return;
                }
                Call<ResponseResult> call = service.deleteReview(
                        String.valueOf(review.getId()), "DELETE", token.getToken());
                call.enqueue(new Callback<ResponseResult>() {
                    @Override
                    public void onResponse(Response<ResponseResult> response) {
                        if (!response.isSuccess()) {
                            Toast.makeText(ReviewActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(ReviewActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (review.getUser_id() == getSharedPreferences("userInfo", MODE_PRIVATE).getInt("ID", 0)) {
            getMenuInflater().inflate(R.menu.review, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, WriteReviewActivity.class);
            intent.putExtra("id", review.getId());
            intent.putExtra("title", review.getTitle());
            intent.putExtra("content", review.getContent());
            intent.putExtra("visible", review.getVisible());
            intent.putExtra("is_update", true);
            startActivityForResult(intent, 0);
        } else if (id == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("删除评论")
                    .setMessage("确定要删除吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteReview();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            review.setTitle(data.getExtras().getString("title"));
            review.setContent(data.getExtras().getString("content"));
            review.setVisible(data.getExtras().getInt("visible"));
            if (review.getTitle().isEmpty()) {
                title.setVisibility(View.GONE);
            } else {
                title.setText(review.getTitle());
                title.setVisibility(View.VISIBLE);
            }
            content.setText(review.getContent());
        }
    }
}
