package net.saoju.dramatown;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import net.saoju.dramatown.Adapters.SearchResultsAdapter;
import net.saoju.dramatown.Models.Drama;
import net.saoju.dramatown.Utils.ItemDivider;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchResultsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDivider(this, R.drawable.light_divider));
        adapter = new SearchResultsAdapter(SearchResultsActivity.this, Collections.EMPTY_LIST);
        recyclerView.setAdapter(adapter);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String keyword = intent.getStringExtra(SearchManager.QUERY);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SaojuService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            SaojuService service = retrofit.create(SaojuService.class);
            Call<List<Drama>> call = service.getSearchResults(keyword);
            call.enqueue(new Callback<List<Drama>>() {
                @Override
                public void onResponse(Response<List<Drama>> response) {
                    if (!response.isSuccess()) {
                        Toast.makeText(SearchResultsActivity.this, "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Drama> dramas = response.body();
                    adapter.reset(dramas);
                    if (dramas.isEmpty()) {
                        Toast.makeText(SearchResultsActivity.this, "未找到结果", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconified(false);
        searchView.clearFocus();
        return true;
    }

}
