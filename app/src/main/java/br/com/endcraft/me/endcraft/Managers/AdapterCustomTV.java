package br.com.endcraft.me.endcraft.Managers;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.endcraft.me.endcraft.Filmes;
import br.com.endcraft.me.endcraft.OnlineTV;
import br.com.endcraft.me.endcraft.R;
import br.com.endcraft.me.endcraft.net.ThreadImage;

/**
 * Created by JonasXPX on 18.jul.2017.
 */

public class AdapterCustomTV extends BaseAdapter {

    private List<OnlineTV> filmes = null;
    private final List<OnlineTV> filmesclone;
    private Activity activity;

    public AdapterCustomTV(List<OnlineTV> filmes, Activity activity) {
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

    public List<OnlineTV> getList(){
        return this.filmes;
    }

    public void setFilmes(List<OnlineTV> filmes) {
        this.filmes = filmes;
    }

    public List<OnlineTV> getFilmesclone() {
        return filmesclone;
    }

   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.filmes, parent, false);
        final OnlineTV onlineTV = filmes.get(position);
        TextView nome = (TextView) view.findViewById(R.id.filme_nome);
       /* TextView desc = (TextView) view.findViewById(R.id.filme_desc);*/
        final ImageView img = (ImageView) view.findViewById(R.id.item_imagem);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataMovie dataMovie = new DataMovie(onlineTV.getName(), activity);
                Filmes.openVideo(onlineTV.getUrl(), 0L, onlineTV.getName(), activity, null, null);
            }
        });
        if(onlineTV != null) {
            nome.setText(onlineTV.getName());
            new Thread(new ThreadImage(onlineTV.getImageUrl(), img, activity)).start();
        } else
            Log.d("DEBUG", "movie is null");
        return view;
    }
}
