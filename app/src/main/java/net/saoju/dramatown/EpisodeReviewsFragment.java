package net.saoju.dramatown;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.saoju.dramatown.Adapters.EpisodeReviewsAdapter;
import net.saoju.dramatown.Models.Reviews;
import net.saoju.dramatown.Utils.ItemDivider;
import net.saoju.dramatown.Utils.LazyFragment;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EpisodeReviewsFragment extends LazyFragment {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private EpisodeReviewsAdapter adapter;
    private LinearLayoutManager layoutManager;

    private int currentPage;
    private String nextPageUrl;

    private int episode;

    private SaojuService service;

    public EpisodeReviewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_index, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDivider(getContext(), R.drawable.light_divider));
        adapter = new EpisodeReviewsAdapter(getActivity(), Collections.EMPTY_LIST);
        recyclerView.setAdapter(adapter);
        Bundle bundle = getArguments();
        episode = bundle.getInt("id");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && adapter != null
                        && adapter.getItemCount() == layoutManager.findLastVisibleItemPosition() + 1) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        isPrepared = true;
        return view;
    }

    @Override
    protected void load() {
        if (!getUserVisibleHint()) {
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SaojuService.class);
        Call<Reviews> reviewsCall = service.getEpisodeReveiws(String.valueOf(episode), null);
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Response<Reviews> response) {
                if (!response.isSuccess()) {
                    Toast.makeText(getContext(), "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Reviews reviews = response.body();
                currentPage = reviews.getCurrent_page();
                nextPageUrl = reviews.getNext_page_url();
                adapter.reset(reviews.getData());
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
        Call<Reviews> newCall = service.getEpisodeReveiws(
                String.valueOf(episode), String.valueOf(currentPage + 1));
        newCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Response<Reviews> response) {
                Reviews reviews = response.body();
                currentPage = reviews.getCurrent_page();
                nextPageUrl = reviews.getNext_page_url();
                adapter.addAll(reviews.getData());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
