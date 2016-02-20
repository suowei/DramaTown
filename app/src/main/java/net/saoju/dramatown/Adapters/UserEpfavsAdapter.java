package net.saoju.dramatown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import net.saoju.dramatown.EpisodeActivity;
import net.saoju.dramatown.Models.EpisodeFavorite;
import net.saoju.dramatown.R;

import java.util.List;

public class UserEpfavsAdapter extends RecyclerView.Adapter<UserEpfavsAdapter.ViewHolder> {

    private List<EpisodeFavorite> favorites;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView duration;
        RatingBar ratingBar;
        TextView cv;
        TextView updated_at;

        public ViewHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            duration = (TextView) view.findViewById(R.id.duration);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
            cv = (TextView) view.findViewById(R.id.cv);
            updated_at = (TextView) view.findViewById(R.id.updated_at);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EpisodeActivity.class);
                    intent.putExtra("id", favorites.get(getPosition()).getEpisode_id());
                    context.startActivity(intent);
                }
            });
        }
    }

    public UserEpfavsAdapter(Context context, List<EpisodeFavorite> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @Override
    public UserEpfavsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user_epfavs, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EpisodeFavorite favorite = favorites.get(position);
        holder.title.setText(context.getResources().getString(R.string.drama_episode_title_short,
                favorite.getEpisode().getDramaTitle(), favorite.getEpisode().getTitle()));
        if (favorite.getRating() != 0) {
            holder.ratingBar.setRating(favorite.getRating());
            holder.ratingBar.setVisibility(View.VISIBLE);
        } else {
            holder.ratingBar.setVisibility(View.GONE);
        }
        holder.duration.setText(context.getResources().getString(
                R.string.new_episode_duration, favorite.getEpisode().getDuration()));
        holder.cv.setText(favorite.getEpisode().getCv());
        holder.updated_at.setText(favorite.getUpdated_at());
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void addAll(List<EpisodeFavorite> favorites) {
        int position = getItemCount();
        this.favorites.addAll(favorites);
        notifyItemRangeInserted(position, favorites.size());
    }
}
