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

    public int addPlayer(Player player) {
        if (game.getgConfig().getMaxTeamMember() == players.size()) {
            return -1;
        }
        this.players.add(player);
        return 0;
    }

    public boolean checkIfHaveSpy() {
        for (Player player : players) {
            if (player.getIsSpy()){
                return true;
            }
        }
        return false;
    }

    public boolean isValid(){
        boolean hasSpy = false;
        boolean hasAgent = false;
        for (Player player : players) {
            if (player.getIsSpy()){
                hasSpy = true;
            }
            else{
                hasAgent = true;
            }
        }
        return hasSpy && hasAgent;
    }
}
