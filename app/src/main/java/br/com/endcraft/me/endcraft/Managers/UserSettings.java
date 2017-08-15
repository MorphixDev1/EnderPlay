package br.com.endcraft.me.endcraft.Managers;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;

import br.com.endcraft.me.endcraft.Filmes;

/**
 * Created by JonasXPX on 02.ago.2017.
 */

public class UserSettings {

    private int subtitle_foregroundColor = Color.WHITE;
    private int subtitle_backgroundColor = Color.TRANSPARENT;
    private int subtitle_windowColor = Color.TRANSPARENT;
    private int subtitle_CaptionStyleCompat = CaptionStyleCompat.EDGE_TYPE_DROP_SHADOW;
    private int subtitle_edgeColor = Color.BLACK;
    private int subtitle_fontSize = 12;

    public UserSettings() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Filmes.instance);
        subtitle_foregroundColor = pref.getInt("subtitle_color", 0);
        subtitle_edgeColor = pref.getInt("subtitle_shadow_color", 0);
        subtitle_fontSize = Integer.parseInt(pref.getString("subtitle_font_size", "12"));
        subtitle_windowColor = pref.getInt("subtitle_window_color", 0);
    }

    public int getSubtitle_foregroundColor() {
        return subtitle_foregroundColor;
    }

    public int getSubtitle_backgroundColor() {
        return subtitle_backgroundColor;
    }

    public int getSubtitle_windowColor() {
        return subtitle_windowColor;
    }

    public int getSubtitle_CaptionStyleCompat() {
        return subtitle_CaptionStyleCompat;
    }

    public int getSubtitle_edgeColor() {
        return subtitle_edgeColor;
    }

    public int getSubtitle_fontSize() {
        return subtitle_fontSize;
    }

    /**
     * Create style by user preference
     *
     * @return A Style of subtitles
     */
    public CaptionStyleCompat getSubtitlesStyle(){
        return new CaptionStyleCompat(this.getSubtitle_foregroundColor(), this.getSubtitle_backgroundColor(),
                this.getSubtitle_windowColor(), CaptionStyleCompat.EDGE_TYPE_DROP_SHADOW, this.getSubtitle_edgeColor(), null);
    }
}
