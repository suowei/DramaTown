package net.saoju.dramatown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.saoju.dramatown.Models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserActivity extends AppCompatActivity {

    private int userId;
    private User user;

    private TextView name;
    private TextView created_at;
    private TextView introduction;
    private TextView epfav0;
    private TextView epfav2;
    private TextView epfav4;
    private TextView favorite0;
    private TextView favorite1;
    private TextView favorite2;
    private TextView favorite3;
    private TextView favorite4;
    private TextView review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        name = (TextView) findViewById(R.id.name);
        created_at = (TextView) findViewById(R.id.created_at);
        introduction = (TextView) findViewById(R.id.introduction);
        epfav0 = (TextView) findViewById(R.id.epfav_0);
        epfav2 = (TextView) findViewById(R.id.epfav_2);
        epfav4 = (TextView) findViewById(R.id.epfav_4);
        favorite0 = (TextView) findViewById(R.id.favorite_0);
        favorite1 = (TextView) findViewById(R.id.favorite_1);
        favorite2 = (TextView) findViewById(R.id.favorite_2);
        favorite3 = (TextView) findViewById(R.id.favorite_3);
        favorite4 = (TextView) findViewById(R.id.favorite_4);
        review = (TextView) findViewById(R.id.review);

        userId = getIntent().getIntExtra("id", 0);

        LinearLayout epfav0Layout = (LinearLayout) findViewById(R.id.epfav_0_layout);
        epfav0Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserEpfavsActivity.class);
                intent.putExtra("user", userId);
                intent.putExtra("type", 0);
                UserActivity.this.startActivity(intent);
            }
        });
        LinearLayout epfav2Layout = (LinearLayout) findViewById(R.id.epfav_2_layout);
        epfav2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserEpfavsActivity.class);
                intent.putExtra("user", userId);
                intent.putExtra("type", 2);
                UserActivity.this.startActivity(intent);
            }
        });
        LinearLayout epfav4Layout = (LinearLayout) findViewById(R.id.epfav_4_layout);
        epfav4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserEpfavsActivity.class);
                intent.putExtra("user", userId);
                intent.putExtra("type", 4);
                UserActivity.this.startActivity(intent);
            }
        });
        LinearLayout favorite0Layout = (LinearLayout) findViewById(R.id.favorite_0_layout);
        favorite0Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserFavoritesActivity.class);
                intent.putExtra("user", userId);
                intent.putExtra("type", 0);
                UserActivity.this.startActivity(intent);
            }
        });
        LinearLayout favorite1Layout = (LinearLayout) findViewById(R.id.favorite_1_layout);
        favorite1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserFavoritesActivity.class);
                intent.putExtra("user", userId);
                intent.putExtra("type", 1);
                UserActivity.this.startActivity(intent);
            }
        });
        LinearLayout favorite2Layout = (LinearLayout) findViewById(R.id.favorite_2_layout);
        favorite2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserFavoritesActivity.class);
                intent.putExtra("user", userId);
                intent.putExtra("type", 2);
                UserActivity.this.startActivity(intent);
            }
        });
        LinearLayout favorite3Layout = (LinearLayout) findViewById(R.id.favorite_3_layout);
        favorite3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserFavoritesActivity.class);
                intent.putExtra("user", userId);
                intent.putExtra("type", 3);
                UserActivity.this.startActivity(intent);
            }
        });
        LinearLayout favorite4Layout = (LinearLayout) findViewById(R.id.favorite_4_layout);
        favorite4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserFavoritesActivity.class);
                intent.putExtra("user", userId);
                intent.putExtra("type", 4);
                UserActivity.this.startActivity(intent);
            }
        });
        LinearLayout reviewLayout = (LinearLayout) findViewById(R.id.review_layout);
        reviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserReviewsActivity.class);
                intent.putExtra("user", userId);
                UserActivity.this.startActivity(intent);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SaojuService service = retrofit.create(SaojuService.class);
        Call<User> call = service.getUser(String.valueOf(userId));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                if (!response.isSuccess()) {
                    Toast.makeText(UserActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                setData(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setData(User data) {
        user = data;
        name.setText(user.getName());
        created_at.setText(getResources().getString(R.string.user_created_at, user.getCreated_at()));
        introduction.setText(getResources().getString(R.string.user_introduction, user.getIntroduction()));
        epfav0.setText(getResources().getString(R.string.user_epfav_0, user.getEpfav0()));
        epfav2.setText(getResources().getString(R.string.user_epfav_2, user.getEpfav2()));
        epfav4.setText(getResources().getString(R.string.user_epfav_4, user.getEpfav4()));
        favorite0.setText(getResources().getString(R.string.user_favorite_0, user.getFavorite0()));
        favorite1.setText(getResources().getString(R.string.user_favorite_1, user.getFavorite1()));
        favorite2.setText(getResources().getString(R.string.user_favorite_2, user.getFavorite2()));
        favorite3.setText(getResources().getString(R.string.user_favorite_3, user.getFavorite3()));
        favorite4.setText(getResources().getString(R.string.user_favorite_4, user.getFavorite4()));
        review.setText(getResources().getString(R.string.user_drama_review, user.getReviews()));
    }
}
