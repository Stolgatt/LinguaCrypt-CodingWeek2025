package linguacrypt.model.statistique;

import linguacrypt.model.Theme;

public class GameStat {
    private long duree;
    private int equipeGagnante;
    private int nombreDeTours;
    private String theme;

    public String getTheme() {return theme;}
    public void setTheme(String theme) {this.theme = theme;}
    public int getNombreDeTours() {return nombreDeTours;}
    public void setNombreDeTours(int nombreDeTours) {this.nombreDeTours = nombreDeTours;}
    public int getEquipeGagnante() {return equipeGagnante;}
    public void setEquipeGagnante(int equipeGagnante) {this.equipeGagnante = equipeGagnante;}
    public long getDuree() {return duree;}
    public void setDuree(long duree) {this.duree = duree;}

    public GameStat() {
        this.duree = 0;
        this.equipeGagnante = -1;
        this.nombreDeTours = 0;
        this.theme = null;
    }

}
