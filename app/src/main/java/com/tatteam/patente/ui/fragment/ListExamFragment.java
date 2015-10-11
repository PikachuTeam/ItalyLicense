package com.tatteam.patente.ui.fragment;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.control.UserManager;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.entity.SheetEntity;
import com.tatteam.patente.ui.widget.GroupExamDialog;
import com.tatteam.patente.utility.StringUtil;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import static com.tatteam.patente.R.id.image_shuffle;

/**
 * Created by ThanhNH on 2/10/2015.
 */
public class ListExamFragment extends BaseFragment implements GroupExamDialog.OnDialogItemClickListener {
    private static final int GROUP_EXAMS_MAX = 50;

    private String actionBarTitle;
    private int categoryId;

    private ListView listView;
    private ListExamAdapter adapter;
    private List<SheetEntity> listExam;
    private List<List<SheetEntity>> subListExam;
    private List<SheetEntity> currentListExam;
    private String[] groupExamText;

    private GroupExamDialog groupExamDialog;
    private int groupExamIndex = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_list_exam, null);
        if (listExam == null || listExam.isEmpty()) {
            loadData();
        }
        findViews(contentView);
        reloadList();
        return contentView;
    }

    private void loadData() {
        Bundle dataBundle = getArguments();
        if (dataBundle != null) {
            categoryId = dataBundle.getInt(BUNDLE_CATEGORY_ID, -1);
            actionBarTitle = dataBundle.getString(BUNDLE_TITLE_BAR, "");
        }

        this.groupExams();
        groupExamDialog = new GroupExamDialog(getActivity(), groupExamText);
        groupExamDialog.registerOnDialogItemClickListener(this);
    }


    private void reloadList() {
        if (groupExamIndex == -1) {
            currentListExam = DataSource.getInstance().getListExam(categoryId);
        } else {
            int fromSheetNo = subListExam.get(groupExamIndex).get(0).sheetNo;
            int toSheetNo = subListExam.get(groupExamIndex).get(subListExam.get(groupExamIndex).size() - 1).sheetNo;
            currentListExam = DataSource.getInstance().getListExam(categoryId, fromSheetNo, toSheetNo);
        }
        adapter = new ListExamAdapter(getActivity(), this, currentListExam);
        listView.setAdapter(adapter);
    }

    private void groupExams() {
        if (listExam == null || listExam.isEmpty()) {
            listExam = DataSource.getInstance().getListExam(categoryId);
            currentListExam = listExam;
        }
        subListExam = new ArrayList<>();
        int index = 0;
        do {
            List<SheetEntity> list;
            if (index + GROUP_EXAMS_MAX < listExam.size() - 1) {
                list = listExam.subList(index, index + GROUP_EXAMS_MAX);
                index += GROUP_EXAMS_MAX;
            } else {
                list = listExam.subList(index, listExam.size());
                index = listExam.size();
            }
            subListExam.add(list);
        } while (index <= listExam.size() - 1);
        groupExamText = new String[subListExam.size()];
        for (int i = 0; i < groupExamText.length; i++) {
            List<SheetEntity> list = subListExam.get(i);
            String text = getString(R.string.exam_num) + " " + StringUtil.formatNumber(list.get(0).sheetNo) + " - " + StringUtil.formatNumber(list.get(list.size() - 1).sheetNo);
            groupExamText[i] = text;
        }
    }

    public void switchToDoExamFragment(SheetEntity entity) {
        DoExamsFragment fragment = new DoExamsFragment();
        Bundle dataBundle = new Bundle();
        dataBundle.putInt(BUNDLE_CATEGORY_ID, entity.categoryId);
        dataBundle.putInt(BUNDLE_SHEET_NO, entity.sheetNo);
        fragment.setArguments(dataBundle);
        replaceFragment(fragment, DoExamsFragment.class.getSimpleName(), DoExamsFragment.TRANSCATION_NAME);
    }

    private void findViews(View contentView) {
        listView = (ListView) contentView.findViewById(R.id.listView);
        contentView.findViewById(R.id.layout_random_quiz).findViewById(R.id.view_highlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoExamsFragment fragment = new DoExamsFragment();
                Bundle dataBundle = new Bundle();
                dataBundle.putInt(BUNDLE_CATEGORY_ID, categoryId);
                dataBundle.putInt(BUNDLE_SHEET_NO, DataSource.getInstance().getRandomSheetNo(categoryId));
                fragment.setArguments(dataBundle);
                replaceFragment(fragment, DoExamsFragment.class.getSimpleName(), DoExamsFragment.TRANSCATION_NAME);
            }
        });
    }

    @Override
    protected void setupActionBar(ActionBar actionBar) {
        super.setupActionBar(actionBar);
        actionBar.setTitle(actionBarTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_sort).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            groupExamDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogItemClick(Dialog dialog, int position) {
        dialog.dismiss();
        groupExamIndex = position;
        reloadList();
    }

    public static class ListExamAdapter extends ArrayAdapter<SheetEntity> {

        private static class ViewHolder {
            public final RelativeLayout rootView;
            public final View viewLineLeft;
            public final ImageView imageArrow;
            public final TextView textViewName;
            public final TextView textViewLastScore;

            private ViewHolder(RelativeLayout rootView, View viewLineLeft, ImageView imageArrow, TextView textViewName, TextView textViewLastScore) {
                this.rootView = rootView;
                this.viewLineLeft = viewLineLeft;
                this.imageArrow = imageArrow;
                this.textViewName = textViewName;
                this.textViewLastScore = textViewLastScore;
            }

            public static ViewHolder create(RelativeLayout rootView) {
                View viewLineLeft = (View) rootView.findViewById(R.id.view_line_left);
                ImageView imageArrow = (ImageView) rootView.findViewById(R.id.image_arrow);
                TextView textViewName = (TextView) rootView.findViewById(R.id.textView_name);
                TextView textViewLastScore = (TextView) rootView.findViewById(R.id.textView_last_score);
                return new ViewHolder(rootView, viewLineLeft, imageArrow, textViewName, textViewLastScore);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                View view = mInflater.inflate(R.layout.item_list_exam, parent, false);
                vh = ViewHolder.create((RelativeLayout) view);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            SheetEntity entity = getItem(position);

            if (position % 2 == 0) {
                vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_1);
            } else {
                vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_2);
            }

            vh.textViewName.setText(getContext().getText(R.string.exam_num) + " " + StringUtil.formatNumber(entity.sheetNo));

            if (entity.duration == 0) {
                vh.textViewLastScore.setVisibility(View.INVISIBLE);
            } else {
                vh.textViewLastScore.setVisibility(View.VISIBLE);
//                String message = getContext().getString(R.string.last_exam_info)+" "
                vh.textViewLastScore.setText("Risultato dell'ultimo esame: " + StringUtil.formatNumber(entity.totalCorrectAnswer) + "/" + StringUtil.formatNumber(entity.totalQuestion));
            }

            View viewHeighLight = vh.rootView.findViewById(R.id.view_highlight);
            viewHeighLight.setTag(entity);
            viewHeighLight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SheetEntity entity = (SheetEntity) v.getTag();
                    fragment.switchToDoExamFragment(entity);
                }
            });
            return vh.rootView;
        }

        private LayoutInflater mInflater;
        private ListExamFragment fragment;

        // Constructors
        public ListExamAdapter(Context context, ListExamFragment fragment, List<SheetEntity> objects) {
            super(context, 0, objects);
            this.mInflater = LayoutInflater.from(context);
            this.fragment = fragment;
        }
    }

}
