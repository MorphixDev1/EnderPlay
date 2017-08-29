package br.com.endcraft.me.endcraft;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * Created by JonasXPX on 29.ago.2017.
 */

public class ItemClickListener implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    public ItemClickListener(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_filme:
                Filmes.instance.refreshLayout.setRefreshing(true);
                Filmes.instance.loadMovies();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_serie:
                Filmes.instance.refreshLayout.setRefreshing(true);
                Filmes.instance.loadSeries();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                Filmes.instance.groupId255(Categoria.byName(item.getTitle().toString()));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return false;
    }
}
