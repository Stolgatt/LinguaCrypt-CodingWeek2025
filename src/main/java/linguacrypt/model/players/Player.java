package linguacrypt.model.players;

import linguacrypt.model.statistique.PlayerStat;

import java.io.Serializable;

public class Player implements Serializable{
    private static final long serialVersionUID = 1L;
    private String name;
    private boolean isSpy; // 0 : Espion / 1 : Agent
    private String urlAvatar;
    private PlayerStat playerStat;

    public Player(String name, boolean isSpy, String urlAvatar, PlayerStat playerStat) {
        
        this.name = name;
        this.isSpy = isSpy;
        this.urlAvatar = urlAvatar;
        this.playerStat = playerStat;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public boolean getIsSpy() {return isSpy;}
    public void setIsSpy(boolean isSpy) {this.isSpy = isSpy;}
    public void setRole(boolean isSpy) {this.isSpy = isSpy;}
    public String getUrlAvatar() {return urlAvatar;}
    public void setUrlAvatar(String urlAvatar) {this.urlAvatar = urlAvatar;}
    public PlayerStat getStat() {return playerStat;}

    public void copyFrom(Player other) {
        this.name = other.name;
        this.isSpy = other.isSpy;
        this.playerStat = other.playerStat; // Assuming deep copy or reference sharing is acceptable
        // Copy any other relevant fields
    }



}
