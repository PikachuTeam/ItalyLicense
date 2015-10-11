package com.tatteam.patente.utility;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;
import com.tatteam.patente.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ThanhNH on 2/17/2015.
 */
public class SharingControler implements SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener {
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    private SocialNetworkManager mSocialNetworkManager;
    private Fragment fragment;

    private String message;

    public SharingControler(Fragment fragment) {
        this.fragment = fragment;
    }

    public void connect() {
        try {
            String TWITTER_CONSUMER_KEY = fragment.getString(R.string.twitter_consumer_key);
            String TWITTER_CONSUMER_SECRET = fragment.getString(R.string.twitter_consumer_secret);
            String TWITTER_CALLBACK_URL = "oauth://ASNE";

            ArrayList<String> fbScope = new ArrayList<String>();
            fbScope.addAll(Arrays.asList("public_profile, email, user_friends"));

            //Use manager to manage SocialNetworks
            mSocialNetworkManager = (SocialNetworkManager) fragment.getFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
            mSocialNetworkManager = new SocialNetworkManager();

            //Init and add to manager FacebookSocialNetwork
            FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(fragment, fbScope);
            mSocialNetworkManager.addSocialNetwork(fbNetwork);

            //Init and add to manager TwitterSocialNetwork
            TwitterSocialNetwork twNetwork = new TwitterSocialNetwork(fragment, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, TWITTER_CALLBACK_URL);
            mSocialNetworkManager.addSocialNetwork(twNetwork);

            //Initiate every network from mSocialNetworkManager
            fragment.getFragmentManager().beginTransaction().add(mSocialNetworkManager, SOCIAL_NETWORK_TAG).commit();

            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } catch (Exception ex) {

        }

    }

    public void disconnect() {
        try {
            if (!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.logout();
                }
            }
            fragment.getFragmentManager().beginTransaction().remove(mSocialNetworkManager);
        } catch (Exception ex) {

        }

    }

    public void share(int networkId, String message) {
        this.message = message;
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        if (socialNetwork != null) {
            try {
                if (!socialNetwork.isConnected()) {
                    socialNetwork.requestLogin();
                } else {
                    performShare(socialNetwork, networkId, message);
                }
            } catch (Exception ex) {
            }
        }
    }


    @Override
    public void onSocialNetworkManagerInitialized() {
        try {
            for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
                socialNetwork.setOnLoginCompleteListener(this);
            }
        } catch (Exception ex) {

        }

    }

    @Override
    public void onLoginSuccess(int i) {
        try {
            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(i);
            performShare(socialNetwork, i, message);
        } catch (Exception ex) {
        }
    }

    @Override
    public void onError(int i, String s, String s2, Object o) {

    }

    private void performShare(SocialNetwork socialNetwork, final int networkId, String message) {
        try {
            socialNetwork.requestPostMessage(message, new OnPostingCompleteListener() {
                @Override
                public void onPostSuccessfully(int i) {
                    String toastMessage = "Shared on your Facebook";
                    if (networkId == TwitterSocialNetwork.ID) {
                        toastMessage = "Shared on your Twitter";
                    }
                    Toast.makeText(fragment.getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(int i, String s, String s2, Object o) {
                    Toast.makeText(fragment.getActivity(), "Try next time", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
        }
    }
}