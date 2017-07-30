package br.com.endcraft.me.endcraft.Managers;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by JonasXPX on 19.jul.2017.
 */

public class Series implements Serializable{

    private String name;
    private TreeMap<Integer, List<Episodio>> temporadas = new TreeMap<>();
    private String urlImg;

    public Series(String name) {
        setName(name);
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TreeMap<Integer, List<Episodio>> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(TreeMap<Integer, List<Episodio>> serie) {
        this.temporadas = serie;
    }

    public static final class Episodio implements Serializable{
        private String name;
        private int epNumber;
        private String url;

        public Episodio(String name, int epNumber, String url) {
            this.name = name;
            this.epNumber = epNumber;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getEpNumber() {
            return epNumber;
        }

        public void setEpNumber(int epNumber) {
            this.epNumber = epNumber;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
