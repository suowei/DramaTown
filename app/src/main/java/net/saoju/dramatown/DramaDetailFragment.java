package net.saoju.dramatown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.saoju.dramatown.Adapters.DramaDetailAdapter;
import net.saoju.dramatown.Models.Drama;

public class DramaDetailFragment extends Fragment {

    private RecyclerView recyclerView;

    public DramaDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drama_detail, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? manager.getSpanCount() : 1;
            }
        });
        return view;
    }

    public void updateData(final Drama drama) {
        recyclerView.setAdapter(new DramaDetailAdapter(drama));
    }
}
