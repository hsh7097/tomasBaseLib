package com.example.hasang.tomas.network.request;

import android.util.Log;

import com.example.hasang.tomas.AppContext;
import com.example.hasang.tomas.AppPreferenceManager;
import com.example.hasang.tomas.AppUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by hasang on 16. 1. 25..
 */
public class TomasRequest {
    private static final String API_REQUEST_TOKEN = "X-AUTH-TOKEN";
    private static final String API_RESPONSE_HEADER_CODE = "code";
    private static final String API_RESPONSE_HEADER_MESSAGE = "message";

    private static final int GET = 1;
    private static final int PUT = 2;
    private static final int POST = 3;
    private static final int DELETE = 4;

    private static TomasRequest mInstance;


    private TomasRequest() {
    }

    public static TomasRequest getInstance() {
        if (mInstance == null) {
            mInstance = new TomasRequest();
        }
        return mInstance;
    }

    private AsyncHttpClient mClient = new AsyncHttpClient();

    private String mBaseUri;

    public void setBaseUri(String baseUri) {
        mBaseUri = baseUri;
    }

    public String getBaseUri() {
        return mBaseUri;
    }

    public void addHeader(String header) {
        Log.e("tomasServer", "header [" + header + "]");
        mClient.setUserAgent(System.getProperty("http.agent"));
        mClient.addHeader(API_REQUEST_TOKEN, header);
        AppPreferenceManager.getInstance().put(API_REQUEST_TOKEN, header);
        mClient.setResponseTimeout(100000);
    }

    public void removeHeader() {
        mClient.removeAllHeaders();
    }

    public void get(String uri, String id, RequestParams params, boolean useBaseUri, ResponseHandler responseHandler) {
        String addressUri = null;
        if (useBaseUri) {
            if (mBaseUri == null) return;
            addressUri = getUriAddress(uri, id, useBaseUri);
        } else {
            addressUri = getUriAddress(uri, id, useBaseUri);
        }
        Log.e("tomasServer", "address : " + addressUri);


        if (params == null) {
            mClient.get(addressUri, getAsyncHandler(responseHandler));
        } else {
            mClient.get(addressUri, params, getAsyncHandler(responseHandler));
        }
    }

    public void put(String uri, String id, HttpEntity httpEntity, ResponseHandler responseHandler) {
        put(uri, id, httpEntity, true, responseHandler);

    }

    public void put(String uri, String id, HttpEntity httpEntity, boolean useBaseUri, ResponseHandler responseHandler) {
        requestData(PUT, uri, id, httpEntity, useBaseUri, responseHandler);
    }

    public void post(String uri, String id, HttpEntity httpEntity, ResponseHandler responseHandler) {
        post(uri, id, httpEntity, true, responseHandler);

    }

    public void post(String uri, String id, HttpEntity httpEntity, boolean useBaseUri, ResponseHandler responseHandler) {
        requestData(POST, uri, id, httpEntity, useBaseUri, responseHandler);
    }

    public void delete(String uri, ResponseHandler responseHandler) {
        delete(uri, true, responseHandler);

    }

    public void delete(String uri, boolean useBaseUri, ResponseHandler responseHandler) {
        requestData(DELETE, uri, null, null, useBaseUri, responseHandler);
    }

//    public void requestData(int methodType, String uri, String id, HttpEntity httpEntity, ResponseHandler responseHandler) {
//        requestData(methodType, uri, id, httpEntity, true, responseHandler);
//    }

    private void requestData(int methodType, String uri, String id, HttpEntity httpEntity, boolean useBaseUri, ResponseHandler responseHandler) {
        String addressUri = getUriAddress(uri, id, useBaseUri);
        Log.e("tomasServer", "address : " + addressUri);
        switch (methodType) {
            case PUT:
                mClient.put(AppContext.getInstance().getContext(), addressUri, httpEntity, "application/json", getAsyncHandler(responseHandler));
                break;
            case POST:
                mClient.post(AppContext.getInstance().getContext(), addressUri, httpEntity, "application/json", getAsyncHandler(responseHandler));
                break;
            case DELETE:
                mClient.delete(AppContext.getInstance().getContext(), addressUri, getAsyncHandler(responseHandler));
                break;
        }
    }


    private ResponseHandlerInterface getAsyncHandler(final ResponseHandler responseHandler) {
        return new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                setCharset("UTF-8");
                if (responseHandler == null) return;
                responseHandler.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                Log.e("tomasServer", "statusCode : " + statusCode);
                HashMap<String, String> headerResult = getDottegiHeader(headers);
                String token = headerResult.get(API_REQUEST_TOKEN);
                if (token != null) {
                    addHeader(token);
                }
                if (responseHandler == null) return;
                try {
                    responseHandler.onSuccess(headerResult.get(API_RESPONSE_HEADER_CODE), headerResult.get(API_RESPONSE_HEADER_MESSAGE), bytes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.e("tomasServer", "onFailure : " + statusCode);
                if (responseHandler == null) return;
                responseHandler.onFailure(statusCode, getDottegiHeader(headers), bytes, throwable);
            }
        };
    }

    private String getUriAddress(String uri, String id, boolean useBaseUri) {
        if (uri == null) return null;
        if (uri.isEmpty()) return null;
        String BaseUri = useBaseUri ? mBaseUri : "";

        if (id == null || id.isEmpty() || id.equals("0")) {
            return AppUtils.stringAppend(BaseUri, uri);
        } else {
            return String.format(AppUtils.stringAppend(BaseUri, uri), id);
        }
    }

    public interface ResponseHandler {
        public void onStart();

        public void onSuccess(String statusCode, String message, byte[] response) throws JSONException;

        public void onFailure(int statusCode, HashMap<String, String> headers, byte[] response, Throwable throwable);
    }


    private HashMap<String, String> getDottegiHeader(Header[] headers) {
        if (headers == null) return null;


        HashMap<String, String> result = new HashMap<String, String>();
        for (Header header : headers) {
            if (header.getName().equals(API_RESPONSE_HEADER_CODE)) {
                result.put(API_RESPONSE_HEADER_CODE, header.getValue());
            } else if (header.getName().equals(API_RESPONSE_HEADER_MESSAGE)) {
                result.put(API_RESPONSE_HEADER_MESSAGE, header.getValue());
            } else if (header.getName().equals(API_REQUEST_TOKEN)) {
                result.put(API_REQUEST_TOKEN, header.getValue());
            }
        }
        return result;
    }

}
