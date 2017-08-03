package br.com.endcraft.me.endcraft;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
            desc.setMovementMethod(new ScrollingMovementMethod());
        } else {
            Toast.makeText(this, "MOVIE IS NULL", Toast.LENGTH_SHORT).show();
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
                            q.setText(position == 0 ? "Padrão" : position == 1 ? "Boa" : "Ruim");
                            q.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Filmes.openVideo((String)getItem(position), seek, movie.getNome(), activity, movie, null);
                                }
                            });
                            return view;
                        }
                    });
                    selectQuality.show();
                } else
                    Filmes.openVideo(movie.getLink(), seek, movie.getNome(), activity, movie, null);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infoMovie.cancel(true);
    }
}