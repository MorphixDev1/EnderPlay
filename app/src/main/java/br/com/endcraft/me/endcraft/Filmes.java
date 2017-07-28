package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;
import java.util.List;

import br.com.endcraft.me.endcraft.Managers.AdManager;
import br.com.endcraft.me.endcraft.Managers.AdapterCustomFilmes;
import br.com.endcraft.me.endcraft.Managers.AdapterCustomSeries;
import br.com.endcraft.me.endcraft.Managers.CheckUpdate;
import br.com.endcraft.me.endcraft.Managers.Series;

/**
 * Created by JonasXPX on 18.jul.2017.
 */

public class Filmes extends AppCompatActivity {

    public static Filmes instance;
    public static String url_final = "";
    public static Series current_serie;
    private GridView list;
    private static InterstitialAd mInterstitialAd;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private View loading;
    private DrawerBuilder drawer;
    private static RewardedVideoAd ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.filmes_main);
        instance = this;

        setSupportActionBar((Toolbar) findViewById(R.id.actionbar));

        createDrawer();

        MobileAds.initialize(this, "ca-app-pub-6681846718813637~1550150705");
        /*mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6681846718813637/2042677168");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());*/

        ad = MobileAds.getRewardedVideoAdInstance(this);


        list = (GridView) findViewById(R.id.itens);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        loadMovies();

        loadAd();
    }

    private static void loadAd() {
        ad.loadAd("ca-app-pub-6681846718813637/2042677168", new AdRequest.Builder().build());
    }

    private void loadMovies(){
        new LoadMovies(this, list).execute("https://ender.tk/filme/data.php?getmovies=1");
    }

    private void loadSeries(){
        new LoadSeries(this, list).execute("https://ender.tk/filme/data.php?getseries=1");
    }

    private void createDrawer() {
        drawer = new DrawerBuilder().withActivity(this).withToolbar((Toolbar) findViewById(R.id.actionbar));
        SectionDrawerItem sesion = new SectionDrawerItem().withName("Categorias").withTextColor(Color.GRAY);
        drawer.addDrawerItems(new DividerDrawerItem());
        drawer.addDrawerItems(new PrimaryDrawerItem().withIdentifier(1)
                .withName("Filmes")
                .withIcon(FontAwesome.Icon.faw_film)
                .withTextColor(Color.WHITE)
                .withSelectedColor(getResources().getColor(R.color.selected_color))
                .withSelectedTextColor(Color.WHITE));
        drawer.addDrawerItems(new SecondaryDrawerItem().withIdentifier(2).withName("Séries").withIcon(FontAwesome.Icon.faw_film)
                .withTextColor(Color.WHITE)
                .withSelectedColor(getResources().getColor(R.color.selected_color))
                .withSelectedTextColor(Color.WHITE));
        drawer.addDrawerItems(sesion);
        for(Categoria categoria : Categoria.values()){
            drawer.addDrawerItems(new SecondaryDrawerItem().withIdentifier(categoria.id).withName(categoria.getNome())
                    .withTextColor(Color.WHITE)
                    .withSelectedColor(getResources().getColor(R.color.selected_color))
                    .withSelectedTextColor(Color.WHITE));
        }

        drawer.withSliderBackgroundColor(Color.rgb(75,75,75));
        drawer.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Log.d("DEGUB", "CLICKED: " + position);
                switch (position){
                    case 1:
                        loadMovies();
                        return false;
                    case 2:
                        loadSeries();
                        return false;
                }
                groupId255(Categoria.byName(((Nameable)drawerItem).getName().getText()));
                return false;
            }
        });

        Drawer d = drawer.build();
    }

    public static void openDesc(String url, long seek, Movie movie){
        Intent descView = new Intent(instance, Descview.class);
        descView.putExtra("seek", seek);
        descView.putExtra("movie", movie);
        instance.startActivity(descView);
    }
    public static void openVideo(String url, final long seek, final String name, Activity activity1){
        if(activity1 == null)
            activity1 = instance;
        final Activity activity = activity1;
        url_final = url;
        AdManager adManager = new AdManager() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
                Log.d("AD", "AD WAS CLOSED " + seek);
                Intent videoView = new Intent(activity, Play.class);
                videoView.putExtra("seek", seek);
                videoView.putExtra("movie", name);
                activity.startActivity(videoView);
                loadAd();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Log.d("AD", "AD WAS CLOSED " + seek);
                Intent videoView = new Intent(activity, Play.class);
                videoView.putExtra("seek", seek);
                videoView.putExtra("movie", name);
                activity.startActivity(videoView);
                loadAd();
            }
        };
        ad.setRewardedVideoAdListener(adManager);
        if (ad.isLoaded()) {
            ad.show();
        } else {
            Log.d("AD", "AD ISN't LOADED");
            Intent videoView = new Intent(activity, Play.class);
            videoView.putExtra("seek", seek);
            videoView.putExtra("movie", name);
            activity.startActivity(videoView);
            //loadAd();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        final Vibrator vibrator = (Vibrator) instance.getSystemService(VIBRATOR_SERVICE);
        SearchView search = (SearchView) MenuItemCompat.getActionView(menuItem);

        menu.add(101, 0, 0, R.string.buscar_at);
        menu.add(100, 0, 0, R.string.sobre);

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
            case 255:
               // groupId255(item);
                break;
            case 100:
                groupId100(item);
                break;
            case 101:
                groupId101(item);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean groupId100(MenuItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(R.string.sobre_msg) + BuildConfig.VERSION_NAME).setTitle("Sobre");
        dialog.create().show();
        return false;
    }

    private boolean groupId101(MenuItem item){
        new CheckUpdate(this).execute(BuildConfig.VERSION_NAME);
        return false;
    }


    private boolean groupId255(Categoria item){
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
            Toast.makeText(instance, "Aguarde", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }
}
