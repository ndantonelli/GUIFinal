package com.nantonelli.guifinal.Service;

import com.nantonelli.guifinal.Response.SongsResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ndantonelli on 11/18/15.
 * Interface used by Retrofit to outline our HTTP Requests.
 * We are using the beta version, so the code might look a little different than what you see online
 * For Reference: http://inthecheesefactory.com/blog/retrofit-2.0/en
 */
public interface RestfulApi {
    @GET("search")
    Call<SongsResponse> getSongs(@Query("term") String searchTerm, @Query("limit") int numResults,
                                 @Query("attribute") String attribute);
}
