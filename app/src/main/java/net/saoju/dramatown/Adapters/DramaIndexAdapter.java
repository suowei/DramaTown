package net.saoju.dramatown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.saoju.dramatown.DramaActivity;
import net.saoju.dramatown.Models.Drama;
import net.saoju.dramatown.R;

import java.util.List;

public class DramaIndexAdapter extends RecyclerView.Adapter<DramaIndexAdapter.ViewHolder> {
    private List<Drama> dramas;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView cv;
        TextView info;

        public ViewHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            cv = (TextView) view.findViewById(R.id.cv);
            info = (TextView) view.findViewById(R.id.info);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DramaActivity.class);
                    intent.putExtra("id", dramas.get(getPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    public DramaIndexAdapter(Context context, List<Drama> dramas) {
        this.context = context;
        this.dramas = dramas;
    }

    @Override
    public DramaIndexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_drama, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drama drama = dramas.get(position);
        holder.title.setText(drama.getTitle());
        holder.cv.setText(drama.getSc());
        holder.info.setText(drama.getInfo());
    }

    @Override
    public int getItemCount() {
        return dramas.size();
    }

    public void addAll(List<Drama> dramas) {
        int position = getItemCount();
        this.dramas.addAll(dramas);
        notifyItemRangeInserted(position, dramas.size());
    }
}
