package com.example.hasang.tomas.customwidget.materialdialog;

import android.view.View;
import android.widget.TextView;

import com.example.hasang.tomas.AppContext;
import com.example.hasang.tomas.R;

/**
 * Created by hasang on 16. 8. 2..
 */
public class MaterialDialogView {


    /**
     * 에러 다이얼 로그 뷰 생성
     *
     * @param errorMsg 생성될 에러 메세지
     * @return 에러 뷰
     */
    public static View getErrorDialogView(String errorMsg) {
        View dialogView = AppContext.getInstance().getActivity().getLayoutInflater().inflate(R.layout.dialog_default, null);
        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialogTitle);
        TextView dialogContent = (TextView) dialogView.findViewById(R.id.dialogContent);

        dialogTitle.setText("도떼기마켓");
        dialogContent.setText(errorMsg);
        return dialogView;
    }

    /**
     * 기본 다이얼 로그 뷰 생성
     *
     * @param title   다이얼로그 뷰의 타이틀
     * @param content 다이얼로그 뷰의 내용
     * @return 다이얼로그 뷰
     */
    public static View getDefaultDialogView(String title, String content) {
        View dialogView = AppContext.getInstance().getActivity().getLayoutInflater().inflate(R.layout.dialog_default, null);

        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialogTitle);
        TextView dialogContent = (TextView) dialogView.findViewById(R.id.dialogContent);
        dialogTitle.setText(title);
        dialogContent.setText(content);

        if (title == null || title.isEmpty()) {
            dialogTitle.setVisibility(View.GONE);
        }
        if (content == null || content.isEmpty()) {
            dialogContent.setVisibility(View.GONE);
        }

        return dialogView;
    }


}
