package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import br.com.endcraft.me.endcraft.serie.Series;

/**
 * Created by Jonas on 08.abr.2018.
 */

public class VideoPremiado implements RewardedVideoAdListener {

    private  String url;
    private long seek;
    private String name;
    private Activity activity;
    private Movie movie;
    private Series series;

    public VideoPremiado(String url, long seek, String name, Activity activity, Movie movie, Series series) {
        this.url = url;
        this.seek = seek;
        this.name = name;
        this.activity = activity;
        this.movie = movie;
        this.series = series;
    }

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
    public void onRewardedVideoAdClosed() {
        Filmes.loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Filmes.openVideo(url, seek, name, activity, movie, series);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }
}
