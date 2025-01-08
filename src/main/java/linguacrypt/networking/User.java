package linguacrypt.networking;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nickname;
    private InetAddress address;
    private int teamId;

    public User(String nickname, InetAddress address, int teamId) {
        this.nickname = nickname;
        this.address = address;
        this.teamId = teamId;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", address=" + address +
                ", teamId=" + teamId +
                '}';
    }
}
