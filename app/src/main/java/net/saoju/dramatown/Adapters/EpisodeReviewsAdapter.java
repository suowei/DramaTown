package net.saoju.dramatown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.saoju.dramatown.Models.Drama;
import net.saoju.dramatown.Models.Episode;
import net.saoju.dramatown.Models.Review;
import net.saoju.dramatown.R;
import net.saoju.dramatown.ReviewActivity;
import net.saoju.dramatown.UserActivity;

import java.util.List;

public class EpisodeReviewsAdapter extends RecyclerView.Adapter<EpisodeReviewsAdapter.ViewHolder> {
    private List<Review> reviews;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView info;
        TextView created_at;
        TextView title;
        TextView content;

        public ViewHolder(final View view) {
            super(view);
            info = (TextView) view.findViewById(R.id.info);
            created_at = (TextView) view.findViewById(R.id.created_at);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("review", reviews.get(getPosition()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    public EpisodeReviewsAdapter(Context context, List<Review> reviews) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public EpisodeReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_content, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review review = reviews.get(position);
        holder.info.setText(review.getUser().getName());
        holder.info.setTextColor(context.getResources().getColor(R.color.linkColor));
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("id", review.getUser_id());
                context.startActivity(intent);
            }
        });
        holder.created_at.setText(review.getCreated_at());
        holder.title.setText(review.getTitle());
        if (review.getTitle().isEmpty()) {
            holder.title.setVisibility(View.GONE);
        } else {
            holder.title.setVisibility(View.VISIBLE);
        }
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void reset(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public void addAll(List<Review> reviews) {
        int position = getItemCount();
        this.reviews.addAll(reviews);
        notifyItemRangeInserted(position, reviews.size());
    }
}
