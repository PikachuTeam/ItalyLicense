package com.tatteam.patente.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.tatteam.patente.R;

/**
 * Created by ThanhNH on 2/25/2016.
 */
public class SignDialog extends Dialog {

    private Bitmap signBitmap;

    public SignDialog(Context context, Bitmap signBitmap) {
        super(context);
        this.signBitmap = signBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(true);

        setContentView(R.layout.dialog_sign);
        ImageView imgSign = (ImageView) findViewById(R.id.img_sign);
        imgSign.setImageBitmap(signBitmap);

        ((View)imgSign.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
