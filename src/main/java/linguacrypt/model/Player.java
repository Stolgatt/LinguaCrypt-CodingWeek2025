package linguacrypt.model;

public class Player {
    private String name;
    private int role; // 0 : Espion / 1 : Agent
    private String urlAvatar;

    public Player(String name, int role, String urlAvatar) {
        this.name = name;
        this.role = role;
        this.urlAvatar = urlAvatar;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getRole() {return role;}
    public void setRole(int role) {this.role = role;}
    public String getUrlAvatar() {return urlAvatar;}
    public void setUrlAvatar(String urlAvatar) {this.urlAvatar = urlAvatar;}




}
