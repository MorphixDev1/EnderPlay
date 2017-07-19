package br.com.endcraft.me.endcraft;

/**
 * Created by JonasXPX on 18.jul.2017.
 */

public class Movie {

    private String link = "";
    private String nome = "";
    private String idioma = "";
    private String imgLink = "";

    public Movie(){  }

    public Movie(String nome, String imgLink){
        this.nome = nome;
        this.imgLink = imgLink;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return nome;
    }
}
