package com.tatteam.patente.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tatteam.patente.R;

/**
 * Created by ThanhNH on 2/8/2015.
 */
public  class QuesNoItemWrapper implements View.OnClickListener {
    private Context context;
    private View view;

    private TextView textViewQuesNum;
    private View viewLineBottom;

    private OnItemQuesClickListener listener;


    public QuesNoItemWrapper(Context context) {
        this.context = context;
        view = View.inflate(context, R.layout.item_do_exam_ques_num, null);
        findViews();
    }

    private void findViews() {
        textViewQuesNum = (TextView) view.findViewById(R.id.textView_ques_num);
        viewLineBottom = (View) view.findViewById(R.id.view_line_bottom);
        view.findViewById(R.id.view_highlight).setOnClickListener(this);
    }

    public void setHighLight(){
        textViewQuesNum.setTextColor(context.getResources().getColor(R.color.do_exam_highlight));
//        textViewQuesNum.setTextSize((float) (textViewQuesNum.getTextSize()));
    }

    public void setActive(boolean active) {
        int color = active ? R.color.item_list_line_in_left_1 : R.color.item_list_line_in_left_2;
        viewLineBottom.setBackgroundResource(color);
    }

    public void setText(String text) {
        textViewQuesNum.setText(text);
    }

    public View getView() {
        return view;
    }

    public void registerOnItemQuesClickListener(OnItemQuesClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (this.listener != null) {
            this.listener.onItemQuesClick(this);
        }
    }

    public static interface OnItemQuesClickListener {
        public void onItemQuesClick(QuesNoItemWrapper quesNoItemWrapper);
    }
}