package net.saoju.dramatown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class WriteReviewActivity extends AppCompatActivity {

    private EditText titleView;
    private EditText contentView;
    private View mProgressView;
    private Button saveButton;
    private int dramaId;
    private int episodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleView = (EditText) findViewById(R.id.title);
        contentView = (EditText) findViewById(R.id.content);

        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReview(false);
            }
        });

        mProgressView = findViewById(R.id.progress_bar);

        dramaId = getIntent().getIntExtra("drama_id", 0);
        episodeId = getIntent().getIntExtra("episode_id", 0);
    }

    private void saveReview(final boolean isUpdate) {
        titleView.setError(null);
        contentView.setError(null);

        final String title = titleView.getText().toString();
        final String content = contentView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(title) && title.length() > 255) {
            titleView.setError(getString(R.string.error_invalid_title));
            focusView = titleView;
            cancel = true;
        }

        if (TextUtils.isEmpty(content)) {
            contentView.setError(getString(R.string.error_invalid_content));
            focusView = contentView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            saveButton.setEnabled(true);
        } else {
            mProgressView.setVisibility(View.VISIBLE);
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
                    Call<ResponseResult> call;
                    if (isUpdate) {
                        // TODO: 2016/2/7 替换为修改评论的请求
                        call = service.addReview(token.getToken(),
                                dramaId, episodeId == 0 ? null : String.valueOf(episodeId), title, content);
                    } else {
                        call = service.addReview(token.getToken(),
                                dramaId, episodeId == 0 ? null : String.valueOf(episodeId), title, content);
                    }
                    call.enqueue(new Callback<ResponseResult>() {
                        @Override
                        public void onResponse(Response<ResponseResult> response) {
                            mProgressView.setVisibility(View.GONE);
                            if (!response.isSuccess()) {
                                Toast.makeText(WriteReviewActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(WriteReviewActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
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

