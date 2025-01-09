package linguacrypt.networking;

import linguacrypt.model.Player.Player;
import linguacrypt.model.statistique.PlayerStat;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {
    private String nickname;
    private InetAddress address;
    private int teamId;

    public User(String nickname, InetAddress address, int teamId) {
        this.nickname = nickname;
        this.address = address;
        this.teamId = teamId;
    }

    // Getter and Setter methods
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    // Convert User to Player
    public Player toPlayer() {
        Player player = new Player(nickname, false,"", new PlayerStat()); // Assuming Player has a constructor that accepts a nickname
        //player.setTeamId(teamId); // Assuming Player has a setTeamId method
        return player;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", address=" + address +
                ", teamId=" + teamId +
                '}';
    }
}
