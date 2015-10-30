package com.tatteam.patente.ui.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.entity.BaseEntity;

import java.util.List;

/**
 * Created by ThanhNH on 2/7/2015.
 */
public class GridDetailArgomentoFragment extends BaseFragment {

    private List<BaseEntity> listArgomento;

    private GridView gridView;
    private GridDetailArgomentoAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_detail_argomento_grid, null);
        if (listArgomento == null || listArgomento.isEmpty()) {
            loadData();
        }
        gridView = (GridView) contentView.findViewById(R.id.gridView);
        adapter = new GridDetailArgomentoAdapter(getActivity(), this, listArgomento);
        gridView.setAdapter(adapter);
        return contentView;
    }

    private void loadData() {
        Bundle dataBundle = getArguments();
        if (dataBundle != null) {
            int categoryId = dataBundle.getInt(BUNDLE_CATEGORY_ID,-1);
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
        dataBundle.putString(BUNDLE_CATEGORY_NAME,entity.name);
        fragment.setArguments(dataBundle);
        replaceFragment(fragment);
    }



    public static class GridDetailArgomentoAdapter extends ArrayAdapter<BaseEntity> {


        private static class ViewHolder {
            public final View rootView;
            public final ImageView imageTrafficSigns;
            public final TextView textViewName;

            private ViewHolder(View rootView, ImageView imageTrafficSigns, TextView textViewName) {
                this.rootView = rootView;
                this.imageTrafficSigns = imageTrafficSigns;
                this.textViewName = textViewName;
            }

            public static ViewHolder create(View rootView) {
                ImageView imageTrafficSigns = (ImageView) rootView.findViewById(R.id.image_traffic_signs);
                TextView textViewName = (TextView) rootView.findViewById(R.id.textView_name);
                return new ViewHolder(rootView, imageTrafficSigns, textViewName);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                View view = mInflater.inflate(R.layout.item_detail_argomento_grid, parent, false);
                vh = ViewHolder.create(view);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            BaseEntity entity = getItem(position);
            vh.textViewName.setText(entity.name);
            vh.imageTrafficSigns.setImageBitmap(entity.image);

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
        private GridDetailArgomentoFragment fragment;

        // Constructors
        public GridDetailArgomentoAdapter(Context context, GridDetailArgomentoFragment fragment, List<BaseEntity> objects) {
            super(context, 0, objects);
            this.mInflater = LayoutInflater.from(context);
            this.fragment = fragment;
        }
    }

}