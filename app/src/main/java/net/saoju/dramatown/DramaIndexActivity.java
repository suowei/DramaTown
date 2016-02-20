package net.saoju.dramatown;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import net.saoju.dramatown.Adapters.DramaIndexAdapter;
import net.saoju.dramatown.Models.Dramas;
import net.saoju.dramatown.Utils.ItemDivider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DramaIndexActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DramaIndexAdapter adapter;
    private LinearLayoutManager layoutManager;

    SaojuService service;

    private int currentPage;
    private String nextPageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drama_index);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDivider(this, R.drawable.light_divider));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SaojuService.class);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && adapter != null && adapter.getItemCount()
                        == layoutManager.findLastVisibleItemPosition() + 1) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        load();
    }

    private void load() {
        swipeRefreshLayout.setRefreshing(true);
        Call<Dramas> dramasCall = service.getDramas(null);
        dramasCall.enqueue(new Callback<Dramas>() {
            @Override
            public void onResponse(Response<Dramas> response) {
                if (!response.isSuccess()) {
                    Toast.makeText(DramaIndexActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Dramas dramas = response.body();
                currentPage = dramas.getCurrent_page();
                nextPageUrl = dramas.getNext_page_url();
                adapter = new DramaIndexAdapter(DramaIndexActivity.this, dramas.getData());
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadMore() {
        if (nextPageUrl == null || nextPageUrl.isEmpty() || swipeRefreshLayout.isRefreshing()) {
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        Call<Dramas> newCall = service.getDramas(String.valueOf(currentPage + 1));
        newCall.enqueue(new Callback<Dramas>() {
            @Override
            public void onResponse(Response<Dramas> response) {
                if (!response.isSuccess()) {
                    Toast.makeText(DramaIndexActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Dramas dramas = response.body();
                currentPage = dramas.getCurrent_page();
                nextPageUrl = dramas.getNext_page_url();
                adapter.addAll(dramas.getData());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
