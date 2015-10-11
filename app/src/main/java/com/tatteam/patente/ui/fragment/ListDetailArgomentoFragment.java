package com.tatteam.patente.ui.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.entity.BaseEntity;

import java.util.List;

/**
 * Created by ThanhNH on 2/6/2015.
 */
public class ListDetailArgomentoFragment extends BaseFragment {

    private List<BaseEntity> listArgomento;

    private ListView listView;
    private DetailArgomentoAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_detail_argomento_list, null);
        if (listArgomento == null || listArgomento.isEmpty()) {
            loadData();
        }
        listView = (ListView) contentView.findViewById(R.id.listView);
        adapter = new DetailArgomentoAdapter(getActivity(), this, listArgomento);
        listView.setAdapter(adapter);
        return contentView;
    }

    private void loadData() {
        Bundle dataBundle = getArguments();
        if (dataBundle != null) {
            int categoryId = dataBundle.getInt(BUNDLE_CATEGORY_ID, -1);
            listArgomento = DataSource.getInstance().getCategories(categoryId);
        }
    }

    @Override
    protected void setupActionBar(ActionBar actionBar) {
        super.setupActionBar(actionBar);
        actionBar.setTitle(R.string.topic);
    }

    public void switchToTipArgomentoFragment(BaseEntity entity) {
        TipArgomentoFragment fragment = new TipArgomentoFragment();
        Bundle dataBundle = new Bundle();
        dataBundle.putInt(BUNDLE_CATEGORY_ID, entity.id);
        dataBundle.putString(BUNDLE_CATEGORY_NAME, entity.name);
        fragment.setArguments(dataBundle);
        replaceFragment(fragment);
    }


    public static class DetailArgomentoAdapter extends ArrayAdapter<BaseEntity> {
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
            final ViewHolder vh;
            if (convertView == null) {
                View view = mInflater.inflate(R.layout.item_detail_argomento_list, parent, false);
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
            // set OnItemClick
            View viewHeighLight = vh.rootView.findViewById(R.id.view_highlight);
            viewHeighLight.setTag(entity);
            viewHeighLight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseEntity entity = (BaseEntity) v.getTag();
                    fragment.switchToTipArgomentoFragment(entity);
                }
            });
            return vh.rootView;
        }

        private LayoutInflater mInflater;
        private ListDetailArgomentoFragment fragment;

        // Constructors
        public DetailArgomentoAdapter(Context context, ListDetailArgomentoFragment fragment, List<BaseEntity> objects) {
            super(context, 0, objects);
            this.mInflater = LayoutInflater.from(context);
            this.fragment = fragment;
        }

    }
}
