package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.GridView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.endcraft.me.endcraft.Managers.AdapterCustomFilmes;

/**
 * Created by JonasXPX on 18.jul.2017.
 */

public class LoadMovies extends AsyncTask<String, Void, List<Movie>> {

    private Activity activity;
    private GridView view;

    public LoadMovies(Activity ab, GridView view) {
        this.activity = ab;
        this.view = view;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        List<Movie> filmes = new ArrayList<>();
        try{
            URL url = new URL(params[0]);
            URLConnection c = url.openConnection();
            c.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            c.connect();
            JSONArray j = new JSONArray(IOUtils.toString(c.getInputStream(), "UTF-8").replaceFirst("\\W", ""));
            Movie m;
            for (int x=0;x<j.length();x++){
                JSONObject data = j.getJSONObject(x);
                m = new Movie();
                m.setImgLink(data.getString("poster"));
                m.setNome(data.getJSONObject("nome").getString("0"));
                JSONObject idioma = data.getJSONObject("idioma");
                m.setIdioma( idioma.has("0") ?  idioma.getString("0") : "Português" );
                m.setLink(data.getJSONObject("url").getString("0"));
                List<Categoria> categorias = new ArrayList<>();
                JSONArray array = data.getJSONArray("categoria");
                for(int e=0; e<array.length();e++){
                    Categoria categoria = Categoria.byName(array.getString(e));
                    if(categoria != null)
                        categorias.add(categoria);
                }
                m.setCategorias(categorias);
                filmes.add(m);
            }
        }catch (Exception e){
            e.printStackTrace();
            return filmes;
        }

        return filmes;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        AdapterCustomFilmes adapterCustomFilmes = new AdapterCustomFilmes(movies, activity);
        view.setAdapter(adapterCustomFilmes);
    }
}
