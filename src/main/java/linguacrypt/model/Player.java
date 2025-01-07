package linguacrypt.model;

public class Player {
    private String name;
    private boolean isSpy; // 0 : Espion / 1 : Agent
    private String urlAvatar;

    public Player(String name, boolean isSpy, String urlAvatar) {
        this.name = name;
        this.isSpy = isSpy;
        this.urlAvatar = urlAvatar;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public boolean getIsSpy() {return isSpy;}
    public void setRole(boolean isSpy) {this.isSpy = isSpy;}
    public String getUrlAvatar() {return urlAvatar;}
    public void setUrlAvatar(String urlAvatar) {this.urlAvatar = urlAvatar;}




}
