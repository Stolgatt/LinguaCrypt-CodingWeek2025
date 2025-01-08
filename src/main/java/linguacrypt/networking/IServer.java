package linguacrypt.networking;

public interface IServer {
    void start();

    void stop();

    void sendMessageToAll(String message);
}
