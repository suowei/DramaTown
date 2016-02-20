package net.saoju.dramatown;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.saoju.dramatown.Adapters.NewEpisodesAdapter;
import net.saoju.dramatown.Models.NewEpisodes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewEpisodesFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NewEpisodesAdapter adapter;
    private LinearLayoutManager layoutManager;

    SaojuService service;

    private int currentPage;
    private String nextPageUrl;

    public NewEpisodesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_episodes, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SaojuService.class);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
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
        refresh();
        return view;
    }

    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        Call<NewEpisodes> newEpisodesCall = service.getNewEpidoes(null);
        newEpisodesCall.enqueue(new Callback<NewEpisodes>() {
            @Override
            public void onResponse(Response<NewEpisodes> response) {
                if (!response.isSuccess()) {
                    Toast.makeText(getContext(), "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                NewEpisodes newEpisodes = response.body();
                currentPage = newEpisodes.getCurrent_page();
                nextPageUrl = newEpisodes.getNext_page_url();
                adapter = new NewEpisodesAdapter(getActivity(), newEpisodes.getData());
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
        Call<NewEpisodes> newCall = service.getNewEpidoes(String.valueOf(currentPage + 1));
        newCall.enqueue(new Callback<NewEpisodes>() {
            @Override
            public void onResponse(Response<NewEpisodes> response) {
                if (!response.isSuccess()) {
                    Toast.makeText(getContext(), "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                NewEpisodes newEpisodes = response.body();
                currentPage = newEpisodes.getCurrent_page();
                nextPageUrl = newEpisodes.getNext_page_url();
                adapter.addAll(newEpisodes.getData());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
