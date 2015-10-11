package com.tatteam.patente.ui.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.control.UserManager;
import com.tatteam.patente.database.DataSource;

/**
 * Created by ThanhNH on 2/3/2015.
 */
public class MainMenuFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_mainmenu, null);
        findViews(contentView);


        return contentView;
    }

    private void findViews(View root) {
        root.findViewById(R.id.layout_menu_1).findViewById(R.id.view_highlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListExamFragment fragment = new ListExamFragment();
                int categoryId = UserManager.getInstance().isLicenseTypeB() ? DataSource.B_PARENT_ID_Simulazione_quiz : DataSource.AM_PARENT_ID_Simulazione_quiz;
                String actionBarTitle = UserManager.getInstance().isLicenseTypeB() ? getString(R.string.patente_b) : getString(R.string.patente_am);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt(BUNDLE_CATEGORY_ID, categoryId);
                dataBundle.putString(BUNDLE_TITLE_BAR, actionBarTitle);
                fragment.setArguments(dataBundle);
                replaceFragment(fragment);
            }
        });
        root.findViewById(R.id.layout_menu_2).findViewById(R.id.view_highlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new QuizPerArgomentoFragment());
            }
        });
        root.findViewById(R.id.layout_menu_3).findViewById(R.id.view_highlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ListaDelleDomandeFragment());
            }
        });
        root.findViewById(R.id.button_quick_exams).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoExamsFragment fragment = new DoExamsFragment();
                int categoryId = UserManager.getInstance().isLicenseTypeB() ? DataSource.B_PARENT_ID_Simulazione_quiz : DataSource.AM_PARENT_ID_Simulazione_quiz;
                int sheetNo = DataSource.getInstance().getRandomSheetNo(categoryId);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt(BUNDLE_CATEGORY_ID,categoryId);
                dataBundle.putInt(BUNDLE_SHEET_NO,sheetNo);
                fragment.setArguments(dataBundle);
                replaceFragment(fragment,DoExamsFragment.class.getSimpleName(),DoExamsFragment.TRANSCATION_NAME);
            }
        });
    }

    @Override
    protected void setupActionBar(ActionBar actionBar) {
        super.setupActionBar(actionBar);
        actionBar.setTitle(R.string.home);
    }

}
