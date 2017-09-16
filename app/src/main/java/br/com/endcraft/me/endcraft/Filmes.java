package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.mikepenz.materialdrawer.DrawerBuilder;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.endcraft.me.endcraft.Managers.AdapterCustomFilmes;
import br.com.endcraft.me.endcraft.carregamento.LoadMovies;
import br.com.endcraft.me.endcraft.carregamento.LoadSeries;
import br.com.endcraft.me.endcraft.serie.AdapterCustomSeries;
import br.com.endcraft.me.endcraft.serie.Series;
import br.com.endcraft.me.endcraft.update.CheckUpdate;

/**
 * Created by JonasXPX on 18.jul.2017.
 */

public class Filmes extends AppCompatActivity {

    public static Filmes instance;
    public static String url_final = "";
    public static Series current_serie;
    public static GridView list;
    private static InterstitialAd mInterstitialAd;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private DrawerBuilder drawer;
    private static RewardedVideoAd ad;
    public static TypeContent current_content;
    private DrawerLayout main_drawer;
    public SwipeRefreshLayout refreshLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.drawer);
        instance = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);


        //Firebase ADs
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setImmersiveMode(true);

        list = (GridView) findViewById(R.id.itens);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        loadMovies();
        new CheckUpdate(this).execute(BuildConfig.VERSION_NAME);

        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navegacao);
        navigationView.setNavigationItemSelectedListener(new ItemClickListener(main_drawer));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, main_drawer, toolbar, R.string.app_name, R.string.app_name);
        main_drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_swipe);
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(current_content == TypeContent.SERIE)
                    loadSeries();
                else if (current_content == TypeContent.MOVIE)
                    loadMovies();
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public static void reloadAd(){
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    protected void loadMovies(){
        new LoadMovies(this, list).execute(getString(R.string.movie_link));
    }

    protected void loadSeries(){
        new LoadSeries(this, list).execute(getString(R.string.series_link));
    }

    public static void openDesc(long seek, Movie movie){
        Intent descView = new Intent(instance, Descview.class);
        descView.putExtra("seek", seek);
        descView.putExtra("movie", movie);
        instance.startActivity(descView);
    }

    public static void openVideo(String url, final long seek, final String name, Activity activity1, @Nullable final Movie movie, @Nullable final Series series){
        if(activity1 == null)
            activity1 = instance;
        Bundle bundle = new Bundle();
       final Activity activity = activity1;
        url_final = url;
        if(mInterstitialAd.isLoaded()){
             mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    Log.d("AD", "Propaganda foi fechada");
                    Intent videoView = new Intent(activity, Play.class);
                    videoView.putExtra("seek", seek);
                    videoView.putExtra("movie", name);
                    if(movie != null){
                        videoView.putExtra("moviedata", movie);
                    }
                    if(series != null){
                        videoView.putExtra("seriesdata", series);
                    }
                    activity.startActivity(videoView);
                    mInterstitialAd.loadAd(new AdRequest.Builder()
                            .setBirthday(new GregorianCalendar(1990,1,1).getTime())
                            .addKeyword("trailer")
                            .build());
                }
            });
        } else {
            Log.d("AD", "Propaganda não foi carregada");
            Intent videoView = new Intent(activity, Play.class);
            videoView.putExtra("seek", seek);
            videoView.putExtra("movie", name);
            if(movie != null){
                videoView.putExtra("moviedata", movie);
            }
            if(series != null){
                videoView.putExtra("seriesdata", series);
            }
            activity.startActivity(videoView);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView search = (SearchView) MenuItemCompat.getActionView(menuItem);

        menu.add(101, 0, 0, R.string.buscar_at);
        menu.add(100, 0, 0, R.string.sobre);
        menu.add(102, 0, 0, R.string.settings);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    BaseAdapter adapter = null;
                    if(list.getAdapter() instanceof AdapterCustomFilmes){
                        adapter = (AdapterCustomFilmes) list.getAdapter();
                        List<Movie> movies = new ArrayList<Movie>();
                        for (Movie mv : ((AdapterCustomFilmes)adapter).getFilmesclone()) {
                            if (mv.getNome().toLowerCase().contains(newText.toLowerCase().trim())) {
                                movies.add(mv);
                            }
                        }
                        ((AdapterCustomFilmes)adapter).setFilmes(movies);
                    } else if(list.getAdapter() instanceof AdapterCustomSeries){
                        adapter = (AdapterCustomSeries) list.getAdapter();

                    }
                    list.setAdapter(adapter);
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
            case 100:
                groupId100(item);
                break;
            case 101:
                groupId101(item);
                break;
            case 102:
                startActivity(new Intent(this ,Settings.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean groupId100(MenuItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(R.string.sobre_msg) + " " + BuildConfig.VERSION_NAME).setTitle("Sobre");
        dialog.create().show();
        return false;
    }

    private boolean groupId101(MenuItem item){
        new CheckUpdate(this).execute(BuildConfig.VERSION_NAME);
        return false;
    }

    protected boolean groupId255(Categoria item){
        if(item == null)
            return true;
        Toast.makeText(this, item.getNome(), Toast.LENGTH_SHORT).show();
        try {
            AdapterCustomFilmes adapter = (AdapterCustomFilmes) list.getAdapter();
            List<Movie> movies = new ArrayList<Movie>();
            for (Movie mv : adapter.getFilmesclone())
                if(mv.getCategorias().contains(item))
                    movies.add(mv);
            adapter.setFilmes(movies);
            list.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(instance, "Não foi possível executar", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }
}
