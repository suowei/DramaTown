package net.saoju.dramatown.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.saoju.dramatown.EpisodeActivity;
import net.saoju.dramatown.Models.Drama;
import net.saoju.dramatown.Models.Episode;
import net.saoju.dramatown.R;

public class DramaDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Drama drama;

    public DramaDetailAdapter(Drama drama) {
        this.drama = drama;
    }

    public class EpisodeViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView release_date;

        public EpisodeViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.poster);
            title = (TextView) view.findViewById(R.id.title);
            release_date = (TextView) view.findViewById(R.id.release_date);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EpisodeActivity.class);
                    intent.putExtra("id", drama.getEpisodes().get(getPosition() - 1).getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public class DramaViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView alias;
        private TextView type;
        private TextView era;
        private TextView genre;
        private TextView origianl;
        private TextView count;
        private TextView state;
        private TextView sc;
        private TextView introduction;
        private TextView commtags;

        public DramaViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            alias = (TextView) view.findViewById(R.id.alias);
            type = (TextView) view.findViewById(R.id.type);
            era = (TextView) view.findViewById(R.id.era);
            genre = (TextView) view.findViewById(R.id.genre);
            origianl = (TextView) view.findViewById(R.id.original);
            count = (TextView) view.findViewById(R.id.count);
            state = (TextView) view.findViewById(R.id.state);
            sc = (TextView) view.findViewById(R.id.sc);
            introduction = (TextView) view.findViewById(R.id.introduction);
            commtags = (TextView) view.findViewById(R.id.commtags);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new DramaViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drama_detail_header, parent, false));
        } else {
            return new EpisodeViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.episode_grid_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DramaViewHolder) {
            DramaViewHolder dramaViewHolder = (DramaViewHolder) holder;
            dramaViewHolder.title.setText(drama.getTitle());
            ForegroundColorSpan span = new ForegroundColorSpan(
                    dramaViewHolder.title.getResources().getColor(R.color.textSecondary));
            SpannableString spanString = new SpannableString(dramaViewHolder.alias.getResources()
                    .getString(R.string.drama_alias, drama.getAlias()));
            spanString.setSpan(span, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dramaViewHolder.alias.setText(spanString);
            spanString = new SpannableString(dramaViewHolder.type.getResources()
                    .getString(R.string.drama_type, drama.getTypeString()));
            spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dramaViewHolder.type.setText(spanString);
            spanString = new SpannableString(dramaViewHolder.era.getResources()
                    .getString(R.string.drama_era, drama.getEraString()));
            spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dramaViewHolder.era.setText(spanString);
            spanString = new SpannableString(dramaViewHolder.genre.getResources()
                    .getString(R.string.drama_genre, drama.getGenre()));
            spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dramaViewHolder.genre.setText(spanString);
            spanString = new SpannableString(dramaViewHolder.origianl.getResources()
                    .getString(R.string.drama_original, drama.getOriginalString()));
            spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dramaViewHolder.origianl.setText(spanString);
            spanString = new SpannableString(dramaViewHolder.count.getResources()
                    .getString(R.string.drama_count, drama.getCount()));
            spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dramaViewHolder.count.setText(spanString);
            spanString = new SpannableString(dramaViewHolder.state.getResources()
                    .getString(R.string.drama_state, drama.getStateString()));
            spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dramaViewHolder.state.setText(spanString);
            spanString = new SpannableString(dramaViewHolder.sc.getResources()
                    .getString(R.string.drama_sc, drama.getSc()));
            spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dramaViewHolder.sc.setText(spanString);
            if (drama.getIntroduction().isEmpty()) {
                dramaViewHolder.introduction.setVisibility(View.GONE);
            } else {
                dramaViewHolder.introduction.setText(drama.getIntroduction());
            }
            spanString = new SpannableString(dramaViewHolder.commtags.getResources()
                    .getString(R.string.drama_commtags, drama.getCommtagsString()));
            spanString.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dramaViewHolder.commtags.setText(spanString);
        } else {
            EpisodeViewHolder episodeViewHolder = (EpisodeViewHolder) holder;
            Episode episode = drama.getEpisodes().get(position - 1);
            if (!episode.getPoster_url().isEmpty())
                Picasso.with(episodeViewHolder.poster.getContext()).load(episode.getPoster_url()).into(episodeViewHolder.poster);
            episodeViewHolder.title.setText(episodeViewHolder.title.getResources().getString(R.string.episode_title,
                    episode.getTitle(), episode.getAlias()));
            episodeViewHolder.release_date.setText(episode.getRelease_date());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return drama.getEpisodes().size() + 1;
    }
}
