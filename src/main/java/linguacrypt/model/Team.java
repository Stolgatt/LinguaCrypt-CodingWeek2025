package linguacrypt.model;

import java.util.ArrayList;

public class Team {
    private ArrayList<Player> players;
    private String name;
    private Game game;
    private int color;

    public Team(String name, int size, Game game,int color) {
        this.name = name;
        this.players = new ArrayList<>(size);
        this.game = game;
        this.color = color;
    }

    public ArrayList<Player> getPlayers() {return players;}
    public void setPlayers(ArrayList<Player> players) {this.players = players;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getColor() {return color;}
    public void setColor(int color) {this.color = color;}

}
