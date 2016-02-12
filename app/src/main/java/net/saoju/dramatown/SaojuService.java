package net.saoju.dramatown;

import net.saoju.dramatown.Models.Drama;
import net.saoju.dramatown.Models.Episode;
import net.saoju.dramatown.Models.NewEpisodes;
import net.saoju.dramatown.Models.Review;
import net.saoju.dramatown.Models.Reviews;
import net.saoju.dramatown.Models.Token;
import net.saoju.dramatown.Models.User;
import net.saoju.dramatown.Utils.ResponseResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @FormUrlEncoded
    @POST("api/epfav")
    Call<ResponseResult> addEpfav(@Field("_token") String token, @Field("episode_id") int id,
                                  @Field("type") int type, @Field("rating") float rating);

    @FormUrlEncoded
    @PUT("api/epfav/{episode}")
    Call<ResponseResult> editEpfav(@Path("episode") String episode, @Field("_token") String token,
                                   @Field("type") int type, @Field("rating") float rating);

    @FormUrlEncoded
    @POST("api/epfav/{episode}")
    Call<ResponseResult> deleteEpfav(@Path("episode") String episode, @Field("_method") String method,
                                     @Field("_token") String token);

    @FormUrlEncoded
    @POST("api/review")
    Call<ResponseResult> addReview(@Field("_token") String token, @Field("drama_id") int dramaId,
                                   @Field("episode_id") int episodeId, @Field("title") String title,
                                   @Field("content") String content);

    @FormUrlEncoded
    @POST("api/epfav2")
    Call<ResponseResult> addEpfavReview(@Field("_token") String token,
                                        @Field("episode_id") int episodeIdd, @Field("drama_id") int dramaIdd,
                                        @Field("type") int type, @Field("rating") float rating,
                                        @Field("title") String title, @Field("content") String content);

    @FormUrlEncoded
    @PUT("api/epfav2/{episode}")
    Call<ResponseResult> updateEpfavReview(@Path("episode") String episode, @Field("_token") String token,
                                         @Field("drama_id") int dramaId,
                                         @Field("type") int type, @Field("rating") float rating,
                                         @Field("title") String title, @Field("content") String content);

    @GET("api/epfav/{episode}/edit")
    Call<Review> editEpfavReview(@Path("episode") String episode);
}
