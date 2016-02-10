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

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.control.UserManager;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.entity.BaseEntity;

import java.util.List;

/**
 * Created by ThanhNH on 2/5/2015.
 */
public class ListaDelleDomandeFragment extends BaseFragment {

    private List<BaseEntity> delleDomandeList;
    private ListView listView;
    private ListaDelleDomandeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_lista_delle_domande, null);
        if (delleDomandeList == null || delleDomandeList.isEmpty()) {
            loadData();
        }
        listView = (ListView) contentView.findViewById(R.id.listView);
        adapter = new ListaDelleDomandeAdapter(getActivity(), this, delleDomandeList);
        listView.setAdapter(adapter);
        return contentView;
    }

    private void loadData() {
        int parentId = (UserManager.getInstance().isLicenseTypeB()) ? DataSource.B_PARENT_ID_Lista_delle_domande : DataSource.AM_PARENT_ID_Lista_delle_domande;
        delleDomandeList = DataSource.getCategories(parentId);
    }

    @Override
    protected void setupActionBar(ActionBar actionBar) {
        super.setupActionBar(actionBar);
//        int stringRes = (UserManager.getInstance().isLicenseTypeB()) ? R.string.patente_b : R.string.patente_am;
//        actionBar.setTitle(stringRes);
        actionBar.setTitle(R.string.title_main_menu_item_3);
    }


    public void switchToDetailArgomentoFragment(int argomentoId) {
        Bundle dataBundle = new Bundle();
        dataBundle.putInt(BUNDLE_CATEGORY_ID, argomentoId);
        if (DataSource.havePictureOnDetailArgomento(argomentoId)) {
            GridDetailArgomentoFragment argomentoFragment = new GridDetailArgomentoFragment();
            argomentoFragment.setArguments(dataBundle);
            replaceFragment(argomentoFragment);
        } else {
            ListDetailArgomentoFragment argomentoFragment = new ListDetailArgomentoFragment();
            argomentoFragment.setArguments(dataBundle);
            replaceFragment(argomentoFragment);
        }
    }

    public static class ListaDelleDomandeAdapter extends ArrayAdapter<BaseEntity> {
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
                View view = mInflater.inflate(R.layout.item_quiz_per_argomento, parent, false);
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
                    BaseEntity entity = (BaseEntity) v.getTag();
                    fragment.switchToDetailArgomentoFragment(entity.id);
                }
            });
            return vh.rootView;
        }

        private LayoutInflater mInflater;
        private ListaDelleDomandeFragment fragment;

        // Constructors
        public ListaDelleDomandeAdapter(Context context, ListaDelleDomandeFragment fragment, List<BaseEntity> objects) {
            super(context, 0, objects);
            this.mInflater = LayoutInflater.from(context);
            this.fragment = fragment;
        }

    }

}
