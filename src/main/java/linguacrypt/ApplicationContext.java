package linguacrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import linguacrypt.controller.*;
import linguacrypt.model.Game;
import linguacrypt.networking.Client;
import linguacrypt.networking.Message;
import linguacrypt.networking.MessageType;
import linguacrypt.networking.Server;
import linguacrypt.view.EditTeamView;
import linguacrypt.view.EditThemeView;
import linguacrypt.view.gameView.EndGameView;
import linguacrypt.view.gameView.GameView;
import linguacrypt.view.LobbyView;
import linguacrypt.view.LocalMenuView;
import linguacrypt.view.MainMenuView;
import linguacrypt.view.MultiplayerMenuView;
import linguacrypt.view.ProfileMenuView;
import linguacrypt.view.gameView.SoloGameView;

public class ApplicationContext {

    //region Attributs

    /** Instance unique de ApplicationContext (Singleton). */
    private static ApplicationContext instance;

    private Server server;
    private Client client;

    /** Stage principal */
    private Stage primaryStage;

    /** Root layout principal de l'application. */
    private BorderPane root;
    private Node editTeamNode;
    private Node MainMenuNode;
    private Node GameNode;
    private Node ProfileMenuNode;
    private Node multplayerMenuNode;
    private Node SoloGameNode;
    private Node editThemeNode;
    private Node LocalNode;
    /** Références aux contrôleurs. */
    private MainMenuController mainMenuController;
    private EditTeamController editTeamController;
    private GameController gameController;
    private ProfileMenuController profileMenuController;
    private SoloGameController soloGameController;
    private EditThemeView editThemeView;

    /** Vues. */
    private MainMenuView mainMenuView;
    private EditTeamView editTeamView;
    private GameView gameView;
    private ProfileMenuView profileMenuView;
    private MultiplayerMenuView multiplayerMenuView;
    private LocalMenuView localMenuView;
    

        private Node lobbyNode;
    private LobbyView lobbyView;
    private SoloGameView soloGameView;

    private Node EndGameNode;
    private EndGameView endGameView;

    /** Modèles */
    private Game game;
    public final int cardHeight = 75;
    public final int cardWidth = 150;
    //endregion

    //region Constructeur

    /**
     * Constructeur privé pour empêcher l'instanciation externe.
     */
    private ApplicationContext() {}

    //endregion

    //region Méthodes

    /**
     * Fournit l'instance unique de ApplicationContext.
     *
     * @return Instance de ApplicationContext.
     */
    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    /**
     * Obtient le root layout principal.
     *
     * @return Le BorderPane root.
     */
    public BorderPane getRoot() {
        return root;
    }

    /**
     * Définit le root layout principal.
     *
     * @param root Le BorderPane root à définir.
     */
    public void setRoot(BorderPane root) {
        this.root = root;
    }

    /**
     * Initialisation des composants de l'application.
     */
    public void initialize() {
        try {

        //Load main menu components
        FXMLLoader mainMenuLoad = new FXMLLoader(getClass().getResource("/FXML/MainMenu.fxml"));
        MainMenuNode =mainMenuLoad.load();
        mainMenuView = mainMenuLoad.getController();
        mainMenuController = new MainMenuController();
        mainMenuController.setView(mainMenuView);
        //Load edit team scene components
        FXMLLoader editTeamLoader = new FXMLLoader(getClass().getResource("/FXML/editTeam.fxml"));
        editTeamNode = editTeamLoader.load();
        editTeamView = editTeamLoader.getController();
        editTeamController = new EditTeamController(game, editTeamView);
        //Load GameView components
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/FXML/Game.fxml"));
        GameNode = gameLoader.load();
        gameView = gameLoader.getController();
        gameController = new GameController(game, gameView);
        //Load SoloGameView components
        FXMLLoader soloGameLoader = new FXMLLoader(getClass().getResource("/FXML/SoloGame.fxml"));
        SoloGameNode = soloGameLoader.load();
        soloGameView = soloGameLoader.getController();
        soloGameController = new SoloGameController(game, soloGameView);

        FXMLLoader endGameLoader = new FXMLLoader(getClass().getResource("/FXML/EndGame.fxml"));
        EndGameNode = endGameLoader.load();
        endGameView = endGameLoader.getController();


        FXMLLoader profileMenuLoader = new FXMLLoader(getClass().getResource("/FXML/ProfileMenu.fxml"));
        ProfileMenuNode = profileMenuLoader.load();
        profileMenuView = profileMenuLoader.getController();
        profileMenuController = new ProfileMenuController(game, profileMenuView);

        FXMLLoader editThemeLoader = new FXMLLoader(getClass().getResource("/FXML/editTheme.fxml"));
        editThemeNode = editThemeLoader.load();
        editThemeView = editThemeLoader.getController();


        FXMLLoader mpMenuloader = new FXMLLoader(getClass().getResource("/FXML/MultiplayerMenu.fxml"));
        multplayerMenuNode = mpMenuloader.load();
        multiplayerMenuView = mpMenuloader.getController();

        FXMLLoader localMenuloader = new FXMLLoader(getClass().getResource("/FXML/LocalMenu.fxml"));
        LocalNode = localMenuloader.load();
        localMenuView = localMenuloader.getController();


        // Load Lobby components
        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/FXML/lobby.fxml"));
        lobbyNode = lobbyLoader.load();
        lobbyView = lobbyLoader.getController();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des composants de l'application : " + e.getMessage());
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void  broadcastGameUpdate(){

        //server-side broadcast to all client.
        if (this.getServer() != null) {
            System.out.println("Updating the game...");
        Message message = new Message(MessageType.GAME_UPDATE, this.getServer().getHostNickname(), "Updating the game...");
            server.synchronizeAllUsersWithGame();
        // Serialize the game instance
        Game game = this.getGame();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(game);
            oos.flush();

            // Add the serialized game to the message
            message.setSerializedGame(bos.toByteArray());
        } catch (IOException e) {
            System.out.println("Error serializing game: " + e.getMessage());
            return;
        }

        // Broadcast the GAME_UPDATE message to all clients
        this.getServer().broadcastMessage(message);
    
        }else if(this.getClient() != null){
            //on the client side the message passes through the server

        System.out.println("Updating the game...");
        Message message = new Message(MessageType.GAME_UPDATE, this.getClient().getUser().getNickname(), "Updating the game...");

        // Serialize the game instance
        Game game = ApplicationContext.getInstance().getGame();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(game);
            oos.flush();

            // Add the serialized game to the message
            message.setSerializedGame(bos.toByteArray());
        } catch (IOException e) {
            System.out.println("Error serializing game: " + e.getMessage());
            return;
        }

        // Broadcast the GAME_UPDATE message to all clients
        this.getClient().sendMessage(message);
        }
        
    }

    //region Getters et Setters

    public Stage getPrimaryStage() {return primaryStage;}

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
               // Stop server and client on application close
               primaryStage.setOnCloseRequest(event -> {
                if (server != null) {
                    server.stop();
                }
                if (client != null) {
                    client.disconnect();
                }
            });
    }

    public void setGame(Game game){
        this.game = game; 
        editTeamView.setGame(game);
        gameView.setGame(game);
        switch (game.getgConfig().getGameMode()) {
            case 0:
                gameView.setTimer();
                break;
            case 1: 
            gameView.setTimer();
            case 2:
            soloGameView.setTimer();
            default:
                break;
        }
        gameController.setGame(game);
        editTeamController.setGame(game);
        profileMenuController.setGame(game);
        profileMenuView.setGame(game);
        soloGameController.setGame(game);
        soloGameView.setGame(game);
        endGameView.setGame(game);
    }

    public Game getGame(){
        return game;
    }

    public Node getEditTeamNode(){
        return editTeamNode;
    }
    //endregion

    public Node getEndGameNode(){return EndGameNode;}
    public EndGameView getEndGameView(){return endGameView;}
    public Node getMainMenuNode() {
        return MainMenuNode;
    }
    public Node getSoloGameNode(){return SoloGameNode;}

    public Node getGameNode() {
        return GameNode;
    }

    public Node getEditThemeNode(){return editThemeNode;}

    public Node getProfileMenuNode() {return ProfileMenuNode;}
    public Node getMPMenuNode() {return multplayerMenuNode;}

    public MultiplayerMenuView getMPMenuView(){
        return multiplayerMenuView;
    }

    public Node getLobbyNode() {
        return lobbyNode;
    }


    public LocalMenuView getLocalMenuView(){return localMenuView;}
    public Node getLocalNode() {return LocalNode;}

    
    public LobbyView getLobbyView() {
        return lobbyView;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

        public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public EditThemeView getEditThemeView() {
        return editThemeView;
    }

    //endregion

    
}
