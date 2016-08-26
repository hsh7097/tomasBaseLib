package com.example.hasang.tomas.webutil;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.hasang.tomas.AppContext;

import java.net.URISyntaxException;

/**
 * Created by hasang on 16. 8. 3..
 */
public class TomasWebViewClient extends WebViewClient {

    private OnWebClientListener onWebClientListener;

    public TomasWebViewClient(OnWebClientListener onWebClientListener) {
        this.onWebClientListener = onWebClientListener;
    }

    public interface OnWebClientListener {
        public void onPayUrl(String payUrl);

        public void onProgress(int progress);

        public void onProgressStatus(boolean isStart);

    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView webView, String url) {
        Log.d("<INICIS_TEST>", "URL : " + url);
        if (url.startsWith("dottegi://")) {
            if (onWebClientListener != null) {
                onWebClientListener.onPayUrl(url);
            }
            return false;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {
            Intent intent;
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Log.d("<INICIS_TEST>", "intent getDataString : " + intent.getDataString());
            } catch (URISyntaxException ex) {
                Log.e("<INICIS_TEST>", "URI syntax error : " + url + ":" + ex.getMessage());
                return false;
            }
            Uri uri = Uri.parse(intent.getDataString());
            intent = new Intent(Intent.ACTION_VIEW, uri);


            try {
                AppContext.getInstance().getActivity().startActivity(intent);
            } catch (ActivityNotFoundException e) {

                if (url.startsWith("ispmobile://")) {
                    new AlertDialog.Builder(AppContext.getInstance().getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("알림")
                            .setMessage("모바일 ISP 어플리케이션이 설치되어 있지 않습니다. \n설치를 눌러 진행 해 주십시요.\n취소를 누르면 결제가 취소 됩니다.")
                            .setPositiveButton("설치", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //ISP 설치 페이지 URL
                                    webView.loadUrl("http://mobile.vpay.co.kr/jsp/MISP/andown.jsp");
                                    AppContext.getInstance().getActivity().finish();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(AppContext.getInstance().getActivity(), "(-1)결제를 취소 하셨습니다.",
                                            Toast.LENGTH_SHORT).show();
                                    AppContext.getInstance().getActivity().finish();
                                }
                            }).create().show();


                    return false;
                } else if (url.startsWith("intent://")) {
                    try {
                        Intent excepIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        String packageNm = excepIntent.getPackage();

                        excepIntent = new Intent(Intent.ACTION_VIEW);
                        excepIntent.setData(Uri.parse("market://search?q=" + packageNm));
                        AppContext.getInstance().getActivity().startActivity(excepIntent);
                    } catch (URISyntaxException e1) {

                    }
                }
            }

        } else {
            webView.loadUrl(url);
            return false;
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (onWebClientListener != null) {
            onWebClientListener.onProgressStatus(false);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (onWebClientListener != null) {
            onWebClientListener.onProgressStatus(true);
        }
    }

}
