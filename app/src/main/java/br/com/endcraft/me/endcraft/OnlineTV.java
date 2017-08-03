package br.com.endcraft.me.endcraft;

/**
 * Created by JonasXPX on 31.jul.2017.
 */

public class OnlineTV {

    private final String name;
    private String url, imageUrl;

    public OnlineTV(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
