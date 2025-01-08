package linguacrypt.model;

import linguacrypt.model.statistique.GameStat;
import linguacrypt.model.statistique.PlayerStat;
import linguacrypt.utils.StatLoader;
import linguacrypt.utils.ThemeLoader;
import linguacrypt.view.Observer;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import linguacrypt.model.Theme;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private GameConfiguration gConfig;
    private Grid grid;
    private int turn; // 0 : Blue team to play / 1 : Red team to play
    private int turnStep = 0;
    private String currentHint;
    private int currentNumberWord;
    private transient ArrayList<Observer> obs = new ArrayList<>();
    private int currentTryCount = 0;
    private int isWin = -1; // -1 : partie en cours / 0 / Bleu a gagne / 1 : Rouge a gagne / 2 : lequipe qui joue a trouve le mot noir
    private transient Random random = new Random();
    private Team[] teams = new Team[2]; // 0 bleu, //1 pour rouge
    private List<String> themeWords;
    private long startTime;
    private int nbTour = 0;
    private GameStat gameStat;

    public Game(GameConfiguration gConfig) {
        this.gConfig = gConfig;

        switch (gConfig.getGameMode()) {
            case 0:                     // Words Game Mode
                this.themeWords = loadThemeWords(gConfig.getTheme());
                this.grid = new Grid(gConfig.getGridSize(), themeWords, 0);
                setUpGame();
                grid.printGrid();
                break;
            case 1:                     // Picture Game Mode
                this.grid = new Grid(gConfig.getGridSize(), null, 1);
                setUpGame();
                grid.printGrid();
                break;
            default:
                break;
        }
    }

    private List<String> loadThemeWords(String themeName) {
        List<Theme> themes = ThemeLoader.loadThemes();
        for (Theme theme : themes) {
            if (theme.getName().equals(themeName)) {
                return theme.getWords();
            }
        }
        return List.of(); // Return an empty list if no theme found
    }

    //SETTER AND GETTER
    public GameConfiguration getgConfig() {return gConfig;}
    public Grid getGrid() {return grid;}
    public int getTurn() {return turn;}
    public void setTurn(int turn) {this.turn = turn;}
    public String getCurrentHint() {return currentHint;}
    public void setCurrentHint(String currentHint) {this.currentHint = currentHint;}
    public int getCurrentNumberWord() {return currentNumberWord;}
    public void setCurrentNumberWord(int currentNumberWord) {this.currentNumberWord = currentNumberWord;}
    public Team getBlueTeam(){return teams[0];}
    public Team getRedTeam(){return teams[1];}
    public void setStartTime(long startTime) {this.startTime = startTime;}
    public int getNbTour(){return nbTour;}
    public void setNbTour(int nbTour) {this.nbTour = nbTour;}

    public void ajouterObservateur(Observer o) {
        this.obs.add(o) ;
    }
    public void notifierObservateurs() {
        if (obs == null) {
            obs = new ArrayList<>();
        }
        for (Observer o : obs) {
            o.reagir();
        }
    }
    public Team getTeam(int id){return teams[id];}
    public int isTurnBegin() {return turnStep;}
    public void setTurnBegin(int turnBegin) {this.turnStep = turnBegin;}
    public int getCurrentTryCount() {return currentTryCount;}
    public void setCurrentTryCount(int currentTryCount) {this.currentTryCount = currentTryCount;}
    public void setIsWin(int isWin) {this.isWin = isWin;}
    public int getIsWin(){return isWin;}
    public void setGrid(Grid loadedGrid) {
        if (this.grid == null) {
            this.grid = new Grid(loadedGrid.getGrid().length, List.of(), gConfig.getGameMode());
        }
        this.grid.copyFrom(loadedGrid);
    }

    //Init Game
    public void setUpGame(){
        turn = random.nextInt(2);
        grid.initGrid(turn);
        teams[0] = new Team("Equipe Bleue",gConfig.getMaxTeamMember(),this,0);
        teams[1] = new Team("Equipe Rouge",gConfig.getMaxTeamMember(),this,1);
    }

    //Set a card visible
    public void flipCard(int row, int col) {
        grid.getCard(row,col).flipCard();
    }

    public void increaseTryCounter(){
        currentTryCount++;
    }

    public int isWinning(){
        boolean blueWinner = true;
        boolean redWinner = true;
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                Card currentCard = grid.getCard(row, col);
                if (!currentCard.isSelected()){
                    if (currentCard.getCouleur() == 1){
                        blueWinner = false;
                    }
                    else if (currentCard.getCouleur() == 2){
                        redWinner = false;
                    }
                }
            }
        }
        if (blueWinner){
            isWin = 0;
        }
        else if (redWinner){
            isWin = 1;
        }
        return isWin;
    }

    public String hintToString(){
        if (turnStep == 2){
            return currentHint + " en " + currentNumberWord;
        }
        else{
            return "";
        }
    }

    // Add these methods to manage observers
    public ArrayList<Observer> getObservateurs() {
        return new ArrayList<>(obs);
    }

    public void clearObservateurs() {
        obs.clear();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        obs = new ArrayList<>();  // Reinitialize the observers list
    }

    public GameConfiguration getGameConfiguration() {
        return this.gConfig;
    }

    public int addPlayer(int teamId,Player player) throws IOException, ClassNotFoundException {
        boolean alreadyExists = false;
        ArrayList<Player> playerList = gConfig.getPlayerList();
        if (playerList == null){
            playerList = new ArrayList<>();
        }
        for (Player p : playerList){
            if (p.getName().equals(player.getName())){
                alreadyExists = true;
                player = p;
                break;
            }
        }
        if (!alreadyExists){
            gConfig.addPlayer(player);
            StatLoader.addPlayer(player);
        }
        int added = teams[teamId].addPlayer(player);
        return added;
    }

    public void updateStat() throws IOException {
        for (Player p : teams[0].getPlayers()){
            PlayerStat stat = p.getStat();
            stat.setPartiesJouees(stat.getPartiesJouees() + 1);
            if (isWin == 0 || (isWin == 2 && turn == 1)){stat.setVictoires(stat.getVictoires() + 1);}
            else{stat.setDefaites(stat.getDefaites() + 1);}
            if (p.getIsSpy()){stat.setNbPartieJoueEspion(stat.getNbPartieJoueEspion() + 1);}
            stat.setRatioVictoiresDefaites((double) stat.getVictoires() / stat.getPartiesJouees());
        }
        for (Player p : teams[1].getPlayers()){
            PlayerStat stat = p.getStat();
            stat.setPartiesJouees(stat.getPartiesJouees() + 1);
            if (isWin == 1|| (isWin == 2 && turn == 0)){stat.setVictoires(stat.getVictoires() + 1);}
            else{stat.setDefaites(stat.getDefaites() + 1);}
            if (p.getIsSpy()){stat.setNbPartieJoueEspion(stat.getNbPartieJoueEspion() + 1);}
            stat.setRatioVictoiresDefaites((double) stat.getVictoires() / stat.getPartiesJouees());
        }
        StatLoader.overwritePlayerList(gConfig.getPlayerList());
    }

    public void updateGameStat(){
        if (isWin == 2){
            gameStat.setEquipeGagnante(Math.abs(1-turn));
        }
        else{
            gameStat.setEquipeGagnante(isWin);
        }
        gameStat.setDuree(System.currentTimeMillis()-startTime);
        gameStat.setNombreDeTours(nbTour);
        gameStat.setTheme(gConfig.getTheme());
    }
}
