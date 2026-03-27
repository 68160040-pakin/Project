import javax.swing.*;
import java.awt.CardLayout;

public class GameFrame extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;

    public GameFrame() {
        setTitle("Snakes & Ladders");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        MainMenuPanel menu = new MainMenuPanel(this);
        mainPanel.add(menu, "Menu");

        add(mainPanel);
        setVisible(true);
    }
    public void startGame(int numPlayers) {
        // สร้าง GamePanel ใหม่ทุกครั้งที่เริ่มเกม เพื่อรีเซ็ตค่าผู้เล่นและกระดาน
        GamePanel gamePanel = new GamePanel(numPlayers, this);
        mainPanel.add(gamePanel, "Game");
        cardLayout.show(mainPanel, "Game");
    }

    // เมธอดสำหรับกลับหน้าเมนู
    public void returnToMenu() {
        cardLayout.show(mainPanel, "Menu");
    }
    // 🛠 เพิ่มส่วนนี้เข้าไปเพื่อให้กด Run ได้ 🛠
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameFrame();
        });
    }
}