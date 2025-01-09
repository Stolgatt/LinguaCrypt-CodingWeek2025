package linguacrypt.model;

import linguacrypt.model.players.*;
import linguacrypt.model.players.AI.*;
import linguacrypt.ApplicationContext;
import linguacrypt.model.game.*;
import linguacrypt.model.statistique.*;


import linguacrypt.utils.StatLoader;
import linguacrypt.utils.ThemeLoader;
import linguacrypt.view.Observer;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;

import java.util.List;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private GameConfiguration gConfig;
    private Grid grid;
    private int turn; // 0 : Blue team to play / 1 : Red team to play
    private int turnStep = 0;
    private String currentHint;
    private int currentNumberWord = 0;
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
                this.themeWords = loadThemeWords(gConfig.getWordTheme());
                this.grid = new Grid(gConfig.getGridSize(), themeWords, 0);
                setUpGame();
                break;
            case 1:                     // Picture Game Mode
            this.themeWords = loadThemeWords(gConfig.getPictTheme());
            this.grid = new Grid(gConfig.getGridSize(), themeWords, 1);
                setUpGame();
                break;
            case 2:                     // Solo Game Mode
                this.themeWords = loadThemeWords(gConfig.getWordTheme());
                this.grid = new Grid(gConfig.getGridSize(), themeWords, 2);
                break;
            default:
                break;
        }

    }

    private List<String> loadThemeWords(String themeName) {
        List<Theme> themes = ThemeLoader.loadThemes(gConfig.getGameMode());
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

    public void increaseNbTour(){nbTour++;}
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
        grid.printGrid();
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
        if (getgConfig().getGameMode() == 2){
            redWinner = false;
        }
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

    public int addPlayer(int teamId, Player player) throws IOException, ClassNotFoundException {
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
        switch (gConfig.getGameMode()){
            case 0:
                gameStat.setTheme(gConfig.getWordTheme());
                break;
            case 1:
                gameStat.setTheme(gConfig.getPictTheme());
                break;
            default:
                gameStat.setTheme("None");
                break;
        }
    }

    public void setUpSoloGame(Player player){
        grid.initGrid(-2);
        grid.printGrid();
        startTime = System.currentTimeMillis();
        teams[0] = new Team("Equipe Bleue",2,this,0);
        teams[1] = new Team("Equipe Rouge",0,this,1);
        teams[0].addPlayer(player);
        if (player.getIsSpy()){
            teams[0].addPlayer(new AIAgent(0));
        }
        else{
            teams[0].addPlayer(new AISpy(0));
            spyAIPlay();
            increaseNbTour();
        }
    }
    public void spyAIPlay(){
        System.out.println("spyAIPlay");
        try {
            Hint hint = ((AISpy) this.getBlueTeam().getPlayers().get(1)).generateHint(this.getGrid());
            this.setCurrentHint(hint.getWord());
            this.setCurrentNumberWord(hint.getCount());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setTurnBegin(2);
    }


    public long getStartTime() {return startTime;}

    public int getRedRemaining(){
        int c = 0;
        for (int i = 0 ;i< grid.getGrid().length;i++){
            for (int j = 0 ;j< grid.getGrid()[i].length;j++){
                if (grid.getCard(i,j).getCouleur()==2 && !grid.getCard(i,j).isSelected()){
                    c++;
                }
            }
        }
        return c;
    }
    public int getBlueRemaining(){
        int c = 0;
        for (int i = 0 ;i< grid.getGrid().length;i++){
            for (int j = 0 ;j< grid.getGrid()[i].length;j++){
                if (grid.getCard(i,j).getCouleur()==1 && !grid.getCard(i,j).isSelected()){
                    c++;
                }
            }
        }
        return c;
    }

    public Player getPlayerByNickname(String nickname) {
        for (Player player : ApplicationContext.getInstance().getGame().getBlueTeam().getPlayers()) {
            if (player.getName().equals(nickname)) {
                return player;
            }
        }
        for (Player player : ApplicationContext.getInstance().getGame().getBlueTeam().getPlayers()) {
            if (player.getName().equals(nickname)) {
                return player;
            }
        }
        System.out.println(nickname + "not found");
        return null; // Not found
    }


}
