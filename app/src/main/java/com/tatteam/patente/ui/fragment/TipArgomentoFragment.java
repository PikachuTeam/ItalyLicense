package com.tatteam.patente.ui.fragment;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.entity.BaseEntity;
import com.tatteam.patente.entity.TipAgromentoEntity;

import java.util.List;

/**
 * Created by ThanhNH on 2/7/2015.
 */
public class TipArgomentoFragment extends BaseFragment {

    private BaseEntity entity;

    private ListView listView;
    private TextView textViewTarget;
    private ImageView imageViewTarget;
    private TipArgomentoAdapter adapter;
    private TipAgromentoEntity tipAgromentoEntity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_tip_argomento, null);

        if (tipAgromentoEntity == null) {
            loadData();
        }

        listView = (ListView) contentView.findViewById(R.id.listView);
        textViewTarget = (TextView) contentView.findViewById(R.id.textView_target);
        imageViewTarget = (ImageView) contentView.findViewById(R.id.image_tagert);

        adapter = new TipArgomentoAdapter(getActivity(), tipAgromentoEntity.subEntities);
        listView.setAdapter(adapter);

        textViewTarget.setText(tipAgromentoEntity.name);

        if (tipAgromentoEntity.image == null) {
            imageViewTarget.setVisibility(View.GONE);
        } else {
            imageViewTarget.setVisibility(View.VISIBLE);
            imageViewTarget.setImageBitmap(tipAgromentoEntity.image);
        }

        return contentView;
    }

    private void loadData() {
        Bundle dataBundle = getArguments();
        if (dataBundle != null) {
            int categoryId = dataBundle.getInt(BUNDLE_CATEGORY_ID, -1);
            String name = dataBundle.getString(BUNDLE_CATEGORY_NAME, "");
            tipAgromentoEntity = DataSource.getInstance().getTipAgromento(categoryId);
            tipAgromentoEntity.name = name;
        }
    }

    @Override
    protected void setupActionBar(ActionBar actionBar) {
        super.setupActionBar(actionBar);
        actionBar.setTitle(R.string.questions);
    }


    public static class TipArgomentoAdapter extends ArrayAdapter<BaseEntity> {


        private static class ViewHolder {
            public final RelativeLayout rootView;
            public final View viewLineLeft;
            public final TextView textViewAnswer;
            public final TextView textViewName;

            private ViewHolder(RelativeLayout rootView, View viewLineLeft, TextView textViewAnswer, TextView textViewName) {
                this.rootView = rootView;
                this.viewLineLeft = viewLineLeft;
                this.textViewAnswer = textViewAnswer;
                this.textViewName = textViewName;
            }

            public static ViewHolder create(RelativeLayout rootView) {
                View viewLineLeft = (View) rootView.findViewById(R.id.view_line_left);
                TextView textViewAnswer = (TextView) rootView.findViewById(R.id.textView_answer);
                TextView textViewName = (TextView) rootView.findViewById(R.id.textView_name);
                return new ViewHolder(rootView, viewLineLeft, textViewAnswer, textViewName);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                View view = mInflater.inflate(R.layout.item_tip_argomento, parent, false);
                vh = ViewHolder.create((RelativeLayout) view);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            BaseEntity entity = getItem(position);

            if (position % 2 == 0) {
                vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_1);
            } else {
                vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_2);
            }
            vh.textViewName.setText(entity.name);

            if (entity.answer == entity.ANSWER_RIGHT) {
                vh.textViewAnswer.setText(R.string.answer_true);
                vh.textViewAnswer.setTextColor(getContext().getResources().getColor(R.color.answer_true));
            } else {
                vh.textViewAnswer.setText(R.string.answer_fail);
                vh.textViewAnswer.setTextColor(getContext().getResources().getColor(R.color.answer_fail));
            }

            View viewHeighLight = vh.rootView.findViewById(R.id.view_highlight);
            viewHeighLight.setTag(entity);
            viewHeighLight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseEntity entity = (BaseEntity) v.getTag();
                    TipArgomentoDialog dialog = new TipArgomentoDialog(getContext(), entity);
                    dialog.show();
                }
            });

            return vh.rootView;
        }

        private LayoutInflater mInflater;

        // Constructors
        public TipArgomentoAdapter(Context context, List<BaseEntity> objects) {
            super(context, 0, objects);
            this.mInflater = LayoutInflater.from(context);
        }
    }

    private static class TipArgomentoDialog extends Dialog {
        private BaseEntity entity;

        private ImageView imageTrafficSigns;
        private TextView textViewQues;
        private TextView textViewCorrectAnswer;


        public TipArgomentoDialog(Context context, BaseEntity examsEntity) {
            super(context);
            this.entity = examsEntity;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            this.setCanceledOnTouchOutside(true);
            setContentView(R.layout.dialog_tip_argomento);
            findViews();
            if (entity.image != null) {
                imageTrafficSigns.setImageBitmap(entity.image);
            } else {
                imageTrafficSigns.setVisibility(View.GONE);
            }
            textViewQues.setText(entity.name);

            if (entity.answer == BaseEntity.ANSWER_RIGHT) {
                textViewCorrectAnswer.setText(" " + getContext().getString(R.string.answer_true));
                textViewCorrectAnswer.setTextColor(getContext().getResources().getColor(R.color.answer_true));
            } else {
                textViewCorrectAnswer.setText(" " + getContext().getString(R.string.answer_fail));
                textViewCorrectAnswer.setTextColor(getContext().getResources().getColor(R.color.answer_fail));
            }
        }

        private void findViews() {
            imageTrafficSigns = (ImageView) findViewById(R.id.image_traffic_signs);
            textViewQues = (TextView) findViewById(R.id.textView_ques);
            textViewCorrectAnswer = (TextView) findViewById(R.id.textView_correct_answer);
        }
    }

}
