package net.saoju.dramatown;

import net.saoju.dramatown.Models.Drama;
import net.saoju.dramatown.Models.Episode;
import net.saoju.dramatown.Models.NewEpisodes;
import net.saoju.dramatown.Models.Reviews;
import net.saoju.dramatown.Models.Token;
import net.saoju.dramatown.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SaojuService {
    String BASE_URL = "http://saoju.net";

    @GET("api/newepisodes")
    Call<NewEpisodes> getNewEpidoes(@Query("page") String page);

    @GET("api/review")
    Call<Reviews> getReviews(@Query("page") String page);

    @GET("api/episode/{episode}")
    Call<Episode> getEpisode(@Path("episode") String episode);

    @GET("api/episode/{episode}/reviews")
    Call<Reviews> getEpisodeReveiws(@Path("episode") String episode, @Query("page") String page);

    @GET("api/drama/{drama}")
    Call<Drama> getDrama(@Path("drama") String drama);

    @GET("api/drama/{drama}/reviews")
    Call<Reviews> getDramaReveiws(@Path("drama") String drama, @Query("page") String page);

    @GET("api/search")
    Call<List<Drama>> getSearchResults(@Query("keyword") String keyword);

    @GET("api/csrftoken")
    Call<Token> getToken();

    @FormUrlEncoded
    @POST("api/auth/login")
    Call<User> login(@Field("_token") String token, @Field("email") String email,
                     @Field("password") String password, @Field("remember") String remember);
}
