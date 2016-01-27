package net.saoju.dramatown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
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

import net.saoju.dramatown.Models.Episode;

public class EpisodeDetailFragment extends Fragment {

    private TextView title;
    private TextView type;
    private TextView era;
    private TextView genre;
    private TextView origianl;
    private TextView duration;
    private TextView releaseDate;
    private TextView sc;
    private TextView introduction;
    private TextView url;

    public EpisodeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode_detail, container, false);
        title = (TextView) view.findViewById(R.id.title);
        type = (TextView) view.findViewById(R.id.type);
        era = (TextView) view.findViewById(R.id.era);
        genre = (TextView) view.findViewById(R.id.genre);
        origianl = (TextView) view.findViewById(R.id.original);
        duration = (TextView) view.findViewById(R.id.duration);
        releaseDate = (TextView) view.findViewById(R.id.release_date);
        sc = (TextView) view.findViewById(R.id.sc);
        introduction = (TextView) view.findViewById(R.id.introduction);
        url = (TextView) view.findViewById(R.id.url);
        return view;
    }

    public void updateData(final Episode episode) {
        String s = getResources().getString(R.string.drama_episode_title,
                episode.getDrama().getTitle(), episode.getTitle(), episode.getAlias());
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getContext(), DramaActivity.class);
                intent.putExtra("id", episode.getDrama_id());
                getContext().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ds.linkColor);
                ds.setUnderlineText(false);
            }
        }, 1, episode.getDrama().getTitle().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.linkColor))),
                1, episode.getDrama().getTitle().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(spannableString);
        title.setMovementMethod(LinkMovementMethod.getInstance());
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.textSecondary));
        SpannableString spanString = new SpannableString(getResources().getString(
                R.string.drama_type, episode.getDrama().getTypeString()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        type.setText(spanString);
        spanString = new SpannableString(getResources().getString(
                R.string.drama_era, episode.getDrama().getEraString()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        era.setText(spanString);
        spanString = new SpannableString(getResources().getString(
                R.string.drama_genre, episode.getDrama().getGenre()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        genre.setText(spanString);
        spanString = new SpannableString(getResources().getString(
                R.string.drama_original, episode.getDrama().getOriginalString()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        origianl.setText(spanString);
        spanString = new SpannableString(getResources().getString(
                R.string.episode_duration, episode.getDuration()));
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        duration.setText(spanString);
        spanString = new SpannableString(getResources().getString(
                R.string.episode_release_date, episode.getRelease_date()));
        spanString.setSpan(span, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        releaseDate.setText(spanString);
        sc.setText(episode.getSc());
        introduction.setText(episode.getIntroduction());
        url.setText(getResources().getString(R.string.episode_url, episode.getUrl()));
    }
}
