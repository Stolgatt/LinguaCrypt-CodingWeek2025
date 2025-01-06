package linguacrypt.model;

public class Card {
    private String word;
    private String urlImage;
    private int couleur; // 0 : white / 1 : blue / 2 : red / 3 : black
    private boolean selected;

    public Card(String word, String urlImage) {
        this.word = word;
        this.urlImage = urlImage;
        this.couleur = 0;
        this.selected = false;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public void flipCard(){
        this.selected = true;
    }
}
