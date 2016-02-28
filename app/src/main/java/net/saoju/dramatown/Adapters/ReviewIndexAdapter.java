package net.saoju.dramatown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.saoju.dramatown.DramaActivity;
import net.saoju.dramatown.EpisodeActivity;
import net.saoju.dramatown.Models.Review;
import net.saoju.dramatown.R;
import net.saoju.dramatown.ReviewActivity;
import net.saoju.dramatown.UserActivity;

import java.util.List;

public class ReviewIndexAdapter extends RecyclerView.Adapter<ReviewIndexAdapter.ViewHolder> {
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
                    Review review = reviews.get(getPosition());
                    review.setVisible(1);
                    bundle.putParcelable("review", review);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    public ReviewIndexAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewIndexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_content, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review review = reviews.get(position);
        String s = context.getResources().getString(R.string.review_info,
                review.getUser().getName(), review.getDrama().getTitle(),
                review.getEpisode() != null ? review.getEpisode().getTitle() : "");
        int start = 0;
        int end = review.getUser().getName().length();
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("id", review.getUser_id());
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);
                ds.setUnderlineText(false);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan((context.getResources().getColor(R.color.linkColor))),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end + 4;
        end = start + review.getDrama().getTitle().length();
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(context, DramaActivity.class);
                intent.putExtra("id", review.getDrama_id());
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);
                ds.setUnderlineText(false);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan((context.getResources().getColor(R.color.linkColor))),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (review.getEpisode() != null) {
            start = end + 1;
            end = start + review.getEpisode().getTitle().length();
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(context, EpisodeActivity.class);
                    intent.putExtra("id", review.getEpisode_id());
                    context.startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(ds.linkColor);
                    ds.setUnderlineText(false);
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan((context.getResources().getColor(R.color.linkColor))),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.info.setText(spannableString);
        holder.info.setMovementMethod(LinkMovementMethod.getInstance());
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
