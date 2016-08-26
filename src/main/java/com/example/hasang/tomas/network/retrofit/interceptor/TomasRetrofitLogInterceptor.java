package com.example.hasang.tomas.network.retrofit.interceptor;

import android.util.Log;

import com.example.hasang.tomas.network.retrofit.TomasRetrofitToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by hasang on 16. 8. 23..
 */
public class TomasRetrofitLogInterceptor implements Interceptor {
    private static final String TAG = "RetrofitLog";
    private final String REQUEST_LOGSTRING = "-----------------------------------------\n" +
            "RequestUrl : %s\n" +
            "RequestBody : %s\n" +
            "\n" +
            "Response code : %s\n" +
            "ResponseBody : %s\n" +
            "-----------------------------------------";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .build();

        Response response = chain.proceed(request);
        String bodyString = response.body().string();
        String networkLog = String.format(REQUEST_LOGSTRING, request.url(), bodyToString(request), response.code(), bodyString);
        Log.d(TAG, networkLog);
        return chain.proceed(request);
    }

    /**
     * 리스폰스바디의 값 가져오기
     *
     * @param request 가져와야할 리퀘스트
     * @return 바디 값
     */
    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        } catch (final NullPointerException e) {
            return "did not have body";
        }
    }
}
