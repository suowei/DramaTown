package net.saoju.dramatown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.saoju.dramatown.EpisodeActivity;
import net.saoju.dramatown.Models.Episode;
import net.saoju.dramatown.R;

import java.util.List;

public class DramaEpisodesAdapter extends RecyclerView.Adapter<DramaEpisodesAdapter.ViewHolder> {

    private List<Episode> episodes;
    private Context context;

    public DramaEpisodesAdapter(Context context, List<Episode> episodes) {
        this.context = context;
        this.episodes = episodes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView release_date;

        public ViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.poster);
            title = (TextView) view.findViewById(R.id.title);
            release_date = (TextView) view.findViewById(R.id.release_date);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EpisodeActivity.class);
                    intent.putExtra("id", episodes.get(getPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public DramaEpisodesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_episode, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Episode episode = episodes.get(position);
        if (!episode.getPoster_url().isEmpty())
            Picasso.with(context).load(episode.getPoster_url()).into(holder.poster);
        holder.title.setText(context.getResources().getString(R.string.episode_title,
                episode.getTitle(), episode.getAlias()));
        holder.release_date.setText(episode.getRelease_date());
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public void reset(List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }
}
