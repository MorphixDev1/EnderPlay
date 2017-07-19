package br.com.endcraft.me.endcraft.Managers;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.com.endcraft.me.endcraft.Filmes;
import br.com.endcraft.me.endcraft.Movie;
import br.com.endcraft.me.endcraft.R;

/**
 * Created by JonasXPX on 18.jul.2017.
 */

public class AdapterCustomFilmes extends BaseAdapter {

    private List<Movie> filmes = null;
    private final List<Movie> filmesclone;
    private Activity activity;

    public AdapterCustomFilmes(List<Movie> filmes, Activity activity) {
        this.filmes = filmes;
        this.filmesclone = new ArrayList<>(filmes);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return filmes.size();
    }

    @Override
    public Object getItem(int position) {
        return filmes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<Movie> getList(){
        return this.filmes;
    }

    public void setFilmes(List<Movie> filmes) {
        this.filmes = filmes;
    }

    public List<Movie> getFilmesclone() {
        return filmesclone;
    }

   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.filmes, parent, false);
        final Movie movie = filmes.get(position);
       /* TextView nome = (TextView) view.findViewById(R.id.filme_nome);
        TextView desc = (TextView) view.findViewById(R.id.filme_desc);*/
        ImageView img = (ImageView) view.findViewById(R.id.item_imagem);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filmes.openVideo(movie.getLink());
            }
        });
        if(movie != null) {
           /* nome.setText(movie.getNome());
            desc.setText(movie.getIdioma());*/
            new DownloadImage(img, activity).execute(movie.getImgLink());
            //img.setImageURI(Uri.parse(movie.getImgLink()));
        } else
            Log.d("movie is null", "movie is null");
        return view;
    }
   /* @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView)activity.getLayoutInflater().inflate(R.layout.filmes, parent, false);
        final Movie movie = filmes.get(position);
        TextView nome = (TextView) view.findViewById(R.id.filme_nome);
        TextView desc = (TextView) view.findViewById(R.id.filme_desc);
        ImageView img = (ImageView) view.findViewById(R.id.item_imagem);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filmes.openVideo(movie.getLink());
            }
        });
        if(movie != null) {
            nome.setText(movie.getNome());
            desc.setText(movie.getIdioma());
            new DownloadImage(img).execute(movie.getImgLink());
        } else
            Log.d("movie is null", "movie is null");
        return view;
    }*/
}
