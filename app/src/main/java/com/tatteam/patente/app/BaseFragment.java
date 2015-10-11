package com.tatteam.patente.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tatteam.patente.R;

/**
 * Created by ThanhNH on 2/3/2015.
 */
public class BaseFragment extends Fragment {

    public static final String BUNDLE_CATEGORY_NAME = "category_name";
    public static final String BUNDLE_CATEGORY_ID = "category_id";
    public static final String BUNDLE_SHEET_NO = "sheet_no";
    public static final String BUNDLE_TITLE_BAR = "title_bar";
    public static final String BUNDLE_TIME = "time";
    public static final String BUNDLE_ANSWER = "answer";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setupActionBar(getActionBar());
        ((BaseActivity) getActivity()).loadADIfNeed();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public boolean onBackPressed() {
        return false;
    }


    protected void setupActionBar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_sort).setVisible(false);
        menu.findItem(R.id.action_finish).setVisible(false);
    }

    public ActionBar getActionBar() {
        return this.getActivity().getActionBar();
    }

    public void replaceFragment(BaseFragment newFragment) {
        replaceFragment(newFragment, newFragment.getClass().getName(), null);
    }

    public void replaceFragment(BaseFragment newFragment, String fragmentTag, String transactionName) {
        replaceFragment(getFragmentManager(), newFragment, fragmentTag, transactionName);
    }

    public static void replaceFragment(FragmentManager fragmentManager, BaseFragment newFragment, String fragmentTag,
                                       String transactionName) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
        transaction.replace(R.id.container, newFragment, fragmentTag);
        transaction.addToBackStack(transactionName);
        transaction.commit();
    }


}
