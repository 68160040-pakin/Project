import javax.swing.*;

public class Dice {
    private ImageIcon[] diceImages = new ImageIcon[6];
    private ImageIcon diceIcon;

    public Dice() {
        for (int i = 0; i < 6; i++) {
            java.net.URL imgURL = getClass().getResource("/dice" + (i+1) + ".png");
            if (imgURL != null) diceImages[i] = new ImageIcon(imgURL);
            else diceImages[i] = new ImageIcon(); // ป้องกัน null
        }
        diceIcon = diceImages[0];
    }
    public void roll(int value) { diceIcon = diceImages[value - 1]; }
    public ImageIcon getIcon() { return diceIcon; }
}