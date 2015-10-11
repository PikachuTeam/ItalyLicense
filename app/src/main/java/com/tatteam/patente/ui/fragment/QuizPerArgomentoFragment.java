package com.tatteam.patente.ui.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.control.UserManager;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.entity.QuizPerArgomentoEntity;

import java.util.List;

/**
 * Created by tnguyenhuy on 2/5/2015.
 */
public class QuizPerArgomentoFragment extends BaseFragment {

    private List<QuizPerArgomentoEntity> argomentoList;
    private ListView listView;
    private QuizPerArgomenAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_quiz_per_argomento, null);

        if(argomentoList == null || argomentoList.isEmpty()){
            loadData();
        }

        listView = (ListView) contentView.findViewById(R.id.listView);
        adapter = new QuizPerArgomenAdapter(getActivity(), this, argomentoList);
        listView.setAdapter(adapter);
        return contentView;
    }

    @Override
    protected void setupActionBar(ActionBar actionBar) {
        super.setupActionBar(actionBar);
        int stringRes = (UserManager.getInstance().isLicenseTypeB()) ? R.string.patente_b : R.string.patente_am;
        actionBar.setTitle(stringRes);
    }

    private void loadData(){
        int parentId = (UserManager.getInstance().isLicenseTypeB()) ? DataSource.B_PARENT_ID_Quiz_per_argomento : DataSource.AM_PARENT_ID_Quiz_per_argomento;
        argomentoList = DataSource.getInstance().getQuizPerArgomento(parentId);
        if (UserManager.getInstance().isLicenseTypeB()) {
            int size = argomentoList.size();
            for (int i = size - 1; i >= 0; i--) {
                QuizPerArgomentoEntity entity = argomentoList.get(i);
                entity.isSection = true;
                List<QuizPerArgomentoEntity> argomentoSubList = DataSource.getInstance().getQuizPerArgomento(entity.id);
                argomentoList.addAll(i + 1, argomentoSubList);
            }
        }
    }

    public void switchToListExamFragment(QuizPerArgomentoEntity entity){
        ListExamFragment fragment = new ListExamFragment();
        Bundle dataBundle = new Bundle();
        dataBundle.putInt(BUNDLE_CATEGORY_ID, entity.id);
        dataBundle.putString(BUNDLE_TITLE_BAR, getString(R.string.topic));
        fragment.setArguments(dataBundle);
        replaceFragment(fragment);
    }

    public static class QuizPerArgomenAdapter extends ArrayAdapter<QuizPerArgomentoEntity> {

        private static class ViewHolder {
            public final RelativeLayout rootView;
            public final View viewLineLeft;
            public final ImageView imageTrafficSigns;
            public final ImageView imageArrow;
            public final TextView textViewName;

            private ViewHolder(RelativeLayout rootView, View viewLineLeft, ImageView imageTrafficSigns, ImageView imageArrow, TextView textViewName) {
                this.rootView = rootView;
                this.viewLineLeft = viewLineLeft;
                this.imageTrafficSigns = imageTrafficSigns;
                this.imageArrow = imageArrow;
                this.textViewName = textViewName;
            }

            public static ViewHolder create(RelativeLayout rootView) {
                View viewLineLeft = (View) rootView.findViewById(R.id.view_line_left);
                ImageView imageTrafficSigns = (ImageView) rootView.findViewById(R.id.image_traffic_signs);
                ImageView imageArrow = (ImageView) rootView.findViewById(R.id.image_arrow);
                TextView textViewName = (TextView) rootView.findViewById(R.id.textView_name);
                return new ViewHolder(rootView, viewLineLeft, imageTrafficSigns, imageArrow, textViewName);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            QuizPerArgomentoEntity entity = getItem(position);
            if (entity.isSection) {
                View sectionView = mInflater.inflate(R.layout.item_quiz_per_argomento_section, parent, false);
                if (position % 2 == 0) {
                    sectionView.findViewById(R.id.view_line_left).setBackgroundResource(R.color.item_list_line_in_left_1);
                } else {
                    sectionView.findViewById(R.id.view_line_left).setBackgroundResource(R.color.item_list_line_in_left_2);
                }
                TextView textViewSection = (TextView) sectionView.findViewById(R.id.textView_name_section);
                textViewSection.setText(entity.name);
                return sectionView;
            } else {
                final ViewHolder vh;
                View view;
                if (entity.image != null) {
                    view = mInflater.inflate(R.layout.item_quiz_per_argomento, parent, false);
                    vh = ViewHolder.create((RelativeLayout) view);
                } else {
                    view = mInflater.inflate(R.layout.item_quiz_per_argomento_no_image, parent, false);
                    vh = ViewHolder.create((RelativeLayout) view);
                }

                if (position % 2 == 0) {
                    vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_1);
                } else {
                    vh.viewLineLeft.setBackgroundResource(R.color.item_list_line_in_left_2);
                }
                vh.textViewName.setText(entity.name);
                if (entity.image != null) {
                    vh.imageTrafficSigns.setVisibility(View.VISIBLE);
                    vh.imageTrafficSigns.setImageBitmap(entity.image);
                }
                // set OnItemClick
                View viewHeighLight = view.findViewById(R.id.view_highlight);
                viewHeighLight.setTag(entity);
                viewHeighLight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuizPerArgomentoEntity entity = (QuizPerArgomentoEntity) v.getTag();
                        fragment.switchToListExamFragment(entity);
                    }
                });
                return vh.rootView;
            }

        }

        private LayoutInflater mInflater;
        private QuizPerArgomentoFragment fragment;

        // Constructors
        public QuizPerArgomenAdapter(Context context, QuizPerArgomentoFragment fragment, List<QuizPerArgomentoEntity> objects) {
            super(context, 0, objects);
            this.mInflater = LayoutInflater.from(context);
            this.fragment = fragment;
        }

    }


}
