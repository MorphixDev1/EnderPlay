package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.endcraft.me.endcraft.Managers.AdapterCustomFilmes;
import br.com.endcraft.me.endcraft.Managers.Tools;
import br.com.endcraft.me.endcraft.net.GetMovieInformation;
import br.com.endcraft.me.endcraft.net.ThreadImage;


/**
 * Created by JonasXPX on 18.jul.2017.
 */
public class Descview extends AppCompatActivity {

    private CharSequence nome;
    private CharSequence idioma;
    private TextView text;
    private Movie movie;
    private long seek;
    private Activity activity;
    private GetMovieInformation infoMovie;
    private AlertDialog.Builder builderView;
    private BottomSheetDialog selectQuality;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filme_desc);
        overridePendingTransition(R.anim.side_in, android.support.v7.appcompat.R.anim.abc_fade_out);

        selectQuality = new BottomSheetDialog(this);

        selectQuality.setContentView(R.layout.select_quality_dialog);
        listView = (ListView)selectQuality.findViewById(R.id.listquality);

        movie = (Movie) this.getIntent().getSerializableExtra("movie");
        seek = this.getIntent().getLongExtra("seek", 0);
        activity = this;
        TextView desc = (TextView) findViewById(R.id.descricao);
        TextView idioma = (TextView) findViewById(R.id.idioma);
        TextView nome = (TextView) findViewById(R.id.movie_name);
        TextView categorias = (TextView) findViewById(R.id.categorias);
        TextView releaceDate = (TextView) findViewById(R.id.releacedate);
        TextView diretor = (TextView) findViewById(R.id.diretor);

        infoMovie = new GetMovieInformation(releaceDate, diretor);
        infoMovie.execute(movie.getNome());

        ImageView play_button = (ImageView) findViewById(R.id.play_button);
        ImageView background_movie = (ImageView) findViewById(R.id.background_movie);

        String banner = movie.getBannerLink();

        if(!banner.equalsIgnoreCase(""))
            new Thread(new ThreadImage(banner, background_movie, this)).start();

        play_button.setOnClickListener(onClick());
        if(movie != null) {
            nome.setText(movie.getNome());
            idioma.setText(movie.getIdioma());
            desc.setText(movie.getDesc());
            //desc.setMovementMethod(new ScrollingMovementMethod());
        } else {
            Toast.makeText(this, "MOVIE IS NULL", Toast.LENGTH_SHORT).show();
        }
        GridView gv = (GridView) findViewById(R.id.outros_filmes);
        Categoria[] c = new Categoria[movie.getCategorias().size()];
        movie.getCategorias().toArray(c);
        gv.setAdapter(new AdapterCustomFilmes(Tools.filterByCat(Filmes.list.getAdapter(), movie, c), this));
        StringBuilder cat = new StringBuilder();
        int count = 0;
        for(Categoria categoria : c){
            count++;
            cat.append(categoria.getNome());
            if(c.length != count){
                cat.append(", ");
            }
        }
        categorias.setText(cat.toString());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movie.getUrls().size() > 1){
                    listView.setAdapter(new BaseAdapter() {

                        List<String> urls = movie.getUrls();

                        @Override
                        public int getCount() {
                            return urls.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return urls.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return 0;
                        }

                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            View view = activity.getLayoutInflater().inflate(R.layout.quality, parent, false);
                            Button q = (Button) view.findViewById(R.id.quality);
                            q.setText(position == 0 ? "Padrão" : position == 1 ? "Baixa" : "Ruim");
                            q.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Filmes.preOpenVideo((String)getItem(position), seek, movie.getNome(), activity, movie, null);
                                }
                            });
                            return view;
                        }
                    });
                    selectQuality.show();
                } else
                    Filmes.preOpenVideo(movie.getLink(), seek, movie.getNome(), activity, movie, null);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infoMovie.cancel(true);
    }

}