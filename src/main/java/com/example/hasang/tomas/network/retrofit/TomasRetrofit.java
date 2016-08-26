package com.example.hasang.tomas.network.retrofit;

import com.example.hasang.tomas.network.retrofit.interceptor.TomasRetrofitLogInterceptor;
import com.example.hasang.tomas.network.retrofit.interceptor.TomasRetrofitTokenInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hasang on 16. 8. 23..
 */
public class TomasRetrofit {

    private final int TIMEOUT_READ = 60;
    private final int TIMEOUT_CONNECT = 60;
    private final int TIMEOUT_WRITE = 60;


    private final String SERVER_STATUS_URL = "http://sell.admin.unionpool.com";

    private String API_BASE_RUL = "";


    /**
     * BaseUrl설정
     *
     * @param apiBaseUrl 설정될 BaseUrl
     */
    public void setApiBaseUrl(String apiBaseUrl) {
        API_BASE_RUL = apiBaseUrl;
    }


    protected <S> S getRequestRetrofit(Class<S> serviceClass) {
        /*
         *   Retofit 설정
         */
        Retrofit client = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()) //Gson Parser추가
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getOkHtteClietn(true))
                .baseUrl(API_BASE_RUL)
                .build();
        return client.create(serviceClass);
    }

    /**
     * 서버 상태를 가죠오기 위한 레프로핏설정
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    protected <S> S getServerStatusRetrofit(Class<S> serviceClass) {
        /*
         *   Retofit 설정
         */
        Retrofit client;
        client = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHtteClietn(false))
                .baseUrl(SERVER_STATUS_URL)
                .build();
        return client.create(serviceClass);
    }

    private OkHttpClient getOkHtteClietn(boolean useHeader) {
         /*
         *  OkHttpClient를 생성 합니다.
         */
        OkHttpClient okHttpClient;
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.readTimeout(TIMEOUT_READ, TimeUnit.SECONDS) //읽기 타임아웃 시간 설정
                .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)      //연결 타임아웃 시간 설정
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)          //쓰기 타임아웃 시간 설정
                .interceptors().clear();

        if (useHeader && TomasRetrofitToken.getToken() != null) {
            okHttpClientBuilder.addInterceptor(new TomasRetrofitTokenInterceptor());
        }
        okHttpClientBuilder.addInterceptor(new TomasRetrofitLogInterceptor());
        okHttpClient = okHttpClientBuilder.build();
        return okHttpClient;
    }

}
