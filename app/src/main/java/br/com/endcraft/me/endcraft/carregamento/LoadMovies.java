package br.com.endcraft.me.endcraft.carregamento;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.endcraft.me.endcraft.Categoria;
import br.com.endcraft.me.endcraft.Filmes;
import br.com.endcraft.me.endcraft.Managers.AdapterCustomFilmes;
import br.com.endcraft.me.endcraft.Movie;
import br.com.endcraft.me.endcraft.TypeContent;

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
                if(data.has("desc")) {
                    JSONObject desc = data.getJSONObject("desc");
                    m.setDesc(desc.has("0") ? desc.getString("0") : "Filme sem descrição :(");
                }
                List<String> listUrls = null;
                if(data.has("urls")){
                    listUrls = new ArrayList<>();
                    JSONObject urls = data.getJSONObject("urls");

                    listUrls.add(urls.getJSONArray("high").getString(0));
                    if(!urls.getJSONArray("medium").getString(0).trim().equalsIgnoreCase("")) {
                        listUrls.add(urls.getJSONArray("medium").getString(0));
                    }
                    if(!urls.getJSONArray("low").getString(0).trim().equalsIgnoreCase("")){
                        listUrls.add(urls.getJSONArray("low").getString(0));
                    }
                }
                if(listUrls!=null)
                    m.setUrls(listUrls);
                if(data.has("banner") && (data.get("banner") instanceof JSONObject)) {
                    if(data.getJSONObject("banner").has("0"))
                     m.setBannerLink(data.getJSONObject("banner").getString("0"));
                }

                m.setIdioma( idioma.has("0") ?  idioma.getString("0") : "Português" );
                m.setLink(data.getJSONObject("url").getString("0"));
                m.setSubtitleLink(data.get("subtitles") instanceof JSONObject ? (data.getJSONObject("subtitles").has("0") ? data.getJSONObject("subtitles").getString("0") : "") : "");
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
        Filmes.instance.refreshLayout.setRefreshing(false);
        Filmes.current_content = TypeContent.MOVIE;
    }
}
