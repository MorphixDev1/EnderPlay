package br.com.endcraft.me.endcraft.Managers;

import android.util.Log;

import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by JonasXPX on 28.jul.2017.
 */

public abstract class AdManager implements RewardedVideoAdListener {


    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public abstract void onRewardedVideoAdClosed();

    @Override
    public abstract void onRewarded(RewardItem rewardItem);

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.d("AD", "AD video failed to load");
    }
}
