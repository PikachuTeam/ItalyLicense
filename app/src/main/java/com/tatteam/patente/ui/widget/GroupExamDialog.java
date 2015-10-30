package com.tatteam.patente.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatteam.patente.R;

/**
 * Created by ThanhNH on 2/11/2015.
 */
public class GroupExamDialog extends Dialog {

    private ListView listView;
    private GroupExamAdapter adapter;
    private String[] groupExamText;
    private OnDialogItemClickListener listener;


    public GroupExamDialog(Context context, String[] groupExamText) {
        super(context);
        this.groupExamText = groupExamText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(true);
        if (groupExamText.length >= 2) {
            setContentView(R.layout.dialog_group_exam);
        } else {
            setContentView(R.layout.dialog_group_exam_small);
        }
        listView = (ListView) findViewById(R.id.listView);
        adapter = new GroupExamAdapter(getContext(), this, groupExamText);
        listView.setAdapter(adapter);
    }

    public void registerOnDialogItemClickListener(OnDialogItemClickListener listener) {
        this.listener = listener;
    }

    public void onItemClick(int position) {
        if (this.listener != null) {
            this.listener.onDialogItemClick(this, position);
        }
    }

    public static interface OnDialogItemClickListener {
        public void onDialogItemClick(Dialog dialog, int position);
    }


    public static class GroupExamAdapter extends ArrayAdapter<String> {
        private static class ViewHolder {
            public final RelativeLayout rootView;
            public final View viewLineLeft;
            public final TextView textViewName;

            private ViewHolder(RelativeLayout rootView, View viewLineLeft, TextView textViewName) {
                this.rootView = rootView;
                this.viewLineLeft = viewLineLeft;
                this.textViewName = textViewName;
            }

            public static ViewHolder create(RelativeLayout rootView) {
                View viewLineLeft = (View) rootView.findViewById(R.id.view_line_left);
                TextView textViewName = (TextView) rootView.findViewById(R.id.textView_name);
                return new ViewHolder(rootView, viewLineLeft, textViewName);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                View view = mInflater.inflate(R.layout.item_dialog_group_exam, parent, false);
                vh = ViewHolder.create((RelativeLayout) view);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            String text = getItem(position);
            vh.textViewName.setText(text);

            if (position % 2 == 0) {
                vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_1);
            } else {
                vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_2);
            }
            View viewHeighLight = vh.rootView.findViewById(R.id.view_highlight);
            viewHeighLight.setTag(position);
            viewHeighLight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    dialog.onItemClick(position);
                }
            });
            return vh.rootView;
        }

        private LayoutInflater mInflater;
        private GroupExamDialog dialog;

        public GroupExamAdapter(Context context, GroupExamDialog dialog, String[] objects) {
            super(context, 0, objects);
            this.mInflater = LayoutInflater.from(context);
            this.dialog = dialog;
        }
    }

}
