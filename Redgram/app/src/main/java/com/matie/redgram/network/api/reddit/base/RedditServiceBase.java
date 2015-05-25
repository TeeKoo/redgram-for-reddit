package com.matie.redgram.network.api.reddit.base;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matie.redgram.MainActivity;
import com.matie.redgram.RedgramApp;
import com.matie.redgram.models.reddit.base.RedditObject;
import com.matie.redgram.utils.reddit.DateTimeDeserializer;
import com.matie.redgram.utils.reddit.RedditObjectDeserializer;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by matie on 15/04/15.
 */
public class RedditServiceBase extends RedditBase {

    private static final Context mContext = RedgramApp.getContext();

    private static final RestAdapter.Builder ADAPTER_BUILDER =  new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setConverter(new GsonConverter(getGson()))
            .setClient(new OkClient(getHttpClient())); //check if needed


    public static RestAdapter getRestAdapter(String url) {
        return ADAPTER_BUILDER.setEndpoint(url)
                .setRequestInterceptor(getInterceptor()).build();
    }

    private static RequestInterceptor getInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request){
                // todo: implement interceptor
                // Use cache by default unless specified otherwise in header.
                request.addHeader("Accept", "application/json");
                if(RedgramApp.isOnline()){
                    int maxAge = 120; //1 minute
                    request.addHeader("Cache-Control", "public, max-age=" + maxAge);
                }else{
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    request.addHeader("Cache-Control",
                            "public, only-if-cached, max-stale=" + maxStale);
                }

                // If authenticated,set Authorization header
                //todo: Auth headers
            }
        };
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(RedditObject.class, new RedditObjectDeserializer())
                .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
                .create();
    }


    private static OkHttpClient getHttpClient(){
        OkHttpClient client = new OkHttpClient();
        client.setCache(getCache());
        return client;
    }

    private static Cache getCache(){
        //setup cache
        File httpCacheDir = new File(mContext.getCacheDir(), "http");

        Cache httpResponseCache = null;

        try {
            httpResponseCache = new Cache(httpCacheDir, 10 * 1024 * 1024);
        } catch (IOException e) {
            Log.e("Retrofit", "Could not create http cache", e);
        }

        return httpResponseCache;
    }

}
