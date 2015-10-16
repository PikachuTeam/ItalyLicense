package com.tatteam.patente.ui.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.control.UserManager;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.entity.BaseEntity;
import com.tatteam.patente.entity.ExamsEntity;
import com.tatteam.patente.ui.widget.ConfirmDialog;
import com.tatteam.patente.ui.widget.QuesNoItemWrapper;
import com.tatteam.patente.utility.StringUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanhNH on 2/4/2015.
 */
public class DoExamsFragment extends BaseFragment implements QuesNoItemWrapper.OnItemQuesClickListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String TRANSCATION_NAME = "To DoExams";

    private static final int EXAMS_DURATION_AM = 25 * 60;
    private static final int EXAMS_DURATION_B = 30 * 60;
    private static final int EXAMS_WARNING_TIME = 5 * 60;

    private int menuId;
    private int sheetNo;

    private List<ExamsEntity> listExams;

    private HorizontalScrollView horizontalScrollView;
    private LinearLayout layoutScrollContent;
    private ViewPager viewPager;
    private TextView textViewSecond;
    private TextView textViewMin;
    private TextView textViewTotal;

    private ExamPagerAdapter adapter;

    private List<QuesNoItemWrapper> listItemQues;

    private CountDownTimer timer;
    private int countDown;

    private Button buttonTrue;
    private Button buttonFail;
    private Button buttonFinish;

    private int currentQuesIndex;
    private int total;

    private int examTotalDuration;
    private ConfirmDialog exitDialog;
    private ConfirmDialog finishDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_doexams, null);
        if (listExams == null || listExams.isEmpty()) {
            loadData();
        }
        findViews(contentView);
        addQuestionList();
        examTotalDuration = UserManager.getInstance().isLicenseTypeB() ? EXAMS_DURATION_B : EXAMS_DURATION_AM;
        startTimer();

        adapter = new ExamPagerAdapter(getActivity(), listExams);
        viewPager.setAdapter(adapter);
        updateTotal();
        return contentView;
    }

    private void loadData() {
        Bundle dataBundle = getArguments();
        if (dataBundle != null) {
            menuId = dataBundle.getInt(BUNDLE_CATEGORY_ID, -1);
            sheetNo = dataBundle.getInt(BUNDLE_SHEET_NO, -1);
            listExams = DataSource.getInstance().getExamList(menuId, sheetNo);
        }
        listItemQues = new ArrayList<>();
        this.setupDialog();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_finish).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            showFinishDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onBackPressed() {
        exitDialog.show();
        return true;
    }

    @Override
    public void onDestroyView() {
        stopTimer();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startTimer() {
        countDown = examTotalDuration + 1;
        timer = new CountDownTimer(Integer.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDown--;
                if (countDown > 0) {
                    int min = countDown / 60;
                    int second = countDown - (min * 60);
                    textViewMin.setText(StringUtil.formatNumber(min));
                    textViewSecond.setText(StringUtil.formatNumber(second));
                    if (countDown >= EXAMS_WARNING_TIME) {
                        textViewMin.setTextColor(getResources().getColor(R.color.do_exam_min_normal));
                    } else {
                        textViewMin.setTextColor(getResources().getColor(R.color.do_exam_min_warning));
                    }
                } else {
                    timer.cancel();
                    switchToCorreggiFragment();
                }
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void addQuestionList() {
        for (int i = 0; i < listExams.size(); i++) {
            QuesNoItemWrapper quesNoItem = new QuesNoItemWrapper(getActivity());
            quesNoItem.setText(StringUtil.formatNumber(listExams.get(i).questionNo));
            quesNoItem.registerOnItemQuesClickListener(this);
            layoutScrollContent.addView(quesNoItem.getView());
            listItemQues.add(quesNoItem);
            if (i == 0) {
                quesNoItem.setActive(true);
            }
        }
        currentQuesIndex = 0;
    }

    private void findViews(View parent) {
        horizontalScrollView = (HorizontalScrollView) parent.findViewById(R.id.horizontalScrollView);
        layoutScrollContent = (LinearLayout) parent.findViewById(R.id.layout_scroll_content);
        textViewSecond = (TextView) parent.findViewById(R.id.textView_second);
        textViewMin = (TextView) parent.findViewById(R.id.textView_min);
        textViewTotal = (TextView) parent.findViewById(R.id.textView_total);

        viewPager = (ViewPager) parent.findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(this);

        buttonTrue = (Button) parent.findViewById(R.id.button_true);
        buttonFail = (Button) parent.findViewById(R.id.button_fail);
        buttonFinish = (Button) parent.findViewById(R.id.button_finish);

        buttonTrue.setOnClickListener(this);
        buttonFail.setOnClickListener(this);
        buttonFinish.setOnClickListener(this);
    }

    private void setupDialog() {
        exitDialog = new ConfirmDialog(getActivity(), null, getString(R.string.exit_message));
        exitDialog.registerOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void onDialogConfirmed(ConfirmDialog dialog, int code) {
                if (code == ConfirmDialog.CONFIRM_OK) {
                    getFragmentManager().popBackStack();
                }
            }
        });


    }

    private void showFinishDialog() {
        String message = MessageFormat.format(getString(R.string.dialog_finish_message), StringUtil.formatNumber(total), String.valueOf(listExams.size()));
        finishDialog = new ConfirmDialog(getActivity(), getString(R.string.dialog_finish_title), message);
        finishDialog.registerOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void onDialogConfirmed(ConfirmDialog dialog, int code) {
                if (code == ConfirmDialog.CONFIRM_OK) {
                    switchToCorreggiFragment();
                }
            }
        });
        finishDialog.show();
    }

    @Override
    protected void setupActionBar(ActionBar actionBar) {
        super.setupActionBar(actionBar);
        actionBar.setTitle(getString(R.string.exam_num) + " " + StringUtil.formatNumber(sheetNo));
    }

    @Override
    public void onItemQuesClick(QuesNoItemWrapper quesNoItemWrapper) {
        setAllQuesNoItemInActive();
        quesNoItemWrapper.setActive(true);
        scrollToCenter(quesNoItemWrapper);

        int index = listItemQues.indexOf(quesNoItemWrapper);
        viewPager.setCurrentItem(index, true);

        currentQuesIndex = index;
        updateAnswerButtonState();
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        setAllQuesNoItemInActive();
        QuesNoItemWrapper quesNoItemWrapper = listItemQues.get(i);
        quesNoItemWrapper.setActive(true);
        scrollToCenter(quesNoItemWrapper);

        currentQuesIndex = i;
        updateAnswerButtonState();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void scrollToCenter(int index) {
        scrollToCenter(listItemQues.get(index));
    }

    private void scrollToCenter(QuesNoItemWrapper quesNoItemWrapper) {
        int centerX = horizontalScrollView.getWidth() / 2;
        int[] itemPos = new int[]{0, 0};
        quesNoItemWrapper.getView().getLocationOnScreen(itemPos);
        int x = itemPos[0];
        int offset = x - centerX + quesNoItemWrapper.getView().getWidth() / 2;
        horizontalScrollView.smoothScrollTo(horizontalScrollView.getScrollX() + offset, 0);
    }

    private void setAllQuesNoItemInActive() {
        for (QuesNoItemWrapper quesNoItemWrapper : listItemQues) {
            quesNoItemWrapper.setActive(false);
        }
    }


    @Override
    public void onClick(View v) {
        ExamsEntity examsEntity = listExams.get(currentQuesIndex);
        if (v == buttonTrue) {
            if (examsEntity.myAnswer != BaseEntity.ANSWER_RIGHT) {
                if (examsEntity.myAnswer == BaseEntity.ANSWER_NOT_CHOOSE) {
                    total++;
                    updateTotal();
                }
                examsEntity.myAnswer = BaseEntity.ANSWER_RIGHT;
                updateAnswerButtonState();
                listItemQues.get(currentQuesIndex).setHighLight();
            }
            nextQuestion();
        } else if (v == buttonFail) {
            if (examsEntity.myAnswer != BaseEntity.ANSWER_WRONG) {
                if (examsEntity.myAnswer == BaseEntity.ANSWER_NOT_CHOOSE) {
                    total++;
                    updateTotal();
                }
                examsEntity.myAnswer = BaseEntity.ANSWER_WRONG;
                updateAnswerButtonState();
                listItemQues.get(currentQuesIndex).setHighLight();
            }
            nextQuestion();
        } else if (v == buttonFinish) {
            showFinishDialog();
        }
    }

    private void switchToCorreggiFragment() {
        if (exitDialog != null && exitDialog.isShowing()) {
            exitDialog.dismiss();
        }
        if (finishDialog != null && finishDialog.isShowing()) {
            finishDialog.dismiss();
        }

        int[] answers = new int[listExams.size()];
        for (int i = 0; i < answers.length; i++) {
            answers[i] = listExams.get(i).myAnswer;
        }

        final CorreggiFragment fragment = new CorreggiFragment();
        Bundle dataBundle = new Bundle();
        dataBundle.putIntArray(BUNDLE_ANSWER, answers);
        dataBundle.putInt(BUNDLE_CATEGORY_ID, menuId);
        dataBundle.putInt(BUNDLE_SHEET_NO, sheetNo);
        dataBundle.putInt(BUNDLE_TIME, examTotalDuration - countDown);
        fragment.setArguments(dataBundle);
        replaceFragment(fragment, true);
    }

    private void updateTotal() {
        String message = getActivity().getResources().getString(R.string.total) + ": " + StringUtil.formatNumber(total) + "/" + String.valueOf(listExams.size());
        textViewTotal.setText(message);
    }

    private void updateAnswerButtonState() {
        if (listExams.get(currentQuesIndex).myAnswer == BaseEntity.ANSWER_RIGHT) {
            setActiveButtonTrue(true);
            setActiveButtonFail(false);
        } else if (listExams.get(currentQuesIndex).myAnswer == BaseEntity.ANSWER_WRONG) {
            setActiveButtonFail(true);
            setActiveButtonTrue(false);
        } else {
            setActiveButtonFail(false);
            setActiveButtonTrue(false);
        }
    }

    private void nextQuestion() {
        if (currentQuesIndex < listExams.size() - 1) {
            viewPager.setCurrentItem(currentQuesIndex + 1, true);
        }
    }


    private void setActiveButtonTrue(boolean active) {
        if (active) {
            buttonTrue.setSelected(true);
            buttonTrue.setBackgroundResource(R.drawable.bg_button_true_active);
        } else {
            buttonTrue.setSelected(false);
            buttonTrue.setBackgroundResource(R.drawable.selector_button_true);
        }
    }

    private void setActiveButtonFail(boolean active) {
        if (active) {
            buttonFail.setSelected(true);
            buttonFail.setBackgroundResource(R.drawable.bg_button_fail_active);
        } else {
            buttonFail.setSelected(false);
            buttonFail.setBackgroundResource(R.drawable.selector_button_fail);
        }
    }


    private class ExamPagerAdapter extends PagerAdapter {
        private List<ExamsEntity> data;
        private Context ctx;

        public ExamPagerAdapter(Context ctx, List<ExamsEntity> data) {
            this.ctx = ctx;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(ctx, R.layout.item_do_exam_content, null);
            ExamsEntity entity = data.get(position);
            TextView textViewTarget = (TextView) view.findViewById(R.id.textView_target);
            ImageView imageViewTarget = (ImageView) view.findViewById(R.id.image_tagert);
            textViewTarget.setText(entity.question);
            if (entity.image != null) {
                imageViewTarget.setVisibility(View.VISIBLE);
                imageViewTarget.setImageBitmap(entity.image);
            } else {
                imageViewTarget.setVisibility(View.INVISIBLE);
            }
            ((ViewPager) container).addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
