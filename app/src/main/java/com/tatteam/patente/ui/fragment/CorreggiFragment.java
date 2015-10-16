package com.tatteam.patente.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.control.UserManager;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.entity.BaseEntity;
import com.tatteam.patente.entity.ExamsEntity;
import com.tatteam.patente.entity.SheetEntity;
import com.tatteam.patente.utility.ShareUtil;
import com.tatteam.patente.utility.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanhNH on 2/9/2015.
 */
public class CorreggiFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUIRE_CORRECT_ANSWER_NUMBER_B = 36;
    private static final int REQUIRE_CORRECT_ANSWER_NUMBER_AM = 27;

    private int categoryId;
    private int sheetNo;
    private List<ExamsEntity> listExams;
    private int duration;

    private TextView textViewResult;
    private TextView textViewTotal;
    private TextView textViewError;
    private TextView textViewDuration;
    private Button buttonNewExam;
    private LinearLayout layoutShare;
    private ImageButton buttonShareFB;
    private ImageButton buttonShareTT;

    private View layoutTabCorrect;
    private View layoutTabWrong;

    private List<ExamsEntity> listExamsCorrect;
    private List<ExamsEntity> listExamsWrong;

    private ListView listViewCorrect;
    private CorreggiAdapter adapterCorrect;

    private ListView listViewWrong;
    private CorreggiAdapter adapterWrong;

    private boolean isTabCorrectActive = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        SheetEntity sheetEntity = new SheetEntity(categoryId, sheetNo, listExams.size(), listExamsCorrect.size(), duration);
        DataSource.getInstance().saveLastScoreInfo(sheetEntity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_correggi, null);

        if (listExams == null || listExams.isEmpty()) {
            loadData();
        }

        findViews(contentView);
        displayResult();
        activeTab(isTabCorrectActive);


        return contentView;
    }

    private void loadData() {
        listExamsCorrect = new ArrayList<>();
        listExamsWrong = new ArrayList<>();

        Bundle dataBundle = getArguments();
        if (dataBundle != null) {
            categoryId = dataBundle.getInt(BUNDLE_CATEGORY_ID, -1);
            sheetNo = dataBundle.getInt(BUNDLE_SHEET_NO, -1);
            duration = dataBundle.getInt(BUNDLE_TIME, -1);
            listExams = DataSource.getInstance().getExamList(categoryId, sheetNo);
            int[] answers = dataBundle.getIntArray(BUNDLE_ANSWER);
            for (int i = 0; i < listExams.size(); i++) {
                ExamsEntity examsEntity = listExams.get(i);
                examsEntity.myAnswer = answers[i];
                if (examsEntity.myAnswer == examsEntity.answer) {
                    listExamsCorrect.add(examsEntity);
                } else {
                    listExamsWrong.add(examsEntity);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().popBackStack(DoExamsFragment.TRANSCATION_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        return true;
    }

    private void activeTab(boolean isCorrectActive) {
        if (isCorrectActive) {
            layoutTabCorrect.setBackgroundResource(R.color.correggi_tab_active);
            layoutTabWrong.setBackgroundResource(R.color.correggi_tab_inactive);
            listViewCorrect.setVisibility(View.VISIBLE);
            listViewWrong.setVisibility(View.INVISIBLE);
        } else {
            layoutTabCorrect.setBackgroundResource(R.color.correggi_tab_inactive);
            layoutTabWrong.setBackgroundResource(R.color.correggi_tab_active);
            listViewCorrect.setVisibility(View.INVISIBLE);
            listViewWrong.setVisibility(View.VISIBLE);
        }
    }

    private void displayResult() {
        int requireCorrectAnswer = UserManager.getInstance().isLicenseTypeB() ? REQUIRE_CORRECT_ANSWER_NUMBER_B : REQUIRE_CORRECT_ANSWER_NUMBER_AM;

        if (listExamsCorrect.size() >= requireCorrectAnswer) {
            textViewResult.setText(R.string.pass_exam);
            textViewResult.setTextColor(getResources().getColor(R.color.answer_true));
        } else {
            textViewResult.setText(R.string.not_pass_exam);
            textViewResult.setTextColor(getResources().getColor(R.color.answer_fail));
        }
        textViewTotal.setText(getString(R.string.correct) + ": " + String.valueOf(listExamsCorrect.size() + "/" + String.valueOf(listExams.size())));
        textViewError.setText(getString(R.string.error) + ": " + String.valueOf(listExamsWrong.size() + "/" + String.valueOf(listExams.size())));

        int min = duration / 60;
        int second = duration - (min * 60);
        textViewDuration.setText(getString(R.string.duration) + ": " + String.valueOf(StringUtil.formatNumber(min)) + ":" + String.valueOf(StringUtil.formatNumber(second)));
    }


    private void findViews(View parent) {
        textViewResult = (TextView) parent.findViewById(R.id.textView_result);
        textViewTotal = (TextView) parent.findViewById(R.id.textView_total);
        textViewError = (TextView) parent.findViewById(R.id.textView_error);
        textViewDuration = (TextView) parent.findViewById(R.id.textView_duration);
        buttonNewExam = (Button) parent.findViewById(R.id.button_new_exam);
        layoutShare = (LinearLayout) parent.findViewById(R.id.layout_share);
        buttonShareFB = (ImageButton) parent.findViewById(R.id.button_shareFB);
        buttonShareTT = (ImageButton) parent.findViewById(R.id.button_shareTT);
        listViewCorrect = (ListView) parent.findViewById(R.id.listView_correct);
        listViewWrong = (ListView) parent.findViewById(R.id.listView_wrong);
        layoutTabCorrect = parent.findViewById(R.id.layout_tab_correct);
        layoutTabWrong = parent.findViewById(R.id.layout_tab_wrong);

//        buttonNewExam.setVisibility(View.INVISIBLE);
        buttonNewExam.setOnClickListener(this);
        buttonShareFB.setOnClickListener(this);
        buttonShareTT.setOnClickListener(this);

        layoutTabCorrect.findViewById(R.id.view_highlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTabCorrectActive) {
                    isTabCorrectActive = true;
                    activeTab(isTabCorrectActive);
                }
            }
        });
        layoutTabWrong.findViewById(R.id.view_highlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTabCorrectActive) {
                    isTabCorrectActive = false;
                    activeTab(isTabCorrectActive);
                }
            }
        });

        adapterCorrect = new CorreggiAdapter(getActivity(), listExamsCorrect);
        listViewCorrect.setAdapter(adapterCorrect);

        adapterWrong = new CorreggiAdapter(getActivity(), listExamsWrong);
        listViewWrong.setAdapter(adapterWrong);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonNewExam) {
            this.onBackPressed();
        } else if (v == buttonShareFB) {
        } else if (v == buttonShareTT) {
        }
    }

    private String getSharingMessage() {
        String message = "I got " + listExamsCorrect.size() + "/" + listExams.size() + ". Do you want to try?\n-------------\n" + ShareUtil.getSharingMessage(getActivity());
        return message;
    }

    public static class CorreggiAdapter extends ArrayAdapter<ExamsEntity> {

        private static class ViewHolder {
            public final RelativeLayout rootView;
            public final View viewLineLeft;
            public final ImageView imageTrafficSigns;
            public final TextView textViewQuesNum;
            public final TextView textViewName;

            private ViewHolder(RelativeLayout rootView, View viewLineLeft, ImageView imageTrafficSigns, TextView textViewQuesNum, TextView textViewName) {
                this.rootView = rootView;
                this.viewLineLeft = viewLineLeft;
                this.imageTrafficSigns = imageTrafficSigns;
                this.textViewQuesNum = textViewQuesNum;
                this.textViewName = textViewName;
            }

            public static ViewHolder create(RelativeLayout rootView) {
                View viewLineLeft = (View) rootView.findViewById(R.id.view_line_left);
                ImageView imageTrafficSigns = (ImageView) rootView.findViewById(R.id.image_traffic_signs);
                TextView textViewQuesNum = (TextView) rootView.findViewById(R.id.textView_ques_num);
                TextView textViewName = (TextView) rootView.findViewById(R.id.textView_name);
                return new ViewHolder(rootView, viewLineLeft, imageTrafficSigns, textViewQuesNum, textViewName);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                View view = mInflater.inflate(R.layout.item_correggi, parent, false);
                vh = ViewHolder.create((RelativeLayout) view);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            ExamsEntity entity = getItem(position);
            if (position % 2 == 0) {
                vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_1);
            } else {
                vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_2);
            }
            vh.textViewName.setText(entity.question);
            vh.textViewQuesNum.setText(getContext().getString(R.string.question) + " " + StringUtil.formatNumber(entity.questionNo));

            if (entity.image != null) {
                vh.imageTrafficSigns.setVisibility(View.VISIBLE);
                vh.imageTrafficSigns.setImageBitmap(entity.image);
            } else {
                vh.imageTrafficSigns.setVisibility(View.INVISIBLE);
            }

            // set OnItemClick
            View viewHeighLight = vh.rootView.findViewById(R.id.view_highlight);
            viewHeighLight.setTag(entity);
            viewHeighLight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExamsEntity examsEntity = (ExamsEntity) v.getTag();
                    DetailResultDialog dialog = new DetailResultDialog(getContext(), examsEntity);
                    dialog.show();
                }
            });

            return vh.rootView;
        }

        private LayoutInflater mInflater;

        // Constructors
        public CorreggiAdapter(Context context, List<ExamsEntity> objects) {
            super(context, 0, objects);
            this.mInflater = LayoutInflater.from(context);
        }
    }

    private static class DetailResultDialog extends Dialog {
        private ExamsEntity entity;

        private ImageView imageTrafficSigns;
        private TextView textViewQuesNum;
        private TextView textViewQues;
        private TextView textViewMyAnswer;
        private TextView textViewCorrectAnswer;


        public DetailResultDialog(Context context, ExamsEntity examsEntity) {
            super(context);
            this.entity = examsEntity;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            this.setCanceledOnTouchOutside(true);
            setContentView(R.layout.dialog_correggi_detail_result);
            findViews();
            if (entity.image != null) {
                imageTrafficSigns.setImageBitmap(entity.image);
            } else {
                imageTrafficSigns.setVisibility(View.GONE);
            }
            textViewQuesNum.setText(getContext().getString(R.string.question) + " " + StringUtil.formatNumber(entity.questionNo));
            textViewQues.setText(entity.question);

            if (entity.myAnswer != BaseEntity.ANSWER_NOT_CHOOSE) {
                if (entity.myAnswer == BaseEntity.ANSWER_RIGHT) {
                    textViewMyAnswer.setText(" " + getContext().getString(R.string.answer_true));
                    textViewMyAnswer.setTextColor(getContext().getResources().getColor(R.color.answer_true));
                } else {
                    textViewMyAnswer.setText(" " + getContext().getString(R.string.answer_fail));
                    textViewMyAnswer.setTextColor(getContext().getResources().getColor(R.color.answer_fail));
                }
            } else {
                textViewMyAnswer.setText("");
            }
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
            textViewQuesNum = (TextView) findViewById(R.id.textView_ques_num);
            textViewQues = (TextView) findViewById(R.id.textView_ques);
            textViewMyAnswer = (TextView) findViewById(R.id.textView_my_answer);
            textViewCorrectAnswer = (TextView) findViewById(R.id.textView_correct_answer);
        }


    }


}
