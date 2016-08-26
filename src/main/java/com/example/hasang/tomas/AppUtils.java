package com.example.hasang.tomas;

import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by hasang on 16. 1. 21..
 */
public class AppUtils {
    public static final int ANIMATION_SELF = Animation.RELATIVE_TO_SELF;
    public static final int ANIMATION_PARENT = Animation.RELATIVE_TO_PARENT;


    /**
     * String 합칠때 사용
     * @param a
     * @param b
     * @return
     */
    public static String stringAppend(String a, String b) {
        return new StringBuilder().append(a).append(b).toString();
    }

    public static String stringAppend(String a, String b, String c) {
        return new StringBuilder().append(a).append(b).append(c).toString();
    }

    public static String stringAppend(String a, String b, String c, String d) {
        return new StringBuilder().append(a).append(b).append(c).append(d).toString();
    }

    public static String stringAppend(String a, String b, String c, String d, String e) {
        return new StringBuilder().append(a).append(b).append(c).append(d).append(e).toString();
    }

    public static String stringAppend(String a, String b, String c, String d, String e, String f) {
        return new StringBuilder().append(a).append(b).append(c).append(d).append(e).append(f).toString();
    }

    public static String stringAppend(String a, String b, String c, String d, String e, String f, String g) {
        return new StringBuilder().append(a).append(b).append(c).append(d).append(e).append(f).append(g).toString();
    }

    /**
     * 폰트 적용
     * @param filename 폭트명
     * @return
     */
    public static Typeface getFontInAssets(String filename) {
        Typeface font = Typeface.createFromAsset(AppContext.getInstance().getContext().getAssets(), filename);
        return font;
    }


    /**
     * .을 _로 변경
     * @param originalClass 변경될 글자
     * @return
     */
    public static String changeDotToLowdash(String originalClass) {
        String changedClassName;
        changedClassName = originalClass.replaceAll("\\.", "_");
        return changedClassName;
    }

    /**
     * 리소스 내 아이디 값을 알아오는 메소드
     * @param resourceName 클래스 명
     * @param resourceClass 찾는 타입
     * @return
     */
    public static int getResourceDataId(String resourceName, Class<?> resourceClass) {
        try {
            Field idField = resourceClass.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return 0;
        }
    }


    public static Animation transLateAnimation(int animationType, float fromX, float toX, float fromY, float toY, long duraion) {
        return transLateAnimation(animationType, fromX, toX, fromY, toY, duraion, 0);
    }

    /**
     * 이동 애니메이션 생성
     * @param animationType 애니메이션 타입
     * @param fromX 커지기 전 x 값
     * @param toX 커진 후 x 값
     * @param fromY 커지기 전 y 값
     * @param toY 커진 후 y 값
     * @param duraion 진행 될 시간
     * @param offset 시작 전 대기시간
     * @return 설정이 완료된 애니메이션
     */ public static Animation transLateAnimation(int animationType, float fromX, float toX, float fromY, float toY, long duraion, long offset) {
        Animation transAnimation = new TranslateAnimation(animationType, fromX, animationType, toX, animationType, fromY, animationType, toY);
        transAnimation.setDuration(duraion);
        transAnimation.setInterpolator(new AccelerateInterpolator());
        transAnimation.setStartOffset(offset);
        return transAnimation;
    }

    public static Animation scaleAnimation(int animationType, float fromX, float toX, float fromY, float toY, float pivotXValue, float pivotYValue, long duraion) {
        return scaleAnimation(animationType, fromX, toX, fromY, toY, pivotXValue, pivotYValue, duraion, 0);
    }

    /**
     * 크기 애니메이션 생성
     * @param animationType 애니메이션 타입
     * @param fromX 커지기 전 x 값
     * @param toX 커진 후 x 값
     * @param fromY 커지기 전 y 값
     * @param toY 커진 후 y 값
     * @param pivotXValue x의 중간점
     * @param pivotYValue y의 중간점
     * @param duraion 진행 될 시간
     * @param offset 시작 전 대기시간
     * @return
     */
    public static Animation scaleAnimation(int animationType, float fromX, float toX, float fromY, float toY, float pivotXValue, float pivotYValue, long duraion, long offset) {
        Animation transAnimation = new ScaleAnimation(fromX, toX, fromY, toY, animationType, pivotXValue, animationType, pivotYValue);
        transAnimation.setDuration(duraion);
        transAnimation.setInterpolator(new AccelerateInterpolator());
        transAnimation.setStartOffset(offset);
        return transAnimation;
    }

    /**
     * 화면의 dp사이즈 가져오기
     * @param size dp사이즈
     * @return 픽셀 사이즈
     */
    public static int getDipSize(float size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, AppContext.getInstance().getDisplayMatrics());
    }


    /**
     * String 내의 값을 ,가 들어간 값으로 수정
     * @param value 입력데이터
     * @return
     */
    public static String convertPriceFormat(String value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.KOREA);
        String sellPrice = format.format(Integer.valueOf(value));

        return sellPrice.substring(1);
    }


    /**
     * 밑줄 추가
     * @param textView 밑줄이 추가될 텍스트뷰
     */
    public static void setunderlineTextView(TextView textView) {
        SpannableString spanString = new SpannableString(textView.getText().toString());
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        textView.setText(spanString);
    }

    public static void setBoldTextView(TextView textView, String inputText, String changeText){
        int startIndex = inputText.indexOf(changeText);
        int endIndex = startIndex + changeText.length();
        Editable editable = new SpannableStringBuilder(inputText);

        SpannableStringBuilder ssb = new SpannableStringBuilder(changeText);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        editable.replace(startIndex, endIndex, ssb);

        textView.setText(editable);
    }

    //이메일 체크
    public static boolean isEmail(String email) {
        if (email == null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
        return b;
    }


    public static void clearWebViewCookie(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(AppContext.getInstance().getContext());
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();

            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

}
