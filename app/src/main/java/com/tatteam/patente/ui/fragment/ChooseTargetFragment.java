package com.tatteam.patente.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseActivity;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.billing.IabHelper;
import com.tatteam.patente.billing.IabResult;
import com.tatteam.patente.billing.Purchase;
import com.tatteam.patente.control.InAppBillingController;
import com.tatteam.patente.control.LocalSharedPreferManager;
import com.tatteam.patente.control.UserManager;
import com.tatteam.patente.ui.MainMenuAcitivty;
import com.tatteam.patente.utility.ShareUtil;

/**
 * Created by ThanhNH on 2/17/2015.
 */
public class ChooseTargetFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager viewPager;
    private TextView textViewTarget;
    private LinearLayout layoutIndicator;
    private TextView textViewRateUs;
    private TextView textViewFeedback;
    private TextView textViewRemoveAds;
    private ImageButton buttonShareFB;
    private ImageButton buttonShareTT;
    private View layoutRemoveAds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.fragment_choose_target, null);
        findViews(contentView);
        viewPager.setAdapter(new ChooseTargetPagerAdapter(getActivity()));
        viewPager.setOnPageChangeListener(this);
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void findViews(View contentView) {
        viewPager = (ViewPager) contentView.findViewById(R.id.viewPager);
        textViewTarget = (TextView) contentView.findViewById(R.id.textView_target);
        layoutIndicator = (LinearLayout) contentView.findViewById(R.id.layout_indicator);
        textViewRateUs = (TextView) contentView.findViewById(R.id.textView_rateUs);
        textViewFeedback = (TextView) contentView.findViewById(R.id.textView_Feedback);
        textViewRemoveAds = (TextView) contentView.findViewById(R.id.textView_removeAds);
        buttonShareFB = (ImageButton) contentView.findViewById(R.id.button_shareFB);
        buttonShareTT = (ImageButton) contentView.findViewById(R.id.button_shareTT);
        layoutRemoveAds = contentView.findViewById(R.id.layout_removeAds);

        buttonShareFB.setOnClickListener(this);
        buttonShareTT.setOnClickListener(this);
        textViewRateUs.setOnClickListener(this);
        textViewFeedback.setOnClickListener(this);
        layoutRemoveAds.setOnClickListener(this);

        //set underline text
        textViewRateUs.setPaintFlags(textViewRateUs.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewFeedback.setPaintFlags(textViewFeedback.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewRemoveAds.setPaintFlags(textViewFeedback.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (LocalSharedPreferManager.getInstance().isPurchased()) {
//            layoutRemoveAds.setVisibility(View.GONE);
        } else {
            layoutRemoveAds.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonShareFB) {
        } else if (v == buttonShareTT) {
        } else if (v == textViewRateUs) {
            ShareUtil.rateApplication(getActivity());
        } else if (v == textViewFeedback) {
            ShareUtil.shareToGMail(getActivity(), new String[]{ShareUtil.MAIL_ADDRESS_DEFAULT}, getString(R.string.subject_mail_feedback), "");
        } else if (v == layoutRemoveAds) {
            if (!LocalSharedPreferManager.getInstance().isPurchased()) {
                this.requestBuyItem();
            }
        }
    }

    private void requestBuyItem() {
        InAppBillingController.getInstace().launchPurchaseFlow(getActivity(), InAppBillingController.SKU_REMOVE_ADS, new IabHelper.OnIabPurchaseFinishedListener() {
            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                if (result.isFailure()) {
                    return;
                } else {
                    if (info.getSku().equals(InAppBillingController.SKU_REMOVE_ADS)) {
                        LocalSharedPreferManager.getInstance().setIsPurchased(true);
//                        layoutRemoveAds.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == 0) {
            textViewTarget.setText(R.string.b);
            layoutIndicator.getChildAt(0).setBackgroundResource(R.drawable.bg_indicator_active);
            layoutIndicator.getChildAt(1).setBackgroundResource(R.drawable.bg_indicator_inactive);
        } else {
            textViewTarget.setText(R.string.am);
            layoutIndicator.getChildAt(1).setBackgroundResource(R.drawable.bg_indicator_active);
            layoutIndicator.getChildAt(0).setBackgroundResource(R.drawable.bg_indicator_inactive);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    private static class ChooseTargetPagerAdapter extends PagerAdapter {
        private Context context;

        public ChooseTargetPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(context, R.layout.item_choose_target, null);
            ImageView image_target = (ImageView) view.findViewById(R.id.image_tagert);
            if (position == 0) {
                image_target.setImageResource(R.drawable.target_b);
            } else {
                image_target.setImageResource(R.drawable.target_am);
            }
            View view_highlight = view.findViewById(R.id.view_highlight);
            view_highlight.setTag(position);
            view_highlight.setOnClickListener(new ItemViewPagerOnClickListener(context));
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

        private static class ItemViewPagerOnClickListener implements View.OnClickListener {
            private Context context;

            private ItemViewPagerOnClickListener(Context context) {
                this.context = context;
            }

            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                if (index == 0) {
                    UserManager.getInstance().setLicenseType(UserManager.LICENSE_TYPE_B);
                } else {
                    UserManager.getInstance().setLicenseType(UserManager.LICENSE_TYPE_AM);
                }
                Intent intent = new Intent(context, MainMenuAcitivty.class);
                BaseActivity.startActivityAnimation((Activity) context, intent);
            }
        }
    }
}
