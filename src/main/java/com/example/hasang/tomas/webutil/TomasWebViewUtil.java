package com.example.hasang.tomas.webutil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.hasang.tomas.AppContext;
import com.example.hasang.tomas.AppUtils;
import com.example.hasang.tomas.webutil.TomasWebViewClient.OnWebClientListener;

/**
 * Created by hasang on 16. 8. 3..
 */
public class TomasWebViewUtil {

    public static void clearWebViewCookei(WebView webView) {
        webView.clearHistory();
        webView.clearCache(true);
        webView.clearView();
        AppUtils.clearWebViewCookie();
        AppContext.getInstance().getActivity().deleteDatabase("webview.db");
        AppContext.getInstance().getActivity().deleteDatabase("webviewCache.db");
    }

    public static void setInicisWebView(final WebView webView, final OnWebClientListener onWebClientListener) {
        webView.setHorizontalScrollBarEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.setInitialScale(1);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(webSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new TomasWebViewClient(onWebClientListener));


        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                new AlertDialog.Builder(AppContext.getInstance().getActivity())
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.confirm();
                                    }
                                }).setCancelable(false).create().show();

                return true;
            }

            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                new AlertDialog.Builder(AppContext.getInstance().getActivity())
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.cancel();
                                    }
                                }).setCancelable(false).create().show();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(onWebClientListener != null){
                    onWebClientListener.onProgress(newProgress);
                }

            }
        });
    }


    public class JSIterface {
//        webView.addJavascriptInterface(new JSIterface(), "dottegiHandler");

        @JavascriptInterface
        public void postMessage(String Message) {
            if (Message.equals("SIGNOUT")) {
            } else {

            }

        }
    }
}
