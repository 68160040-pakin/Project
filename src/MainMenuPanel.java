import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private Image backgroundImage;

    public MainMenuPanel(GameFrame frame) {
        // 1. โหลดรูปพื้นหลัง
        try {
            // ใช้ / นำหน้าเพื่อให้หาไฟล์ในโฟลเดอร์ resources เจอ
            backgroundImage = new ImageIcon(getClass().getResource("/bg.png")).getImage();
        } catch (Exception e) {
            System.out.println("Main Menu Image not found");
        }

        // 2. จัดวางปุ่ม
        setLayout(new GridLayout(4, 1, 10, 30)); // เพิ่มระยะห่างระหว่างปุ่มเป็น 30
        setBorder(BorderFactory.createEmptyBorder(100, 250, 100, 250));

        JLabel label = new JLabel("Select Number of Players", SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 40));
        label.setForeground(Color.WHITE);
        add(label);

        for (int i = 1; i <= 3; i++) {
            int players = i;
            JButton btn = new JButton(players + " Player" + (players > 1 ? "s" : ""));

            // สไตล์ปุ่ม: สีครีมอ่อนๆ และโปร่งใส (Alpha = 220)
            btn.setBackground(new Color(255, 255, 240, 220));
            btn.setFont(new Font("Tahoma", Font.BOLD, 20));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // เปลี่ยนเมาส์เป็นรูปมือเมื่อชี้ปุ่ม

            btn.addActionListener(e -> {
                // ✅ เพิ่มเสียง SFX ตอนกดเลือก
                SoundManager.playSFX("S2.wav");

                // เริ่มเกม
                frame.startGame(players);
            });
            add(btn);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Gradient สวยๆ กรณีโหลดรูปไม่ขึ้น
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, new Color(44, 62, 80), 0, getHeight(), new Color(76, 161, 175));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}