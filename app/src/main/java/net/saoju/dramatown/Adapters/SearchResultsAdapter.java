package net.saoju.dramatown.Adapters;

import android.content.Context;
import android.content.Intent;
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
import net.saoju.dramatown.Models.Drama;
import net.saoju.dramatown.R;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private List<Drama> dramas;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView sc;

        public ViewHolder(View view) {
            super(view);
            sc = (TextView) view.findViewById(R.id.sc);
            title = (TextView) view.findViewById(R.id.title);
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

    public SearchResultsAdapter(Context context, List<Drama> dramas) {
        this.context = context;
        this.dramas = dramas;
    }

    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Drama drama = dramas.get(position);
        String title = context.getResources().getString(R.string.drama_title, drama.getTitle(), drama.getAlias());
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new ForegroundColorSpan((context.getResources().getColor(R.color.textPrimary))),
                0, drama.getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.title.setText(spannableString);
        holder.title.setMovementMethod(LinkMovementMethod.getInstance());
        holder.sc.setText(drama.getSc());
    }

    @Override
    public int getItemCount() {
        return dramas.size();
    }

    public void reset(List<Drama> dramas) {
        this.dramas = dramas;
        notifyDataSetChanged();
    }
}
