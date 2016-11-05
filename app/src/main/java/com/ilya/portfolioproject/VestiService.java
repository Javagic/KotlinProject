package com.ilya.portfolioproject;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Ilya on 11/5/2016.
 */
public interface VestiService {
    @GET("articles")
    Call<ResponseBody> getArticles();
}
