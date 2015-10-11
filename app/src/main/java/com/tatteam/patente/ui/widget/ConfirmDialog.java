package com.tatteam.patente.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatteam.patente.R;

/**
 * Created by ThanhNH on 2/14/2015.
 */
public class ConfirmDialog extends Dialog {

    public static final int CONFIRM_CANCEL = 0;
    public static final int CONFIRM_OK = 1;

    private TextView textViewTitle;
    private TextView textViewMessage;
    private RelativeLayout layoutCancel;
    private RelativeLayout layoutOk;

    private String title;
    private String message;

    private OnConfirmListener listener;

    public ConfirmDialog(Context context, String title, String message) {
        super(context);
        this.title = title;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(true);

        setContentView(R.layout.dialog_confirm);
        this.findViews();
        if (title == null) {
            title = getContext().getString(R.string.dialog_confirm_title);
        }
        if (message == null) {
            message = getContext().getString(R.string.exit_message);
        }
        textViewTitle.setText(this.title);
        textViewMessage.setText(this.message);
    }

    public void registerOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    private void findViews() {
        textViewTitle = (TextView) findViewById(R.id.textView_title);
        textViewMessage = (TextView) findViewById(R.id.textView_message);
        layoutCancel = (RelativeLayout) findViewById(R.id.layout_cancel);
        layoutOk = (RelativeLayout) findViewById(R.id.layout_ok);

        layoutCancel.findViewById(R.id.view_highlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onDialogConfirmed(ConfirmDialog.this, CONFIRM_CANCEL);
                }
            }
        });
        layoutOk.findViewById(R.id.view_highlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onDialogConfirmed(ConfirmDialog.this, CONFIRM_OK);
                }
            }
        });
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void show() {
        super.show();
    }

    public static interface OnConfirmListener {
        public void onDialogConfirmed(ConfirmDialog dialog, int code);
    }

}
