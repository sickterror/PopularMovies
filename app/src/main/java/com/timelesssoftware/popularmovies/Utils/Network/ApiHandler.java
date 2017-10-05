package com.timelesssoftware.popularmovies.Utils.Network;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.timelesssoftware.popularmovies.BuildConfig;
import com.timelesssoftware.popularmovies.PopularMoviesApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Luka on 3. 10. 2017.
 */

public class ApiHandler {

    OkHttpClient mOkHttp;
    Gson mGson;
    SharedPreferences mPreferenceManager;

    public static final String API_ENDPOINT = "https://api.themoviedb.org/3/";
    public static final String API_KEY = BuildConfig.API_KEY;

    public ApiHandler(OkHttpClient mOkHttp, Gson mGson, SharedPreferences mPreferenceManager) {
        this.mOkHttp = mOkHttp;
        this.mGson = mGson;
        this.mPreferenceManager = mPreferenceManager;
      //  PopularMoviesApp.getNetComponent().inject(this);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public <T> void getJsonObject(final String method, Map<String,String>params, final Class<T> clazz, final ApiHandlerJsonObjectListener listener) {
        HttpUrl.Builder httpBuider = HttpUrl.parse(API_ENDPOINT+ method).newBuilder();
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                httpBuider.addQueryParameter(param.getKey(),param.getValue());
            }
        }
        httpBuider.addQueryParameter("api_key",API_KEY);
        Log.d("ApiHandler", httpBuider.build().toString());
        Request request = new Request.Builder().url(httpBuider.build()).build();
        mOkHttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("ApiHandler", e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200) {
                    T list = mGson.fromJson(response.body().charStream(), clazz);
                    listener.onSuccesJsonObject(list);
                    return;
                }
                listener.onError(response.code());
            }
        });
    }

    public <T> Observable<T> get(final String method, Map<String,String>params, final Class<T> clazz){
        HttpUrl.Builder httpBuider = HttpUrl.parse(API_ENDPOINT+ method).newBuilder();
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                httpBuider.addQueryParameter(param.getKey(),param.getValue());
            }
        }
        httpBuider.addQueryParameter("api_key",API_KEY);
        Log.d("ApiHandler", httpBuider.build().toString());
        final Request request = new Request.Builder().url(httpBuider.build()).build();
        return  Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<T> e) throws Exception {
                mOkHttp.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("ApiHandler", e.toString());
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        if (response.code() == 200) {
                            T list = mGson.fromJson(response.body().charStream(), clazz);
                            e.onNext(list);
                            e.onComplete();
                            return;
                        }
                    }
                });
            }
        });
    }

    public <T> void postJsonObject(final String method, @Nullable HashMap<String, String> params, final Boolean sync, final Class<T> clazz, final ApiHandlerJsonObjectListener apiHandlerJsonArrayListener) {
        Request.Builder request = buildRequest(method);
        if (params != null) {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            RequestBody formBody = builder.build();
            request.post(formBody);
        }
        mOkHttp.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                apiHandlerJsonArrayListener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200) {
                    T list = mGson.fromJson(response.body().charStream(), clazz);
                    apiHandlerJsonArrayListener.onSuccesJsonObject(list);
                    return;
                }
                apiHandlerJsonArrayListener.onError(response.code());
            }
        });
    }


    public interface ApiHandlerJsonObjectListener {
        public <T> void onSuccesJsonObject(T object);

        public void onFailure(IOException e);

        public void onError(int statusCode);
    }

    //TODO: adding api key to the url should be done difrently. Beacouse of the & and ? sings in url.
    private Request.Builder buildRequest(String method) {
        String url = API_ENDPOINT + method + "&api_key=" + API_KEY;
        Log.d("ApiHandler","Url: "+ url);
        return new Request.Builder().tag(method).url(url);
    }

    public void cancelRequest(String tag) {
        for (Call call : mOkHttp.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(tag)) {
                Log.i("cancelRequest", "queued request with tag " + tag + " canceled");
                call.cancel();
            }
        }
        for (Call call : mOkHttp.dispatcher().runningCalls()) {
            if (call.request().tag().equals(tag)) {
                Log.i("cancelRequest", "running request with tag " + tag + " canceled");
                call.cancel();
            }
        }
    }
}