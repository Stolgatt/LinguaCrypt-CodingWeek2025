package linguacrypt.model.statistique;


import java.io.Serializable;

public class PlayerStat implements Serializable {
    private static final long serialVersionUID = 1L;

    private int partiesJouees;
    private int victoires;
    private int defaites;
    private double ratioVictoiresDefaites;
    private long tempsTotal;
    private int nbPartieJoueEspion;


    public PlayerStat() {
        partiesJouees = 0;
        victoires = 0;
        defaites = 0;
        ratioVictoiresDefaites = 0;
        nbPartieJoueEspion = 0;
        tempsTotal = 0;
    }

    public int getPartiesJouees() {return partiesJouees;}
    public void setPartiesJouees(int partiesJouees) {this.partiesJouees = partiesJouees;}
    public int getVictoires() {return victoires;}
    public void setVictoires(int victoires) {this.victoires = victoires;}
    public int getDefaites() {return defaites;}
    public void setDefaites(int defaites) {this.defaites = defaites;}
    public double getRatioVictoiresDefaites() {return ratioVictoiresDefaites;}
    public void setRatioVictoiresDefaites(double ratioVictoiresDefaites) {this.ratioVictoiresDefaites = ratioVictoiresDefaites;}
    public long getTempsTotal() {return tempsTotal;}
    public void setTempsTotal(long tempsTotal) {this.tempsTotal = tempsTotal;}
    public int getNbPartieJoueEspion() {return nbPartieJoueEspion;}
    public void setNbPartieJoueEspion(int nbPartieJoueEspion) {this.nbPartieJoueEspion = nbPartieJoueEspion;}



    public String displayStat() {
        return String.format(
                "Parties jouées : %d | Victoires : %d | Défaites : %d | Ratio V/D : %.2f| Temps total : %d min | Rôle favori : %s",
                partiesJouees,
                victoires,
                defaites,
                ratioVictoiresDefaites,
                tempsTotal / 60, // Conversion en minutes
                (nbPartieJoueEspion > partiesJouees - nbPartieJoueEspion) ? "Espion" : "Agent"
        );
    }
}
