package net.saoju.dramatown.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.saoju.dramatown.EpisodeActivity;
import net.saoju.dramatown.Models.NewEpisode;
import net.saoju.dramatown.R;

import java.util.List;

public class NewEpisodesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NewEpisode> newEpisodes;

    public class NormalItemHolder extends RecyclerView.ViewHolder {
        LinearLayout card;
        TextView title;
        TextView sc;
        TextView duration;
        TextView info;

        public NormalItemHolder(final View view) {
            super(view);
            card = (LinearLayout) view.findViewById(R.id.card_content);
            title = (TextView) view.findViewById(R.id.title);
            sc = (TextView) view.findViewById(R.id.sc);
            duration = (TextView) view.findViewById(R.id.duration);
            info = (TextView) view.findViewById(R.id.info);
            view.findViewById(R.id.card_content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), EpisodeActivity.class);
                    intent.putExtra("id", newEpisodes.get(getPosition()).getEpisodeId());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public class GroupItemHolder extends NormalItemHolder {
        TextView releaseDate;

        public GroupItemHolder(View view) {
            super(view);
            releaseDate = (TextView) view.findViewById(R.id.release_date);
        }
    }

    public NewEpisodesAdapter(List<NewEpisode> newEpisodes) {
        this.newEpisodes = newEpisodes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new NormalItemHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_no_title, parent, false));
        } else {
            return new GroupItemHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_with_title, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewEpisode newEpisode = newEpisodes.get(position);
        if (holder instanceof GroupItemHolder) {
            GroupItemHolder groupItemHolder = (GroupItemHolder) holder;
            groupItemHolder.releaseDate.setText(newEpisode.getReleaseDateString());
            setData(groupItemHolder, newEpisode);
        } else {
            NormalItemHolder normalItemHolder = (NormalItemHolder) holder;
            setData(normalItemHolder, newEpisode);
        }
    }

    public void setData(NormalItemHolder holder, NewEpisode newEpisode) {
        holder.title.setText(holder.title.getResources().getString(R.string.new_episode_title,
                newEpisode.getEraString(), newEpisode.getTypeString(),
                newEpisode.getDramaTitle(), newEpisode.getEpisodeTitle(), newEpisode.getAlias()));
        holder.duration.setText(holder.duration.getResources()
                .getString(R.string.new_episode_duration, newEpisode.getDuration()));
        holder.sc.setText(newEpisode.getSc());
        if (newEpisode.isOriginal()) {
            holder.info.setText(newEpisode.getOriginalString());
        } else {
            holder.info.setText("");
        }
        if (newEpisode.getState() == 1) {
            holder.title.getPaint().setFakeBoldText(true);
            holder.title.setTextColor(holder.title.getResources().getColor(R.color.bsTextInfo));
            holder.sc.setTextColor(holder.title.getResources().getColor(R.color.bsTextInfo));
            holder.card.setBackgroundColor(holder.title.getResources().getColor(R.color.bsColorInfo));

        } else {
            holder.title.getPaint().setFakeBoldText(false);
            holder.title.setTextColor(holder.title.getResources().getColor(R.color.textPrimary));
            holder.sc.setTextColor(holder.title.getResources().getColor(R.color.textPrimary));
            holder.card.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return newEpisodes.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || !newEpisodes.get(position - 1).getReleaseDate()
                .equals(newEpisodes.get(position).getReleaseDate())) {
            return 1;
        } else {
            return 0;
        }
    }

    public void addAll(List<NewEpisode> newEpisodes) {
        int position = getItemCount();
        this.newEpisodes.addAll(newEpisodes);
        notifyItemRangeInserted(position, newEpisodes.size());
    }
}
