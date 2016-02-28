package net.saoju.dramatown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.saoju.dramatown.Adapters.DramaEpisodesAdapter;
import net.saoju.dramatown.Models.Episode;

import java.util.Collections;
import java.util.List;

public class DramaEpisodesFragment extends Fragment {

    private DramaEpisodesAdapter adapter;

    public DramaEpisodesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drama_episodes, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);
        adapter = new DramaEpisodesAdapter(getActivity(), Collections.EMPTY_LIST);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void setData(List<Episode> episodes) {
        adapter.reset(episodes);
    }
}
