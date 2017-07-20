package br.com.endcraft.me.endcraft;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import br.com.endcraft.me.endcraft.Managers.AdapterCustomFilmes;

/**
 * Created by JonasXPX on 18.jul.2017.
 */

public class Filmes extends AppCompatActivity {

    public static Filmes instance;
    public static String url_final = "";
    private GridView list;
    private static InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.filmes_main);
        setSupportActionBar((Toolbar) findViewById(R.id.actionbar));
        MobileAds.initialize(this, "ca-app-pub-6681846718813637~1550150705");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6681846718813637/7457083506");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        instance = this;
        list = (GridView) findViewById(R.id.itens);
        new LoadMovies(this, list).execute("https://ender.tk/filme/data.php?getmovies=1");

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

    }

    public static void openDesc(Movie mov){
        Intent descView = new Intent(instance, Descview.class);
        Bundle b = new Bundle();
        b.putCharSequence("movie", mov.getNome());
        b.putCharSequence("idioma", mov.getIdioma());
        descView.putExtras(b);
        instance.startActivity(descView);
    }
    public static void openVideo(String url, final long seek, final String name){
        url_final = url;
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    Log.d("AD", "AD WAS CLOSED " + seek);
                    Intent videoView = new Intent(instance, Play.class);
                    videoView.putExtra("seek", seek);
                    videoView.putExtra("movie", name);
                    instance.startActivity(videoView);
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            });
        } else {
            Log.d("AD", "AD ISn't LOADED");
            Toast.makeText(instance, "The interstitial wasn't loaded yet.", Toast.LENGTH_SHORT);
            Intent videoView = new Intent(instance, Play.class);
            videoView.putExtra("seek", seek);
            videoView.putExtra("movie", name);
            instance.startActivity(videoView);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        final Vibrator vibrator = (Vibrator) instance.getSystemService(VIBRATOR_SERVICE);
        SearchView search = (SearchView) MenuItemCompat.getActionView(menuItem);

        SubMenu sub_cat = menu.addSubMenu(0, -1, 0, "Categorias");

        menu.add(100, 0, 0, R.string.sobre);

        for(Categoria categoria : Categoria.values()){
            sub_cat.add(255, categoria.id, 0, categoria.getNome());
        }

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vibrator.vibrate(250);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    AdapterCustomFilmes adpter = (AdapterCustomFilmes) list.getAdapter();
                    List<Movie> movies = new ArrayList<Movie>();
                    for (Movie mv : adpter.getFilmesclone()) {
                        if (mv.getNome().toLowerCase().contains(newText.toLowerCase().trim())) {
                            movies.add(mv);
                        }
                    }
                    adpter.setFilmes(movies);
                    list.setAdapter(adpter);
                }catch (Exception e){
                    Toast.makeText(instance, "Aguarde", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getGroupId()){
            case 255:
                groupId255(item);
            case 100:
                groupId100(item);
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean groupId100(MenuItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.sobre_msg).setTitle("Sobre");
        dialog.create().show();
        return false;
    }

    private boolean groupId255(MenuItem item){
        Categoria cat = Categoria.byId(item.getItemId());
        if(cat == null)
            return true;
        Toast.makeText(this, cat.getNome(), Toast.LENGTH_SHORT).show();

        try {
            AdapterCustomFilmes adapter = (AdapterCustomFilmes) list.getAdapter();
            List<Movie> movies = new ArrayList<Movie>();
            for (Movie mv : adapter.getFilmesclone())
                if(mv.getCategorias().contains(cat))
                    movies.add(mv);
            adapter.setFilmes(movies);
            list.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(instance, "Aguarde", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }
}
