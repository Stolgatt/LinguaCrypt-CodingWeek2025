package linguacrypt.networking;

import java.io.Serializable;
public enum MessageType {
    CONNECT,        // Sent when a client wants to connect to the server
    CONNECT_OK,     // Sent by the server to confirm a successful connection
    CONNECT_FAILED, // Sent by the server when a connection attempt fails
    DISCONNECT,     // Sent when a client disconnects
    CHAT,           // Chat message
    USER_JOINED,    // Notification that a user has joined
    USER_LEFT,      // Notification that a user has left
    READY,          // Indicates a user is ready to start the game
    GAME_START,     // Sent when the game starts
    GAME_UPDATE,    // Sent to update game state
    TEAM_UPDATE,    // Updates the teams (if a player changes team)
    ERROR           // Generic error message
}

