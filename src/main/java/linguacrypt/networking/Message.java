package linguacrypt.networking;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private MessageType type;
    private String nickname;
    private String content;
    private int team; // Add this field for the team
    private byte[] serializedGame;

    // Constructors
    public Message(MessageType type, String nickname, String content, int team) {
        this.type = type;
        this.nickname = nickname;
        this.content = content;
        this.team = team; // Initialize team
    }

    public Message(MessageType type, String nickname, String content) {
        this(type, nickname, content, -1); // Default team is -1 if not specified
    }

    public Message() {
        this(null, null, null, -1);
    }

    // Getters and Setters
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTeam() { // Getter for team
        return team;
    }

    public void setTeam(int team) { // Setter for team
        this.team = team;
    }

    public byte[] getSerializedGame() {
        return serializedGame;
    }

    public void setSerializedGame(byte[] serializedGame) {
        this.serializedGame = serializedGame;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                ", team=" + team +
                '}';
    }
}
