package net.saoju.dramatown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.saoju.dramatown.Adapters.DramaReviewsAdapter;
import net.saoju.dramatown.Models.Reviews;
import net.saoju.dramatown.Utils.ItemDivider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DramaReviewsFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DramaReviewsAdapter adapter;
    private LinearLayoutManager layoutManager;

    SaojuService service;

    private int perPage;

    private int drama;

    public DramaReviewsFragment() {
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
        swipeRefreshLayout.setEnabled(false);
        Bundle bundle = getArguments();
        drama = bundle.getInt("id");
        return view;
    }

    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SaojuService.class);
        Call<Reviews> reviewsCall = service.getDramaReveiws(String.valueOf(drama), null);
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Response<Reviews> response) {
                Reviews reviews = response.body();
                perPage = reviews.getPer_page();
                adapter = new DramaReviewsAdapter(reviews.getData());
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && adapter.getItemCount() == layoutManager.findLastVisibleItemPosition() + 1
                                && adapter.getItemCount() >= perPage) {
                            swipeRefreshLayout.setRefreshing(true);
                            Call<Reviews> newCall = service.getDramaReveiws(String.valueOf(drama),
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
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
