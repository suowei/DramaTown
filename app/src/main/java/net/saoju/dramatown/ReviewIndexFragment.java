package net.saoju.dramatown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.saoju.dramatown.Adapters.ReviewIndexAdapter;
import net.saoju.dramatown.Models.Reviews;
import net.saoju.dramatown.Utils.ItemDivider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewIndexFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ReviewIndexAdapter adapter;
    private LinearLayoutManager layoutManager;

    SaojuService service;
    Call<Reviews> reviewsCall;

    private int perPage = 20;

    public ReviewIndexFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_index, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDivider(getContext(), R.drawable.light_divider));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SaojuService.class);
        reviewsCall = service.getReviews(null);
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Response<Reviews> response) {
                Reviews reviews = response.body();
                perPage = reviews.getPer_page();
                adapter = new ReviewIndexAdapter(reviews.getData());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Call<Reviews> newCall = reviewsCall.clone();
                newCall.enqueue(new Callback<Reviews>() {
                    @Override
                    public void onResponse(Response<Reviews> response) {
                        Reviews reviews = response.body();
                        adapter.reset(reviews.getData());
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && adapter.getItemCount()
                        == layoutManager.findLastVisibleItemPosition() + 1) {
                    swipeRefreshLayout.setRefreshing(true);
                    Call<Reviews> newCall = service.getReviews(
                            String.valueOf(adapter.getItemCount() / perPage + 1));
                    newCall.enqueue(new Callback<Reviews>() {
                        @Override
                        public void onResponse(Response<Reviews> response) {
                            Reviews reviews = response.body();
                            adapter.addAll(reviews.getData());
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }
}
