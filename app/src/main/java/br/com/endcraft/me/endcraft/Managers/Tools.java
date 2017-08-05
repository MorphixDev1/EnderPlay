package br.com.endcraft.me.endcraft.Managers;

import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.endcraft.me.endcraft.Categoria;
import br.com.endcraft.me.endcraft.Movie;

/**
 * Created by JonasXPX on 05.ago.2017.
 */

public class Tools {

    public static List<Movie> filterByCat(ListAdapter adapter, Movie mov, Categoria ... cat){
        int count = adapter.getCount();
        List<Movie> mv = new ArrayList<>();
        for(int x=0; x< count;x++){
            Movie m = (Movie)adapter.getItem(x);
            if(m.getNome().equalsIgnoreCase(mov.getNome()))
                continue;
            if(Arrays.deepEquals(m.getCategorias().toArray(), cat)){
                mv.add(m);
            }
        }
        return mv;
    }
}
