package net.saoju.dramatown;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import net.saoju.dramatown.Models.Episode;
import net.saoju.dramatown.Models.EpisodeFavorite;
import net.saoju.dramatown.Models.Token;
import net.saoju.dramatown.Utils.AddCookiesInterceptor;
import net.saoju.dramatown.Utils.ReceivedCookiesInterceptor;
import net.saoju.dramatown.Utils.ResponseResult;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EpisodeDetailFragment extends Fragment {

    private TextView title;
    private LinearLayout favoriteLayout;
    private TextView favoriteType;
    private RatingBar ratingBar;
    private LinearLayout addFavorite;
    private Button addFavoriteBtn;
    private Button editFavoriteBtn;
    private Button deleteFavoriteBtn;
    private Button addReviewBtn;
    private TextView type;
    private TextView era;
    private TextView genre;
    private TextView origianl;
    private TextView duration;
    private TextView releaseDate;
    private TextView sc;
    private TextView introduction;
    private TextView url;
    private int episodeId;
    private SaojuService service;
    private Episode episode;

    public EpisodeDetailFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_episode_detail, container, false);
        title = (TextView) view.findViewById(R.id.title);
        favoriteLayout = (LinearLayout) view.findViewById(R.id.favorite);
        favoriteType = (TextView) view.findViewById(R.id.favorite_type);
        ratingBar = (RatingBar) view.findViewById(R.id.rating);
        ratingBar.setIsIndicator(true);
        addFavorite = (LinearLayout) view.findViewById(R.id.add_favorite);
        addFavoriteBtn = (Button) view.findViewById(R.id.add_favorite_btn);
        editFavoriteBtn = (Button) view.findViewById(R.id.edit_favorite_btn);
        deleteFavoriteBtn = (Button) view.findViewById(R.id.delete_favorite_btn);
        addReviewBtn = (Button) view.findViewById(R.id.add_review_btn);
        type = (TextView) view.findViewById(R.id.type);
        era = (TextView) view.findViewById(R.id.era);
        genre = (TextView) view.findViewById(R.id.genre);
        origianl = (TextView) view.findViewById(R.id.original);
        duration = (TextView) view.findViewById(R.id.duration);
        releaseDate = (TextView) view.findViewById(R.id.release_date);
        sc = (TextView) view.findViewById(R.id.sc);
        introduction = (TextView) view.findViewById(R.id.introduction);
        url = (TextView) view.findViewById(R.id.url);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor(getContext()))
                .addInterceptor(new ReceivedCookiesInterceptor(getContext()))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SaojuService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(SaojuService.class);
        Bundle bundle = getArguments();
        episodeId = bundle.getInt("id");
        Call<Episode> episodeCall = service.getEpisode(String.valueOf(episodeId));
        episodeCall.enqueue(new Callback<Episode>() {
            @Override
            public void onResponse(Response<Episode> response) {
                if (!response.isSuccess()) {
                    Toast.makeText(getContext(), "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Episode episode = response.body();
                ((EpisodeActivity) getActivity()).setData(episode);
                setData(episode);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        return view;
    }

    private void saveFavorite(View view, final boolean isUpdate) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.favorite_type);
        final int type;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.favorite_type_0:
                type = 0;
                break;
            case R.id.favorite_type_2:
                type = 2;
                break;
            case R.id.favorite_type_4:
                type = 4;
                break;
            default:
                Toast.makeText(getContext(), "请选择收藏类型", Toast.LENGTH_SHORT).show();
                return;
        }
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating);
        final float rating = ratingBar.getRating();
        Call<Token> tokenCall = service.getToken();
        tokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                Token token = response.body();
                Call<ResponseResult> call;
                if (isUpdate) {
                    call = service.editEpfav(String.valueOf(episodeId), token.getToken(), type, rating);
                } else {
                    call = service.addEpfav(token.getToken(), episodeId, type, rating);
                }
                call.enqueue(new Callback<ResponseResult>() {
                    @Override
                    public void onResponse(Response<ResponseResult> response) {
                        if (!response.isSuccess()) {
                            Toast.makeText(getActivity(), "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        updateFavorite(type, rating);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deleteFavorite() {
        Call<Token> tokenCall = service.getToken();
        tokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response) {
                Token token = response.body();
                Call<ResponseResult> call = service.deleteEpfav(String.valueOf(episodeId), "DELETE", token.getToken());
                call.enqueue(new Callback<ResponseResult>() {
                    @Override
                    public void onResponse(Response<ResponseResult> response) {
                        if (!response.isSuccess()) {
                            Toast.makeText(getActivity(), "错误码：" + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        episode.setUserFavorite(null);
                        favoriteLayout.setVisibility(View.GONE);
                        addFavorite.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setFavoriteDialog(View view, EpisodeFavorite favorite) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.favorite_type);
        switch (favorite.getType()) {
            case 0:
                radioGroup.check(R.id.favorite_type_0);
                break;
            case 2:
                radioGroup.check(R.id.favorite_type_2);
                break;
            case 4:
                radioGroup.check(R.id.favorite_type_4);
                break;
            default:
                Toast.makeText(getContext(), "请选择收藏类型", Toast.LENGTH_SHORT).show();
                return;
        }
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating);
        ratingBar.setRating(favorite.getRating());
    }

    private void updateFavorite(int type, float rating) {
        if (episode.getUserFavorite() == null) {
            episode.setUserFavorite(new EpisodeFavorite(type, rating));
        } else {
            episode.getUserFavorite().setType(type);
            episode.getUserFavorite().setRating(rating);
        }
        favoriteLayout.setVisibility(View.VISIBLE);
        addFavorite.setVisibility(View.GONE);
        favoriteType.setText(episode.getUserFavorite().getTypeString());
        if (episode.getUserFavorite().getRating() != 0) {
            ratingBar.setRating(episode.getUserFavorite().getRating());
            ratingBar.setVisibility(View.VISIBLE);
        } else {
            ratingBar.setVisibility(View.GONE);
        }
    }

    private void setData(Episode e) {
        episode = e;
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
        if (episode.getUserFavorite() != null) {
            updateFavorite(episode.getUserFavorite().getType(), episode.getUserFavorite().getRating());
        } else {
            favoriteLayout.setVisibility(View.GONE);
            addFavorite.setVisibility(View.VISIBLE);
        }
        addFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_epfav, null);
                new AlertDialog.Builder(getActivity())
                        .setTitle("添加收藏")
                        .setView(dialogView)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveFavorite(dialogView, false);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        editFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_epfav, null);
                setFavoriteDialog(dialogView, episode.getUserFavorite());
                new AlertDialog.Builder(getActivity())
                        .setTitle("修改收藏")
                        .setView(dialogView)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveFavorite(dialogView, true);
                            }
                        }).setNegativeButton("取消", null)
                        .show();
            }
        });
        deleteFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除收藏")
                        .setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFavorite();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WriteReviewActivity.class);
                intent.putExtra("drama_id", episode.getDrama_id());
                intent.putExtra("episode_id", episode.getId());
                getActivity().startActivity(intent);
            }
        });
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
