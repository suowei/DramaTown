package net.saoju.dramatown;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.saoju.dramatown.Adapters.UserFavoritesAdapter;
import net.saoju.dramatown.Models.Favorites;
import net.saoju.dramatown.Utils.ItemDivider;
import net.saoju.dramatown.Utils.LazyFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserFavoritesFragment extends LazyFragment {

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private UserFavoritesAdapter adapter;
    private LinearLayoutManager layoutManager;

    SaojuService service;

    private int currentPage;
    private String nextPageUrl;

    private int user;
    private int type;

    public UserFavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_favorites, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDivider(getContext(), R.drawable.light_divider));
        swipeRefreshLayout.setEnabled(false);
        Bundle bundle = getArguments();
        user = bundle.getInt("user");
        type = bundle.getInt("type");
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
        load();
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
        Call<Favorites> call = service.getUserFavorites(String.valueOf(user), String.valueOf(type), null);
        call.enqueue(new Callback<Favorites>() {
            @Override
            public void onResponse(Response<Favorites> response) {
                Favorites favorites = response.body();
                currentPage = favorites.getCurrent_page();
                nextPageUrl = favorites.getNext_page_url();
                adapter = new UserFavoritesAdapter(getActivity(), favorites.getData());
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                hasLoadedOnce = true;
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
        Call<Favorites> newCall = service.getUserFavorites(
                String.valueOf(user), String.valueOf(type), String.valueOf(currentPage + 1));
        newCall.enqueue(new Callback<Favorites>() {
            @Override
            public void onResponse(Response<Favorites> response) {
                Favorites favorites = response.body();
                currentPage = favorites.getCurrent_page();
                nextPageUrl = favorites.getNext_page_url();
                adapter.addAll(favorites.getData());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
