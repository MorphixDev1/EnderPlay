package br.com.endcraft.me.endcraft;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import br.com.endcraft.me.endcraft.Managers.DataMovie;

/**
 * Created by JonasXPX on 18.jul.2017.
 */
public class Play extends AppCompatActivity {

    private FullscreenVideoLayout l;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private final static String TAG = "PLAYER";
    private LoopingMediaSource loopingSource;
    private Play instance;
    private long seek;
    private String movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.play);
        instance = this;


        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector( videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoPlayer);

        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

        simpleExoPlayerView.setPlayer(player);

        seek = getIntent().getLongExtra("seek", 0);
        movie = getIntent().getStringExtra("movie");

        Log.d("LOG","\n\n\n\n\n\n\n\n\n\n\n\n\n"+seek + " --- " + movie);
        play(Filmes.url_final);

    }
    public void play(String url){
        Uri videoUri = Uri.parse(url.replaceAll(" ", "%20"));
        Log.d("LOG", "TRY TO PLAY: " + videoUri);
        Log.d("LOG", "MATCHES: " + url.matches("http[:s].*"));
        try{
            DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "EnderFilmes"), bandwidthMeterA);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource videoSource;
            if(url.endsWith(".m3u8")){
                videoSource = new HlsMediaSource(videoUri, dataSourceFactory, null, null);
            } else {
                videoSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
            }
            loopingSource = new LoopingMediaSource(videoSource);
            player.prepare(loopingSource);
            player.seekTo(seek);
            player.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {
                    Log.v(TAG,"Listener-onTimelineChanged... TO: " + seek);
                    player.seekTo(seek);
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                    Log.v(TAG,"Listener-onTracksChanged...");

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    Log.v(TAG,"Listener-onLoadingChanged...");
                    hideSystemUI();
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Log.v(TAG,"Listener-onPlayerStateChanged...");

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    if(error.getMessage() == null){
                            Toast.makeText(instance, "Erro ao executar o filme", Toast.LENGTH_LONG);
                            instance.finish();
                        return;
                    }
                    Log.v(TAG,"Listener-onPlayerError..." + error.getMessage());
                    player.stop();
                    player.prepare(loopingSource);
                    player.setPlayWhenReady(true);

                }

                @Override
                public void onPositionDiscontinuity() {
                    Log.v(TAG,"Listener-onPositionDiscontinuity...");

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
            });

            player.setPlayWhenReady(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onPause();
        player.setPlayWhenReady(false);
        Log.d(TAG, "Player called onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        player.setPlayWhenReady(true);
        Log.d(TAG, "Player called onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataMovie dataMovie = new DataMovie(movie, instance);
        dataMovie.saveSeek(player.getCurrentPosition() > 5000 ? player.getCurrentPosition() -5000: player.getCurrentPosition());
        Log.v(TAG, "Destruido: " + player.getCurrentPosition());
        player.release();
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void showSystemUI(View mDecorView) {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
