package br.com.endcraft.me.endcraft.update;

/**
 * Created by JonasXPX on 20.jul.2017.
 */

public class Update {

    private final String version;
    private boolean newVersion;
    private String lastVersion;
    private String link;

    public Update(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setDownloadLink(String link){
        this.link = link;
    }

    public String getDownloadLink() {
        return link;
    }

    public boolean hasNewVersion(){
        return newVersion;
    }

    public void setNewVersion(boolean b){
        this.newVersion = b;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }
}
