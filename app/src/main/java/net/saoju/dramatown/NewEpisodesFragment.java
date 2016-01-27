package net.saoju.dramatown;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    Call<NewEpisodes> newEpisodesCall;

    private int perPage = 20;

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
        newEpisodesCall = service.getNewEpidoes(null);
        newEpisodesCall.enqueue(new Callback<NewEpisodes>() {
            @Override
            public void onResponse(Response<NewEpisodes> response) {
                NewEpisodes newEpisodes = response.body();
                perPage = newEpisodes.getPer_page();
                adapter = new NewEpisodesAdapter(newEpisodes.getData());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Call<NewEpisodes> newCall = newEpisodesCall.clone();
                newCall.enqueue(new Callback<NewEpisodes>() {
                    @Override
                    public void onResponse(Response<NewEpisodes> response) {
                        NewEpisodes newEpisodes = response.body();
                        adapter.reset(newEpisodes.getData());
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
                    Call<NewEpisodes> newCall = service.getNewEpidoes(
                            String.valueOf(adapter.getItemCount() / perPage + 1));
                    newCall.enqueue(new Callback<NewEpisodes>() {
                        @Override
                        public void onResponse(Response<NewEpisodes> response) {
                            NewEpisodes newEpisodes = response.body();
                            adapter.addAll(newEpisodes.getData());
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
